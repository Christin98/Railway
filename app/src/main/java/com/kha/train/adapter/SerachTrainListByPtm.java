package com.kha.train.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.kha.train.R;
import com.kha.train.fragment.TrainLiveStatusListFragment;
import com.kha.train.fragment.TrainScheduleListFragment;
import com.kha.train.model.Train;
import com.kha.train.widget.MyTextView;
import java.util.ArrayList;

public class SerachTrainListByPtm extends Adapter<SerachTrainListByPtm.MyViewHolder> {

    Context context;
    private ArrayList<Train> train_list;
    setTrianListClickListner trianListClickListner;

    public interface setTrianListClickListner {
        void onClickTrianList(Train train);
    }

    class MyViewHolder extends ViewHolder {
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



    public SerachTrainListByPtm(ArrayList<Train> arrayList, Context context, TrainLiveStatusListFragment trainLiveStatusListFragment) {
        this.train_list = arrayList;
        this.context = context;
        this.trianListClickListner = trainLiveStatusListFragment;
    }

    public SerachTrainListByPtm(ArrayList<Train> arrayList, Context context, TrainScheduleListFragment trainScheduleListFragment) {
        this.train_list = arrayList;
        this.context = this.context;
        this.trianListClickListner = trainScheduleListFragment;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchtrainbycdnlist_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_city_name.setText(((Train) this.train_list.get(i)).train_number);
        myViewHolder.tv_station_name.setText(((Train) this.train_list.get(i)).train_name);
        myViewHolder.rl_city.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SerachTrainListByPtm.this.trianListClickListner.onClickTrianList((Train) SerachTrainListByPtm.this.train_list.get(i));
            }
        });
    }

    public int getItemCount() {
        return this.train_list.size();
    }
}
