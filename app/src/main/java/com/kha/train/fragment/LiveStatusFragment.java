package com.kha.train.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kha.train.R;
import com.kha.train.adapter.TrainLiveStatusAdapter;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.TrainListBeen;
import com.kha.train.model.TrainListBeen.TrainBtwnStnsList;
import com.kha.train.model.TrainLiveStatusBeen;
import com.kha.train.model.TrainLiveStatusBeen.Station;
import com.kha.train.util.SharedPreference;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyTextView;
import com.kha.train.widget.SlidingUpPanelLayout;
import com.kha.train.widget.SlidingUpPanelLayout.PanelSlideListener;
import com.kha.train.widget.SlidingUpPanelLayout.PanelState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

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

import static com.kha.train.BuildConfig.trainlivestatus;

public class LiveStatusFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Slidepanel";
    public static TrainLiveStatusBeen liveStatusBeen;
    Activity activity;
    DataLoaderdialog dataLoaderdialog;
    public String dos;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    boolean is24formt = true;
    LinearLayoutManager linearLayoutManager;
    TrainLiveStatusAdapter liveStatusAdapter;
    Context mContext;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rl_header_g)
    RelativeLayout rl_header_g;
    @BindView(R.id.rl_header_r)
    RelativeLayout rl_header_r;
    @BindView(R.id.rpb_clock_g)
    RippleBackground rpb_clock_g;
    @BindView(R.id.rpb_clock_r)
    RippleBackground rpb_clock_r;
    @BindView(R.id.rv_status)
    RecyclerView rv_status;
    @BindView(R.id.sb_distance)
    AppCompatSeekBar sb_distance;
    SharedPreference sharedPreference;
    TrainBtwnStnsList trainBtwnStnsList;
    @BindView(R.id.tv_dep)
    MyTextView tv_dep;
    @BindView(R.id.tv_end_km)
    MyTextView tv_end_km;
    @BindView(R.id.tv_end_station)
    MyTextView tv_end_station;
    @BindView(R.id.tv_last_date)
    MyTextView tv_last_date;
    @BindView(R.id.tv_last_dep)
    MyTextView tv_last_dep;
    @BindView(R.id.tv_last_plateform)
    MyTextView tv_last_plateform;
    @BindView(R.id.tv_last_schd)
    MyTextView tv_last_schd;
    @BindView(R.id.tv_last_station)
    MyTextView tv_last_station;
    @BindView(R.id.tv_last_update)
    MyTextView tv_last_update;
    @BindView(R.id.tv_nxt_schd)
    MyTextView tv_nxt_schd;
    @BindView(R.id.tv_nxt_stat_date)
    MyTextView tv_nxt_stat_date;
    @BindView(R.id.tv_nxt_station)
    MyTextView tv_nxt_station;
    @BindView(R.id.tv_plateform)
    MyTextView tv_plateform;
    @BindView(R.id.tv_search_date)
    MyTextView tv_search_date;
    @BindView(R.id.tv_start_date)
    MyTextView tv_start_date;
    @BindView(R.id.tv_start_km)
    MyTextView tv_start_km;
    @BindView(R.id.tv_start_station)
    MyTextView tv_start_station;
    @BindView(R.id.tv_train_id)
    MyTextView tv_train_id;
    @BindView(R.id.tv_train_name)
    MyTextView tv_train_name;
    @BindView(R.id.tv_train_status_g)
    MyTextView tv_train_status_g;
    @BindView(R.id.tv_train_status_r)
    MyTextView tv_train_status_r;
    @BindView(R.id.tv_trian_id)
    MyTextView tv_trian_id;
    @BindView(R.id.tv_trian_name)
    MyTextView tv_trian_name;
    @BindView(R.id.tv_update_date)
    MyTextView tv_update_date;
    RelativeLayout layout_no_data;
    LinearLayout refreshLay;


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public LiveStatusFragment() {
        new TrainListBeen().getClass();
        this.trainBtwnStnsList = new TrainBtwnStnsList();
    }

    public static LiveStatusFragment newInstance(String str, String str2) {
        LiveStatusFragment liveStatusFragment = new LiveStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        liveStatusFragment.setArguments(bundle);
        return liveStatusFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
            this.trainBtwnStnsList = (TrainBtwnStnsList) new Gson().fromJson(this.mParam1, new TypeToken<TrainBtwnStnsList>() {
            }.getType());
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_status, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);

        layout_no_data=inflate.findViewById(R.id.layout_no_data);
        layout_no_data.setVisibility(View.GONE);

        this.mContext = getActivity();
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);
        dataLoaderdialog.setCancelable(false);
        this.sharedPreference = new SharedPreference(this.mContext);
        this.is24formt = this.sharedPreference.getTimeFormate();
        this.linearLayoutManager = new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false);
        this.rv_status.setLayoutManager(this.linearLayoutManager);
        this.dos = this.mParam2;

        refreshLay=inflate.findViewById(R.id.refreshLay);

        MyTextView myTextView = this.tv_search_date;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        stringBuilder.append(Utils.changeDateFormate(this.mParam2, "yyyyMMdd", "dd MMM yy"));
        myTextView.setText(stringBuilder.toString());
        if (Utils.isNetworkAvailable(this.mContext)) {
            CallApi();
        } else {
            Context context = this.mContext;
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            layout_no_data.setVisibility(View.VISIBLE);
        }
        this.mLayout.addPanelSlideListener(new PanelSlideListener() {
            public void onPanelSlide(View view, float f) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPanelSlide, offset ");
                stringBuilder.append(f);
                Log.i(LiveStatusFragment.TAG, stringBuilder.toString());
            }

            public void onPanelStateChanged(View view, PanelState panelState, PanelState panelState2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPanelStateChanged ");
                stringBuilder.append(panelState2);
                Log.i(LiveStatusFragment.TAG, stringBuilder.toString());
                if (panelState2 == PanelState.COLLAPSED) {
                    dragView.setVisibility(View.GONE);
                }
            }
        });
        this.mLayout.setFadeOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
        inflate.setFocusableInTouchMode(true);
        inflate.requestFocus();
        inflate.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 4 || mLayout == null || (mLayout.getPanelState() != PanelState.EXPANDED && mLayout.getPanelState() != PanelState.ANCHORED)) {
                    return false;
                }
                mLayout.setPanelState(PanelState.COLLAPSED);
                dragView.setVisibility(View.GONE);
                return true;
            }
        });
        return inflate;
    }

    @OnClick({R.id.rl_date})
    public void onClickDate() {
        final Dialog dialog = new Dialog(this.mContext);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_live_status_date);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.ll_day_before);
        LinearLayout linearLayout2 = (LinearLayout) dialog.findViewById(R.id.ll_yesterday);
        LinearLayout linearLayout3 = (LinearLayout) dialog.findViewById(R.id.ll_today);
        LinearLayout linearLayout4 = (LinearLayout) dialog.findViewById(R.id.ll_tommorr);
        final MyTextView myTextView = (MyTextView) dialog.findViewById(R.id.tv_day_befor);
        final MyTextView myTextView2 = (MyTextView) dialog.findViewById(R.id.tv_yester);
        final MyTextView myTextView3 = (MyTextView) dialog.findViewById(R.id.tv_today);
        final MyTextView myTextView4 = (MyTextView) dialog.findViewById(R.id.tv_tommorr);
        Date convertStrToDate = Utils.convertStrToDate(this.mParam2);
        Calendar instance = Calendar.getInstance();
        instance.setTime(convertStrToDate);
        String str = "EEEE, dd MMM yy";
        myTextView3.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, -2);
        myTextView.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, 1);
        myTextView2.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, 2);
        myTextView4.setText(Utils.changeDateFormate(instance.getTime(), str));
        linearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                dos = myTextView.getText().toString();
                tv_search_date.setText(Utils.changeDateFormate(dos, "EEEE, dd MMM yy", "dd MMM yy"));
                if (Utils.isNetworkAvailable(mContext)) {
                    CallApiAfterDateChng();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    layout_no_data.setVisibility(View.VISIBLE);
                }
            }
        });
        linearLayout2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                dos = myTextView2.getText().toString();
                tv_search_date.setText(Utils.changeDateFormate(dos, "EEEE, dd MMM yy", "dd MMM yy"));
                if (Utils.isNetworkAvailable(mContext)) {
                    CallApiAfterDateChng();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    layout_no_data.setVisibility(View.VISIBLE);
                }
            }
        });
        linearLayout3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                dos = myTextView3.getText().toString();
                tv_search_date.setText(Utils.changeDateFormate(dos, "EEEE, dd MMM yy", "dd MMM yy"));
                if (Utils.isNetworkAvailable(mContext)) {
                    CallApiAfterDateChng();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    layout_no_data.setVisibility(View.VISIBLE);
                }
            }
        });
        linearLayout4.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                dos = myTextView4.getText().toString();
                tv_search_date.setText(Utils.changeDateFormate(dos, "EEEE, dd MMM yy", "dd MMM yy"));
                if (Utils.isNetworkAvailable(mContext)) {
                    CallApiAfterDateChng();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    layout_no_data.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.getWindow().setLayout(-1, -2);
        dialog.show();
    }

    @OnClick({R.id.iv_share,R.id.shareLay})
    public void onClickShare() {
        String str = "(";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Live Status:\n");
            stringBuilder.append(liveStatusBeen.trainDetails.number);
            stringBuilder.append(",");
            stringBuilder.append(liveStatusBeen.trainDetails.name);
            stringBuilder.append("\n\n\nTrain Start: ");
            stringBuilder.append(liveStatusBeen.trainDetails.startDate);
            stringBuilder.append("\n\n");
            stringBuilder.append(liveStatusBeen.metaDetails.otherDetails.distanceDetail);
            stringBuilder.append("\n\nOrignal Station: ");
            stringBuilder.append(liveStatusBeen.metaDetails.topStation.station.name);
            stringBuilder.append(str);
            stringBuilder.append(liveStatusBeen.metaDetails.topStation.station.code);
            stringBuilder.append(")\nExp. Dept: ");
            stringBuilder.append(liveStatusBeen.metaDetails.topStation.departureDetails.actualDepartureTime);
            stringBuilder.append(" , Sch. Dept: ");
            stringBuilder.append(liveStatusBeen.metaDetails.topStation.departureDetails.scheduledDepartureTime);
            stringBuilder.append("\n\nLast Station: ");
            stringBuilder.append(liveStatusBeen.metaDetails.bottomStation.station.name);
            stringBuilder.append(str);
            stringBuilder.append(liveStatusBeen.metaDetails.bottomStation.station.code);
            stringBuilder.append(")\nExp. Arrv: ");
            stringBuilder.append(liveStatusBeen.metaDetails.bottomStation.arrivalDetails.actualArrivalTime);
            stringBuilder.append(", Sch. Arrv: ");
            stringBuilder.append(liveStatusBeen.metaDetails.bottomStation.arrivalDetails.scheduledArrivalTime);
            stringBuilder.append("\n\n\nLast Updated at ");
            stringBuilder.append(liveStatusBeen.lastUpdated);
            Utils.ShareDetails(this.mContext, stringBuilder.toString());
        } catch (Exception unused) {
            Log.e(TAG, "onClickShare:share error:  "+unused.getMessage() );
        }
    }

    @OnClick({R.id.iv_info})
    public void onClickInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===>>>");
        stringBuilder.append(this.mLayout.getPanelState());
        Log.e("pannel states", stringBuilder.toString());
        if (this.mLayout.getPanelState() == PanelState.COLLAPSED || this.mLayout.getPanelState() == PanelState.HIDDEN) {
            this.dragView.setVisibility(View.VISIBLE);
            this.mLayout.setPanelState(PanelState.EXPANDED);
        }
    }

    @OnClick({R.id.iv_refresh, R.id.iv_ref,R.id.refreshLay})
    public void onClickRef() {
        CallApi();
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getActivity().finish();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void CallApi() {
        String str = "=====>>>>";
        Activity activity = this.activity;
        if (!(activity == null || activity.isFinishing())) {
            DataLoaderdialog dataLoaderdialog = this.dataLoaderdialog;
            if (dataLoaderdialog != null) {
                dataLoaderdialog.show();
                refreshLay.setClickable(false);
            }
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Builder newBuilder = HttpUrl.parse(trainlivestatus).newBuilder();
        JSONObject jSONObject = new JSONObject();
        MediaType parse = MediaType.parse("application/json");
        try {
            jSONObject.put("dateOfJourney", Utils.changeDateFormate(this.dos, "yyyyMMdd", "dd-MM-yyyy"));
            jSONObject.put("stationCode", this.trainBtwnStnsList.frmStnCode);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("affiliateCode", "MMT001");
            jSONObject2.put("channelCode", "ANDROID");
            jSONObject.put("trackingParams", jSONObject2);
            jSONObject.put("trainNumber", this.trainBtwnStnsList.trainNumber);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(jSONObject);
            Log.e("send data jsonObject", stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request build = new Request.Builder().url(newBuilder.build()).post(RequestBody.create(parse, jSONObject.toString())).build();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(build.toString());
        Log.e("send data Request", stringBuilder2.toString());
        okHttpClient.newCall(build).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                                dataLoaderdialog.dismiss();
                                refreshLay.setClickable(true);
                                Toast.makeText(mContext, "Network Problem Please Refresh!", Toast.LENGTH_SHORT).show();
                                layout_no_data.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody body = response.body();
                String string = body.string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                if (string.equalsIgnoreCase("") || string.length() <= 0) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!(activity == null || activity.isFinishing() || dataLoaderdialog == null)) {
                                dataLoaderdialog.dismiss();
                                refreshLay.setClickable(true);
                                layout_no_data.setVisibility(View.GONE);
                            }
                            try {
                                Log.i("AsyncOkHttp_no_data", body.string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                LiveStatusFragment.liveStatusBeen = new TrainLiveStatusBeen();
                try {
                    if (new JSONObject(string).has("Error")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(activity == null || activity.isFinishing() || dataLoaderdialog == null)) {
                                    dataLoaderdialog.dismiss();
                                    refreshLay.setClickable(true);
                                    if (LiveStatusFragment.liveStatusBeen.trainDetails != null) {
                                        tv_search_date.setText(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.trainDetails.startDate, "dd-MM-yyyy", "dd MMM yy"));
                                        Toast.makeText(mContext, "Train is not Running on given date.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(mContext, "Something went Wrong.!", Toast.LENGTH_SHORT).show();
                                    layout_no_data.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        return;
                    }

                    LiveStatusFragment.liveStatusBeen = (TrainLiveStatusBeen) new Gson().fromJson(string, new TypeToken<TrainLiveStatusBeen>() {
                    }.getType());
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            MyTextView myTextView;
                            StringBuilder stringBuilder;
                            layout_no_data.setVisibility(View.GONE);
                            String str = "dd-MMM-yyyy h:mm a";
                            liveStatusAdapter = new TrainLiveStatusAdapter(mContext, Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.trainDetails.startDate, "dd-MM-yyyy", str));
                            rv_status.setAdapter(liveStatusAdapter);
                            dataLoaderdialog.dismiss();
                            refreshLay.setClickable(true);
                            int i = 0;
                            while (i < LiveStatusFragment.liveStatusBeen.stations.size()) {
                                try {
                                    if (LiveStatusFragment.liveStatusBeen.trainDetails.currentStation.name.equalsIgnoreCase(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i)).station.name)) {
                                        break;
                                    }
                                    i++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            i = 0;
                            linearLayoutManager.scrollToPositionWithOffset(i, 1);
                            if (LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.delay == null || !LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.delay.booleanValue()) {
                                rpb_clock_g.startRippleAnimation();
                                rpb_clock_r.setVisibility(View.INVISIBLE);
                                rpb_clock_g.setVisibility(View.VISIBLE);
                                myTextView = tv_train_status_g;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.distanceDetail);
                                stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.timeDetail);
                                myTextView.setText(stringBuilder.toString());
                            } else {
                                rpb_clock_r.startRippleAnimation();
                                rpb_clock_g.setVisibility(View.INVISIBLE);
                                rpb_clock_r.setVisibility(View.VISIBLE);
                                myTextView = tv_train_status_r;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.distanceDetail);
                                stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.timeDetail);
                                myTextView.setText(stringBuilder.toString());
                            }
                            tv_trian_id.setText(trainBtwnStnsList.trainNumber);
                            tv_train_id.setText(trainBtwnStnsList.trainNumber);
                            tv_trian_name.setText(trainBtwnStnsList.trainName);
                            tv_train_name.setText(trainBtwnStnsList.trainName);
                            String str2 = "dd-MM-yyyy HH:mm:ss";
                            tv_update_date.setText(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.lastUpdated, str2, "dd MMM yy HH:mm:ss"));
                            myTextView = tv_last_update;
                            stringBuilder = new StringBuilder();
                          //  stringBuilder.append("Last Updated ");
                            stringBuilder.append(LiveStatusFragment.liveStatusBeen.lastUpdated);
                            myTextView.setText(stringBuilder.toString());
                            tv_start_date.setText(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.trainDetails.startDate, str2, str));
                            tv_start_station.setText(((Station) LiveStatusFragment.liveStatusBeen.stations.get(0)).station.name);
                            myTextView = tv_start_km;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(0)).distance);
                            String str3 = " Km";
                            stringBuilder.append(str3);
                            myTextView.setText(stringBuilder.toString());
                            tv_end_station.setText(((Station) LiveStatusFragment.liveStatusBeen.stations.get(LiveStatusFragment.liveStatusBeen.stations.size() - 1)).station.name);
                            myTextView = tv_end_km;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(LiveStatusFragment.liveStatusBeen.stations.size() - 1)).distance);
                            stringBuilder.append(str3);
                            myTextView.setText(stringBuilder.toString());

                            if (!TextUtils.isEmpty(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance) && TextUtils.isDigitsOnly(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance)) {
                                sb_distance.setMax(Integer.parseInt(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance));
                            } else {
                                sb_distance.setMax(0);
                            }


                            sb_distance.setProgress(LiveStatusFragment.liveStatusBeen.trainDetails.distanceCovered.intValue());
                            sb_distance.setEnabled(false);
                            StringBuilder stringBuilder2 = new StringBuilder();
                            String str4 = "====>>>>";
                            stringBuilder2.append(str4);
                            stringBuilder2.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.name);
                            Log.e("tv_nxt_station", stringBuilder2.toString());
                            MyTextView myTextView2 = tv_nxt_station;
                            StringBuilder stringBuilder3 = new StringBuilder();
                            String str5 = "";
                            stringBuilder3.append(str5);
                            stringBuilder3.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.name);
                            myTextView2.setText(stringBuilder3.toString());
                            myTextView2 = tv_plateform;
                            stringBuilder3 = new StringBuilder();
                            str3 = "Plateform #";
                            stringBuilder3.append(str3);
                            stringBuilder3.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.platformNumber);
                            myTextView2.setText(stringBuilder3.toString());
                            myTextView2 = tv_dep;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("DEP. ");
                            stringBuilder3.append(Utils.changeTimeFormat(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.actualDepartureTime, is24formt));
                            myTextView2.setText(stringBuilder3.toString());
                            String str6 = "#EB2026";
                            String str7 = "#26B5A9";
                            if (LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.departureDelay != null) {
                                tv_dep.setTextColor(Color.parseColor(str6));
                            } else {
                                tv_dep.setTextColor(Color.parseColor(str7));
                            }
                            myTextView2 = tv_nxt_stat_date;
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append(str5);
                            stringBuilder4.append(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.actualDepartureDate, str2, str));
                            myTextView2.setText(stringBuilder4.toString());
                            myTextView2 = tv_nxt_schd;
                            stringBuilder4 = new StringBuilder();
                            String str8 = "Sch-";
                            stringBuilder4.append(str8);
                            stringBuilder4.append(Utils.changeTimeFormat(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.scheduledDepartureTime, is24formt));
                            myTextView2.setText(stringBuilder4.toString());
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(str4);
                            stringBuilder2.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.name);
                            Log.e("tv_last_station", stringBuilder2.toString());
                            myTextView2 = tv_last_station;
                            StringBuilder stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(str5);
                            stringBuilder5.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.name);
                            myTextView2.setText(stringBuilder5.toString());
                            myTextView2 = tv_last_plateform;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(str3);
                            stringBuilder5.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.platformNumber);
                            myTextView2.setText(stringBuilder5.toString());
                            myTextView2 = tv_last_dep;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append("ARR. ");
                            stringBuilder5.append(Utils.changeTimeFormat(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.actualArrivalTime, is24formt));
                            myTextView2.setText(stringBuilder5.toString());
                            if (LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.arrivalDelay == null || LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.arrivalDelay.intValue() <= 0) {
                                tv_last_dep.setTextColor(Color.parseColor(str7));
                            } else {
                                tv_last_dep.setTextColor(Color.parseColor(str6));
                            }
                            myTextView2 = tv_last_date;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(str5);
                            stringBuilder5.append(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.actualArrivalDate, str2, str));
                            myTextView2.setText(stringBuilder5.toString());
                            myTextView2 = tv_last_schd;
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(str8);
                            stringBuilder5.append(Utils.changeTimeFormat(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.scheduledArrivalTime, is24formt));
                            myTextView2.setText(stringBuilder5.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void CallApiAfterDateChng() {
        String str = "=====>>>>";
        if (!getActivity().isFinishing()) {
            this.dataLoaderdialog.show();
            refreshLay.setClickable(false);
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Builder newBuilder = HttpUrl.parse(trainlivestatus).newBuilder();
        JSONObject jSONObject = new JSONObject();
        MediaType parse = MediaType.parse("application/json");
        try {
            jSONObject.put("dateOfJourney", Utils.changeDateFormate(this.dos, "EEEE, dd MMM yy", "dd-MM-yyyy"));
            jSONObject.put("stationCode", this.trainBtwnStnsList.frmStnCode);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("affiliateCode", "MMT001");
            jSONObject2.put("channelCode", "ANDROID");
            jSONObject.put("trackingParams", jSONObject2);
            jSONObject.put("trainNumber", this.trainBtwnStnsList.trainNumber);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(jSONObject);
            Log.e("send data jsonObject", stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request build = new Request.Builder().url(newBuilder.build()).post(RequestBody.create(parse, jSONObject.toString())).build();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(build.toString());
        Log.e("send data Request", stringBuilder2.toString());
        okHttpClient.newCall(build).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                            dataLoaderdialog.dismiss();
                            refreshLay.setClickable(true);
                            Toast.makeText(mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                            layout_no_data.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody body = response.body();
                String string = body.string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                try {
                    if (new JSONObject(string).has("Error")) {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                                    dataLoaderdialog.dismiss();
                                    refreshLay.setClickable(true);
                                    try {
                                        Toast.makeText(mContext, "Train is not running on given date.", Toast.LENGTH_SHORT).show();
                                        layout_no_data.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        return;
                    }
                    if (!string.equalsIgnoreCase("")) {
                        if (string.length() > 0) {
                            LiveStatusFragment.liveStatusBeen = (TrainLiveStatusBeen) new Gson().fromJson(string, new TypeToken<TrainLiveStatusBeen>() {
                            }.getType());
                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    MyTextView myTextView;
                                    StringBuilder stringBuilder;
                                    dataLoaderdialog.dismiss();
                                    refreshLay.setClickable(true);
                                    layout_no_data.setVisibility(View.GONE);
                                    String str = "dd MMM yy";
                                    liveStatusAdapter = new TrainLiveStatusAdapter(mContext, Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.trainDetails.startDate, "dd-MM-yyyy", str));
                                    rv_status.setAdapter(liveStatusAdapter);
                                    if (LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.delay == null || !LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.delay.booleanValue()) {
                                        rl_header_g.setVisibility(View.VISIBLE);
                                        rl_header_r.setVisibility(View.GONE);
                                        rpb_clock_g.startRippleAnimation();
                                        myTextView = tv_train_status_g;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.distanceDetail);
                                        stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.timeDetail);
                                        myTextView.setText(stringBuilder.toString());
                                    } else {
                                        rl_header_r.setVisibility(View.VISIBLE);
                                        rl_header_g.setVisibility(View.GONE);
                                        rpb_clock_r.startRippleAnimation();
                                        myTextView = tv_train_status_r;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.distanceDetail);
                                        stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.otherDetails.timeDetail);
                                        myTextView.setText(stringBuilder.toString());
                                    }
                                    tv_trian_id.setText(trainBtwnStnsList.trainNumber);
                                    tv_trian_name.setText(trainBtwnStnsList.trainName);
                                    String str2 = "dd-MM-yyyy HH:mm:ss";
                                    tv_update_date.setText(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.lastUpdated, str2, "dd MMM yy HH:mm:ss"));
                                    myTextView = tv_last_update;
                                    stringBuilder = new StringBuilder();
                                  //  stringBuilder.append("Last Updateed ");
                                    stringBuilder.append(LiveStatusFragment.liveStatusBeen.lastUpdated);
                                    myTextView.setText(stringBuilder.toString());
                                    tv_start_date.setText(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.trainDetails.startDate, str2, str));
                                    tv_start_station.setText(((Station) LiveStatusFragment.liveStatusBeen.stations.get(0)).station.name);
                                    myTextView = tv_start_km;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(0)).distance);
                                    String str3 = " Km";
                                    stringBuilder.append(str3);
                                    myTextView.setText(stringBuilder.toString());
                                    tv_end_station.setText(((Station) LiveStatusFragment.liveStatusBeen.stations.get(LiveStatusFragment.liveStatusBeen.stations.size() - 1)).station.name);
                                    myTextView = tv_end_km;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(LiveStatusFragment.liveStatusBeen.stations.size() - 1)).distance);
                                    stringBuilder.append(str3);
                                    myTextView.setText(stringBuilder.toString());


                                    if (!TextUtils.isEmpty(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance) && TextUtils.isDigitsOnly(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance)) {
                                        sb_distance.setMax(Integer.parseInt(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.distance));
                                    } else {
                                        sb_distance.setMax(0);
                                    }


                                    sb_distance.setProgress(LiveStatusFragment.liveStatusBeen.trainDetails.distanceCovered.intValue());
                                    sb_distance.setEnabled(false);
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    String str4 = "====>>>>";
                                    stringBuilder2.append(str4);
                                    stringBuilder2.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.name);
                                    Log.e("tv_nxt_station", stringBuilder2.toString());
                                    myTextView = tv_nxt_station;
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    str3 = "";
                                    stringBuilder3.append(str3);
                                    stringBuilder3.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.name);
                                    myTextView.setText(stringBuilder3.toString());
                                    myTextView = tv_plateform;
                                    stringBuilder3 = new StringBuilder();
                                    String str5 = "Plateform #";
                                    stringBuilder3.append(str5);
                                    stringBuilder3.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.station.platformNumber);
                                    myTextView.setText(stringBuilder3.toString());
                                    myTextView = tv_dep;
                                    stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("DEP. ");
                                    stringBuilder3.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.actualDepartureTime);
                                    myTextView.setText(stringBuilder3.toString());
                                    String str6 = "#EB2026";
                                    String str7 = "#26B5A9";
                                    if (LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.departureDelay != null) {
                                        tv_dep.setTextColor(Color.parseColor(str6));
                                    } else {
                                        tv_dep.setTextColor(Color.parseColor(str7));
                                    }
                                    myTextView = tv_nxt_stat_date;
                                    StringBuilder stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append(str3);
                                    stringBuilder4.append(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.actualDepartureDate, str2, str));
                                    myTextView.setText(stringBuilder4.toString());
                                    myTextView = tv_nxt_schd;
                                    stringBuilder4 = new StringBuilder();
                                    String str8 = "Sch-";
                                    stringBuilder4.append(str8);
                                    stringBuilder4.append(LiveStatusFragment.liveStatusBeen.metaDetails.topStation.departureDetails.scheduledDepartureTime);
                                    myTextView.setText(stringBuilder4.toString());
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(str4);
                                    stringBuilder2.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.name);
                                    Log.e("tv_last_station", stringBuilder2.toString());
                                    myTextView = tv_last_station;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str3);
                                    stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.name);
                                    myTextView.setText(stringBuilder.toString());
                                    myTextView = tv_last_plateform;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str5);
                                    stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.station.platformNumber);
                                    myTextView.setText(stringBuilder.toString());
                                    myTextView = tv_last_dep;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("ARR. ");
                                    stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.actualArrivalTime);
                                    myTextView.setText(stringBuilder.toString());
                                    if (LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.arrivalDelay == null || LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.arrivalDelay.intValue() <= 0) {
                                        tv_last_dep.setTextColor(Color.parseColor(str7));
                                    } else {
                                        tv_last_dep.setTextColor(Color.parseColor(str6));
                                    }
                                    myTextView = tv_last_date;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str3);
                                    stringBuilder.append(Utils.changeDateFormate(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.actualArrivalDate, str2, str));
                                    myTextView.setText(stringBuilder.toString());
                                    myTextView = tv_last_schd;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str8);
                                    stringBuilder.append(LiveStatusFragment.liveStatusBeen.metaDetails.bottomStation.arrivalDetails.scheduledArrivalTime);
                                    myTextView.setText(stringBuilder.toString());
                                }
                            });
                            return;
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                                dataLoaderdialog.dismiss();
                                refreshLay.setClickable(true);
                                try {
                                    Log.i("AsyncOkHttp_no_data", body.string());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
