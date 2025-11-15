package com.pompa.android.util

import android.content.Context
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
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setProvince(code: Int, name: String) {
        prefs.edit().putInt(SELECTED_PROVINCE_CODE, code).putString(SELECTED_PROVINCE_NAME, name)
            .apply()
    }

    fun getSelectedProvinceCode(): Int {
        return prefs.getInt(SELECTED_PROVINCE_CODE, -1)
    }

    fun getSelectedProvinceName(): String? {
        return prefs.getString(SELECTED_PROVINCE_NAME, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

}