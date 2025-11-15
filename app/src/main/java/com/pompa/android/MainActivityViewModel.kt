package com.pompa.android

import androidx.lifecycle.ViewModel
import com.pompa.android.util.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun checkProvinceAlreadySelected(): Boolean {
        val code = userPreferences.getSelectedProvinceCode()
        return code != -1
    }
}