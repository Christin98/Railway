package com.kha.train.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kha.train.R;
import com.kha.train.util.SharedPreference;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyTextView;

public class SettingActivity extends AppCompatActivity {
    Context mContext;
    @BindView(R.id.rl_privcy)
    RelativeLayout rl_privcy;
    SharedPreference sharedPreference;
    @BindView(R.id.tv_time_fomt)
    MyTextView tv_time_fomt;
    @BindView(R.id.wb_privcy)
    WebView wb_privcy;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_setting);
        ButterKnife.bind((Activity) this);
        this.mContext = this;
        this.sharedPreference = new SharedPreference(this.mContext);
        if (this.sharedPreference.getTimeFormate()) {
            this.tv_time_fomt.setText("24 hours");
        } else {
            this.tv_time_fomt.setText("12 hours");
        }
    }

    @OnClick({R.id.ll_time_formate})
    public void onClickTimeFormate() {
        final Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_time_formate_change);
        MyTextView myTextView = (MyTextView) dialog.findViewById(R.id.tv_24hr);
        dialog.findViewById(R.id.tv_12hr).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingActivity.this.tv_time_fomt.setText("12 hours");
                SettingActivity.this.sharedPreference.setTimeFormate(false);
                dialog.dismiss();
            }
        });
        myTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingActivity.this.tv_time_fomt.setText("24 hours");
                SettingActivity.this.sharedPreference.setTimeFormate(true);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }

    @OnClick({R.id.ll_privacy})
    public void onClickPrivacy() {
        if (Utils.isNetworkAvailable(this.mContext)) {
            this.rl_privcy.setVisibility(View.VISIBLE);
            this.wb_privcy.loadUrl(getResources().getString(R.string.privacy_policy));
            return;
        }
        Context context = this.mContext;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(getString(R.string.no_internet));
        Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.iv_priv_back})
    public void onClickPrivcyBack() {
        this.rl_privcy.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        finish();
    }
}
