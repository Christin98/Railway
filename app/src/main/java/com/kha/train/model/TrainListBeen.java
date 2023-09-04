package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class TrainListBeen {
    @SerializedName("clientConfig")
    @Expose
    public ClientConfig clientConfig;
    @SerializedName("quotaList")
    @Expose
    public List<String> quotaList = null;
    @SerializedName("trainBtwnStnsList")
    @Expose
    public ArrayList<TrainBtwnStnsList> trainBtwnStnsList = null;
    @SerializedName("wakeupTime")
    @Expose
    public Long wakeupTime;

    public class ClientConfig {
        @SerializedName("cdnTncUrl")
        @Expose
        public String cdnTncUrl;
        @SerializedName("hotelCrossSellBannerText")
        @Expose
        public String hotelCrossSellBannerText;
        @SerializedName("hotelsCrossSellDeepLink")
        @Expose
        public String hotelsCrossSellDeepLink;
    }

    public static class TrainBtwnStnsList {
        @SerializedName("arrivalTime")
        @Expose
        public String arrivalTime;
        @SerializedName("avlClasses")
        @Expose
        public List<String> avlClasses = null;
        @SerializedName("departureTime")
        @Expose
        public String departureTime;
        @SerializedName("distance")
        @Expose
        public Long distance;
        @SerializedName("duration")
        @Expose
        public Long duration;
        @SerializedName("frmStnCode")
        @Expose
        public String frmStnCode;
        @SerializedName("frmStnName")
        @Expose
        public String frmStnName;
        @SerializedName("runningFri")
        @Expose
        public String runningFri;
        @SerializedName("runningMon")
        @Expose
        public String runningMon;
        @SerializedName("runningSat")
        @Expose
        public String runningSat;
        @SerializedName("runningSun")
        @Expose
        public String runningSun;
        @SerializedName("runningThu")
        @Expose
        public String runningThu;
        @SerializedName("runningTue")
        @Expose
        public String runningTue;
        @SerializedName("runningWed")
        @Expose
        public String runningWed;
        @SerializedName("toStnCode")
        @Expose
        public String toStnCode;
        @SerializedName("toStnName")
        @Expose
        public String toStnName;
        @SerializedName("trainName")
        @Expose
        public String trainName;
        @SerializedName("trainNumber")
        @Expose
        public String trainNumber;
        @SerializedName("trainType")
        @Expose
        public List<String> trainType = null;
    }
}
