package com.kha.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.kha.train.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends AppCompatActivity {
    public InterstitialAd mInterstitialAdSS;
    int pp_position;

    public void onBackPressed() {
    }

    public void requestInterstitialSS() {
        this.mInterstitialAdSS.loadAd(new Builder().build());
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        this.mInterstitialAdSS = new InterstitialAd(this);
        this.mInterstitialAdSS.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdSS.setAdListener(new AdListener() {
            public void onAdClosed() {
                SplashActivity.this.requestInterstitialSS();
                SplashActivity splashActivity = SplashActivity.this;
                splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                SplashActivity.this.finish();
            }
        });
        this.mInterstitialAdSS.loadAd(new Builder().build());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SplashActivity.this.mInterstitialAdSS.isLoaded()) {
                    SplashActivity.this.mInterstitialAdSS.show();
                    return;
                }
                SplashActivity splashActivity = SplashActivity.this;
                splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 1500);
    }
}
