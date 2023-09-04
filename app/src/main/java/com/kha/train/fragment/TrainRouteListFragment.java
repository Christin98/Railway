package com.kha.train.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kha.train.R;
import com.kha.train.activity.MainActivity;
import com.kha.train.adapter.TrainListAdapter;
import com.kha.train.adapter.TrainListAdapter.setOnTrainDetailListner;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.TrainListBeen;
import com.kha.train.model.TrainListBeen.TrainBtwnStnsList;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyTextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.kha.train.BuildConfig.trainroutelist;

public class TrainRouteListFragment extends Fragment implements setOnTrainDetailListner {

    String TAG = "TrainRouteListFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    Activity activity;
    DataLoaderdialog dataLoaderdialog;
    private String date;
    public String doj;
    private String fromCode;
    @BindView(R.id.layout_no_data)
    View layout_no_data;
    @BindView(R.id.ll_arrival)
    LinearLayout ll_arrival;
    @BindView(R.id.ll_data)
    RelativeLayout ll_data;
    @BindView(R.id.ll_departure)
    LinearLayout ll_departure;
    @BindView(R.id.ll_travel_time)
    LinearLayout ll_travel_time;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String qury;
    private String route;
    @BindView(R.id.rv_rail)
    RecyclerView rv_rail;
    private String toCode;
    TrainListAdapter trainListAdapter;
    TrainListBeen trainListBeen = new TrainListBeen();
    @BindView(R.id.tv_route_date)
    MyTextView tv_route_date;
    @BindView(R.id.tv_route_name)
    MyTextView tv_route_name;
    LinearLayout lay1, lay2, lay3;
    TextView text1, text2, text3;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static TrainRouteListFragment newInstance(String str, String str2, String str3, String str4, String str5, String str6) {
        TrainRouteListFragment trainRouteListFragment = new TrainRouteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        bundle.putString(ARG_PARAM3, str3);
        bundle.putString(ARG_PARAM4, str4);
        bundle.putString(ARG_PARAM5, str5);
        bundle.putString(ARG_PARAM6, str6);
        trainRouteListFragment.setArguments(bundle);
        return trainRouteListFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.route = getArguments().getString(ARG_PARAM1);
            this.date = getArguments().getString(ARG_PARAM2);
            this.qury = getArguments().getString(ARG_PARAM3);
            this.doj = getArguments().getString(ARG_PARAM4);
            this.fromCode = getArguments().getString(ARG_PARAM5);
            this.toCode = getArguments().getString(ARG_PARAM6);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("====>>>>>");
            stringBuilder.append(this.date);
            Log.e("222222dates", stringBuilder.toString());
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_train_route_list, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);

        lay1 = inflate.findViewById(R.id.lay1);
        lay2 = inflate.findViewById(R.id.lay2);
        lay3 = inflate.findViewById(R.id.lay3);
        text1 = inflate.findViewById(R.id.text1traveltime);
        text2 = inflate.findViewById(R.id.text2departure);
        text3 = inflate.findViewById(R.id.text3arrive);

        this.mContext = getActivity();
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);
        this.tv_route_name.setText(this.route);
        String firstStr=date;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            firstStr = date.replace(",", System.lineSeparator());
        }

        this.tv_route_date.setText(firstStr);
        this.rv_rail.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        if (Utils.isNetworkAvailable(this.mContext)) {
            CallApi();
        } else {
            Context context = this.mContext;
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        this.tv_route_name.setSelected(true);
        return inflate;
    }

    @OnClick({R.id.ll_travel_time})
    public void onClickTravelTime() {
        changeTopTab(1);
        lay1.setBackgroundResource(R.drawable.blue_fill_bt);
        lay2.setBackgroundResource(R.drawable.blue_fill_out_bt);
        lay3.setBackgroundResource(R.drawable.blue_fill_out_bt);
        text1.setTextColor(Color.parseColor("#ffffff"));
        text2.setTextColor(Color.parseColor("#2c1e5b"));
        text3.setTextColor(Color.parseColor("#2c1e5b"));
    }

    @OnClick({R.id.ll_departure})
    public void onClickDepature() {
        changeTopTab(2);

        lay1.setBackgroundResource(R.drawable.blue_fill_out_bt);
        lay2.setBackgroundResource(R.drawable.blue_fill_bt);
        lay3.setBackgroundResource(R.drawable.blue_fill_out_bt);

        text1.setTextColor(Color.parseColor("#2c1e5b"));
        text2.setTextColor(Color.parseColor("#ffffff"));
        text3.setTextColor(Color.parseColor("#2c1e5b"));
    }

    @OnClick({R.id.ll_arrival})
    public void onClickArrival() {
        changeTopTab(3);
        lay1.setBackgroundResource(R.drawable.blue_fill_out_bt);
        lay2.setBackgroundResource(R.drawable.blue_fill_out_bt);
        lay3.setBackgroundResource(R.drawable.blue_fill_bt);

        text1.setTextColor(Color.parseColor("#2c1e5b"));
        text2.setTextColor(Color.parseColor("#2c1e5b"));
        text3.setTextColor(Color.parseColor("#ffffff"));
    }

    public void changeTopTab(int i) {
        String str = "1";
        String str2 = "0";
        if (i == 1) {
            this.lay1.getChildAt(1).setVisibility(View.VISIBLE);
            this.lay2.getChildAt(1).setVisibility(View.GONE);
            this.lay3.getChildAt(1).setVisibility(View.GONE);
            ((MyTextView) this.lay1.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.medium));
            ((MyTextView) this.lay2.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            ((MyTextView) this.lay3.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            if (this.lay1.getTag().toString().equalsIgnoreCase(str2)) {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return Utils.getTimeDiff(trainBtwnStnsList.departureTime, trainBtwnStnsList.arrivalTime).compareTo(Utils.getTimeDiff(trainBtwnStnsList2.departureTime, trainBtwnStnsList2.arrivalTime));
                    }
                });
                this.lay1.setTag(str);
                this.lay1.getChildAt(1).setRotation(0.0f);
            } else {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return Utils.getTimeDiff(trainBtwnStnsList2.departureTime, trainBtwnStnsList2.arrivalTime).compareTo(Utils.getTimeDiff(trainBtwnStnsList.departureTime, trainBtwnStnsList.arrivalTime));
                    }
                });
                this.lay1.setTag(str2);
                this.lay1.getChildAt(1).setRotation(180.0f);
            }
            this.trainListAdapter.notifyDataSetChanged();
        } else if (i == 2) {
            this.lay1.getChildAt(1).setVisibility(View.GONE);
            this.lay2.getChildAt(1).setVisibility(View.VISIBLE);
            this.lay3.getChildAt(1).setVisibility(View.GONE);
            ((MyTextView) this.lay1.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            ((MyTextView) this.lay2.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.medium));
            ((MyTextView) this.lay3.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            if (this.lay2.getTag().toString().equalsIgnoreCase(str2)) {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return trainBtwnStnsList.departureTime.compareTo(trainBtwnStnsList2.departureTime);
                    }
                });
                this.lay2.setTag(str);
                this.lay2.getChildAt(1).setRotation(0.0f);
            } else {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return trainBtwnStnsList2.departureTime.compareTo(trainBtwnStnsList.departureTime);
                    }
                });
                this.lay2.setTag(str2);
                this.lay2.getChildAt(1).setRotation(180.0f);
            }
            this.trainListAdapter.notifyDataSetChanged();
        } else if (i == 3) {
            this.lay1.getChildAt(1).setVisibility(View.GONE);
            this.lay2.getChildAt(1).setVisibility(View.GONE);
            this.lay3.getChildAt(1).setVisibility(View.VISIBLE);
            ((MyTextView) this.lay1.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            ((MyTextView) this.lay2.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.regular));
            ((MyTextView) this.lay3.getChildAt(0)).setMyTypeface(this.mContext, getString(R.string.medium));
            if (this.lay3.getTag().toString().equalsIgnoreCase(str2)) {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return trainBtwnStnsList.arrivalTime.compareTo(trainBtwnStnsList2.arrivalTime);
                    }
                });
                this.lay3.setTag(str);
                this.lay3.getChildAt(1).setRotation(0.0f);
            } else {
                Collections.sort(this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                    public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                        return trainBtwnStnsList2.arrivalTime.compareTo(trainBtwnStnsList.arrivalTime);
                    }
                });
                this.lay3.setTag(str2);
                this.lay3.getChildAt(1).setRotation(180.0f);
            }
            this.trainListAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("ResourceType")
    public void onClickTicketClassListner(int i) {

        final String toJson = new Gson().toJson(this.trainListBeen.trainBtwnStnsList.get(i));
        if (this.fromCode.equalsIgnoreCase(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).frmStnCode)) {
            LiveStatusFragment liveStatusFragment = new LiveStatusFragment();
            openFragment(LiveStatusFragment.newInstance(toJson, this.doj), "LiveStatusFragment");
            return;
        }
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this.mContext);
        bottomSheetDialog.setContentView((int) R.layout.dialog_train_not_found);
        MyTextView myTextView = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_search_label);
        MyTextView myTextView2 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_error_label);
        MyTextView myTextView3 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_point1);
        MyTextView myTextView4 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_point2);
        MyTextView myTextView5 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_point3);
        MyTextView myTextView6 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_back);
        MyTextView myTextView7 = (MyTextView) bottomSheetDialog.findViewById(R.id.tv_ok);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You Searched for trains between ");
        stringBuilder.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).frmStnCode);
        stringBuilder.append(" and ");
        stringBuilder.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).toStnCode);
        myTextView.setText(stringBuilder.toString());
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("But this train travel between ");
        stringBuilder2.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).frmStnCode);
        stringBuilder2.append("( ");
        stringBuilder2.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).frmStnName);
        stringBuilder2.append(") and ");
        stringBuilder2.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).toStnCode);
        stringBuilder2.append("(");
        stringBuilder2.append(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).toStnName);
        stringBuilder2.append(")");
        myTextView2.setText(stringBuilder2.toString());
        myTextView3.setText(this.fromCode);
        myTextView4.setText(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).frmStnCode);
        myTextView5.setText(((TrainBtwnStnsList) this.trainListBeen.trainBtwnStnsList.get(i)).toStnCode);
        myTextView6.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        myTextView7.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                TrainRouteListFragment trainRouteListFragment = TrainRouteListFragment.this;
                LiveStatusFragment liveStatusFragment = new LiveStatusFragment();
                trainRouteListFragment.openFragment(LiveStatusFragment.newInstance(toJson, TrainRouteListFragment.this.doj), "LiveStatusFragment");
            }
        });
        bottomSheetDialog.getWindow().setGravity(80);
        bottomSheetDialog.getWindow().setBackgroundDrawableResource(17170445);
        bottomSheetDialog.getWindow().setLayout(-1, -2);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void CallApi() {
        Activity activity = this.activity;
        if (!(activity == null || activity.isFinishing())) {
            DataLoaderdialog dataLoaderdialog = this.dataLoaderdialog;
            if (dataLoaderdialog != null) {
                dataLoaderdialog.show();
            }
        }
        Log.e(TAG, "CallApi:quary "+this.qury );
        OkHttpClient okHttpClient = new OkHttpClient();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(trainroutelist);
        stringBuilder.append(this.qury);
        okHttpClient.newCall(new Builder().url(HttpUrl.parse(stringBuilder.toString()).newBuilder().build()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                TrainRouteListFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!(TrainRouteListFragment.this.activity == null || TrainRouteListFragment.this.activity.isFinishing() || TrainRouteListFragment.this.dataLoaderdialog == null)) {
                            TrainRouteListFragment.this.dataLoaderdialog.dismiss();
                        }
                        Toast.makeText(TrainRouteListFragment.this.mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody body = response.body();
                String string = body.string();
                Log.e(TAG, "onResponse: api response string "+string );
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                if (string.equalsIgnoreCase("") || string.length() <= 0) {
                    TrainRouteListFragment.this.activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!(TrainRouteListFragment.this.activity == null || TrainRouteListFragment.this.activity.isFinishing() || TrainRouteListFragment.this.dataLoaderdialog == null)) {
                                TrainRouteListFragment.this.dataLoaderdialog.dismiss();
                            }
                            try {
                                TrainRouteListFragment.this.ll_data.setVisibility(View.GONE);
                                TrainRouteListFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                                Log.i("AsyncOkHttp_no_data", body.string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<TrainListBeen>() {
                }.getType();
                TrainRouteListFragment.this.trainListBeen = (TrainListBeen) gson.fromJson(string, type);
                TrainRouteListFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.e(TAG, "run: 1");
                        if (!(TrainRouteListFragment.this.activity == null || TrainRouteListFragment.this.activity.isFinishing() || TrainRouteListFragment.this.dataLoaderdialog == null)) {
                            TrainRouteListFragment.this.dataLoaderdialog.dismiss();
                            Log.e(TAG, "run: 2");
                        }
                        if (TrainRouteListFragment.this.trainListBeen.trainBtwnStnsList == null) {
                            Log.e(TAG, "run: 3");
                            TrainRouteListFragment.this.ll_data.setVisibility(View.GONE);
                            TrainRouteListFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                        } else if (TrainRouteListFragment.this.trainListBeen.trainBtwnStnsList.size() > 0) {
                            Log.e(TAG, "run: 4");
                            Collections.sort(TrainRouteListFragment.this.trainListBeen.trainBtwnStnsList, new Comparator<TrainBtwnStnsList>() {
                                public int compare(TrainBtwnStnsList trainBtwnStnsList, TrainBtwnStnsList trainBtwnStnsList2) {
                                    try {
                                        Log.e(TAG, "run: 5");
                                        String timeDiff = Utils.getTimeDiff(trainBtwnStnsList.departureTime, trainBtwnStnsList.arrivalTime);
                                        timeDiff.getClass();
                                        timeDiff = timeDiff;
                                        String timeDiff2 = Utils.getTimeDiff(trainBtwnStnsList2.departureTime, trainBtwnStnsList2.arrivalTime);
                                        timeDiff2.getClass();
                                        return timeDiff.compareTo(timeDiff2);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return 0;
                                    }
                                }
                            });
                            Log.e(TAG, "run: 6");
                            TrainRouteListFragment.this.trainListAdapter = new TrainListAdapter(TrainRouteListFragment.this.mContext, TrainRouteListFragment.this.trainListBeen.trainBtwnStnsList, TrainRouteListFragment.this, TrainRouteListFragment.this.doj);
                            TrainRouteListFragment.this.rv_rail.setAdapter(TrainRouteListFragment.this.trainListAdapter);
                        } else {
                            TrainRouteListFragment.this.ll_data.setVisibility(View.GONE);
                            TrainRouteListFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        // enter backpress method here
        // (getActivity()).onBackPressed();

        Fragment fragment = new TrainBookFragment();
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame, fragment, "TrainBookFragment");
        beginTransaction.commit();
        MainActivity.bottomNavigation.setVisibility(View.VISIBLE);
    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame, fragment, tag);
        beginTransaction.addToBackStack(fragment.toString());
        beginTransaction.commit();
    }

    public void onDestroy() {
        super.onDestroy();
        DataLoaderdialog dataLoaderdialog = this.dataLoaderdialog;
        if (dataLoaderdialog != null && dataLoaderdialog.isShowing()) {
            this.dataLoaderdialog.cancel();
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
