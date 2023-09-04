package com.kha.train.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.kha.train.R;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.model.TrainListBeen.TrainBtwnStnsList;
import com.kha.train.model.TrainTicketClassBeen;
import com.kha.train.model.TrainTicketClassBeen.AvlDayList;
import com.kha.train.util.Constants;
import com.kha.train.util.SharedPreference;
import com.kha.train.util.Utils;
import com.kha.train.widget.MyTextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.kha.train.BuildConfig.trainlistavailabity;

public class TrainListAdapter extends Adapter<TrainListAdapter.MyViewHolder> {

    private ArrayList<String> al_images;
    // View child;
    String doj = "20190315";
    LayoutInflater inflater;
    boolean is24formt = true;
    Context mContext;
    setOnTrainDetailListner onTrainDetailListner;
    int pos = 0;
    String quota = "GN";
    ArrayList<TrainBtwnStnsList> trainBtwnStnsList;
    String TAG = "TrainListAdapter";
    public static String trainName = "";
    public static String trainNumber = "";
    AsyncTask asyncTask;
//   trainName=((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).trainName;
//    trainNumber=((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).trainNumber;


    public interface setOnTrainDetailListner {
        void onClickTicketClassListner(int i);
    }

    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_general_check)
        ImageView iv_general_check;
        @BindView(R.id.iv_ladies_check)
        ImageView iv_ladies_check;
        @BindView(R.id.iv_tatkal_check)
        ImageView iv_tatkal_check;
        @BindView(R.id.ll_gen)
        LinearLayout ll_gen;
        @BindView(R.id.ll_ladis)
        LinearLayout ll_ladis;
        @BindView(R.id.ll_main)
        LinearLayout ll_main;
        @BindView(R.id.ll_nearby_date)
        LinearLayout ll_nearby_date;
        @BindView(R.id.ll_nearby_train)
        LinearLayout ll_nearby_train;
        @BindView(R.id.ll_nearby_train_list)
        LinearLayout ll_nearby_train_list;
        @BindView(R.id.ll_site)
        LinearLayout ll_site;
        @BindView(R.id.ll_tatkal)
        LinearLayout ll_tatkal;
        @BindView(R.id.pb_loader)
        ProgressBar pb_loader;
        @BindView(R.id.tv_arrive_time)
        MyTextView tv_arrive_time;
        @BindView(R.id.tv_depture_time)
        MyTextView tv_depture_time;
        @BindView(R.id.tv_from_name)
        MyTextView tv_from_name;
        @BindView(R.id.tv_time_diff)
        MyTextView tv_time_diff;
        @BindView(R.id.tv_title)
        MyTextView tv_title;
        @BindView(R.id.tv_to_name)
        MyTextView tv_to_name;
        @BindView(R.id.tv_train_id)
        MyTextView tv_train_id;

        ImageView dropDownIC;

        public MyViewHolder(View view) {
            super(view);
            dropDownIC = view.findViewById(R.id.dropDownIC);
            ButterKnife.bind((Object) this, view);
        }
    }


    public TrainListAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TrainListAdapter(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.al_images = arrayList;
    }

    public TrainListAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onTrainDetailListner = trainRouteListFragment;
    }

    public TrainListAdapter(Context context, ArrayList<TrainBtwnStnsList> arrayList, TrainRouteListFragment trainRouteListFragment, String str) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onTrainDetailListner = trainRouteListFragment;
        this.trainBtwnStnsList = arrayList;
        this.doj = str;
        this.is24formt = new SharedPreference(context).getTimeFormate();
        Log.e(TAG, "TrainListAdapter: call adapter");
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raillist_rowlist, viewGroup, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(final MyViewHolder myViewHolder, final int ip) {
        myViewHolder.tv_title.setText(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).trainName);
//        MyTextView myTextView = myViewHolder.tv_train_id;
        // removed Hashtag from train number
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).trainNumber);
        myViewHolder.tv_train_id.setText(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).trainNumber);
        myViewHolder.tv_depture_time.setText(com.kha.train.util.Utils.changeTimeFormat(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).departureTime, this.is24formt));
        myViewHolder.tv_arrive_time.setText(com.kha.train.util.Utils.changeTimeFormat(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).arrivalTime, this.is24formt));
//        myTextView = myViewHolder.tv_from_name;
        stringBuilder = new StringBuilder();
        stringBuilder.append(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).frmStnCode);
        String str = " ";
        stringBuilder.append(str);
        stringBuilder.append(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).frmStnName);
        myViewHolder.tv_from_name.setText(stringBuilder.toString());
//        myTextView = myViewHolder.tv_to_name;
        stringBuilder = new StringBuilder();
        stringBuilder.append(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).toStnCode);
        stringBuilder.append(str);
        stringBuilder.append(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).toStnName);

        myViewHolder.tv_to_name.setText(stringBuilder.toString());
        myViewHolder.tv_time_diff.setText(com.kha.train.util.Utils.getTimeDiff(((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).departureTime, ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).arrivalTime));
        myViewHolder.ll_main.removeAllViews();
        myViewHolder.ll_nearby_train.setVisibility(View.GONE);
        myViewHolder.ll_site.setVisibility(View.VISIBLE);
        myViewHolder.ll_nearby_date.setTag("0");

        myViewHolder.ll_nearby_date.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String str = "0";
                if (myViewHolder.ll_nearby_date.getTag().toString().equalsIgnoreCase(str)) {

                    myViewHolder.ll_nearby_train.setVisibility(View.VISIBLE);

                    myViewHolder.ll_nearby_date.setTag("1");

                    Glide.with(mContext).load(R.drawable.ic_drop_up_arrow).into(myViewHolder.dropDownIC);

                    myViewHolder.ll_site.setVisibility(View.GONE);

                    myViewHolder.ll_main.removeAllViews();


                    for (int iL = 0; iL < ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.size(); iL++) {

                        TrainListAdapter trainListAdapter = TrainListAdapter.this;

                        View child = trainListAdapter.inflater.inflate(R.layout.layout_ticketclass_rowlist, null, false);

                        final LinearLayout linearLayoutf = (LinearLayout) child.findViewById(R.id.ll_main);

                        MyTextView myTextView8 = (MyTextView) child.findViewById(R.id.tv_name);

                        final MyTextView myTextView2 = (MyTextView) child.findViewById(R.id.tv_price);

                        if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iL)).equalsIgnoreCase("1A")) {
                            myTextView8.setText(Constants.TIER_1);
                        } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iL)).equalsIgnoreCase("2A")) {
                            myTextView8.setText(Constants.TIER_2);
                        } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iL)).equalsIgnoreCase("3A")) {
                            myTextView8.setText(Constants.TIER_3);
                        } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iL)).equalsIgnoreCase("Sl")) {
                            myTextView8.setText(Constants.SLEEPER);
                        }

                        int finalIL1 = iL;
                        try {
                            new AsyncTask<Void, Void, String>() {
                                public void onPreExecute() {
                                    super.onPreExecute();
                                    myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                                }

                                public String doInBackground(Void... voidArr) {
                                    if (com.kha.train.util.Utils.isNetworkAvailable(mContext)) {
                                        String CallApi="";
                                        try { CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(finalIL1), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber);
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("=====>>>");
                                            stringBuilder.append(CallApi);
                                            Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                            Log.e(TAG, "doInBackground: "+e.getMessage() );
                                        }
                                        return CallApi;
                                    }
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        public void run() {
                                            Context context = mContext;
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("");
                                            stringBuilder.append(mContext.getString(R.string.no_internet));
                                            Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return null;
                                }

                                public void onPostExecute(String str) {
                                    super.onPostExecute(str);
                                    if (str != null) {
                                        myViewHolder.pb_loader.setVisibility(View.GONE);
                                        TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                        }.getType());
                                        if (trainTicketClassBeen.totalFare != null) {
                                            MyTextView myTextView = myTextView2;
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append(mContext.getString(R.string.Rs));
                                            stringBuilder.append(" ");
                                            stringBuilder.append(trainTicketClassBeen.totalFare);
                                            myTextView.setText(stringBuilder.toString());
                                        }
                                        myTextView2.setBackgroundResource(0);

                                        return;
                                    }
                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                }
                            }.execute(new Void[0]);
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                        }

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(iL);
                        stringBuilder.append("");
                        linearLayoutf.setTag(stringBuilder.toString());
                        child.findViewById(R.id.v_select);

                        Log.e("myViewHolder.ll_main333", "" + myViewHolder.ll_main.getChildCount());


                        //int finalIL = ip;
                        int finalIL = iL;
                        linearLayoutf.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {

                                for (int i = 0; i < myViewHolder.ll_main.getChildCount(); i++) {

                                    Log.e("count lin", "=========**" + myViewHolder.ll_main.getChildCount());


                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("==>>");
                                    stringBuilder.append(linearLayoutf.getTag().toString());
                                    Log.e("ll_child_main.getTag()", stringBuilder.toString());
                                    LinearLayout linearLayout = (LinearLayout) ((LinearLayout) myViewHolder.ll_main.getChildAt(i)).getChildAt(0);

                                    String obj = linearLayoutf.getTag().toString();

                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(i);
                                    stringBuilder2.append("");
                                    if (obj.equalsIgnoreCase(stringBuilder2.toString())) {
                                        Log.e("innnnnnnnnn", "=========>>");
                                        linearLayout.getChildAt(2).setVisibility(View.VISIBLE);
                                        myViewHolder.ll_nearby_train_list.removeAllViews();

                                        int finalI = ip;
                                        try {
                                            new AsyncTask<Void, Void, String>() {
                                                public void onPreExecute() {
                                                    super.onPreExecute();
                                                    myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                                                }

                                                public String doInBackground(Void... voidArr) {
                                                    if (Utils.isNetworkAvailable(mContext)) {
                                                        String CallApi="";
                                                        try { CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).avlClasses.get(finalIL), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).trainNumber);
                                                            StringBuilder stringBuilder = new StringBuilder();
                                                            stringBuilder.append("=====>>>");
                                                            stringBuilder.append(CallApi);
                                                            Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                                            Log.e(TAG, "doInBackground: "+e.getMessage() );
                                                        }
                                                        return CallApi;
                                                    }
                                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                                        public void run() {
                                                            Context context = mContext;
                                                            StringBuilder stringBuilder = new StringBuilder();
                                                            stringBuilder.append("");
                                                            stringBuilder.append(mContext.getString(R.string.no_internet));
                                                            Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    return null;
                                                }

                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                public void onPostExecute(String str) {
                                                    super.onPostExecute(str);
                                                    if (str != null) {
                                                        myViewHolder.pb_loader.setVisibility(View.GONE);
                                                        TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                                        }.getType());
                                                        if (trainTicketClassBeen.totalFare != null) {
                                                            MyTextView myTextView = myTextView2;
                                                            StringBuilder stringBuilder = new StringBuilder();
                                                            stringBuilder.append(mContext.getString(R.string.Rs));
                                                            stringBuilder.append(" ");
                                                            stringBuilder.append(trainTicketClassBeen.totalFare);
                                                            myTextView.setText(stringBuilder.toString());
                                                        }
                                                        myTextView2.setBackgroundResource(0);
                                                        if (trainTicketClassBeen.avlDayList != null) {

                                                            Log.e("ClassavlDayList.size()", "" + trainTicketClassBeen.avlDayList.size());
                                                            for (int j = 0; j < trainTicketClassBeen.avlDayList.size(); j++) {

                                                                Log.e("value", "" + j);
                                                                View child0 = inflater.inflate(R.layout.layout_nearby_train_rowlist, null, false);
                                                                RelativeLayout relativeLayout = (RelativeLayout) child0.findViewById(R.id.rl_nearby_train);
                                                                MyTextView myTextView2 = (MyTextView) child0.findViewById(R.id.tv_avalib_tid);

                                                                int cut1 = Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).lastIndexOf(',');

                                                                if (cut1 != -1) {
                                                                    Log.e(TAG, "onPostExecute: " + Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).substring(cut1 + 1));

                                                                }
                                                                String firstStr = Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).replace(", ", System.lineSeparator());
                                                                ((MyTextView) child0.findViewById(R.id.tv_date)).setText(firstStr);

                                                                // ((MyTextView) child0.findViewById(R.id.tv_date)).setText(Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate));
                                                                StringBuilder stringBuilder2 = new StringBuilder();
                                                                String str2 = "";
                                                                stringBuilder2.append(str2);
                                                                stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityStatus);
                                                                myTextView2.setText(stringBuilder2.toString());
                                                                StringBuilder stringBuilder3 = new StringBuilder();
                                                                stringBuilder3.append(j);
                                                                stringBuilder3.append(str2);
                                                                relativeLayout.setTag(stringBuilder3.toString());
                                                                int finalI = j;
                                                                relativeLayout.setOnClickListener(new OnClickListener() {
                                                                    public void onClick(View view) {

                                                                        trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                                                                        trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                                                                        onTrainDetailListner.onClickTicketClassListner(finalI);
                                                                    }
                                                                });
                                                                myViewHolder.ll_nearby_train_list.addView(child0);
                                                            }
                                                        }
                                                        return;
                                                    }
                                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                                }
                                            }.execute(new Void[0]);
                                        } catch (Exception e) {
                                            Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                                        }
                                    } else {
                                        linearLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        });
                        myViewHolder.ll_main.addView(child);
                        //linearLayoutf.performClick();
                    }
                    return;
                } else {
                    myViewHolder.ll_site.setVisibility(View.VISIBLE);
                    myViewHolder.ll_nearby_train.setVisibility(View.GONE);
                    myViewHolder.ll_nearby_date.setTag(str);
                    myViewHolder.dropDownIC.setImageResource(R.drawable.ic_down_filled_arrow);
                    Log.e(TAG, "onClick: ic_down_filled_arrow ");
                }

            }
        });

        myViewHolder.ll_gen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                Log.e(TAG, "onClick: ll_gen clicked");
                myViewHolder.iv_general_check.setImageResource(R.drawable.ic_blue_check_box_);
                myViewHolder.iv_tatkal_check.setImageResource(R.drawable.ic_empty_check_box);
                myViewHolder.iv_ladies_check.setImageResource(R.drawable.ic_empty_check_box);

               /* myViewHolder.ll_gen.getChildAt(0).setVisibility(View.VISIBLE);
                myViewHolder.ll_tatkal.getChildAt(0).setVisibility(View.GONE);
                myViewHolder.ll_ladis.getChildAt(0).setVisibility(View.GONE);*/
                quota = "GN";
                myViewHolder.ll_main.removeAllViews();
                myViewHolder.ll_nearby_train_list.removeAllViews();


                for (int io = 0; io < ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.size(); io++) {

                    TrainListAdapter trainListAdapter = TrainListAdapter.this;


                    View child = trainListAdapter.inflater.inflate(R.layout.layout_ticketclass_rowlist, null, false);


                    final LinearLayout linearLayoutf = (LinearLayout) child.findViewById(R.id.ll_main);

                    MyTextView myTextView8 = (MyTextView) child.findViewById(R.id.tv_name);

                    final MyTextView myTextView2 = (MyTextView) child.findViewById(R.id.tv_price);

                    if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(io)).equalsIgnoreCase("1A")) {
                        myTextView8.setText(Constants.TIER_1);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(io)).equalsIgnoreCase("2A")) {
                        myTextView8.setText(Constants.TIER_2);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(io)).equalsIgnoreCase("3A")) {
                        myTextView8.setText(Constants.TIER_3);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(io)).equalsIgnoreCase("Sl")) {
                        myTextView8.setText(Constants.SLEEPER);
                    }


                    int finalIo1 = io;
                    try {
                        new AsyncTask<Void, Void, String>() {
                            public void onPreExecute() {
                                super.onPreExecute();
                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                            }

                            public String doInBackground(Void... voidArr) {
                                if (com.kha.train.util.Utils.isNetworkAvailable(mContext)) {
                                    String CallApi="";
                                    try { CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(finalIo1), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("=====>>>");
                                        stringBuilder.append(CallApi);
                                        Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                        Log.e(TAG, "doInBackground: "+e.getMessage() );
                                    }
                                    return CallApi;
                                }
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    public void run() {
                                        Context context = mContext;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("");
                                        stringBuilder.append(mContext.getString(R.string.no_internet));
                                        Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return null;
                            }

                            public void onPostExecute(String str) {
                                super.onPostExecute(str);
                                if (str != null) {
                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                    TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                    }.getType());
                                    if (trainTicketClassBeen.totalFare != null) {
                                        MyTextView myTextView = myTextView2;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(mContext.getString(R.string.Rs));
                                        stringBuilder.append(" ");
                                        stringBuilder.append(trainTicketClassBeen.totalFare);
                                        myTextView.setText(stringBuilder.toString());
                                    }
                                    myTextView2.setBackgroundResource(0);

                                    return;
                                }
                                myViewHolder.pb_loader.setVisibility(View.GONE);
                            }
                        }.execute(new Void[0]);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(io);
                    stringBuilder.append("");
                    linearLayoutf.setTag(stringBuilder.toString());
                    child.findViewById(R.id.v_select);

                    Log.e("myViewHolder.ll_main333", "" + myViewHolder.ll_main.getChildCount());

                    // int finalIo = ip;
                    int finalIo = io;

                    //Log.e("jsonobject", stringBuilder1.toString());


                    linearLayoutf.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {

/////////////////////////
                            Log.e(TAG, "onClick: linearLayoutf clicked");


                            for (int i = 0; i < myViewHolder.ll_main.getChildCount(); i++) {


                                Log.e("count lin", "=========**" + myViewHolder.ll_main.getChildCount());


                        /*StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("==>>");
                        stringBuilder.append(linearLayoutf.getTag().toString());
                        Log.e("ll_child_main.getTag()", stringBuilder.toString());*/
                                LinearLayout linearLayout = (LinearLayout) ((LinearLayout) myViewHolder.ll_main.getChildAt(i)).getChildAt(0);

                                String obj = linearLayoutf.getTag().toString();

                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(i);
                                stringBuilder2.append("");
                                if (obj.equalsIgnoreCase(stringBuilder2.toString())) {
                                    Log.e("innnnnnnnnn", "=========>>");
                                    linearLayout.getChildAt(2).setVisibility(View.VISIBLE);
                                    myViewHolder.ll_nearby_train_list.removeAllViews();

                                    int finalI = ip;

                                    try {
                                        new AsyncTask<Void, Void, String>() {
                                            public void onPreExecute() {
                                                super.onPreExecute();
                                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                                            }

                                            public String doInBackground(Void... voidArr) {
                                                if (com.kha.train.util.Utils.isNetworkAvailable(mContext)) {
                                                    String CallApi="";
                                                    try { CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).avlClasses.get(finalIo), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).trainNumber);
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append("=====>>>");
                                                        stringBuilder.append(CallApi);
                                                        Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                                        Log.e(TAG, "doInBackground: "+e.getMessage() );
                                                    }
                                                    return CallApi;
                                                }
                                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Context context = mContext;
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append("");
                                                        stringBuilder.append(mContext.getString(R.string.no_internet));
                                                        Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                return null;
                                            }

                                            public void onPostExecute(String str) {
                                                super.onPostExecute(str);
                                                if (str != null) {
                                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                                    TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                                    }.getType());
                                                    if (trainTicketClassBeen.totalFare != null) {
                                                        MyTextView myTextView = myTextView2;
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append(mContext.getString(R.string.Rs));
                                                        stringBuilder.append(" ");
                                                        stringBuilder.append(trainTicketClassBeen.totalFare);
                                                        myTextView.setText(stringBuilder.toString());
                                                    }
                                                    myTextView2.setBackgroundResource(0);
                                                    if (trainTicketClassBeen.avlDayList != null) {

                                                        Log.e("ClassavlDayList.size()", "" + trainTicketClassBeen.avlDayList.size());
                                                        for (int j = 0; j < trainTicketClassBeen.avlDayList.size(); j++) {

                                                            Log.e("value", "" + j);
                                                            View child0 = inflater.inflate(R.layout.layout_nearby_train_rowlist, null, false);
                                                            RelativeLayout relativeLayout = (RelativeLayout) child0.findViewById(R.id.rl_nearby_train);
                                                            MyTextView myTextView2 = (MyTextView) child0.findViewById(R.id.tv_avalib_tid);
                                                            // ((MyTextView) child0.findViewById(R.id.tv_date)).setText(com.kha.train.util.Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate));

                                                            String firstStr = null;
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                                                firstStr = Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).replace(", ", System.lineSeparator());
                                                            }
                                                            ((MyTextView) child0.findViewById(R.id.tv_date)).setText(firstStr);

                                                            StringBuilder stringBuilder2 = new StringBuilder();
                                                            String str2 = "";
                                                            stringBuilder2.append(str2);
                                                            stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityStatus);
                                                            myTextView2.setText(stringBuilder2.toString());
                                                            StringBuilder stringBuilder3 = new StringBuilder();
                                                            stringBuilder3.append(j);
                                                            stringBuilder3.append(str2);
                                                            relativeLayout.setTag(stringBuilder3.toString());
                                                            int finalIf = j;
                                                            relativeLayout.setOnClickListener(new OnClickListener() {
                                                                public void onClick(View view) {
                                                                    trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                                                                    trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                                                                    onTrainDetailListner.onClickTicketClassListner(finalIf);
                                                                }
                                                            });
                                                            myViewHolder.ll_nearby_train_list.addView(child0);
                                                        }
                                                    }
                                                    return;
                                                }
                                                myViewHolder.pb_loader.setVisibility(View.GONE);
                                            }
                                        }.execute(new Void[0]);
                                    } catch (Exception e) {
                                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                                    }
                                } else {
                                    linearLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                                }
                            }

                            ///////////////////////////////////////
                        }
                    });
                    myViewHolder.ll_main.addView(child);
                    //linearLayoutf.performClick();
                }
            }
        });
        myViewHolder.ll_tatkal.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                myViewHolder.iv_general_check.setImageResource(R.drawable.ic_empty_check_box);
                myViewHolder.iv_tatkal_check.setImageResource(R.drawable.ic_blue_check_box_);
                myViewHolder.iv_ladies_check.setImageResource(R.drawable.ic_empty_check_box);

                /*myViewHolder.ll_tatkal.getChildAt(0).setVisibility(View.VISIBLE);
                myViewHolder.ll_gen.getChildAt(0).setVisibility(View.GONE);
                myViewHolder.ll_ladis.getChildAt(0).setVisibility(View.GONE);*/
                quota = "TQ";
                myViewHolder.ll_main.removeAllViews();
                myViewHolder.ll_nearby_train_list.removeAllViews();

                for (int im = 0; im < ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.size(); im++) {

                    TrainListAdapter trainListAdapter = TrainListAdapter.this;
                    View child3 = trainListAdapter.inflater.inflate(R.layout.layout_ticketclass_rowlist, null, false);
                    final LinearLayout linearLayouth = (LinearLayout) child3.findViewById(R.id.ll_main);
                    MyTextView myTextView6 = (MyTextView) child3.findViewById(R.id.tv_name);
                    final MyTextView myTextView2 = (MyTextView) child3.findViewById(R.id.tv_price);


                    if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(im)).equalsIgnoreCase("1A")) {
                        myTextView6.setText(Constants.TIER_1);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(im)).equalsIgnoreCase("2A")) {
                        myTextView6.setText(Constants.TIER_2);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(im)).equalsIgnoreCase("3A")) {
                        myTextView6.setText(Constants.TIER_3);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(im)).equalsIgnoreCase("Sl")) {
                        myTextView6.setText(Constants.SLEEPER);
                    }


                    int finalIm1 = im;
                    try {
                        new AsyncTask<Void, Void, String>() {
                            public void onPreExecute() {
                                super.onPreExecute();
                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                            }

                            public String doInBackground(Void... voidArr) {
                                return CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(finalIm1), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber);
                            }

                            public void onPostExecute(String str) {
                                String str2 = "";
                                super.onPostExecute(str);
                                if (str != null) {
                                    try {
                                        myViewHolder.pb_loader.setVisibility(View.GONE);
                                        TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                        }.getType());
//                                                    MyTextView myTextView = myTextView2;
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(mContext.getString(R.string.Rs));
                                        stringBuilder.append(" ");
                                        stringBuilder.append(trainTicketClassBeen.totalFare);
                                        myTextView2.setText(stringBuilder.toString());
                                        myTextView2.setBackgroundResource(0);
                                        return;
                                    } catch (Exception unused) {
                                        return;
                                    }
                                }
                                myViewHolder.pb_loader.setVisibility(View.GONE);
                            }
                        }.execute(new Void[0]);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(im);
                    stringBuilder.append("");
                    linearLayouth.setTag(stringBuilder.toString());
                    child3.findViewById(R.id.v_select);
                    //  int finalIm = ip;
                    int finalIm = im;

                    Log.e(TAG, "onClick: LOOP CALL=======" + im);
                    linearLayouth.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            for (int i = 0; i < myViewHolder.ll_main.getChildCount(); i++) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("==>>");
                                stringBuilder.append(linearLayouth.getTag().toString());
                                Log.e("ll_child_main.getTag()", stringBuilder.toString());
                                LinearLayout linearLayout = (LinearLayout) ((LinearLayout) myViewHolder.ll_main.getChildAt(i)).getChildAt(0);
                                String obj = linearLayouth.getTag().toString();
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(i);
                                stringBuilder2.append("");
                                if (obj.equalsIgnoreCase(stringBuilder2.toString())) {
                                    Log.e("innnnnnnnnn", "=========>>");
                                    linearLayout.getChildAt(2).setVisibility(View.VISIBLE);
                                    myViewHolder.ll_nearby_train_list.removeAllViews();
                                    int finalI = ip;
                                    try {
                                        new AsyncTask<Void, Void, String>() {
                                            public void onPreExecute() {
                                                super.onPreExecute();
                                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                                            }

                                            public String doInBackground(Void... voidArr) {
                                                return CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).avlClasses.get(finalIm), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).trainNumber);
                                            }

                                            public void onPostExecute(String str) {
                                                String str2 = "";
                                                super.onPostExecute(str);
                                                if (str != null) {
                                                    try {
                                                        myViewHolder.pb_loader.setVisibility(View.GONE);
                                                        TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                                        }.getType());
//                                                    MyTextView myTextView = myTextView2;
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append(mContext.getString(R.string.Rs));
                                                        stringBuilder.append(" ");
                                                        stringBuilder.append(trainTicketClassBeen.totalFare);
                                                        myTextView2.setText(stringBuilder.toString());
                                                        myTextView2.setBackgroundResource(0);
                                                        for (int j = 0; j < trainTicketClassBeen.avlDayList.size(); j++) {
                                                            View child4 = inflater.inflate(R.layout.layout_nearby_train_rowlist, null, false);
                                                            RelativeLayout relativeLayout = (RelativeLayout) child4.findViewById(R.id.rl_nearby_train);
                                                            MyTextView myTextView2 = (MyTextView) child4.findViewById(R.id.tv_avalib_tid);
                                                            //  ((MyTextView) child4.findViewById(R.id.tv_date)).setText(com.kha.train.util.Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate));
                                                            String firstStr = null;
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                                                firstStr = Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).replace(", ", System.lineSeparator());
                                                            }
                                                            ((MyTextView) child4.findViewById(R.id.tv_date)).setText(firstStr);

                                                            StringBuilder stringBuilder2 = new StringBuilder();
                                                            stringBuilder2.append(str2);
                                                            stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityStatus);
                                                            myTextView2.setText(stringBuilder2.toString());
                                                            StringBuilder stringBuilder3 = new StringBuilder();
                                                            stringBuilder3.append(j);
                                                            stringBuilder3.append(str2);
                                                            relativeLayout.setTag(stringBuilder3.toString());
                                                            int finalId = j;
                                                            relativeLayout.setOnClickListener(new OnClickListener() {
                                                                public void onClick(View view) {

                                                                    trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                                                                    trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                                                                    onTrainDetailListner.onClickTicketClassListner(finalId);
                                                                }
                                                            });
                                                            myViewHolder.ll_nearby_train_list.addView(child4);
                                                        }
                                                        return;
                                                    } catch (Exception unused) {
                                                        return;
                                                    }
                                                }
                                                myViewHolder.pb_loader.setVisibility(View.GONE);
                                            }
                                        }.execute(new Void[0]);
                                    } catch (Exception e) {
                                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                                    }
                                } else {
                                    linearLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                    myViewHolder.ll_main.addView(child3);
                }
            }
        });
        myViewHolder.ll_ladis.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                myViewHolder.iv_general_check.setImageResource(R.drawable.ic_empty_check_box);
                myViewHolder.iv_tatkal_check.setImageResource(R.drawable.ic_empty_check_box);
                myViewHolder.iv_ladies_check.setImageResource(R.drawable.ic_blue_check_box_);

               /* myViewHolder.ll_ladis.getChildAt(0).setVisibility(View.VISIBLE);
                myViewHolder.ll_tatkal.getChildAt(0).setVisibility(View.GONE);
                myViewHolder.ll_gen.getChildAt(0).setVisibility(View.GONE);*/
                quota = "LD";

                myViewHolder.ll_main.removeAllViews();

                myViewHolder.ll_nearby_train_list.removeAllViews();

                Log.e(TAG, "onClick: Class  SIZE=======" + trainBtwnStnsList.get(ip).avlClasses.size());


                for (int iw = 0; iw < ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.size(); iw++) {

                    Log.e(TAG, "onClick: LOOP CALL=======" + iw);
                    TrainListAdapter trainListAdapter = TrainListAdapter.this;
                    View child5 = trainListAdapter.inflater.inflate(R.layout.layout_ticketclass_rowlist, null, false);
                    final LinearLayout linearLayouti = (LinearLayout) child5.findViewById(R.id.ll_main);

                    MyTextView myTextView3 = (MyTextView) child5.findViewById(R.id.tv_name);

                    final MyTextView myTextView2 = (MyTextView) child5.findViewById(R.id.tv_price);


                    Log.e(TAG, "onClick: Class=======" + trainBtwnStnsList.get(ip).avlClasses.toString());

                    if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iw)).equalsIgnoreCase("1A")) {
                        myTextView3.setText(Constants.TIER_1);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iw)).equalsIgnoreCase("2A")) {
                        myTextView3.setText(Constants.TIER_2);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iw)).equalsIgnoreCase("3A")) {
                        myTextView3.setText(Constants.TIER_3);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iw)).equalsIgnoreCase("Sl")) {
                        myTextView3.setText(Constants.SLEEPER);
                    } else if (((String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(iw)).equalsIgnoreCase("2S")) {
                        myTextView3.setText(Constants.SLEEPER_2);
                    }


                    int finalIw1 = iw;
                    try {
                        new AsyncTask<Void, Void, String>() {
                            public void onPreExecute() {
                                super.onPreExecute();
                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                            }

                            public String doInBackground(Void... voidArr) {
                                String CallApi="";
                                try { CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).avlClasses.get(finalIw1), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber);
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("=====>>>");
                                    stringBuilder.append(CallApi);
                                    Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                    Log.e(TAG, "doInBackground: "+e.getMessage() );
                                }
                                return CallApi;
                            }

                            public void onPostExecute(String str) {
                                super.onPostExecute(str);
                                if (str != null) {
                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                    TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                    }.getType());
                                    // MyTextView myTextView = myTextView2;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(mContext.getString(R.string.Rs));
                                    stringBuilder.append(" ");
                                    stringBuilder.append(trainTicketClassBeen.totalFare);
                                    myTextView2.setText(stringBuilder.toString());
                                    myTextView2.setBackgroundResource(0);
                                    return;
                                }
                                linearLayouti.getChildAt(2).setVisibility(View.INVISIBLE);
                            }
                        }.execute(new Void[0]);

                    } catch (Exception e) {
                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(iw);

                    stringBuilder.append("");
                    linearLayouti.setTag(stringBuilder.toString());
                    child5.findViewById(R.id.v_select);
                    //int finalIw = ip;
                    int finalIw = iw;
                    linearLayouti.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            for (int i = 0; i < myViewHolder.ll_main.getChildCount(); i++) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("==>>");
                                stringBuilder.append(linearLayouti.getTag().toString());
                                Log.e("ll_child_main.getTag()", stringBuilder.toString());
                                final LinearLayout linearLayout = (LinearLayout) ((LinearLayout) myViewHolder.ll_main.getChildAt(i)).getChildAt(0);
                                String obj = linearLayouti.getTag().toString();
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(i);
                                stringBuilder2.append("");
                                if (obj.equalsIgnoreCase(stringBuilder2.toString())) {
                                    Log.e("innnnnnnnnn", "=========>>");
                                    linearLayout.getChildAt(2).setVisibility(View.VISIBLE);
                                    myViewHolder.ll_nearby_train_list.removeAllViews();
                                    int finalI = ip;
                                    try {
                                        new AsyncTask<Void, Void, String>() {
                                            public void onPreExecute() {
                                                super.onPreExecute();
                                                myViewHolder.pb_loader.setVisibility(View.VISIBLE);
                                            }

                                            public String doInBackground(Void... voidArr) {
                                                String CallApi="";
                                                try {CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).avlClasses.get(finalIw), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(finalI)).trainNumber);
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append("=====>>>");
                                                    stringBuilder.append(CallApi);
                                                    Log.e("jsonobject", stringBuilder.toString());}catch (Exception e){
                                                    Log.e(TAG, "doInBackground: "+e.getMessage() );
                                                }
                                                return CallApi;
                                            }

                                            public void onPostExecute(String str) {
                                                super.onPostExecute(str);
                                                if (str != null) {
                                                    myViewHolder.pb_loader.setVisibility(View.GONE);
                                                    TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                                    }.getType());
                                                    // MyTextView myTextView = myTextView2;
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    stringBuilder.append(mContext.getString(R.string.Rs));
                                                    stringBuilder.append(" ");
                                                    stringBuilder.append(trainTicketClassBeen.totalFare);
                                                    myTextView2.setText(stringBuilder.toString());
                                                    myTextView2.setBackgroundResource(0);
                                                    for (int j = 0; j < trainTicketClassBeen.avlDayList.size(); j++) {
                                                        View child6 = inflater.inflate(R.layout.layout_nearby_train_rowlist, null, false);
                                                        RelativeLayout relativeLayout = (RelativeLayout) child6.findViewById(R.id.rl_nearby_train);
                                                        MyTextView myTextView2 = (MyTextView) child6.findViewById(R.id.tv_avalib_tid);
                                                        // ((MyTextView) child6.findViewById(R.id.tv_date)).setText(com.kha.train.util.Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate));
                                                        String firstStr = null;
                                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                                            firstStr = Utils.changeDateFormate(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityDate).replace(", ", System.lineSeparator());
                                                        }
                                                        ((MyTextView) child6.findViewById(R.id.tv_date)).setText(firstStr);


                                                        StringBuilder stringBuilder2 = new StringBuilder();
                                                        String str2 = "";
                                                        stringBuilder2.append(str2);
                                                        stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(j)).availablityStatus);
                                                        myTextView2.setText(stringBuilder2.toString());
                                                        StringBuilder stringBuilder3 = new StringBuilder();
                                                        stringBuilder3.append(j);
                                                        stringBuilder3.append(str2);
                                                        relativeLayout.setTag(stringBuilder3.toString());
                                                        int finalIs = j;
                                                        relativeLayout.setOnClickListener(new OnClickListener() {
                                                            public void onClick(View view) {

                                                                trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                                                                trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                                                                onTrainDetailListner.onClickTicketClassListner(finalIs);
                                                            }
                                                        });
                                                        myViewHolder.ll_nearby_train_list.addView(child6);
                                                    }
                                                    return;
                                                }
                                                linearLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                                            }
                                        }.execute(new Void[0]);
                                    } catch (Exception e) {
                                        Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                                    }
                                } else {
                                    linearLayout.getChildAt(2).setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                    myViewHolder.ll_main.addView(child5);
                }
            }
        });


        myViewHolder.ll_site.removeAllViews();


        for (int i2 = 0; i2 < ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).avlClasses.size(); i2++) {

            View child7 = this.inflater.inflate(R.layout.layout_cardview_ticketclass_rowlist, null, false);
            LinearLayout linearLayouta = (LinearLayout) child7.findViewById(R.id.ll_main);
            MyTextView myTextView2 = (MyTextView) child7.findViewById(R.id.tv_ticket_class);
            final MyTextView myTextView3 = (MyTextView) child7.findViewById(R.id.tv_ticket_price);
            final MyTextView myTextView4 = (MyTextView) child7.findViewById(R.id.tv_ticket_status);
            MyTextView myTextView5 = (MyTextView) child7.findViewById(R.id.tv_ticket_sync);

            if (((String) ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).avlClasses.get(i2)).equalsIgnoreCase("1A")) {
                myTextView2.setText(Constants.TIER_1);
            } else if (((String) ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).avlClasses.get(i2)).equalsIgnoreCase("2A")) {
                myTextView2.setText(Constants.TIER_2);
            } else if (((String) ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).avlClasses.get(i2)).equalsIgnoreCase("3A")) {
                myTextView2.setText(Constants.TIER_3);
            } else if (((String) ((TrainBtwnStnsList) this.trainBtwnStnsList.get(ip)).avlClasses.get(i2)).equalsIgnoreCase("Sl")) {
                myTextView2.setText(Constants.SLEEPER);
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(i2);
            stringBuilder2.append("");
            linearLayouta.setTag(stringBuilder2.toString());
            final int i3 = ip;
            final int i4 = i2;

            if (myTextView3.getText().toString().length() > 0) {
                trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                onTrainDetailListner.onClickTicketClassListner(i3);
            } else {
                try {
                    asyncTask = new AsyncTask<Void, Void, String>() {
                        public String doInBackground(Void... voidArr) {
                            String CallApi="";

                            try {
                               CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).avlClasses.get(i4), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).trainNumber);
                               StringBuilder stringBuilder = new StringBuilder();
                               stringBuilder.append("=====>>>");
                               stringBuilder.append(CallApi);
                               Log.e("jsonobject", stringBuilder.toString());
                           }catch (Exception e) {
                               Log.e(TAG, "onBindViewHolder: asyncTask error: " + e.getMessage());
                           }
                            return CallApi;
                        }
                        String CallApi="";

                        public void onPostExecute(String str) {
                            super.onPostExecute(str);
                            if (str != null) {
                                MyTextView myTextView;
                                TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                }.getType());
                                try{
                                if (trainTicketClassBeen.totalFare != null) {
                                    myTextView = myTextView3;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(mContext.getString(R.string.Rs));
                                    stringBuilder.append(" ");
                                    stringBuilder.append(trainTicketClassBeen.totalFare);
                                    myTextView.setText(stringBuilder.toString());
                                }
                                if (trainTicketClassBeen.avlDayList != null && trainTicketClassBeen.avlDayList.size() > 0) {
                                    myTextView = myTextView4;
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(0)).availablityStatus);
                                    stringBuilder2.append("");
                                    myTextView.setText(stringBuilder2.toString());
                                }
                                myTextView3.setBackgroundResource(0);
                                myTextView4.setBackgroundResource(0);}catch (Exception e){
                                    Log.e(TAG, "onPostExecute: "+e.getMessage() );
                                }
                            }
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    Log.e(TAG, "onBindViewHolder: asyncTask error: " + e.getMessage());
                }
            }

            linearLayouta.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (myTextView3.getText().toString().length() > 0) {

                        trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                        trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                        onTrainDetailListner.onClickTicketClassListner(i3);
                    } else {
                        try {
                            new AsyncTask<Void, Void, String>() {
                                public String doInBackground(Void... voidArr) {
                                    String CallApi="";
                                    try {
                                        CallApi = CallApi(((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).frmStnCode, ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).toStnCode, (String) ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).avlClasses.get(i4), quota, doj, ((TrainBtwnStnsList) trainBtwnStnsList.get(i3)).trainNumber);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("=====>>>");
                                        stringBuilder.append(CallApi);
                                        Log.e("jsonobject", stringBuilder.toString());
                                    }catch (Exception e){
                                        Log.e(TAG, "doInBackground: "+e.getMessage() );
                                    }
                                    return CallApi;
                                }

                                public void onPostExecute(String str) {
                                    super.onPostExecute(str);
                                    if (str != null) {
                                        MyTextView myTextView;
                                        TrainTicketClassBeen trainTicketClassBeen = (TrainTicketClassBeen) new Gson().fromJson(str, new TypeToken<TrainTicketClassBeen>() {
                                        }.getType());
                                        if (trainTicketClassBeen.totalFare != null) {
                                            myTextView = myTextView3;
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append(mContext.getString(R.string.Rs));
                                            stringBuilder.append(" ");
                                            stringBuilder.append(trainTicketClassBeen.totalFare);
                                            myTextView.setText(stringBuilder.toString());
                                        }
                                        if (trainTicketClassBeen.avlDayList != null && trainTicketClassBeen.avlDayList.size() > 0) {
                                            myTextView = myTextView4;
                                            StringBuilder stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append(((AvlDayList) trainTicketClassBeen.avlDayList.get(0)).availablityStatus);
                                            stringBuilder2.append("");
                                            myTextView.setText(stringBuilder2.toString());
                                        }
                                        myTextView3.setBackgroundResource(0);
                                        myTextView4.setBackgroundResource(0);
                                    }
                                }
                            }.execute(new Void[0]);
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: asyncTask Error:  " + e.getMessage());
                        }
                    }
                }
            });
            myViewHolder.ll_site.addView(child7);
        }
        myViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                trainName = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainName;
                trainNumber = ((TrainBtwnStnsList) trainBtwnStnsList.get(ip)).trainNumber;
                onTrainDetailListner.onClickTicketClassListner(ip);
            }
        });
    }

    public int getItemCount() {
        return this.trainBtwnStnsList.size();
    }

    public String CallApi(String str, String str2, String str3, String str4, String str5, String str6) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("===>>>");
        stringBuilder.append(str3);
        Log.e("CallApi", stringBuilder.toString());
        MediaType parse = MediaType.parse("application/json");
        OkHttpClient okHttpClient = new OkHttpClient();
        String str7 = trainlistavailabity;
        HttpUrl.parse(str7).newBuilder();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("reservationChoice", "99");
            jSONObject.put("source", str);
            jSONObject.put("destination", str2);
            jSONObject.put("class", str3);
            jSONObject.put("quota", str4);
            jSONObject.put("doj", str5);
            jSONObject.put("trainNumber", str6);
            jSONObject.put("moreThanOneDay", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Response execute = okHttpClient.newCall(new Builder().url(str7).post(RequestBody.create(parse, jSONObject.toString())).build()).execute();
            if (execute.isSuccessful()) {
                Log.e("TrainListAdapter", "Got response from server for JSON post using OkHttp ");
                return execute.body().string();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
