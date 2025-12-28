package com.pompa.android.features.sort.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pompa.android.R
import com.pompa.android.features.sort.SortDataSource
import com.pompa.android.features.sort.model.SortOption
import com.pompa.android.features.sort.viewmodel.SortScreenViewModel
import com.pompa.android.ui.components.PompaBottomSheetHandle
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SortScreen(
    modifier: Modifier = Modifier,
    viewModel: SortScreenViewModel = hiltViewModel(),
    onSortOptionClick: (SortOption) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    SortScreenContent(
        modifier = modifier,
        sortOptions = viewModel.sortOptionsState,
        onSortOptionClick = {
            viewModel.onSortClicked(it)
            coroutineScope.launch {
                delay(500)
                onSortOptionClick(it)
            }
        })

}

@Composable
fun SortScreenContent(
    sortOptions: List<SortOption>,
    modifier: Modifier = Modifier,
    onSortOptionClick: (SortOption) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PompaBottomSheetHandle()

        Text(
            stringResource(R.string.sort),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.pompaColorPalette.textColors.onHighlight,
            modifier = Modifier.padding(start = 8.dp)
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.pompaColorPalette.borderColor
        )

        val selectedOption = sortOptions.firstOrNull { it.selected }
        sortOptions.forEachIndexed { index, option ->
            SortItem(
                option,
                onItemClick = {
                    if (selectedOption != option) {
                        onSortOptionClick(option)
                    }
                }
            )

            if (index != sortOptions.lastIndex) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.pompaColorPalette.borderColor
                )
            }
        }
    }
}

@Composable
fun SortItem(
    sortOption: SortOption,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .safeClickable(indication = ClickIndication.None) {
                onItemClick()
            }
            .height(24.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            sortOption.title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.pompaColorPalette.textColors.onHighlight,
        )

        AnimatedVisibility(
            visible = sortOption.selected,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(
                    R.drawable.ic_check
                ),
                contentDescription = null,
                tint = Color(0xff4CAF50)
            )
        }
    }
}

@Preview
@Composable
private fun SortScreenContentPrev() {
    PompaTheme {
        SortScreenContent(
            sortOptions = SortDataSource.getSortOptions(LocalContext.current)
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun SortItemPrev() {
    PompaTheme {
        SortItem(
            sortOption = SortDataSource.getSortOptions(LocalContext.current)[1],
            onItemClick = {

            }
        )
    }
}