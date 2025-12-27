package com.pompa.android.util

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        private const val PREFS_NAME = "com.pompa.android.USER_PREFERENCES"
        private const val SELECTED_PROVINCE_CODE = "selected_province_code"
        private const val SELECTED_PROVINCE_NAME = "selected_province_name"
        private const val FAVORITE_PROVIDER_ID = "favorite_provider_id"
        private const val FAVORITE_PROVIDER_NAME = "favorite_provider_name"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setProvince(code: String, name: String) {
        prefs.edit {
            putString(SELECTED_PROVINCE_CODE, code).putString(SELECTED_PROVINCE_NAME, name)
        }
    }

    fun getSelectedProvinceCode(): String? {
        return prefs.getString(SELECTED_PROVINCE_CODE, "")
    }

    fun getSelectedProvinceName(): String? {
        return prefs.getString(SELECTED_PROVINCE_NAME, null)
    }

    fun setFavoriteProviderId(providerId: Int) {
        prefs.edit { putInt(FAVORITE_PROVIDER_ID, providerId) }
    }

    fun setFavoriteProviderName(providerName: String) {
        prefs.edit { putString(FAVORITE_PROVIDER_NAME, providerName) }
    }

    fun getSelectedProvince(): Pair<String?, String?> {
        val code = getSelectedProvinceCode()
        val name = getSelectedProvinceName()
        return Pair(code, name)
    }

    fun getFavoriteProviderName(): String? {
        return prefs.getString(
            FAVORITE_PROVIDER_NAME,
            ""
        )
    }

    fun clear() {
        prefs.edit { clear() }
    }

}