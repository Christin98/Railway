package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainLiveStatusBeen {
    @SerializedName("Disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("Error")
    @Expose
    public Error error;
    @SerializedName("LastUpdated")
    @Expose
    public String lastUpdated;
    @SerializedName("MetaDetails")
    @Expose
    public MetaDetails_ metaDetails;
    @SerializedName("Stations")
    @Expose
    public List<Station> stations = null;
    @SerializedName("TrainDetails")
    @Expose
    public TrainDetails trainDetails;

    public class ArrivalDetails {
        @SerializedName("actualArrivalDate")
        @Expose
        public String actualArrivalDate;
        @SerializedName("actualArrivalTime")
        @Expose
        public String actualArrivalTime;
        @SerializedName("arrivalDelay")
        @Expose
        public Long arrivalDelay;
        @SerializedName("scheduledArrivalDate")
        @Expose
        public String scheduledArrivalDate;
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class ArrivalDetails_ {
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class ArrivalDetails__ {
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class ArrivalDetails___ {
        @SerializedName("actualArrivalDate")
        @Expose
        public String actualArrivalDate;
        @SerializedName("actualArrivalTime")
        @Expose
        public String actualArrivalTime;
        @SerializedName("arrivalDelay")
        @Expose
        public Long arrivalDelay;
        @SerializedName("scheduledArrivalDate")
        @Expose
        public String scheduledArrivalDate;
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class ArrivalDetails____ {
        @SerializedName("actualArrivalDate")
        @Expose
        public String actualArrivalDate;
        @SerializedName("actualArrivalTime")
        @Expose
        public String actualArrivalTime;
        @SerializedName("scheduledArrivalDate")
        @Expose
        public String scheduledArrivalDate;
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class ArrivalDetails_____ {
        @SerializedName("actualArrivalDate")
        @Expose
        public String actualArrivalDate;
        @SerializedName("actualArrivalTime")
        @Expose
        public String actualArrivalTime;
        @SerializedName("arrivalDelay")
        @Expose
        public Integer arrivalDelay;
        @SerializedName("scheduledArrivalDate")
        @Expose
        public String scheduledArrivalDate;
        @SerializedName("scheduledArrivalTime")
        @Expose
        public String scheduledArrivalTime;
    }

    public class BottomStation {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails_____ arrivalDetails;
        @SerializedName("DayDetails")
        @Expose
        public DayDetails___ dayDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails_____ departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("HaltMinutes")
        @Expose
        public Long haltMinutes;
        @SerializedName("JourneyDate")
        @Expose
        public String journeyDate;
        @SerializedName("MetaDetails")
        @Expose
        public MetaDetails____ metaDetails;
        @SerializedName("Station")
        @Expose
        public Station______ station;
    }

    public class CancelledFromStation {
    }

    public class CancelledToStation {
    }

    public class CurrentStation {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class CurrentStation_ {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails___ arrivalDetails;
        @SerializedName("DayDetails")
        @Expose
        public DayDetails_ dayDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails___ departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("HaltMinutes")
        @Expose
        public Long haltMinutes;
        @SerializedName("IntermediateStations")
        @Expose
        public List<IntermediateStation_> intermediateStations = null;
        @SerializedName("JourneyDate")
        @Expose
        public String journeyDate;
        @SerializedName("MetaDetails")
        @Expose
        public MetaDetails__ metaDetails;
        @SerializedName("Station")
        @Expose
        public Station____ station;
    }

    public class DayDetails {
        @SerializedName("dayCount")
        @Expose
        public Long dayCount;
        @SerializedName("dayDifference")
        @Expose
        public String dayDifference;
        @SerializedName("scheduledDayCount")
        @Expose
        public Long scheduledDayCount;
    }

    public class DayDetails_ {
        @SerializedName("dayCount")
        @Expose
        public Long dayCount;
        @SerializedName("dayDifference")
        @Expose
        public String dayDifference;
        @SerializedName("scheduledDayCount")
        @Expose
        public Long scheduledDayCount;
    }

    public class DayDetails__ {
        @SerializedName("dayCount")
        @Expose
        public Long dayCount;
        @SerializedName("dayDifference")
        @Expose
        public String dayDifference;
        @SerializedName("scheduledDayCount")
        @Expose
        public Long scheduledDayCount;
    }

    public class DayDetails___ {
        @SerializedName("dayCount")
        @Expose
        public Long dayCount;
        @SerializedName("dayDifference")
        @Expose
        public String dayDifference;
        @SerializedName("scheduledDayCount")
        @Expose
        public Long scheduledDayCount;
    }

    public class DepartureDetails {
        @SerializedName("actualDepartureDate")
        @Expose
        public String actualDepartureDate;
        @SerializedName("actualDepartureTime")
        @Expose
        public String actualDepartureTime;
        @SerializedName("departed")
        @Expose
        public Boolean departed;
        @SerializedName("departureDelay")
        @Expose
        public Long departureDelay;
        @SerializedName("scheduledDepartureDate")
        @Expose
        public String scheduledDepartureDate;
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class DepartureDetails_ {
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class DepartureDetails__ {
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class DepartureDetails___ {
        @SerializedName("actualDepartureDate")
        @Expose
        public String actualDepartureDate;
        @SerializedName("actualDepartureTime")
        @Expose
        public String actualDepartureTime;
        @SerializedName("departed")
        @Expose
        public Boolean departed;
        @SerializedName("departureDelay")
        @Expose
        public Long departureDelay;
        @SerializedName("scheduledDepartureDate")
        @Expose
        public String scheduledDepartureDate;
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class DepartureDetails____ {
        @SerializedName("actualDepartureDate")
        @Expose
        public String actualDepartureDate;
        @SerializedName("actualDepartureTime")
        @Expose
        public String actualDepartureTime;
        @SerializedName("departureDelay")
        @Expose
        public Integer departureDelay;
        @SerializedName("scheduledDepartureDate")
        @Expose
        public String scheduledDepartureDate;
        @SerializedName("scheduledDepartureTime")
        @Expose
        public String scheduledDepartureTime;
    }

    public class DepartureDetails_____ {
    }

    public class Error {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;
    }

    public class IntermediateStation {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails_ arrivalDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails_ departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("Location")
        @Expose
        public Location location;
        @SerializedName("Station")
        @Expose
        public Station__ station;
        @SerializedName("StopNumber")
        @Expose
        public Long stopNumber;
    }

    public class IntermediateStation_ {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails__ arrivalDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails__ departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("Location")
        @Expose
        public Location_ location;
        @SerializedName("Station")
        @Expose
        public Station___ station;
        @SerializedName("StopNumber")
        @Expose
        public Long stopNumber;
    }

    public class Location {
        @SerializedName("latitude")
        @Expose
        public Double latitude;
        @SerializedName("longitude")
        @Expose
        public Double longitude;
    }

    public class Location_ {
        @SerializedName("latitude")
        @Expose
        public Double latitude;
        @SerializedName("longitude")
        @Expose
        public Double longitude;
    }

    public class MetaDetails {
        @SerializedName("CancelledStation")
        @Expose
        public Boolean cancelledStation;
        @SerializedName("DivertedStation")
        @Expose
        public Boolean divertedStation;
        @SerializedName("StoppingStation")
        @Expose
        public Boolean stoppingStation;
    }

    public class MetaDetails_ {
        @SerializedName("BottomStation")
        @Expose
        public BottomStation bottomStation;
        @SerializedName("CurrentStation")
        @Expose
        public CurrentStation_ currentStation;
        @SerializedName("OtherDetails")
        @Expose
        public OtherDetails otherDetails;
        @SerializedName("TopStation")
        @Expose
        public TopStation topStation;
    }

    public class MetaDetails__ {
        @SerializedName("CancelledStation")
        @Expose
        public Boolean cancelledStation;
        @SerializedName("Details")
        @Expose
        public List<String> details = null;
        @SerializedName("DivertedStation")
        @Expose
        public Boolean divertedStation;
        @SerializedName("StoppingStation")
        @Expose
        public Boolean stoppingStation;
    }

    public class MetaDetails___ {
        @SerializedName("CancelledStation")
        @Expose
        public Boolean cancelledStation;
        @SerializedName("Details")
        @Expose
        public List<String> details = null;
        @SerializedName("DivertedStation")
        @Expose
        public Boolean divertedStation;
        @SerializedName("StoppingStation")
        @Expose
        public Boolean stoppingStation;
    }

    public class MetaDetails____ {
        @SerializedName("CancelledStation")
        @Expose
        public Boolean cancelledStation;
        @SerializedName("Details")
        @Expose
        public List<String> details = null;
        @SerializedName("DivertedStation")
        @Expose
        public Boolean divertedStation;
        @SerializedName("StoppingStation")
        @Expose
        public Boolean stoppingStation;
    }

    public class OtherDetails {
        @SerializedName("Delay")
        @Expose
        public Boolean delay;
        @SerializedName("DistanceDetail")
        @Expose
        public String distanceDetail;
        @SerializedName("TimeDetail")
        @Expose
        public String timeDetail;
    }

    public class Station {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails arrivalDetails;
        @SerializedName("DayDetails")
        @Expose
        public DayDetails dayDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("HaltMinutes")
        @Expose
        public Long haltMinutes;
        @SerializedName("IntermediateStations")
        @Expose
        public List<IntermediateStation> intermediateStations = null;
        @SerializedName("JourneyDate")
        @Expose
        public String journeyDate;
        @SerializedName("MetaDetails")
        @Expose
        public MetaDetails metaDetails;
        @SerializedName("Station")
        @Expose
        public Station_ station;
    }

    public class Station_ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("platformNumber")
        @Expose
        public Long platformNumber;
    }

    public class Station__ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Station___ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Station____ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class Station_____ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("platformNumber")
        @Expose
        public Long platformNumber;
    }

    public class Station______ {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("platformNumber")
        @Expose
        public Long platformNumber;
    }

    public class TopStation {
        @SerializedName("ArrivalDetails")
        @Expose
        public ArrivalDetails____ arrivalDetails;
        @SerializedName("DayDetails")
        @Expose
        public DayDetails__ dayDetails;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails____ departureDetails;
        @SerializedName("Distance")
        @Expose
        public String distance;
        @SerializedName("HaltMinutes")
        @Expose
        public Long haltMinutes;
        @SerializedName("JourneyDate")
        @Expose
        public String journeyDate;
        @SerializedName("MetaDetails")
        @Expose
        public MetaDetails___ metaDetails;
        @SerializedName("Station")
        @Expose
        public Station_____ station;
    }

    public class TrainDetails {
        @SerializedName("CancelledFromStation")
        @Expose
        public CancelledFromStation cancelledFromStation;
        @SerializedName("CancelledToStation")
        @Expose
        public CancelledToStation cancelledToStation;
        @SerializedName("CurrentStation")
        @Expose
        public CurrentStation currentStation;
        @SerializedName("Departed")
        @Expose
        public Boolean departed;
        @SerializedName("DistanceCovered")
        @Expose
        public Integer distanceCovered;
        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("Number")
        @Expose
        public String number;
        @SerializedName("StartDate")
        @Expose
        public String startDate;
        @SerializedName("StartDay")
        @Expose
        public String startDay;
        @SerializedName("StartDayDifference")
        @Expose
        public String startDayDifference;
        @SerializedName("Terminated")
        @Expose
        public Boolean terminated;
    }
}
