package com.kha.train.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.kha.train.R;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.model.LiveStationBeen.Train;
import com.kha.train.model.TrainListByCdNBeen.C1089R;
import com.kha.train.util.SharedPreference;
import com.kha.train.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveStationListAdapter extends Adapter<LiveStationListAdapter.MyViewHolder> {
    boolean is24formt = true;
    boolean isSrc = false;
    Context mContext;
    SharedPreference sharedPreference;
    List<Train> trains;
    com.kha.train.adapter.SearchTrainListByCdNAdapter.setTrianListClickListner trianListClickListner;

    public interface setTrianListClickListner {
        void onClickTrianList(C1089R c1089r);
    }

    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.tv_dep_expt)
        MyTextView tv_dep_expt;
        @BindView(R.id.tv_dep_schd)
        MyTextView tv_dep_schd;
        @BindView(R.id.tv_expt)
        MyTextView tv_expt;
        @BindView(R.id.tv_plateform)
        MyTextView tv_plateform;
        @BindView(R.id.tv_schd)
        MyTextView tv_schd;
        @BindView(R.id.tv_st_type)
        MyTextView tv_st_type;
        @BindView(R.id.tv_train_id)
        MyTextView tv_train_id;
        @BindView(R.id.tv_train_name)
        MyTextView tv_train_name;
        @BindView(R.id.tv_train_status)
        MyTextView tv_train_status;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }


    public LiveStationListAdapter(Context context) {
        this.mContext = context;
    }

    public LiveStationListAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
    }

    public LiveStationListAdapter(Context context, List<Train> list) {
        this.mContext = context;
        this.trains = list;
        this.sharedPreference = new SharedPreference(context);
        this.is24formt = this.sharedPreference.getTimeFormate();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.livestationlist_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_train_id.setText(((Train) this.trains.get(i)).train.number);
        myViewHolder.tv_train_name.setText(((Train) this.trains.get(i)).train.name);
        MyTextView myTextView = myViewHolder.tv_plateform;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Expected Platform #");
        stringBuilder.append(((Train) this.trains.get(i)).expectedPlatformNo);
        myTextView.setText(stringBuilder.toString());


        myViewHolder.tv_train_status.setText(((Train) this.trains.get(i)).delayString);
        String str = "#D0021B";
        String str2 = "#1A7971";
        if (((Train) this.trains.get(i)).delayString.contains("Delayed by")) {
            myViewHolder.tv_train_status.setTextColor(Color.parseColor(str));
        } else if (((Train) this.trains.get(i)).delayString.contains("Running on time")) {
            myViewHolder.tv_train_status.setTextColor(Color.parseColor(str2));
        } else {
            myViewHolder.tv_train_status.setTextColor(Color.parseColor(str2));
        }
        String str3 = "---";
        String str4 = "";
        String str5 = "Expt. ";
        String str6 = "Schd. ";
        StringBuilder stringBuilder2;
        if (((Train) this.trains.get(i)).arrivalDetails.actualArrivalTime.equalsIgnoreCase("SRC")) {
            myViewHolder.tv_st_type.setVisibility(View.VISIBLE);
            this.isSrc = true;
            myViewHolder.tv_st_type.setText("Source");
            myTextView = myViewHolder.tv_dep_expt;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str6);
            stringBuilder2.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).departureDetails.scheduledDepartureTime, this.is24formt));
            myTextView.setText(stringBuilder2.toString());
            myTextView = myViewHolder.tv_dep_expt;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str5);
            stringBuilder2.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).departureDetails.actualDepartureTime, this.is24formt));
            myTextView.setText(stringBuilder2.toString());
            myViewHolder.tv_schd.setText(str4);
            myViewHolder.tv_expt.setText(str3);
        } else {
            myViewHolder.tv_st_type.setVisibility(View.GONE);
            this.isSrc = false;
            myTextView = myViewHolder.tv_schd;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str6);
            stringBuilder2.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).arrivalDetails.scheduledArrivalTime, this.is24formt));
            myTextView.setText(stringBuilder2.toString());
            myTextView = myViewHolder.tv_expt;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str5);
            stringBuilder2.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).arrivalDetails.actualArrivalTime, this.is24formt));
            myTextView.setText(stringBuilder2.toString());
            if (((Train) this.trains.get(i)).arrivalDetails.actualArrivalTime.compareTo(((Train) this.trains.get(i)).arrivalDetails.scheduledArrivalTime) == 0) {
                myViewHolder.tv_expt.setTextColor(Color.parseColor(str2));
            } else if (((Train) this.trains.get(i)).arrivalDetails.actualArrivalTime.compareTo(((Train) this.trains.get(i)).arrivalDetails.scheduledArrivalTime) > 0) {
                myViewHolder.tv_expt.setTextColor(Color.parseColor(str));
            }
        }
        if (((Train) this.trains.get(i)).departureDetails.actualDepartureTime.equalsIgnoreCase("DSTN")) {
            myViewHolder.tv_st_type.setVisibility(View.VISIBLE);
            myViewHolder.tv_st_type.setText("Destination");
            myTextView = myViewHolder.tv_expt;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str6);
            stringBuilder.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).arrivalDetails.scheduledArrivalTime, this.is24formt));
            myTextView.setText(stringBuilder.toString());
            myTextView = myViewHolder.tv_schd;
            stringBuilder = new StringBuilder();
            stringBuilder.append(str5);
            stringBuilder.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).arrivalDetails.actualArrivalTime, this.is24formt));
            myTextView.setText(stringBuilder.toString());
            myViewHolder.tv_dep_schd.setText(str4);
            myViewHolder.tv_dep_expt.setText(str3);
            this.isSrc = true;
            return;
        }
        if (this.isSrc) {
            myViewHolder.tv_st_type.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.tv_st_type.setVisibility(View.GONE);
        }
        myTextView = myViewHolder.tv_dep_schd;
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str6);
        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).departureDetails.scheduledDepartureTime, this.is24formt));
        myTextView.setText(stringBuilder3.toString());
        myTextView = myViewHolder.tv_dep_expt;
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append(str5);
        stringBuilder3.append(com.kha.train.util.Utils.changeTimeFormat(((Train) this.trains.get(i)).departureDetails.actualDepartureTime, this.is24formt));
        myTextView.setText(stringBuilder3.toString());
        if (((Train) this.trains.get(i)).departureDetails.actualDepartureTime.compareTo(((Train) this.trains.get(i)).departureDetails.scheduledDepartureTime) == 0) {
            myViewHolder.tv_dep_expt.setTextColor(Color.parseColor(str2));
        } else if (((Train) this.trains.get(i)).departureDetails.actualDepartureTime.compareTo(((Train) this.trains.get(i)).departureDetails.scheduledDepartureTime) > 0) {
            myViewHolder.tv_dep_expt.setTextColor(Color.parseColor(str));
        }
    }

    public int getItemCount() {
        return this.trains.size();
    }
}
