package com.pompa.android.features.home.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.pompa.android.R
import com.pompa.android.model.fuel.ChangeDirection
import com.pompa.android.model.fuel.PriceTrend

data class PriceTrendUiModel(
    val fuelKey: String?,
    val previousPrice: String?,
    val priceChange: String?,
    val changeDirection: ChangeDirection,
    val color: Color?,
    @param:DrawableRes val icon: Int?
) {

    companion object {
        fun mapToTrendUiModel(priceTrend: PriceTrend?): PriceTrendUiModel {
            return PriceTrendUiModel(
                fuelKey = priceTrend?.fuelKey,
                previousPrice = priceTrend?.previousPrice?.toString(),
                priceChange = priceTrend?.let { setPriceChange(priceTrend) },
                changeDirection = ChangeDirection.valueOf(priceTrend?.changeDirection!!),
                color = setColor(ChangeDirection.valueOf(priceTrend.changeDirection)),
                icon = setIcon(ChangeDirection.valueOf(priceTrend.changeDirection))
            )
        }

        private fun setColor(changeDirection: ChangeDirection): Color? {
            return when (changeDirection) {
                ChangeDirection.UP -> {
                    Color(0xFFFF7043)
                }

                ChangeDirection.DOWN -> {
                    Color.Green
                }

                ChangeDirection.SAME, ChangeDirection.NO_DATA -> {
                    null
                }
            }
        }

        private fun setIcon(changeDirection: ChangeDirection): Int? {
            return when (changeDirection) {
                ChangeDirection.UP -> {
                    R.drawable.ic_upward
                }

                ChangeDirection.DOWN -> {
                    R.drawable.ic_downward
                }

                ChangeDirection.SAME, ChangeDirection.NO_DATA -> {
                    null
                }
            }
        }

        private fun setPriceChange(priceTrend: PriceTrend): String? {
            val changeDirection = ChangeDirection.valueOf(priceTrend.changeDirection!!)
            return when (changeDirection) {
                ChangeDirection.UP, ChangeDirection.DOWN -> priceTrend.priceChange?.toString()
                else -> null
            }
        }
    }
}