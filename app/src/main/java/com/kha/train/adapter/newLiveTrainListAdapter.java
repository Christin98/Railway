package com.kha.train.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kha.train.R;
import com.kha.train.model.CityBeen;
import com.kha.train.widget.MyTextView;

import java.util.ArrayList;

public class newLiveTrainListAdapter  extends RecyclerView.Adapter<newLiveTrainListAdapter.myApp> {

    String TAG="newLiveTrainListAdapter";
    Context mContext;
    ArrayList<CityBeen> cityBeenArrayList;
    newLiveTrainListAdapter.setTrianListClickListner trianListClickListner;
    boolean isFrom = true;

    public interface setTrianListClickListner {
        void onClickTrianList(CityBeen cityBeen, boolean z);
    }

    public newLiveTrainListAdapter(Context mContext, ArrayList<CityBeen> cityBeenArrayList, newLiveTrainListAdapter.setTrianListClickListner trianListClickListner) {
        Log.e(TAG, "newLiveTrainListAdapter: call Adapter " );
        this.mContext = mContext;
        this.cityBeenArrayList = cityBeenArrayList;
        this.trianListClickListner = trianListClickListner;
    }
    @NonNull
    @Override
    public newLiveTrainListAdapter.myApp onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.searchtrainlist_rowlist,parent,false);
        myApp myApp=new myApp(view);
        return myApp;
    }

    @Override
    public void onBindViewHolder(@NonNull newLiveTrainListAdapter.myApp myViewHolder, int i) {


        Log.e(TAG, "cityName: call brfore " +cityBeenArrayList.get(i).cityName);
        try {
            if (this.cityBeenArrayList != null && this.cityBeenArrayList.size() > i) {

                Log.e(TAG, "cityName: call cityName " +cityBeenArrayList.get(i).cityName);

                myViewHolder.tv_city_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).cityName);
                myViewHolder.tv_station_name.setText(((CityBeen) this.cityBeenArrayList.get(i)).name);
                myViewHolder.tv_code.setText(((CityBeen) this.cityBeenArrayList.get(i)).code);
                myViewHolder.rl_city.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (trianListClickListner != null) {
                           trianListClickListner.onClickTrianList((CityBeen) cityBeenArrayList.get(i), isFrom);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return cityBeenArrayList.size();
    }

    public class myApp extends RecyclerView.ViewHolder {
        RelativeLayout rl_city;
        MyTextView tv_city_name;
        MyTextView tv_code;
        MyTextView tv_station_name;

        public myApp(@NonNull View itemView) {
            super(itemView);
            rl_city=itemView.findViewById(R.id.rl_city);
            tv_city_name=itemView.findViewById(R.id.tv_city_name);
            tv_code=itemView.findViewById(R.id.tv_code);
            tv_station_name=itemView.findViewById(R.id.tv_station_name);
        }
    }
}
