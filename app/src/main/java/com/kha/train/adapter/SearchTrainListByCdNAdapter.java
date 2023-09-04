package com.kha.train.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.kha.train.R;
import com.kha.train.fragment.TrainBookFragment;
import com.kha.train.fragment.TrainLiveStatusListFragment;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.fragment.TrainScheduleListFragment;
import com.kha.train.model.TrainListByCdNBeen.C1089R;
import com.kha.train.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

public class SearchTrainListByCdNAdapter extends Adapter<SearchTrainListByCdNAdapter.MyViewHolder> {
    private ArrayList<String> al_images;
    View child;
    List<C1089R> cityBeenArrayList;
    Fragment fragment;
    LayoutInflater inflater;
    boolean isFrom = true;
    Context mContext;
    int pos = 0;
    TrainBookFragment trainBookFragment;
    setTrianListClickListner trianListClickListner;

    public interface setTrianListClickListner {
        void onClickTrianList(C1089R c1089r);
    }

    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_city)
        RelativeLayout rl_city;
        @BindView(R.id.tv_city_name)
        MyTextView tv_city_name;
        @BindView(R.id.tv_station_name)
        MyTextView tv_station_name;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }



    public SearchTrainListByCdNAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchTrainListByCdNAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
    }

    public SearchTrainListByCdNAdapter(Context context, List<C1089R> list, TrainLiveStatusListFragment trainLiveStatusListFragment) {
        this.mContext = context;
        this.cityBeenArrayList = list;
        this.trianListClickListner = trainLiveStatusListFragment;
    }

    public SearchTrainListByCdNAdapter(Context context, List<C1089R> list, TrainScheduleListFragment trainScheduleListFragment) {
        this.mContext = context;
        this.cityBeenArrayList = list;
        this.trianListClickListner = trainScheduleListFragment;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchtrainbycdnlist_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_city_name.setText(((C1089R) this.cityBeenArrayList.get(i)).xtr.tid);
        myViewHolder.tv_station_name.setText(((C1089R) this.cityBeenArrayList.get(i)).f104n);
        myViewHolder.rl_city.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SearchTrainListByCdNAdapter.this.trianListClickListner.onClickTrianList((C1089R) SearchTrainListByCdNAdapter.this.cityBeenArrayList.get(i));
            }
        });
    }

    public int getItemCount() {
        return this.cityBeenArrayList.size();
    }
}
