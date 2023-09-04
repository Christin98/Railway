package com.kha.train.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kha.train.R;
import com.kha.train.activity.LiveStationMainActivity;
import com.kha.train.activity.LiveStatusMainActivity;
import com.kha.train.activity.MainActivity;
import com.kha.train.activity.PNRStatusMainActivity;
import com.kha.train.activity.TrainScheduleMainActivity;
import com.kha.train.adapter.SearchTrainListAdapter;
import com.kha.train.adapter.SearchTrainListAdapter.setTrianListClickListner;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.CityBeen;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyEditText;
import com.kha.train.widget.MyTextView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kha.train.BuildConfig.trainbook;

public class TrainBookFragment extends Fragment implements setTrianListClickListner {

    String TAG = "TrainBookFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Activity activity;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    ArrayList<CityBeen> cityBeenArrayList = new ArrayList();
    ArrayList<CityBeen> cityBeenArrayList1 = new ArrayList();
    Calendar clickedDayCalendar = Calendar.getInstance();
    DataLoaderdialog dataLoaderdialog;
    @BindView(R.id.et_city)
    MyEditText et_city;
    CityBeen fromCityBeen;
    boolean isDateSelect = false;
    boolean isFrom = true;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    private AdView mAdView;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;
    @BindView(R.id.rl_datepicker)
    RelativeLayout rl_datepicker;
    @BindView(R.id.rv_popular_search)
    RecyclerView rv_popular_search;
    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    CityBeen toCityBeen;
    SearchTrainListAdapter trainListAdapter;
    SearchTrainListAdapter trainpopularListAdapter;
    @BindView(R.id.tv_date)
    MyTextView tv_date;
    /*@BindView(R.id.tv_date_day)
    MyTextView tv_date_day;
    @BindView(R.id.tv_date_month)
    MyTextView tv_date_month;
    */
    @BindView(R.id.tv_from)
    MyTextView tv_from;
    @BindView(R.id.tv_from_sation)
    MyTextView tv_from_sation;
    @BindView(R.id.tv_popular_lable)
    MyTextView tv_popular_lable;
    @BindView(R.id.tv_to)
    MyTextView tv_to;
    @BindView(R.id.tv_to_station)
    MyTextView tv_to_station;
    ImageView alternativBT;
    LinearLayout cv_train_schedule, cv_live_status, cv_live_station, cv_pnr_status;
    SwitchCompat toggal;

    public static SharedPreferences preference;
    public static SharedPreferences.Editor editor;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static TrainBookFragment newInstance(String str, String str2) {
        TrainBookFragment trainBookFragment = new TrainBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        trainBookFragment.setArguments(bundle);
        return trainBookFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_train_book, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);

        toggal = inflate.findViewById(R.id.toggal);

        alternativBT = inflate.findViewById(R.id.alternativBT);
        preference = getActivity().getSharedPreferences("trainStationinfo", getActivity().MODE_PRIVATE);
        editor = preference.edit();

        // LinearLayout cv_train_schedule, cv_live_status, cv_live_station, cv_pnr_status;
        cv_train_schedule = inflate.findViewById(R.id.cv_train_schedule);
        cv_live_status = inflate.findViewById(R.id.cv_live_status);
        cv_live_station = inflate.findViewById(R.id.cv_live_station);
        cv_pnr_status = inflate.findViewById(R.id.cv_pnr_status);
        btnsClick();

        toggal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ShowToast(mContext, "AC");
                } else {

                    ShowToast(mContext, "Non AC");
                }
            }
        });

        alternativBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String from = tv_from.getText().toString();
                String fromSation = tv_from_sation.getText().toString();
                String to = tv_to.getText().toString();
                String toSation = tv_to_station.getText().toString();

                if (fromSation.isEmpty()) {
                    Toast.makeText(activity, "Select Enter From", Toast.LENGTH_SHORT).show();
                } else if (toSation.isEmpty()) {
                    Toast.makeText(activity, "Select Enter To", Toast.LENGTH_SHORT).show();
                } else {

                    String pfrom = preference.getString("from", null);
                    String pfromSation = preference.getString("fromSation", null);
                    String pfromCode = preference.getString("fromcode", null);

                    String pto = preference.getString("to", null);
                    String ptoSation = preference.getString("toSation", null);
                    String ptoCode = preference.getString("tocode", null);

                    editor.putString("from", pto);
                    editor.apply();
                    editor.putString("fromSation", ptoSation);
                    editor.apply();
                    editor.putString("fromcode", ptoCode);
                    editor.apply();

                    editor.putString("to", pfrom);
                    editor.apply();
                    editor.putString("toSation", pfromSation);
                    editor.apply();
                    editor.putString("tocode", pfromCode);
                    editor.apply();

                    tv_from.setText(preference.getString("from", null));
                    tv_from_sation.setText(preference.getString("fromSation", null));

                    tv_to.setText(preference.getString("to", null));
                    tv_to_station.setText(preference.getString("toSation", null));

                    fromCityBeen = new CityBeen();
                    fromCityBeen.cityName = preference.getString("from", null);
                    fromCityBeen.name = preference.getString("fromSation", null);
                    fromCityBeen.code = preference.getString("fromcode", null);

                    toCityBeen = new CityBeen();
                    toCityBeen.cityName = preference.getString("to", null);
                    toCityBeen.name = preference.getString("toSation", null);
                    toCityBeen.code = preference.getString("tocode", null);

                }
            }
        });


        this.mAdView = (AdView) inflate.findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
        this.mContext = getActivity();
        this.progressDialog = new ProgressDialog(this.mContext);
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);
        this.calendarView.setOnDayClickListener(new OnDayClickListener() {
            public void onDayClick(EventDay eventDay) {
                if (eventDay.getCalendar().getTime().compareTo(Calendar.getInstance().getTime()) >= 0 || DateUtils.isToday(eventDay.getCalendar().getTimeInMillis())) {
                    TrainBookFragment.this.clickedDayCalendar = eventDay.getCalendar();
                    TrainBookFragment.this.isDateSelect = true;
                    return;
                }
                TrainBookFragment.this.isDateSelect = false;
            }
        });
        Date time = this.clickedDayCalendar.getTime();
        String format = new SimpleDateFormat("dd/MM/yyyy").format(time);
        String format2 = new SimpleDateFormat("MMMM").format(time);
        String format3 = new SimpleDateFormat("EEEE").format(time);
        String format4 = new SimpleDateFormat("yyyy").format(time);
        this.tv_date.setText(format);
      /*  MyTextView myTextView = this.tv_date_month;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(format2);
        stringBuilder.append(" ");
        stringBuilder.append(format4);
        myTextView.setText(stringBuilder.toString());
        this.tv_date_day.setText(format3);
      */
        this.rv_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        this.rv_popular_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        inflate.setFocusableInTouchMode(true);
        inflate.requestFocus();
        inflate.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 4 || !TrainBookFragment.this.ll_search.isShown()) {
                    return false;
                }
                TrainBookFragment.this.ll_search.setVisibility(View.GONE);
                Log.e("KEYCODE_BACK", "=========>>>>>>>");
                return true;
            }
        });
        this.et_city.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("=====>>>>>");
                String str = " ";
                stringBuilder.append(editable.toString().startsWith(str));
                Log.e("afterTextChanged", stringBuilder.toString());
                if (editable.toString().equalsIgnoreCase("") || editable.toString().startsWith(str)) {
                    TrainBookFragment.this.rv_search.setVisibility(View.GONE);
                    TrainBookFragment.this.tv_popular_lable.setVisibility(View.VISIBLE);
                    TrainBookFragment.this.rv_popular_search.setVisibility(View.VISIBLE);
                    return;
                }
                if (!TrainBookFragment.this.rv_search.isShown()) {
                    TrainBookFragment.this.rv_search.setVisibility(View.VISIBLE);
                    TrainBookFragment.this.tv_popular_lable.setVisibility(View.GONE);
                    TrainBookFragment.this.rv_popular_search.setVisibility(View.GONE);
                }
                if (Utils.isNetworkAvailable(TrainBookFragment.this.mContext)) {
                    TrainBookFragment.this.CallApi(editable.toString());
                } else {
                    Toast.makeText(TrainBookFragment.this.mContext, TrainBookFragment.this.mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.cityBeenArrayList1 = (ArrayList) new Gson().fromJson(getString(R.string.popular_place), new TypeToken<ArrayList<CityBeen>>() {
        }.getType());


        if (preference.getString("from", null) != null) {
            tv_from.setText(preference.getString("from", null));
            tv_from_sation.setText(preference.getString("fromSation", null));
            fromCityBeen = new CityBeen();
            fromCityBeen.cityName = preference.getString("from", null);
            fromCityBeen.name = preference.getString("fromSation", null);
            fromCityBeen.code = preference.getString("fromcode", null);
        }
        if (preference.getString("to", null) != null) {

            tv_to.setText(preference.getString("to", null));
            tv_to_station.setText(preference.getString("toSation", null));
            toCityBeen = new CityBeen();
            toCityBeen.cityName = preference.getString("to", null);
            toCityBeen.name = preference.getString("toSation", null);
            toCityBeen.code = preference.getString("tocode", null);
        }

        return inflate;
    }

    @OnClick({R.id.ll_date})
    public void onClickDate() {
        this.rl_datepicker.setVisibility(View.VISIBLE);
        OpenCalender();
    }

    @OnClick({R.id.tv_done})
    public void onClickDone() {
        if (this.isDateSelect) {
            this.rl_datepicker.setVisibility(View.GONE);
            Date time = this.clickedDayCalendar.getTime();
            String format = new SimpleDateFormat("dd/MM/yyyy").format(time);
            String format2 = new SimpleDateFormat("MMMM").format(time);
            String format3 = new SimpleDateFormat("EEEE").format(time);
            String format4 = new SimpleDateFormat("yyyy").format(time);
            this.tv_date.setText(format);
          /*  MyTextView myTextView = this.tv_date_month;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(format2);
            stringBuilder.append(" ");
            stringBuilder.append(format4);
            myTextView.setText(stringBuilder.toString());
            this.tv_date_day.setText(format3);
          */
            return;
        }
        Toast.makeText(this.mContext, "Please Select Date", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv_search})
    public void onClickSearch() {
        CityBeen cityBeen = this.fromCityBeen;
        if (cityBeen == null || this.toCityBeen == null) {
            Toast.makeText(this.mContext, "Please select city.", Toast.LENGTH_SHORT).show();
        } else if (cityBeen.code == null || this.toCityBeen.code == null) {
            Toast.makeText(this.mContext, "Please select genuine city.", Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.tv_from.getText().toString());
            stringBuilder.append(" To ");
            stringBuilder.append(this.tv_to.getText().toString());
            String stringBuilder2 = stringBuilder.toString();
            Date time = this.clickedDayCalendar.getTime();
            String str = "yyyyMMdd";
            String format = new SimpleDateFormat(str).format(time);
            String format2 = new SimpleDateFormat("dd MMM,EEEE").format(time);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(this.fromCityBeen.code);
            String str2 = "/";
            stringBuilder3.append(str2);
            stringBuilder3.append(this.toCityBeen.code);
            stringBuilder3.append(str2);
            stringBuilder3.append(new SimpleDateFormat(str).format(time));
            String stringBuilder4 = stringBuilder3.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append("===>>>");
            stringBuilder.append(format2);
            Log.e("dates", stringBuilder.toString());
            TrainRouteListFragment trainRouteListFragment = new TrainRouteListFragment();
            openFragment(TrainRouteListFragment.newInstance(stringBuilder2, format2, stringBuilder4, format, this.fromCityBeen.code, this.toCityBeen.code), "TrainRouteListFragment");
            this.fromCityBeen = null;
            this.toCityBeen = null;
        }
    }

    @OnClick({R.id.ll_from})
    public void onClickFrom() {
        this.isFrom = true;
        this.trainpopularListAdapter = new SearchTrainListAdapter(this.mContext, this.cityBeenArrayList1, this, this.isFrom);
        this.rv_popular_search.setAdapter(this.trainpopularListAdapter);
        this.ll_search.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ll_to})
    public void onClickTo() {
        this.isFrom = false;
        this.trainpopularListAdapter = new SearchTrainListAdapter(this.mContext, this.cityBeenArrayList1, this, this.isFrom);
        this.rv_popular_search.setAdapter(this.trainpopularListAdapter);
        this.ll_search.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ll_to})
    public void onClickSearchCity() {
        if (!(this.et_city.getText().toString().toString().equalsIgnoreCase("") || this.et_city.getText().toString().toString().startsWith(" "))) {
            if (Utils.isNetworkAvailable(this.mContext)) {
                CallApi(this.et_city.getText().toString());
            } else {
                Context context = this.mContext;
                Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void OpenCalender() {
        Date date = new Date();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===>>>>");
        stringBuilder.append(date);
        Log.e("current date", stringBuilder.toString());
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTime(date);
        instance.add(5, -1);
        instance2.setTime(date);
        instance2.add(2, 4);
        this.calendarView.setMinimumDate(instance);
        this.calendarView.setMaximumDate(instance2);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void CallApi(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder newBuilder = HttpUrl.parse(trainbook).newBuilder();
        newBuilder.addQueryParameter("city", str);
        okHttpClient.newCall(new Request.Builder().url(newBuilder.build()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
            }

            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                if (string.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CityBeen>>() {
                    }.getType();
                    try {
                        cityBeenArrayList = (ArrayList) gson.fromJson(string, type);
                        trainListAdapter = new SearchTrainListAdapter(mContext, cityBeenArrayList, TrainBookFragment.this, TrainBookFragment.this.isFrom);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(activity == null || activity.isFinishing() || dataLoaderdialog == null)) {
                                    dataLoaderdialog.dismiss();
                                }
                                try {
                                    rv_search.setAdapter(trainListAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                Log.i("AsyncOkHttp_no_data", "Failed To load");
            }
        });
    }

    public void onClickTrianList(CityBeen cityBeen, boolean z) {
        String str = ",";
        String str2 = "";
        MyTextView myTextView;
        StringBuilder stringBuilder;
        if (z) {
            try {
                this.fromCityBeen = new CityBeen();
                this.fromCityBeen = cityBeen;
                Log.e(TAG, "onClickTrianList: " + cityBeen.cityName);
                if (this.fromCityBeen.cityName != null) {
                    myTextView = this.tv_from;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this.fromCityBeen.cityName.split(str)[0]);
                    stringBuilder.append(str2);
                    myTextView.setText(stringBuilder.toString());
                    editor.putString("from", stringBuilder.toString());
                    editor.apply();
                }
                myTextView = this.tv_from_sation;
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.fromCityBeen.code);
                stringBuilder.append(str2);
                myTextView.setText(stringBuilder.toString());
                editor.putString("fromSation", stringBuilder.toString());
                editor.apply();
                editor.putString("fromcode", cityBeen.code);
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.toCityBeen = new CityBeen();
            this.toCityBeen = cityBeen;
            Log.e(TAG, "onClickTrianList: " + cityBeen.cityName);
            if (this.toCityBeen.cityName != null) {
                myTextView = this.tv_to;
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.toCityBeen.cityName.split(str)[0]);
                stringBuilder.append(str2);
                myTextView.setText(stringBuilder.toString());
                editor.putString("to", stringBuilder.toString());
                editor.apply();

            }
            myTextView = this.tv_to_station;
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.toCityBeen.code);
            stringBuilder.append(str2);
            myTextView.setText(stringBuilder.toString());
            editor.putString("toSation", stringBuilder.toString());
            editor.apply();
            editor.putString("tocode", cityBeen.code);
            editor.apply();

        }
        this.et_city.setText(str2);
        this.cityBeenArrayList.clear();
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_city);
    }

    public void openFragment(Fragment fragment, String tag) {
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame, fragment, tag);
        beginTransaction.addToBackStack(fragment.toString());
        beginTransaction.commit();
        MainActivity.bottomNavigation.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back_search})
    public void onClickSearchBack() {
        this.ll_search.setVisibility(View.GONE);
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

    public void btnsClick() {
        cv_train_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrainScheduleMainActivity.class));
            }
        });

        cv_live_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LiveStatusMainActivity.class));

            }
        });
        cv_live_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LiveStationMainActivity.class));

            }
        });
        cv_pnr_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PNRStatusMainActivity.class));

            }
        });
    }


    public void ShowToast(Context context, String info) {
        Toast toast = Toast.makeText(context, Html.fromHtml("<font color='#2c1e5b' ><b>" + info + "</b></font>"), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.DISPLAY_CLIP_VERTICAL, 0, 0);
        toast.show();
    }
}
