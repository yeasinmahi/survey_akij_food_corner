package com.devfolder.yeasin.survey;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AddLoader {
    private static AdRequest adRequest;

    public static void loadBannerAd(AdView adView) {
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public static void loadInterstialAd(final InterstitialAd interstitial) {

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                displayInterstitial(interstitial);
            }
        });
    }

    public static void displayInterstitial(InterstitialAd interstitial) {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
