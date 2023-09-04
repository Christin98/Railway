package com.kha.train.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.kha.train.R;
import com.kha.train.fragment.LiveStatusFragment;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.model.TrainLiveStatusBeen.IntermediateStation;
import com.kha.train.model.TrainLiveStatusBeen.Station;
import com.kha.train.util.SharedPreference;
import com.kha.train.widget.MyTextView;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

public class TrainLiveStatusAdapter extends Adapter {
    public static final int HEADER_TYPE = 0;
    public static final int VIEW_TYPE = 1;
    private ArrayList<String> al_images;
    View child;
    LayoutInflater inflater;
    boolean is24formt = true;
    Context mContext;
    com.kha.train.adapter.TravellerDetailListAdapter.setOnTrainDetailListner onTrainDetailListner;
    int pos = 0;
    String startDate;

    public interface setOnTrainDetailListner {
        void onClickTicketClassListner();
    }

    public class MyHeaderViewHolder extends ViewHolder {
        @BindView(R.id.tv_date)
        MyTextView tv_date;

        public MyHeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }


    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_dot)
        ImageView iv_dot;
        @BindView(R.id.iv_train)
        ImageView iv_train;
        @BindView(R.id.ll_add_sub_sta)
        LinearLayout ll_add_sub_sta;
        @BindView(R.id.rl_btwn_station)
        RelativeLayout rl_btwn_station;
        @BindView(R.id.rl_sub_station)
        RelativeLayout rl_sub_station;
        @BindView(R.id.rpb_content)
        RippleBackground rpb_content;
        @BindView(R.id.rpb_sub_content)
        RippleBackground rpb_sub_content;
        @BindView(R.id.tv_arr_schdtime)
        MyTextView tv_arr_schdtime;
        @BindView(R.id.tv_arr_time)
        MyTextView tv_arr_time;
        @BindView(R.id.tv_code)
        MyTextView tv_code;
        @BindView(R.id.tv_dep_schdtime)
        MyTextView tv_dep_schdtime;
        @BindView(R.id.tv_dep_time)
        MyTextView tv_dep_time;
        @BindView(R.id.tv_distnce)
        MyTextView tv_distnce;
        @BindView(R.id.tv_name)
        MyTextView tv_name;
        @BindView(R.id.tv_platform)
        MyTextView tv_platform;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }


    public int getItemViewType(int i) {
        return i == 0 ? 0 : 1;
    }

    public TrainLiveStatusAdapter(Context context, String str) {
        this.mContext = context;
        this.startDate = str;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.is24formt = new SharedPreference(context).getTimeFormate();
    }

    public TrainLiveStatusAdapter(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.al_images = arrayList;
        this.is24formt = new SharedPreference(context).getTimeFormate();
    }

    public TrainLiveStatusAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.is24formt = new SharedPreference(context).getTimeFormate();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new MyHeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_live_status_header_view, viewGroup, false));
        }
        if (i != 1) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.livestatus_rowlist, viewGroup, false));
        }
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.livestatus_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int itemViewType = viewHolder.getItemViewType();
        if (itemViewType == 0) {
            ((MyHeaderViewHolder) viewHolder).tv_date.setText(this.startDate);
        } else if (itemViewType == 1) {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
//            MyTextView myTextView = myViewHolder.tv_name;
            StringBuilder stringBuilder3 = new StringBuilder();
            String str = "";
            stringBuilder3.append(str);
            int i2 = i - 1;

            Log.e("station.name", "" + ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.name);

            Log.e("station.code", "" + ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.code);
            Log.e("distance", "" + ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).distance);
            Log.e("station.platformNumber", "" + ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.platformNumber);
            Log.e("arrivalDeta.actualArri", "" + ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.actualArrivalTime);


            stringBuilder3.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.name);

            myViewHolder.tv_name.setText(stringBuilder3.toString());
//            myTextView = myViewHolder.tv_code;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.code);
            myViewHolder.tv_code.setText(stringBuilder3.toString());
//            myTextView = myViewHolder.tv_distnce;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).distance);
            myViewHolder.tv_distnce.setText(stringBuilder3.toString());
            myViewHolder.rpb_content.setVisibility(View.GONE);
            str = "m";
            String str2 = " | Stop ";
            String str3 = "Platform #";
            StringBuilder stringBuilder4;
            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.platformNumber != null) {
//                myTextView = myViewHolder.tv_platform;
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str3);
                stringBuilder4.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.platformNumber);
                stringBuilder4.append(str2);
                stringBuilder4.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).haltMinutes);
                stringBuilder4.append(str);
                myViewHolder.tv_platform.setText(stringBuilder4.toString());
            } else {
//                myTextView = myViewHolder.tv_platform;
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str3);
                stringBuilder4.append("NA");
                stringBuilder4.append(str2);
                stringBuilder4.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).haltMinutes);
                stringBuilder4.append(str);
                myViewHolder.tv_platform.setText(stringBuilder4.toString());
            }
            str = "ARR. ";
            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.actualArrivalTime != null) {
//                myTextView = myViewHolder.tv_arr_time;
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(str);
                stringBuilder5.append(com.kha.train.util.Utils.changeTimeFormat(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.actualArrivalTime, this.is24formt));
                myViewHolder.tv_arr_time.setText(stringBuilder5.toString());
            }
            str2 = "Schd. ";
            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.scheduledArrivalTime != null) {
//                myTextView = myViewHolder.tv_arr_schdtime;
                stringBuilder = new StringBuilder();
                stringBuilder.append(str2);
                stringBuilder.append(com.kha.train.util.Utils.changeTimeFormat(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                myViewHolder.tv_arr_schdtime.setText(stringBuilder.toString());
            }
            str3 = "#EB2026";
            String str4 = "#26B5A9";
            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.arrivalDelay == null || ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).arrivalDetails.arrivalDelay.longValue() <= 0) {
                myViewHolder.tv_arr_time.setTextColor(Color.parseColor(str4));
            } else {
                myViewHolder.tv_arr_time.setTextColor(Color.parseColor(str3));
            }
            String str5 = "======>>";
            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).departureDetails.departureDelay == null || ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).departureDetails.departureDelay.longValue() <= 0) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str5);
                stringBuilder2.append("NA");
                Log.e("departureDelay1111", stringBuilder2.toString());
                myViewHolder.tv_dep_time.setTextColor(Color.parseColor(str4));
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str5);
                stringBuilder2.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).departureDetails.departureDelay);
                Log.e("departureDelay", stringBuilder2.toString());
                myViewHolder.tv_dep_time.setTextColor(Color.parseColor(str3));
            }
//            myTextView = myViewHolder.tv_dep_time;
            stringBuilder = new StringBuilder();
            String str6 = "DEP. ";
            stringBuilder.append(str6);
            stringBuilder.append(com.kha.train.util.Utils.changeTimeFormat(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).departureDetails.actualDepartureTime, this.is24formt));
            myViewHolder.tv_dep_time.setText(stringBuilder.toString());
//            myTextView = myViewHolder.tv_dep_schdtime;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str2);
            stringBuilder.append(com.kha.train.util.Utils.changeTimeFormat(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).departureDetails.scheduledDepartureTime, this.is24formt));
            myViewHolder.tv_dep_schdtime.setText(stringBuilder.toString());
            boolean z = false;

            try {
                if (LiveStatusFragment.liveStatusBeen.trainDetails.currentStation.code.equalsIgnoreCase(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.code)) {
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("====>>>>>");
                    stringBuilder2.append(((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).station.code);
                    Log.e("active_code", stringBuilder2.toString());
                    myViewHolder.rpb_content.setVisibility(View.VISIBLE);
                    myViewHolder.iv_dot.setVisibility(View.INVISIBLE);
                    myViewHolder.rpb_content.startRippleAnimation();
                } else {
                    myViewHolder.iv_dot.setVisibility(View.VISIBLE);
                    myViewHolder.rpb_content.stopRippleAnimation();
                    myViewHolder.rpb_content.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations != null) {
                myViewHolder.rl_btwn_station.setVisibility(View.VISIBLE);
                myViewHolder.rl_sub_station.setVisibility(View.VISIBLE);
                itemViewType = 0;
                while (itemViewType < ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.size()) {
                    MyTextView myTextView2;
                    MyTextView myTextView3;
                    MyTextView myTextView4;
                    if (LiveStatusFragment.liveStatusBeen.trainDetails.currentStation.code.equalsIgnoreCase(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).station.code)) {
                        this.child = this.inflater.inflate(R.layout.layout_sub_station_active_status_rowlist, null, z);
                        myTextView2 = (MyTextView) this.child.findViewById(R.id.tv_subname);
                        myTextView3 = (MyTextView) this.child.findViewById(R.id.tv_sub_distnce);
                        MyTextView myTextView5 = (MyTextView) this.child.findViewById(R.id.tv_subarr_time);
                        myTextView4 = (MyTextView) this.child.findViewById(R.id.tv_subarr_schdtime);
                        MyTextView myTextView6 = (MyTextView) this.child.findViewById(R.id.tv_subdep_time);
                        MyTextView myTextView7 = (MyTextView) this.child.findViewById(R.id.tv_subdep_schdtime);
                        RippleBackground rippleBackground = (RippleBackground) this.child.findViewById(R.id.rpb_sub_content1);
                        ((MyTextView) this.child.findViewById(R.id.tv_sub_code)).setText(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).station.code);
                        myTextView2.setText(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).station.name);
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).distance);
                        stringBuilder3.append(" Km");
                        myTextView3.setText(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                        myTextView5.setText(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str2);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                        myTextView4.setText(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str6);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).departureDetails.scheduledDepartureTime, this.is24formt));
                        myTextView6.setText(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str2);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).departureDetails.scheduledDepartureTime, this.is24formt));
                        myTextView7.setText(stringBuilder3.toString());
                        rippleBackground.startRippleAnimation();
                    } else {
                        this.child = this.inflater.inflate(R.layout.layout_sub_station_status_rowlist, null, false);
                        myTextView2 = (MyTextView) this.child.findViewById(R.id.tv_sub_distnce);
                        MyTextView myTextView8 = (MyTextView) this.child.findViewById(R.id.tv_subname);
                        myTextView3 = (MyTextView) this.child.findViewById(R.id.tv_arr_sub_time);
                        myTextView4 = (MyTextView) this.child.findViewById(R.id.tv_dep_sub_schdtime);
                        ImageView imageView = (ImageView) this.child.findViewById(R.id.iv_train);
                        ((MyTextView) this.child.findViewById(R.id.tv_sub_code)).setText(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).station.code);
                        myTextView8.setText(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).station.name);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).distance);
                        stringBuilder.append(" Km");
                        myTextView2.setText(stringBuilder.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).arrivalDetails.scheduledArrivalTime, this.is24formt));
                        myTextView3.setText(stringBuilder3.toString());
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str6);
                        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((IntermediateStation) ((Station) LiveStatusFragment.liveStatusBeen.stations.get(i2)).intermediateStations.get(itemViewType)).departureDetails.scheduledDepartureTime, this.is24formt));
                        myTextView4.setText(stringBuilder3.toString());
                    }
                    myViewHolder.ll_add_sub_sta.addView(this.child);
                    itemViewType++;
                    z = false;
                }
            } else {
                myViewHolder.rl_btwn_station.setVisibility(View.GONE);
                myViewHolder.rl_sub_station.setVisibility(View.GONE);
            }
            myViewHolder.rl_btwn_station.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (myViewHolder.rl_sub_station.isShown()) {
                        myViewHolder.rl_sub_station.setVisibility(View.GONE);
                    } else {
                        myViewHolder.rl_sub_station.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return LiveStatusFragment.liveStatusBeen.stations.size() + 1;
    }
}
