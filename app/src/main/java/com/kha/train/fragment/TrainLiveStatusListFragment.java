package com.kha.train.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.kha.train.R;
import com.kha.train.adapter.SearchTrainListByCdNAdapter;
import com.kha.train.adapter.SearchTrainListByCdNAdapter.setTrianListClickListner;
import com.kha.train.adapter.SerachTrainListByPtm;
import com.kha.train.model.Train;
import com.kha.train.model.TrainListBeen;
import com.kha.train.model.TrainListBeen.TrainBtwnStnsList;
import com.kha.train.model.TrainListByCdNBeen;
import com.kha.train.model.TrainListByCdNBeen.C1089R;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyEditText;
import com.kha.train.widget.MyTextView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.kha.train.BuildConfig.livetrainstatuslist;

public class TrainLiveStatusListFragment extends Fragment implements setTrianListClickListner, SerachTrainListByPtm.setTrianListClickListner {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static FragmentActivity fragmentActivity;
    Activity activity;
    TrainListByCdNBeen cityBeenArrayList1;
    @BindView(R.id.et_train)
    MyEditText et_train;
    @BindView(R.id.fl_adplaceholder)
    FrameLayout fl_adplaceholder;
    TrainListByCdNBeen listByCdNBeen;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.ll_search_date)
    LinearLayout ll_search_date;
    private AdView mAdView;
    Context mContext;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_popular_search)
    RecyclerView rv_popular_search;
    @BindView(R.id.rv_search)
    RecyclerView rv_search;
    SerachTrainListByPtm serachTrainListByPtmAdapter;
    SearchTrainListByCdNAdapter trainListAdapter;
    ArrayList<Train> train_list;
    @BindView(R.id.tv_date)
    MyTextView tv_date;
    @BindView(R.id.tv_day_befor)
    MyTextView tv_day_befor;
    @BindView(R.id.tv_popular_lable)
    MyTextView tv_popular_lable;
    @BindView(R.id.tv_today)
    MyTextView tv_today;
    @BindView(R.id.tv_tommorr)
    MyTextView tv_tommorr;
    @BindView(R.id.tv_train_id)
    MyTextView tv_train_id;
    @BindView(R.id.tv_train_name)
    MyTextView tv_train_name;
    @BindView(R.id.tv_train_stop)
    MyTextView tv_train_stop;
    @BindView(R.id.tv_yester)
    MyTextView tv_yester;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void onSaveInstanceState(Bundle bundle) {
    }

    public void onClickTrianList(Train train) {
        MyTextView myTextView = this.tv_train_id;
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        stringBuilder.append(str);
        stringBuilder.append(train.train_number);
        myTextView.setText(stringBuilder.toString());
        myTextView = this.tv_train_name;
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(train.train_name);
        myTextView.setText(stringBuilder.toString());
        this.et_train.setText(str);
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_train);
    }

    public static TrainLiveStatusListFragment newInstance(String str, String str2) {
        TrainLiveStatusListFragment trainLiveStatusListFragment = new TrainLiveStatusListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, str);
        bundle.putString(ARG_PARAM2, str2);
        trainLiveStatusListFragment.setArguments(bundle);
        return trainLiveStatusListFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_train_live_status_list, viewGroup, false);
        fragmentActivity = getActivity();
        ButterKnife.bind((Object) this, inflate);


        if (TrainBookFragment.preference.getString("TrainLiveStatusTrainNumber",null)!=null){
            this.tv_train_id.setText(TrainBookFragment.preference.getString("TrainLiveStatusTrainNumber",null));
            this.tv_train_name.setText(TrainBookFragment.preference.getString("TrainLiveStatusTrainName",null));
        }

        this.mAdView = (AdView) inflate.findViewById(R.id.ad_view);
        this.mAdView.loadAd(new Builder().build());
        this.mContext = getActivity();
        inflate.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                if (TrainLiveStatusListFragment.this.ll_search.isShown()) {
                    TrainLiveStatusListFragment.this.ll_search.setVisibility(View.GONE);
                    Log.e("KEYCODE_BACK", "=========>>>>>>>");
                    return true;
                } else if (!TrainLiveStatusListFragment.this.ll_search_date.isShown()) {
                    return false;
                } else {
                    TrainLiveStatusListFragment.this.ll_search_date.setVisibility(View.GONE);
                    return true;
                }
            }
        });
        this.rv_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        this.rv_popular_search.setLayoutManager(new LinearLayoutManager(this.mContext, RecyclerView.VERTICAL, false));
        this.et_train.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("=====>>>>>");
                stringBuilder.append(editable.toString());
                Log.e("afterTextChanged", stringBuilder.toString());
                if (editable.toString().equalsIgnoreCase("") || editable.toString().startsWith(" ")) {
                    TrainLiveStatusListFragment.this.rv_search.setVisibility(View.GONE);
                    TrainLiveStatusListFragment.this.tv_popular_lable.setVisibility(View.VISIBLE);
                    TrainLiveStatusListFragment.this.rv_popular_search.setVisibility(View.VISIBLE);
                    return;
                }
                if (!TrainLiveStatusListFragment.this.rv_search.isShown()) {
                    TrainLiveStatusListFragment.this.rv_search.setVisibility(View.VISIBLE);
                    TrainLiveStatusListFragment.this.tv_popular_lable.setVisibility(View.GONE);
                    TrainLiveStatusListFragment.this.rv_popular_search.setVisibility(View.GONE);
                }
                if (Utils.isNetworkAvailable(TrainLiveStatusListFragment.this.mContext)) {
                    TrainLiveStatusListFragment.this.CallApi(editable.toString());
                } else {
                    Toast.makeText(TrainLiveStatusListFragment.this.mContext, TrainLiveStatusListFragment.this.mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Calendar instance = Calendar.getInstance();
        String str = "EEEE, dd MMM yy";
        this.tv_today.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, -2);
        this.tv_day_befor.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, 1);
        this.tv_yester.setText(Utils.changeDateFormate(instance.getTime(), str));
        instance.add(5, 2);
        this.tv_tommorr.setText(Utils.changeDateFormate(instance.getTime(), str));
        this.tv_date.setText(this.tv_today.getText().toString());
        this.cityBeenArrayList1 = (TrainListByCdNBeen) new Gson().fromJson(getString(R.string.popular_train_schedule), new TypeToken<TrainListByCdNBeen>() {
        }.getType());
        return inflate;
    }

    @OnClick({R.id.ll_train})
    public void onClickTrain() {
        this.trainListAdapter = new SearchTrainListByCdNAdapter(this.mContext, this.cityBeenArrayList1.data.f101r, this);
        this.rv_popular_search.setAdapter(this.trainListAdapter);
        this.ll_search.setVisibility(View.VISIBLE);
        this.ll_search_date.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_date})
    public void onClickDate() {
        this.ll_search_date.setVisibility(View.VISIBLE);
        this.ll_search.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_day_before})
    public void onClickDayBefore() {
        this.tv_date.setText(this.tv_day_befor.getText().toString());
        this.ll_search_date.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_yesterday})
    public void onClickYesterDay() {
        this.tv_date.setText(this.tv_yester.getText().toString());
        this.ll_search_date.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_today})
    public void onClicktoday() {
        this.tv_date.setText(this.tv_today.getText().toString());
        this.ll_search_date.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_tommorr})
    public void onClickTommorr() {
        this.tv_date.setText(this.tv_tommorr.getText().toString());
        this.ll_search_date.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_search})
    public void onClickSearch() {
        if (this.tv_train_id.getText().toString().length() > 0) {
            new TrainListBeen().getClass();
            TrainBtwnStnsList trainBtwnStnsList = new TrainBtwnStnsList();
            trainBtwnStnsList.trainNumber = this.tv_train_id.getText().toString();
            trainBtwnStnsList.trainName = this.tv_train_name.getText().toString();
            init(new Gson().toJson(trainBtwnStnsList), Utils.changeDateFormate(this.tv_date.getText().toString(), "EEEE, dd MMM yy", "yyyyMMdd"));

            TrainBookFragment.editor.putString("TrainLiveStatusTrainNumber", this.tv_train_id.getText().toString());
            TrainBookFragment.editor.apply();
            TrainBookFragment.editor.putString("TrainLiveStatusTrainName", this.tv_train_name.getText().toString());
            TrainBookFragment.editor.apply();
            return;
        }
        Toast.makeText(this.mContext, "Please Select Train", Toast.LENGTH_SHORT).show();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    public void init(String str, String str2) {
        LiveStatusFragment liveStatusFragment = new LiveStatusFragment();
        openFragment(LiveStatusFragment.newInstance(str, str2));
    }

    public void onClickTrianList(C1089R c1089r) {
        MyTextView myTextView = this.tv_train_id;
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        stringBuilder.append(str);
        stringBuilder.append(c1089r.xtr.tid);
        myTextView.setText(stringBuilder.toString());
        myTextView = this.tv_train_name;
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(c1089r.f104n);
        myTextView.setText(stringBuilder.toString());
        this.et_train.setText(str);
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_train);
    }

    @OnClick({R.id.iv_search})
    public void onClickSearch1() {
        if (!(this.et_train.getText().toString().equalsIgnoreCase("") || this.et_train.getText().toString().startsWith(" "))) {
            if (Utils.isNetworkAvailable(this.mContext)) {
                CallApi(this.et_train.getText().toString());
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
    public void onClickBackeSearch() {
        this.et_train.setText("");
        this.ll_search.setVisibility(View.GONE);
        Utils.hideKeyboardFrom(this.mContext, this.et_train);
    }

    public void CallApi(String str) {
        OkHttpClient okHttpClient = new OkHttpClient();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(livetrainstatuslist);
        stringBuilder.append(str);
        okHttpClient.newCall(new Request.Builder().url(HttpUrl.parse(stringBuilder.toString()).newBuilder().build()).build()).enqueue(new Callback() {
            public void onFailure(Call call, IOException iOException) {
                Log.e("AsyncOkHttp", "Error in getting response");
                TrainLiveStatusListFragment.this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(TrainLiveStatusListFragment.this.mContext, "Network Problem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                try {
                    TrainLiveStatusListFragment.this.train_list = new ArrayList();
                    if (string.length() > 0) {
                        JSONArray jSONArray = new JSONObject(string).getJSONArray("body");
                        int i = 0;
                        jSONArray = jSONArray.getJSONObject(0).getJSONArray("trains");
                        if (jSONArray.length() > 0) {
                            while (i < jSONArray.length()) {
                                JSONObject jSONObject = jSONArray.getJSONObject(i);
                                Train train = new Train();
                                train.train_name = jSONObject.get("trainName").toString();
                                train.train_number = jSONObject.get("trainNumber").toString();
                                TrainLiveStatusListFragment.this.train_list.add(train);
                                i++;
                            }
                        }
                        TrainLiveStatusListFragment.this.serachTrainListByPtmAdapter = new SerachTrainListByPtm(TrainLiveStatusListFragment.this.train_list, TrainLiveStatusListFragment.this.mContext, TrainLiveStatusListFragment.this);
                        TrainLiveStatusListFragment.this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                TrainLiveStatusListFragment.this.rv_search.setAdapter(TrainLiveStatusListFragment.this.serachTrainListByPtmAdapter);
                            }
                        });
                        return;
                    }
                    Log.i("AsyncOkHttp_no_data", body.string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction beginTransaction;
        if (getActivity() != null) {
            beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.frame, fragment);
            beginTransaction.addToBackStack(fragment.toString());
            beginTransaction.commit();
            return;
        }
        beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
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
