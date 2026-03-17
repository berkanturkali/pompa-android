package com.pompa.android.util

import androidx.core.net.toUri
import com.pompa.android.BuildConfig

fun resolveBackendAssetUrl(rawUrl: String): String {
    val sourceUri = rawUrl.toUri()

    val activeApiBaseUrl = if (DeviceManager.checkIfTheDeviceIsEmulator()) {
        BuildConfig.POMPA_EMULATOR_BASE_URL
    } else {
        BuildConfig.POMPA_BASE_URL
    }

    val targetUri = activeApiBaseUrl.toUri()
    val targetScheme = targetUri.scheme ?: return rawUrl
    val targetAuthority = targetUri.encodedAuthority ?: return rawUrl

    return sourceUri.buildUpon()
        .scheme(targetScheme)
        .encodedAuthority(targetAuthority)
        .build()
        .toString()
}
