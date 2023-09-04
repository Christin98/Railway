package com.kha.train.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.kha.train.R;
import com.kha.train.fragment.LiveStationFragment;
import com.kha.train.fragment.LiveStationListFragment;
import com.kha.train.fragment.TrainBookFragment;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.model.CityBeen;
import com.kha.train.widget.MyTextView;
import java.util.ArrayList;

public class SearchTrainListAdapter extends Adapter<SearchTrainListAdapter.MyViewHolder> {
    private ArrayList<String> al_images;
    View child;
    Context mContext;
    ArrayList<CityBeen> cityBeenArrayList;
    setTrianListClickListner trianListClickListner;
    LayoutInflater inflater;
    boolean isFrom = true;

    int pos = 0;
    TrainBookFragment trainBookFragment;

    public interface setTrianListClickListner {
        void onClickTrianList(CityBeen cityBeen, boolean z);
    }

    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_city)
        RelativeLayout rl_city;
        @BindView(R.id.tv_city_name)
        MyTextView tv_city_name;
        @BindView(R.id.tv_code)
        MyTextView tv_code;
        @BindView(R.id.tv_station_name)
        MyTextView tv_station_name;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }



    public SearchTrainListAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchTrainListAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, TrainBookFragment trainBookFragment, boolean z) {
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = trainBookFragment;
        this.isFrom = z;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, LiveStationListFragment liveStationListFragment) {
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = liveStationListFragment;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, LiveStationFragment liveStationFragment) {
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = liveStationFragment;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchtrainlist_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        try {
            if (this.cityBeenArrayList != null && this.cityBeenArrayList.size() > i) {
                myViewHolder.tv_city_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).cityName);
                myViewHolder.tv_station_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).name);
                myViewHolder.tv_code.setText(((CityBeen) this.cityBeenArrayList.get(i)).code);
                myViewHolder.rl_city.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (SearchTrainListAdapter.this.trianListClickListner != null) {
                            SearchTrainListAdapter.this.trianListClickListner.onClickTrianList((CityBeen) SearchTrainListAdapter.this.cityBeenArrayList.get(i), SearchTrainListAdapter.this.isFrom);
                        } else {
                            Toast.makeText(SearchTrainListAdapter.this.mContext, SearchTrainListAdapter.this.mContext.getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.cityBeenArrayList.size();
    }
}



/*

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.kha.train.R;
import com.kha.train.fragment.LiveStationFragment;
import com.kha.train.fragment.LiveStationListFragment;
import com.kha.train.fragment.TrainBookFragment;
import com.kha.train.fragment.TrainRouteListFragment;
import com.kha.train.model.CityBeen;
import com.kha.train.widget.MyTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTrainListAdapter extends Adapter<SearchTrainListAdapter.MyViewHolder> {
    private ArrayList<String> al_images;
    View child;
    String TAG="SearchTrainListAdapter";
    ArrayList<CityBeen> cityBeenArrayList;
    LayoutInflater inflater;
    boolean isFrom = true;
    boolean isfrag = false;
    Context mContext;
    int pos = 0;
    TrainBookFragment trainBookFragment;
    setTrianListClickListner trianListClickListner;

    public interface setTrianListClickListner {
        void onClickTrianList(CityBeen cityBeen, boolean z);
    }

    public class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_city)
        RelativeLayout rl_city;
        @BindView(R.id.tv_city_name)
        MyTextView tv_city_name;
        @BindView(R.id.tv_code)
        MyTextView tv_code;
        @BindView(R.id.tv_station_name)
        MyTextView tv_station_name;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind((Object) this, view);
        }
    }


    public SearchTrainListAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SearchTrainListAdapter(Context context, TrainRouteListFragment trainRouteListFragment) {
        this.mContext = context;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, TrainBookFragment trainBookFragment, boolean z) {
        Log.e(TAG, "SearchTrainListAdapter: method 1 call");
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = trainBookFragment;
        this.isFrom = z;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, LiveStationListFragment liveStationListFragment) {
        for (int i = 0; i < arrayList.size(); i++) {
            Log.e(TAG, "SearchTrainListAdapter: "+ arrayList.get(i).cityName );
        }
        Log.e(TAG, "SearchTrainListAdapter: method 2 call" );
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = liveStationListFragment;
    }

    public SearchTrainListAdapter(Context context, ArrayList<CityBeen> arrayList, LiveStationFragment liveStationFragment) {
        Log.e(TAG, "SearchTrainListAdapter: method 3 call " );
        this.mContext = context;
        this.cityBeenArrayList = arrayList;
        this.trianListClickListner = liveStationFragment;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchtrainlist_rowlist, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        Log.e(TAG, "onBindViewHolder:  " );
        try {
            if (this.cityBeenArrayList != null && this.cityBeenArrayList.size() > i) {

                myViewHolder.tv_city_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).cityName);
                myViewHolder.tv_station_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).name);
                myViewHolder.tv_code.setText(((CityBeen) this.cityBeenArrayList.get(i)).code);
                Log.e(TAG, "onBindViewHolder: "+cityBeenArrayList.get(i).code );
                myViewHolder.rl_city.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (trianListClickListner != null) {
                            trianListClickListner.onClickTrianList((CityBeen) cityBeenArrayList.get(i), isFrom);

                        } else {
                            Toast.makeText(mContext, SearchTrainListAdapter.this.mContext.getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.cityBeenArrayList.size();
    }
}
*/
