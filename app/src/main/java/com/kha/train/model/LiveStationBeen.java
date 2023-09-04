package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LiveStationBeen {
    @SerializedName("Disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("StationDetails")
    @Expose
    public List<StationDetail> stationDetails = null;
    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("Trains")
    @Expose
    public List<Train> trains = null;

    public class ArrivalDetails {
        @SerializedName("actualArrivalTime")
        @Expose
        public String actualArrivalTime;
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class DepartureDetails {
        @SerializedName("actualDepartureTime")
        @Expose
        public String actualDepartureTime;
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class StationDetail {
        @SerializedName("code")
        @Expose
        public String code;
    }

    public class Train {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails arrivalDetails;
        @SerializedName("DelayString")
        @Expose
        public String delayString;
        @SerializedName("DelayTime")
        @Expose
        public String delayTime;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails departureDetails;
        @SerializedName("EndStation")
        @Expose
        public String endStation;
        @SerializedName("ExpectedPlatformNo")
        @Expose
        public String expectedPlatformNo;
        @SerializedName("StartStation")
        @Expose
        public String startStation;
        @SerializedName("Train")
        @Expose
        public Train_ train;
    }

    public class Train_ {
        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("Number")
        @Expose
        public String number;
    }
}
