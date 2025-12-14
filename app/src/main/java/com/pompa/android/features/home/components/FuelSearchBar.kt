package com.pompa.android.features.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pompa.android.R
import com.pompa.android.ui.providers.pompaColorPalette
import com.pompa.android.ui.theme.PompaTheme
import com.pompa.android.util.ClickIndication
import com.pompa.android.util.safeClickable

@Composable
fun FuelSearchBar(
    value: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
) {

    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    var isFocused by remember { androidx.compose.runtime.mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                onValueChanged(it)
            },
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.pompaColorPalette.searchBarColors.textColor
            ),
            cursorBrush = SolidColor(MaterialTheme.pompaColorPalette.searchBarColors.cursorColor),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .padding(8.dp),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .focusRequester(focusRequester)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        tint = MaterialTheme.pompaColorPalette.searchBarColors.startIconColor
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = stringResource(R.string.fuel_search_bar_hint),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.pompaColorPalette.searchBarColors.hintColor,
                                    fontWeight = FontWeight.Medium,
                                    fontStyle = FontStyle.Italic
                                ),
                            )
                        }
                        innerTextField()
                    }
                    AnimatedVisibility(
                        visible = value.isNotEmpty(),
                        enter = slideInHorizontally { it },
                        exit = slideOutHorizontally { it }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = "Clear text",
                            tint = MaterialTheme.pompaColorPalette.searchBarColors.closeIconColor,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .safeClickable(
                                    indication = ClickIndication.None
                                ) { onValueChanged("") }
                        )
                    }
                }

            }
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun FuelSearchBarPrev() {
    PompaTheme {
        FuelSearchBar(
            value = ""
        ) {

        }
    }
}