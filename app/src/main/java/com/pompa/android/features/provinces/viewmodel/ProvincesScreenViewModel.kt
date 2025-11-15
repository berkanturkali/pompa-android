package com.pompa.android.features.provinces.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.repo.province.ProvinceRepo
import com.pompa.android.data.util.collectResource
import com.pompa.android.model.provinces.Province
import com.pompa.android.model.util.UIText
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvincesScreenViewModel @Inject constructor(
    private val provinceRepo: ProvinceRepo,
    private val userPreferences: UserPreferences,
) : ViewModel() {

    val showLoading = mutableStateOf(false)

    var provinceList by mutableStateOf(emptyList<Province>())

    var errorMessage by mutableStateOf<UIText?>(null)


    init {
        fetchProvinces()
    }

    fun fetchProvinces() {
        viewModelScope.launch {
            provinceRepo.getProvinces().collectResource(
                onError = {
                    errorMessage = it
                },
                loadingState = showLoading

            ) { provinces ->
                provinceList = provinces ?: emptyList()
            }
        }
    }

    fun saveSelectedProvince(province: Province) {
        userPreferences.setProvince(
            code = province.code,
            name = province.name
        )
    }
}