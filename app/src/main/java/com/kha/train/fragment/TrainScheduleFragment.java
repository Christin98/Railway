package com.kha.train.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.kha.train.R;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.TrainSchedulBeen;
import com.kha.train.model.TrainSchedulBeen.Schedule;
import com.kha.train.util.SharedPreference;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import static com.kha.train.BuildConfig.trainschedule;

public class TrainScheduleFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Activity activity;
    DataLoaderdialog dataLoaderdialog;
    int dayno = 1;
    LayoutInflater inflater1;
    boolean is24formt = true;
    @BindView(R.id.layout_no_data)
    View layout_no_data;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.ll_train_schd)
    LinearLayout ll_train_schd;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String mParam2;
    SharedPreference sharedPreference;
    @BindView(R.id.sv_data)
    ScrollView sv_data;
    TrainSchedulBeen trainSchedulBeen = new TrainSchedulBeen();
    private String train_id;
    @BindView(R.id.tv_disclam)
    MyTextView tv_disclam;
    @BindView(R.id.tv_num_stop)
    MyTextView tv_num_stop;
    @BindView(R.id.tv_train_class)
    MyTextView tv_train_class;
    @BindView(R.id.tv_train_day)
    MyTextView tv_train_day;
    @BindView(R.id.tv_train_dur)
    MyTextView tv_train_dur;
    @BindView(R.id.tv_train_id)
    MyTextView tv_train_id;
    @BindView(R.id.tv_train_name)
    MyTextView tv_train_name;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static TrainScheduleFragment newInstance(String str) {
        TrainScheduleFragment trainScheduleFragment = new TrainScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        trainScheduleFragment.setArguments(bundle);
        return trainScheduleFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.train_id = getArguments().getString(ARG_PARAM1);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_train_schedule, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.mContext = getActivity();
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);
        this.sharedPreference = new SharedPreference(this.mContext);
        this.inflater1 = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.is24formt = this.sharedPreference.getTimeFormate();
        if (Utils.isNetworkAvailable(this.mContext)) {
            callApi();
        } else {
            Context context = this.mContext;
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        return inflate;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    private void callApi() {
        this.dataLoaderdialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        Builder newBuilder = HttpUrl.parse(trainschedule).newBuilder();
        JSONObject jSONObject = new JSONObject();
        MediaType parse = MediaType.parse("application/json");
        try {
            jSONObject.put("trainNumber", this.train_id);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("channelCode", "ANDROID");
            jSONObject2.put("affiliateCode", "MMT001");
            jSONObject.put("trackingParams", jSONObject2);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("=====>>>>");
            stringBuilder.append(jSONObject);
            Log.e("send data jsonObject", stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url(newBuilder.build()).post(RequestBody.create(parse, jSONObject.toString())).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                TrainScheduleFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!(TrainScheduleFragment.this.activity == null || TrainScheduleFragment.this.activity.isFinishing() || TrainScheduleFragment.this.dataLoaderdialog == null)) {
                            TrainScheduleFragment.this.dataLoaderdialog.dismiss();
                        }
                        Toast.makeText(TrainScheduleFragment.this.mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                        TrainScheduleFragment.this.sv_data.setVisibility(View.GONE);
                        TrainScheduleFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutputtrain", stringBuilder.toString());
                try {
                    if (new JSONObject(string).has("Error")) {
                        TrainScheduleFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(TrainScheduleFragment.this.activity == null || TrainScheduleFragment.this.activity.isFinishing() || TrainScheduleFragment.this.dataLoaderdialog == null)) {
                                    TrainScheduleFragment.this.dataLoaderdialog.dismiss();
                                }
                                TrainScheduleFragment.this.sv_data.setVisibility(View.GONE);
                                TrainScheduleFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (string.length() > 0) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<TrainSchedulBeen>() {
                        }.getType();
                        TrainScheduleFragment.this.trainSchedulBeen = (TrainSchedulBeen) gson.fromJson(string, type);
                        TrainScheduleFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(TrainScheduleFragment.this.activity == null || TrainScheduleFragment.this.activity.isFinishing() || TrainScheduleFragment.this.dataLoaderdialog == null || !TrainScheduleFragment.this.dataLoaderdialog.isShowing())) {
                                    TrainScheduleFragment.this.dataLoaderdialog.dismiss();
                                }
                                if (TrainScheduleFragment.this.trainSchedulBeen.schedule.size() > 0) {
                                    TrainScheduleFragment.this.addTrainDetail();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("===>>");
                                    stringBuilder.append(TrainScheduleFragment.this.trainSchedulBeen.train);
                                    Log.e("number", stringBuilder.toString());
                                }

                                TrainScheduleFragment.this.layout_no_data.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        Log.i("AsyncOkHttp_no_data", body.string());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addTrainDetail() {
        String str = "";
        try {
            StringBuilder stringBuilder;
            MyTextView myTextView = this.tv_train_id;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(this.trainSchedulBeen.train.number);
            myTextView.setText(stringBuilder2.toString());
            this.tv_train_name.setText(this.trainSchedulBeen.train.name);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder2 = new StringBuilder();
            if (this.trainSchedulBeen.daysOfRun.mon.booleanValue()) {
                stringBuilder3.append("M  ");
            }
            String str2 = "T  ";
            if (this.trainSchedulBeen.daysOfRun.tue.booleanValue()) {
                stringBuilder3.append(str2);
            }
            if (this.trainSchedulBeen.daysOfRun.wed.booleanValue()) {
                stringBuilder3.append("W  ");
            }
            if (this.trainSchedulBeen.daysOfRun.thu.booleanValue()) {
                stringBuilder3.append(str2);
            }
            if (this.trainSchedulBeen.daysOfRun.fri.booleanValue()) {
                stringBuilder3.append("F  ");
            }
            if (this.trainSchedulBeen.daysOfRun.sat.booleanValue()) {
                stringBuilder3.append("S  ");
            }
            if (this.trainSchedulBeen.daysOfRun.sun.booleanValue()) {
                stringBuilder3.append("S");
            }
            this.tv_train_day.setText(stringBuilder3.toString());


            if (trainSchedulBeen.train.classes != null) {


                for (int i = 0; i < this.trainSchedulBeen.train.classes.size(); i++) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append((String) this.trainSchedulBeen.train.classes.get(i));
                    stringBuilder.append("  ");
                    stringBuilder2.append(stringBuilder.toString());

                    Log.e("classes", trainSchedulBeen.train.classes.get(i).toString());
                }
            }


            this.tv_train_class.setText(stringBuilder2.toString());
            this.tv_train_dur.setText(this.trainSchedulBeen.totalDuration);
            MyTextView myTextView2 = this.tv_num_stop;
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append(this.trainSchedulBeen.numberOfStops);
            myTextView2.setText(stringBuilder4.toString());
            myTextView2 = this.tv_disclam;
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append(this.trainSchedulBeen.disclaimer);
            Log.e("disclaimer", this.trainSchedulBeen.disclaimer);
            myTextView2.setText(stringBuilder4.toString());
            for (int i2 = 0; i2 < this.trainSchedulBeen.schedule.size(); i2++) {
                MyTextView myTextView3;
                String str3 = "Day";
                View inflate;
                StringBuilder stringBuilder5;
                if (i2 == 0) {
                    inflate = this.inflater1.inflate(R.layout.layout_train_schd_day, null, false);
                    myTextView3 = (MyTextView) inflate.findViewById(R.id.tv_date);
                    stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(str3);
                    stringBuilder5.append(this.dayno);
                    myTextView3.setText(stringBuilder5.toString());
                    this.ll_train_schd.addView(inflate);
                } else if (((Schedule) this.trainSchedulBeen.schedule.get(i2)).day.intValue() > this.dayno) {
                    inflate = this.inflater1.inflate(R.layout.layout_train_schd_day, null, false);
                    myTextView3 = (MyTextView) inflate.findViewById(R.id.tv_date);
                    this.dayno = ((Schedule) this.trainSchedulBeen.schedule.get(i2)).day.intValue();
                    stringBuilder5 = new StringBuilder();
                    stringBuilder5.append(str3);
                    stringBuilder5.append(this.dayno);
                    myTextView3.setText(stringBuilder5.toString());
                    this.ll_train_schd.addView(inflate);
                }
                View inflate2 = this.inflater1.inflate(R.layout.layout_train_schd_traindetail, null, false);
                myTextView3 = (MyTextView) inflate2.findViewById(R.id.tv_code);
                MyTextView myTextView4 = (MyTextView) inflate2.findViewById(R.id.tv_distnce);
                MyTextView myTextView5 = (MyTextView) inflate2.findViewById(R.id.tv_name);
                MyTextView myTextView6 = (MyTextView) inflate2.findViewById(R.id.tv_plateform);
                MyTextView myTextView7 = (MyTextView) inflate2.findViewById(R.id.tv_arr_time);
                MyTextView myTextView8 = (MyTextView) inflate2.findViewById(R.id.tv_dep_time);
                View findViewById = inflate2.findViewById(R.id.v_line1);
                myTextView3.setText(((Schedule) this.trainSchedulBeen.schedule.get(i2)).station.code);
                myTextView5.setText(((Schedule) this.trainSchedulBeen.schedule.get(i2)).station.name);
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(((Schedule) this.trainSchedulBeen.schedule.get(i2)).distance);
                stringBuilder4.append(" Km");
                myTextView4.setText(stringBuilder4.toString());
                String str4 = "DEP. ";
                String str5 = "Plateform #";
                if (i2 == 0) {
                    StringBuilder stringBuilder6 = new StringBuilder();
                    stringBuilder6.append(str5);
                    stringBuilder6.append(((Schedule) this.trainSchedulBeen.schedule.get(i2)).expectedPlatformNo);
                    stringBuilder6.append(" | Journey Start");
                    myTextView6.setText(stringBuilder6.toString());
                    myTextView7.setVisibility(View.GONE);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str4);
                    stringBuilder.append(Utils.changeTimeFormat(((Schedule) this.trainSchedulBeen.schedule.get(i2)).departureDetails.scheduledDepartureTime, this.is24formt));
                    myTextView8.setText(stringBuilder.toString());
                } else {
                    String str6 = "ARR. ";
                    if (i2 == this.trainSchedulBeen.schedule.size() - 1) {
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(str5);
                        stringBuilder4.append(((Schedule) this.trainSchedulBeen.schedule.get(i2)).expectedPlatformNo);
                        stringBuilder4.append(" | Journey End");
                        myTextView6.setText(stringBuilder4.toString());
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(str6);
                        stringBuilder4.append(Utils.changeTimeFormat(((Schedule) this.trainSchedulBeen.schedule.get(i2)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                        myTextView7.setText(stringBuilder4.toString());
                        myTextView8.setVisibility(View.GONE);
                        findViewById.setVisibility(View.GONE);
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str5);
                        stringBuilder.append(((Schedule) this.trainSchedulBeen.schedule.get(i2)).expectedPlatformNo);
                        stringBuilder.append(" | Stop ");
                        stringBuilder.append(((Schedule) this.trainSchedulBeen.schedule.get(i2)).haltMinutes);
                        stringBuilder.append("m");
                        myTextView6.setText(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str6);
                        stringBuilder.append(Utils.changeTimeFormat(((Schedule) this.trainSchedulBeen.schedule.get(i2)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                        myTextView7.setText(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str4);
                        stringBuilder.append(Utils.changeTimeFormat(((Schedule) this.trainSchedulBeen.schedule.get(i2)).departureDetails.scheduledDepartureTime, this.is24formt));
                        myTextView8.setText(stringBuilder.toString());
                    }
                }
                this.ll_train_schd.addView(inflate2);
            }
        } catch (Exception unused) {

            Log.e("Exception", "" + unused.toString());
        }
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        try {
            this.activity.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onButtonPressed(Uri uri) {
        OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
