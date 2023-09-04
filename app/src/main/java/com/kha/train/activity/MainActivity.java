package com.kha.train.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

import com.kha.train.R;
import com.kha.train.fragment.SettingFragment;
import com.kha.train.fragment.TrainBookFragment;
import com.kha.train.widget.GpsTracker;
import com.kha.train.widget.GpsUtils;
import com.kha.train.widget.GpsUtils.onGpsListener;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {
    GpsTracker gpsTracker;
    boolean isBack = false;
    private AdView mAdView;
    public InterstitialAd mInterstitialAdFT;
    public InterstitialAd mInterstitialAdLS;
    public InterstitialAd mInterstitialAdPS;
    public InterstitialAd mInterstitialAdSET;
    public InterstitialAd mInterstitialAdTRS;
   public static MeowBottomNavigation bottomNavigation;
    Switch toggle;

    public void requestInterstitialSET() {
        this.mInterstitialAdSET.loadAd(new Builder().build());
    }

    public void requestInterstitialFT() {
        this.mInterstitialAdFT.loadAd(new Builder().build());
    }

    public void requestInterstitialTRS() {
        this.mInterstitialAdTRS.loadAd(new Builder().build());
    }

    public void requestInterstitialLS() {
        this.mInterstitialAdLS.loadAd(new Builder().build());
    }

    public void requestInterstitialPS() {
        this.mInterstitialAdPS.loadAd(new Builder().build());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void bottomNavigationEvent() {
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_blue));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_setting_blue));
        bottomNavigation.show(1, true);
        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SettingFragment(),"SettingFragment").commit();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TrainBookFragment(),"TrainBookFragment").commit();
                    break;
            }
            return null;
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        ButterKnife.bind((Activity) this);
        int_ads();


        bottomNavigation=findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TrainBookFragment(),"TrainBookFragment").commit();

        this.mInterstitialAdSET = new InterstitialAd(this);
        this.mInterstitialAdSET.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdSET.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.requestInterstitialSET();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, SettingActivity.class));
            }
        });
        this.mInterstitialAdFT = new InterstitialAd(this);
        this.mInterstitialAdFT.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdFT.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.requestInterstitialFT();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, BookTicketMainActivity.class));
            }
        });
        this.mInterstitialAdTRS = new InterstitialAd(this);
        this.mInterstitialAdTRS.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdTRS.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.requestInterstitialTRS();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, TrainScheduleMainActivity.class));
            }
        });
        this.mInterstitialAdLS = new InterstitialAd(this);
        this.mInterstitialAdLS.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdLS.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.requestInterstitialLS();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, LiveStationMainActivity.class));
            }
        });
        this.mInterstitialAdPS = new InterstitialAd(this);
        this.mInterstitialAdPS.setAdUnitId(getString(R.string.interstitial_ad_id));
        this.mInterstitialAdPS.setAdListener(new AdListener() {
            public void onAdClosed() {
                MainActivity.this.requestInterstitialPS();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity, PNRStatusMainActivity.class));
            }
        });
        AdRequest build = new Builder().build();
        this.mInterstitialAdSET.loadAd(build);
        this.mInterstitialAdFT.loadAd(build);
        this.mInterstitialAdTRS.loadAd(build);
        this.mInterstitialAdLS.loadAd(build);
        this.mInterstitialAdPS.loadAd(build);
        CheckGps();

        bottomNavigationEvent();
    }

    public void int_ads() {
        this.mAdView = (AdView) findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
    }

    private void CheckGps() {
        this.gpsTracker = new GpsTracker(this);
        if (!this.gpsTracker.isGPSEnable()) {
            new GpsUtils(this).turnGPSOn(new onGpsListener() {
                public void gpsStatus(boolean z) {
                }
            });
        }
    }

    /*@OnClick({R.id.tv_train_ticket, R.id.cv_find_train})
    public void onClickbookTrain() {
        if (this.mInterstitialAdFT.isLoaded()) {
            this.mInterstitialAdFT.show();
        } else {
            startActivity(new Intent(this, BookTicketMainActivity.class));
        }
    }

    @OnClick({R.id.ll_live_status, R.id.cv_live_status})
    public void onClickLiveStatus() {
        startActivity(new Intent(this, LiveStatusMainActivity.class));
    }

    @OnClick({R.id.tv_train_schedule, R.id.cv_train_schedule})
    public void onClickTrainSchd() {
        if (this.mInterstitialAdTRS.isLoaded()) {
            this.mInterstitialAdTRS.show();
        } else {
            startActivity(new Intent(this, TrainScheduleMainActivity.class));
        }
    }

    @OnClick({R.id.tv_live_station, R.id.cv_live_station})
    public void onClickLiveStation() {
        if (this.mInterstitialAdLS.isLoaded()) {
            this.mInterstitialAdLS.show();
        } else {
            startActivity(new Intent(this, LiveStationMainActivity.class));
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @OnClick({R.id.tv_pnr_status, R.id.cv_pnr_status})
    public void onClickPnrStatus() {
        if (this.mInterstitialAdPS.isLoaded()) {
            this.mInterstitialAdPS.show();
        } else {
            startActivity(new Intent(this, PNRStatusMainActivity.class));
        }
    }

    @OnClick({R.id.tv_setting})
    public void onClickSetting() {
        if (this.mInterstitialAdSET.isLoaded()) {
            this.mInterstitialAdSET.show();
        } else {
            startActivity(new Intent(this, SettingActivity.class));
        }
    }*/

    @Override
    public void onBackPressed() {

        TrainBookFragment trainBookFragment= (TrainBookFragment) getSupportFragmentManager().findFragmentByTag("TrainBookFragment");
        if (trainBookFragment != null && !trainBookFragment.isVisible()) {
            // add your code here
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TrainBookFragment(), "TrainBookFragment").commit();
            bottomNavigation.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }


}
