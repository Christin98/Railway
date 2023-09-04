package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainListByCdNBeen {
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("t")
    @Expose
    public Double f100t;
    @SerializedName("success")
    @Expose
    public Boolean success;

    public class C1089R {
        @SerializedName("dn")
        @Expose
        public String f102dn;
        @SerializedName("_id")
        @Expose
        public String f103id;
        @SerializedName("n")
        @Expose
        public String f104n;
        @SerializedName("xtr")
        @Expose
        public Xtr xtr;
    }

    public class Data {
        @SerializedName("r")
        @Expose
        public List<C1089R> f101r = null;
    }

    public class Xtr {
        @SerializedName("tid")
        @Expose
        public String tid;
    }
}
