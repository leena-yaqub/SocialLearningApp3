package com.example.sociallearningapp.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                adSize = AdSize.BANNER
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test ad unit ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
