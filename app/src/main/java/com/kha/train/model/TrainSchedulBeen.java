package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainSchedulBeen {
    @SerializedName("DaysOfRun")
    @Expose
    public DaysOfRun daysOfRun;
    @SerializedName("Destination")
    @Expose
    public Destination destination;
    @SerializedName("Disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("FromCity")
    @Expose
    public String fromCity;
    @SerializedName("LastUpdatedTime")
    @Expose
    public String lastUpdatedTime;
    @SerializedName("NumberOfStops")
    @Expose
    public Long numberOfStops;
    @SerializedName("RatingDetails")
    @Expose
    public RatingDetails ratingDetails;
    @SerializedName("Schedule")
    @Expose
    public List<Schedule> schedule = null;
    @SerializedName("Source")
    @Expose
    public Source source;
    @SerializedName("ToCity")
    @Expose
    public String toCity;
    @SerializedName("TotalDuration")
    @Expose
    public String totalDuration;
    @SerializedName("Train")
    @Expose
    public Train train;

    public class ArrivalDetails {
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class DaysOfRun {
        @SerializedName("Fri")
        @Expose
        public Boolean fri;
        @SerializedName("Mon")
        @Expose
        public Boolean mon;
        @SerializedName("Sat")
        @Expose
        public Boolean sat;
        @SerializedName("Sun")
        @Expose
        public Boolean sun;
        @SerializedName("Thu")
        @Expose
        public Boolean thu;
        @SerializedName("Tue")
        @Expose
        public Boolean tue;
        @SerializedName("Wed")
        @Expose
        public Boolean wed;
    }

    public class DepartureDetails {
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class Destination {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Location {
        @SerializedName("latitude")
        @Expose
        public Double latitude;
        @SerializedName("longitude")
        @Expose
        public Double longitude;
    }

    public class RatingDetails {
        @SerializedName("PunctualityRating")
        @Expose
        public Double punctualityRating;
    }

    public class Schedule {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails arrivalDetails;
        @SerializedName("DataChanged")
        @Expose
        public Boolean dataChanged;
        @SerializedName("Day")
        @Expose
        public Integer day;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("ExpectedPlatformNo")
        @Expose
        public String expectedPlatformNo;
        @SerializedName("HaltMinutes")
        @Expose
        public String haltMinutes;
        @SerializedName("Location")
        @Expose
        public Location location;
        @SerializedName("RouteNo")
        @Expose
        public Long routeNo;
        @SerializedName("Station")
        @Expose
        public Station station;
        @SerializedName("StopNumber")
        @Expose
        public Long stopNumber;
        @SerializedName("TrainMappingKey")
        @Expose
        public String trainMappingKey;
    }

    public class Source {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Station {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Train {
        @SerializedName("Classes")
        @Expose
        public List<String> classes = null;
        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("Number")
        @Expose
        public String number;
    }
}
