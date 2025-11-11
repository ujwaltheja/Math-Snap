package com.mathsnap.app.util

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Manager for AdMob integration
 * Handles initialization and ad loading
 */
class AdManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        initializeAds()
    }

    private fun initializeAds() {
        scope.launch {
            MobileAds.initialize(context) { initializationStatus ->
                // Initialization complete
                loadInterstitialAd()
            }
        }
    }

    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        // Using test ad unit ID - replace with your actual ad unit ID in production
        val adUnitId = "ca-app-pub-3940256099942544/1033173712"

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    fun showInterstitialAd(
        activity: android.app.Activity,
        onAdDismissed: () -> Unit = {}
    ) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Ad dismissed, load a new one
                    loadInterstitialAd()
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    // Ad failed to show
                    loadInterstitialAd()
                }

                override fun onAdShowedFullScreenContent() {
                    // Ad showed successfully
                    interstitialAd = null
                }
            }
            ad.show(activity)
        } ?: run {
            // Ad not ready, load a new one
            loadInterstitialAd()
            onAdDismissed()
        }
    }

    fun isInterstitialAdReady(): Boolean {
        return interstitialAd != null
    }

    companion object {
        // Show ads every X problems completed
        const val AD_FREQUENCY = 10

        fun shouldShowAd(problemsCompleted: Int, isPremium: Boolean): Boolean {
            return !isPremium && problemsCompleted > 0 && problemsCompleted % AD_FREQUENCY == 0
        }
    }
}
