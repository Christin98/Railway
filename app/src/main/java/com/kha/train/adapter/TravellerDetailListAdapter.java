package com.kha.train.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.ButterKnife;
import com.kha.train.R;
import com.kha.train.fragment.TrainRouteListFragment;
import java.util.ArrayList;

public class TravellerDetailListAdapter extends Adapter<TravellerDetailListAdapter.MyViewHolder> {
    private ArrayList<String> al_images;
    View child;
    LayoutInflater inflater;
    Context mContext;
    setOnTrainDetailListner onTrainDetailListner;
    int pos = 0;

    public interface setOnTrainDetailListner {
        void onClickTicketClassListner();
    }

    public class MyViewHolder extends ViewHolder {
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }

    public int getItemCount() {
        return 3;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
    }

    public TravellerDetailListAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TravellerDetailListAdapter(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.al_images = arrayList;
    }

    public TravellerDetailListAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travellerdetaillist_rowlist, viewGroup, false));
    }
}
