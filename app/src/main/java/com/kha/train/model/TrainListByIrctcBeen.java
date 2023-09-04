package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainListByIrctcBeen {
    @SerializedName("object")
    @Expose
    public Obj obj;

    public class C1089R {
        @SerializedName("0")
        @Expose
        public String f102dn;
    }

    public class Obj {
        @SerializedName("body")
        @Expose
        public List<C1089R> f101r = null;
    }

    public class Xtr {
        @SerializedName("tid")
        @Expose
        public String tid;
    }
}
