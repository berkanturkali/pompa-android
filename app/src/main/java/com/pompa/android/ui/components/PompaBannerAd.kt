package com.pompa.android.ui.components

import android.util.Log
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.pompa.android.BuildConfig
import com.pompa.android.ui.providers.pompaColorPalette

@Composable
fun PompaBannerAd(
    height: Dp,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var isAdVisible by remember { mutableStateOf(false) }
    val adView = remember {
        AdView(context).apply {
            adUnitId = BuildConfig.ADMOB_BANNER_UNIT_ID
            setAdSize(AdSize.BANNER)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isAdVisible = true
                    Log.d(TAG, "Banner ad loaded for unitId=$adUnitId")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    isAdVisible = false
                    Log.e(
                        TAG,
                        "Banner ad failed for unitId=$adUnitId code=${p0.code} message=${p0.message}"
                    )
                }

                override fun onAdImpression() {
                    Log.d(TAG, "Banner ad impression recorded for unitId=$adUnitId")
                }
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(adView) {
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .height(if (isAdVisible) height else 0.dp)
            .background(MaterialTheme.pompaColorPalette.backgroundColors.primary),
        factory = { adView }
    )
}

private const val TAG = "PompaBannerAd"
