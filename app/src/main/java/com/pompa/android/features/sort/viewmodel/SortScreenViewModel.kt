package com.pompa.android.features.sort.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pompa.android.data.datastore.PompaFilterPrefs
import com.pompa.android.features.sort.SortDataSource
import com.pompa.android.features.sort.model.SortDirection
import com.pompa.android.features.sort.model.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortScreenViewModel @Inject constructor(
    private val pompaFilterPrefs: PompaFilterPrefs,
    @ApplicationContext context: Context
) : ViewModel() {

    val baseOptions = SortDataSource.getSortOptions(context = context)

    var sortOptionsState by mutableStateOf(baseOptions)
        private set

    var selectedOption by mutableStateOf<SortOption?>(null)
        private set

    init {
        viewModelScope.launch {
            pompaFilterPrefs.filterPreferences.collect { preferences ->
                val directionValue = preferences.sortDirection ?: SortDirection.ASCENDING.value

                val selectedDirection =
                    if (directionValue == SortDirection.ASCENDING.value)
                        SortDirection.ASCENDING
                    else
                        SortDirection.DESCENDING

                selectedOption = baseOptions.firstOrNull { it.direction == selectedDirection }

                sortOptionsState = baseOptions.map {
                    it.copy(selected = it.direction == selectedDirection)
                }
            }
        }
    }

    fun onSortClicked(option: SortOption) {
        selectedOption = option
        sortOptionsState = sortOptionsState.map {
            it.copy(selected = it.direction == option.direction)
        }

        viewModelScope.launch {
            pompaFilterPrefs.setSelectedSort(option.direction.value)
        }
    }

}