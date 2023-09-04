package com.kha.train.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kha.train.R;
import com.kha.train.adapter.SearchTrainListAdapter.setTrianListClickListner;
import com.kha.train.adapter.newLiveTrainListAdapter;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.CityBeen;
import com.kha.train.util.Utils;
import com.kha.train.widget.GpsTracker;
import com.kha.train.widget.MyEditText;
import com.kha.train.widget.MyTextView;

import com.google.android.gms.ads.AdRequest.Builder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kha.train.BuildConfig.traincitylivestation;

public class LiveStationListFragment extends Fragment implements setTrianListClickListner, newLiveTrainListAdapter.setTrianListClickListner {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Activity activity;
    String TAG="LiveStationListFragment";

    ArrayList<CityBeen> cityBeenArrayList = new ArrayList();
    ArrayList<CityBeen> cityBeenArrayList1 = new ArrayList();
    DataLoaderdialog dataLoaderdialog;
    @BindView(R.id.et_station)
    MyEditText et_station;
    @BindView(R.id.fl_adplaceholder)
    FrameLayout fl_adplaceholder;
    GpsTracker gpsTracker;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.ll_station)
    LinearLayout ll_station;
    private com.google.android.gms.ads.AdView mAdView;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_popular_search)
    RecyclerView rv_popular_search;
    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    newLiveTrainListAdapter trainListAdapter;
    @BindView(R.id.tv_popular_lable)
    MyTextView tv_popular_lable;
    @BindView(R.id.tv_station_code)
    MyTextView tv_station_code;
    @BindView(R.id.tv_station_name)
    MyTextView tv_station_name;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static LiveStationListFragment newInstance(String str, String str2) {
        LiveStationListFragment liveStationListFragment = new LiveStationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        liveStationListFragment.setArguments(bundle);
        return liveStationListFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_station_list, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.mAdView = (com.google.android.gms.ads.AdView) inflate.findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
        this.mContext = getActivity();
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);

        if (TrainBookFragment.preference.getString("LiveStationFragmentTrainCode",null)!=null){
            tv_station_code.setText(TrainBookFragment.preference.getString("LiveStationFragmentTrainCode",null));
            tv_station_name.setText(TrainBookFragment.preference.getString("LiveStationFragmentTrainName",null));

        }

        cityBeenArrayList1.clear();

        this.cityBeenArrayList1 = (ArrayList) new Gson().fromJson(getString(R.string.popular_station_place), new TypeToken<ArrayList<CityBeen>>() {  }.getType());


        Log.e(TAG, "onCreateView: "+cityBeenArrayList1.get(0).cityName );
        rv_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        rv_popular_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));


        trainListAdapter=new newLiveTrainListAdapter(getActivity(), cityBeenArrayList1, new LiveStationListFragment());
        rv_popular_search.setAdapter(trainListAdapter);
        this.et_station.setText("");

        this.et_station.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("=====>>>>>");
                stringBuilder.append(editable.toString());
                Log.e("afterTextChanged", stringBuilder.toString());
                if (editable.toString().equalsIgnoreCase("") || editable.toString().startsWith(" ")) {
                    rv_search.setVisibility(View.GONE);
                    tv_popular_lable.setVisibility(View.VISIBLE);
                    rv_popular_search.setVisibility(View.VISIBLE);
                    return;
                }
                if (!rv_search.isShown()) {
                    rv_search.setVisibility(View.VISIBLE);
                    tv_popular_lable.setVisibility(View.GONE);
                    rv_popular_search.setVisibility(View.GONE);
                }
                if (Utils.isNetworkAvailable(mContext)) {
                    CallApi(editable.toString());

                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // (ArrayList) new Gson().fromJson(getString(R.string.popular_place), new TypeToken<ArrayList<CityBeen>>() { }.getType());

      //  LoadLocationCity();

        return inflate;
    }


    private void LoadLocationCity() {
        this.gpsTracker = new GpsTracker(getActivity());
        if (this.gpsTracker.canGetLocation()) {
            try {
                List fromLocation = new Geocoder(getActivity(), Locale.getDefault()).getFromLocation(this.gpsTracker.getLatitude(), this.gpsTracker.getLongitude(), 1);
                if (fromLocation.size() > 0) {
                    this.et_station.setText(((Address) fromLocation.get(0)).getLocality());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.ll_station})
    public void onClickStation() {
        Log.e(" ll_station", "onClickStation: call method" );
        ll_search.setVisibility(View.VISIBLE);
        trainListAdapter = new newLiveTrainListAdapter(this.mContext, cityBeenArrayList1, this);
        rv_popular_search.setAdapter(trainListAdapter);

    }

    @OnClick({R.id.tv_search})
    public void onClickSearch() {
        if (this.tv_station_code.getText().toString().length() > 0) {
            LiveStationFragment liveStationFragment = new LiveStationFragment();
            openFragment(LiveStationFragment.newInstance(this.tv_station_code.getText().toString()));

            TrainBookFragment.editor.putString("LiveStationFragmentTrainCode",tv_station_code.getText().toString());
            TrainBookFragment.editor.apply();
            TrainBookFragment.editor.putString("LiveStationFragmentTrainName",tv_station_name.getText().toString());
            TrainBookFragment.editor.apply();
            return;
        }
        Toast.makeText(this.mContext, "Please Select Station.", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.iv_search})
    public void onClickSearch1() {
        if (!(this.et_station.getText().toString().equalsIgnoreCase("") || this.et_station.getText().toString().startsWith(" "))) {
            if (Utils.isNetworkAvailable(this.mContext)) {
                CallApi(this.et_station.getText().toString());
            } else {
                Context context = this.mContext;
                Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        getActivity().onBackPressed();
    }

    @OnClick({R.id.iv_back_search})
    public void onClickSearchBack() {
        this.et_station.setText("");
        this.cityBeenArrayList.clear();
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_station);
    }

    public void CallApi(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(traincitylivestation);
        stringBuilder.append(str);
        okHttpClient.newCall(new Request.Builder().url(HttpUrl.parse(stringBuilder.toString()).newBuilder().build()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                            Toast.makeText(mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                        trainListAdapter = new newLiveTrainListAdapter(mContext, cityBeenArrayList, LiveStationListFragment.this);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (activity != null && !activity.isFinishing() && dataLoaderdialog != null) {
                                    rv_search.setAdapter(trainListAdapter);
                                }
                            }
                        });
                    } catch (Exception unused) {
                    }
                }
            }
        });
    }

    public void onClickTrianList(CityBeen cityBeen, boolean z) {
        CityBeen cityBeen2 = new CityBeen();
        MyTextView myTextView = this.tv_station_name;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cityBeen.name);
        String str = "";
        stringBuilder.append(str);
        myTextView.setText(stringBuilder.toString());
        myTextView = this.tv_station_code;
        stringBuilder = new StringBuilder();
        stringBuilder.append(cityBeen.code);
        stringBuilder.append(str);
        myTextView.setText(stringBuilder.toString());
        this.et_station.setText(str);
        this.cityBeenArrayList.clear();
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_station);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.frame, fragment);
        beginTransaction.addToBackStack(fragment.toString());
        beginTransaction.commit();
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
