package com.kha.train.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kha.train.R;
import com.kha.train.util.SharedPreference;
import com.kha.train.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingFragment extends Fragment {

    Context mContext;
    @BindView(R.id.rl_privcy)
    RelativeLayout rl_privcy;
    SharedPreference sharedPreference;
    @BindView(R.id.wb_privcy)
    WebView wb_privcy;
    @BindView(R.id.tv_24hr)
    TextView tv_24hr;
    @BindView(R.id.tv_12hr)
    TextView tv_12hr;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View inflate = inflater.inflate(R.layout.fragment_setting, container, false);
       ButterKnife.bind((Object) this, inflate);
       this.mContext = getContext();
       this.sharedPreference = new SharedPreference(this.mContext);
       if (this.sharedPreference.getTimeFormate()) {
           tv_24hr.setBackgroundResource(R.drawable.blue_fill_bt);
           tv_24hr.setTextColor(Color.parseColor("#ffffff"));
           tv_12hr.setBackgroundResource(R.drawable.blue_fill_out_bt);
           tv_12hr.setTextColor(Color.parseColor("#2c1e5b"));
       } else {
           tv_12hr.setBackgroundResource(R.drawable.blue_fill_bt);
           tv_12hr.setTextColor(Color.parseColor("#ffffff"));
           tv_24hr.setBackgroundResource(R.drawable.blue_fill_out_bt);
           tv_24hr.setTextColor(Color.parseColor("#2c1e5b"));
       }

       tv_12hr.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tv_12hr.setBackgroundResource(R.drawable.blue_fill_bt);
               tv_12hr.setTextColor(Color.parseColor("#ffffff"));
               tv_24hr.setBackgroundResource(R.drawable.blue_fill_out_bt);
               tv_24hr.setTextColor(Color.parseColor("#2c1e5b"));
               sharedPreference.setTimeFormate(false);
               Toast.makeText(mContext, "12 hours", Toast.LENGTH_SHORT).show();
           }
       });
       tv_24hr.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tv_24hr.setBackgroundResource(R.drawable.blue_fill_bt);
               tv_24hr.setTextColor(Color.parseColor("#ffffff"));
               tv_12hr.setBackgroundResource(R.drawable.blue_fill_out_bt);
               tv_12hr.setTextColor(Color.parseColor("#2c1e5b"));
               sharedPreference.setTimeFormate(true);
               Toast.makeText(mContext, "24 hours", Toast.LENGTH_SHORT).show();
           }
       });
       
        return inflate;
    }


   /* @OnClick({R.id.ll_time_formate})
    public void onClickTimeFormate() {
        final Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_time_formate_change);
        MyTextView myTextView = (MyTextView) dialog.findViewById(R.id.tv_24hr);
        dialog.findViewById(R.id.tv_12hr).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tv_time_fomt.setText("12 hours");
                sharedPreference.setTimeFormate(false);
                dialog.dismiss();
            }
        });
        myTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tv_time_fomt.setText("24 hours");
                sharedPreference.setTimeFormate(true);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }*/

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

    }
}