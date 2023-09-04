package com.kha.train.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kha.train.R;
import com.kha.train.adapter.LiveStationListAdapter;
import com.kha.train.adapter.SearchTrainListAdapter;
import com.kha.train.adapter.SearchTrainListAdapter.setTrianListClickListner;
import com.kha.train.dialog.DataLoaderdialog;
import com.kha.train.model.CityBeen;
import com.kha.train.model.LiveStationBeen;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyEditText;
import com.kha.train.widget.MyTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

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

import static com.kha.train.BuildConfig.traincitylivestation;
import static com.kha.train.BuildConfig.trainlivestation;

public class LiveStationFragment extends Fragment implements setTrianListClickListner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Activity activity;
    ArrayList<CityBeen> cityBeenArrayList = new ArrayList();
    ArrayList<CityBeen> cityBeenArrayList1 = new ArrayList();
    DataLoaderdialog dataLoaderdialog;
    String desCode = "";
    @BindView(R.id.et_des_name)
    MyEditText et_des_name;
    @BindView(R.id.layout_no_data)
    View layout_no_data;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.ll_st_list)
    RelativeLayout ll_st_list;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String mParam2;
    @BindView(R.id.rv_live_st)
    RecyclerView rv_live_st;
    @BindView(R.id.rv_popular_search)
    RecyclerView rv_popular_search;
    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    private String stCode = "SUR";
    SearchTrainListAdapter trainListAdapter;
    @BindView(R.id.tv_dest_st)
    MyTextView tv_dest_st;
    @BindView(R.id.tv_junction)
    MyTextView tv_junction;
    @BindView(R.id.tv_popular_lable)
    MyTextView tv_popular_lable;

    @Override
    public void onClickTrianList(CityBeen cityBeen, boolean z) {
        CityBeen cityBeen2 = new CityBeen();
        this.desCode = cityBeen.code;
        this.et_des_name.setText("");
        this.cityBeenArrayList.clear();
        this.ll_search.setVisibility(View.GONE);
        this.ll_st_list.setVisibility(View.VISIBLE);
        Utils.hideKeyboardFrom(this.mContext, this.et_des_name);
        callApi();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static LiveStationFragment newInstance(String str) {
        LiveStationFragment liveStationFragment = new LiveStationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        liveStationFragment.setArguments(bundle);
        return liveStationFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.stCode = getArguments().getString(ARG_PARAM1);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_station, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.mContext = getActivity();
        this.dataLoaderdialog = new DataLoaderdialog(this.mContext);
        this.rv_live_st.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        this.rv_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        this.rv_popular_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        if (Utils.isNetworkAvailable(this.mContext)) {
            callApi();
        } else {
            Context context = this.mContext;
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        this.et_des_name.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.toString().equalsIgnoreCase("")) {
                        LiveStationFragment.this.rv_search.setVisibility(View.GONE);
                        LiveStationFragment.this.tv_popular_lable.setVisibility(View.VISIBLE);
                        LiveStationFragment.this.rv_popular_search.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (!LiveStationFragment.this.rv_search.isShown()) {
                        LiveStationFragment.this.rv_search.setVisibility(View.VISIBLE);
                        LiveStationFragment.this.tv_popular_lable.setVisibility(View.GONE);
                        LiveStationFragment.this.rv_popular_search.setVisibility(View.GONE);
                    }
                    if (Utils.isNetworkAvailable(LiveStationFragment.this.mContext)) {
                        try {
                            LiveStationFragment.this.CallStationApi(editable.toString());
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    Toast.makeText(LiveStationFragment.this.mContext, LiveStationFragment.this.mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.cityBeenArrayList1 = (ArrayList) new Gson().fromJson(getString(R.string.popular_station_place), new TypeToken<ArrayList<CityBeen>>() {
        }.getType());
        return inflate;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void callApi() {
        this.dataLoaderdialog.show();
        OkHttpClient okHttpClient = new OkHttpClient();
        Builder newBuilder = HttpUrl.parse(trainlivestation).newBuilder();
        JSONObject jSONObject = new JSONObject();
        MediaType parse = MediaType.parse("application/json");
        try {
            jSONObject.put("sourceStationCode", this.stCode);
            jSONObject.put("destinationStationCode", this.desCode);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("channelCode", "PWA");
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
                LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!(LiveStationFragment.this.activity == null || LiveStationFragment.this.activity.isFinishing() || LiveStationFragment.this.dataLoaderdialog == null)) {
                            LiveStationFragment.this.dataLoaderdialog.dismiss();
                        }
                        Toast.makeText(LiveStationFragment.this.mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                try {
                    if (new JSONObject(string).has("Error")) {
                        LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(LiveStationFragment.this.activity == null || LiveStationFragment.this.activity.isFinishing() || LiveStationFragment.this.dataLoaderdialog == null)) {
                                    LiveStationFragment.this.dataLoaderdialog.dismiss();
                                }
                                LiveStationFragment.this.ll_data.setVisibility(View.GONE);
                                LiveStationFragment.this.layout_no_data.setVisibility(View.VISIBLE);
                            }
                        });
                    } else if (string.length() > 0) {
                        final LiveStationBeen liveStationBeen = (LiveStationBeen) new Gson().fromJson(string, new TypeToken<LiveStationBeen>() {
                        }.getType());
                        final LiveStationListAdapter liveStationListAdapter = new LiveStationListAdapter(LiveStationFragment.this.mContext, liveStationBeen.trains);
                        LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (!(LiveStationFragment.this.activity == null || LiveStationFragment.this.activity.isFinishing() || LiveStationFragment.this.dataLoaderdialog == null || !LiveStationFragment.this.dataLoaderdialog.isShowing())) {
                                    LiveStationFragment.this.dataLoaderdialog.dismiss();
                                }
                                try {
                                    MyTextView myTextView = LiveStationFragment.this.tv_junction;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("");
                                    stringBuilder.append(liveStationBeen.title);
                                    myTextView.setText(stringBuilder.toString());
                                    LiveStationFragment.this.rv_live_st.setAdapter(liveStationListAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Log.i("AsyncOkHttp_no_data", body.string());
                        LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (LiveStationFragment.this.activity != null && !LiveStationFragment.this.activity.isFinishing() && LiveStationFragment.this.dataLoaderdialog != null) {
                                    LiveStationFragment.this.dataLoaderdialog.dismiss();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void CallStationApi(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(traincitylivestation);
        stringBuilder.append(str);
        okHttpClient.newCall(new Request.Builder().url(HttpUrl.parse(stringBuilder.toString()).newBuilder().build()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LiveStationFragment.this.mContext, "Error in getting response", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===>>>>");
                stringBuilder.append(string);
                Log.e("jsonOutput", stringBuilder.toString());
                LiveStationFragment.this.cityBeenArrayList.clear();
                if (string.length() > 0) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<CityBeen>>() {
                    }.getType();
                    try {
                        LiveStationFragment.this.cityBeenArrayList = (ArrayList) gson.fromJson(string, type);
                        LiveStationFragment.this.trainListAdapter = new SearchTrainListAdapter(LiveStationFragment.this.mContext, LiveStationFragment.this.cityBeenArrayList, LiveStationFragment.this);
                        LiveStationFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                LiveStationFragment.this.rv_search.setAdapter(LiveStationFragment.this.trainListAdapter);
                            }
                        });
                        return;
                    } catch (Exception unused) {
                        return;
                    }
                }
                Log.i("AsyncOkHttp_no_data", body.string());
            }
        });
    }

    @OnClick({R.id.tv_dest_st})
    public void onClickDestSt() {
        this.ll_st_list.setVisibility(View.GONE);
        this.trainListAdapter = new SearchTrainListAdapter(this.mContext, this.cityBeenArrayList1, this);
        this.rv_popular_search.setAdapter(this.trainListAdapter);
        this.ll_search.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.iv_back_search})
    public void onClickSearchBack() {
        this.et_des_name.setText("");
        this.cityBeenArrayList.clear();
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_des_name);
    }

    @OnClick({R.id.iv_back})
    public void onClickBack() {
        getActivity().onBackPressed();
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
