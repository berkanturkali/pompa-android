package com.pompa.android.model.fuel

import android.os.Parcelable
import com.pompa.android.features.home.model.FuelPriceUiModel
import kotlinx.serialization.Serializable
import kotlin.reflect.full.memberProperties

@Serializable
@kotlinx.parcelize.Parcelize
data class FuelPriceProvider(
    val provider: String,
    val providerLogo: String,
    val averagePrice: Double,
    val ok: Boolean,
    val data: List<FuelPriceRecord>,
    val error: String?,
) : Parcelable

@Serializable
@kotlinx.parcelize.Parcelize
data class FuelPriceRecord(
    val brand: String,
    val cityCode: String?,
    val cityName: String,
    val districtName: String,
    val prices: FuelPrices,
    val unit: String,
    val weightUnit: String,
    val source: String,
    val fetchedAt: String
) : Parcelable

@Serializable
@kotlinx.parcelize.Parcelize
data class FuelPrices(
    val gasoline95: Double?,
    val gasoline95Premium: Double?,
    val diesel: Double?,
    val dieselPremium: Double?,
    val kerosene: Double?,
    val heatingOil: Double?,
    val fuelOil: Double?,
    val fuelOilHighSulfur: Double?,
    val autogas: Double?
) : Parcelable {
    fun mapToUiItems(
        unit: String,
        weightUnit: String,
    ): List<FuelPriceUiModel> {
        val items = mutableListOf<FuelPriceUiModel>()

        gasoline95
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.gasoline95,
                    price = it,
                    unit = unit
                )
            }

        gasoline95Premium
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.gasoline95Premium,
                    price = it,
                    unit = unit
                )
            }

        diesel
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.diesel,
                    price = it,
                    unit = unit
                )
            }

        dieselPremium
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.dieselPremium,
                    price = it,
                    unit = unit
                )
            }

        kerosene
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.kerosene,
                    price = it,
                    unit = unit
                )
            }

        heatingOil
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.heatingOil,
                    price = it,
                    unit = weightUnit
                )
            }

        fuelOil
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.fuelOil,
                    price = it,
                    unit = weightUnit
                )
            }

        fuelOilHighSulfur
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.fuelOilHighSulfur,
                    price = it,
                    unit = weightUnit
                )
            }

        autogas
            .toText()
            ?.let {
                items += FuelPriceUiModel(
                    title = com.pompa.android.R.string.autogas,
                    price = it,
                    unit = unit
                )
            }

        return items
    }

    fun notNullCount(): Int {
        return FuelPrices::class.memberProperties.count { prop ->
            prop.get(this) != null
        }
    }

    fun Double?.toText(): String? =
        this?.let { "%.2f".format(it) }
}