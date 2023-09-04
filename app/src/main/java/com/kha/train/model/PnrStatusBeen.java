package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PnrStatusBeen {
    @SerializedName("CityDetails")
    @Expose
    public CityDetails cityDetails;
    @SerializedName("Disclaimer")
    @Expose
    public String disclaimer;
    @SerializedName("PassengerDetails")
    @Expose
    public PassengerDetails passengerDetails;
    @SerializedName("PnrDetails")
    @Expose
    public PnrDetails pnrDetails;
    @SerializedName("RatingDetails")
    @Expose
    public RatingDetails ratingDetails;
    @SerializedName("StationDetails")
    @Expose
    public StationDetails stationDetails;
    @SerializedName("TrainDetails")
    @Expose
    public TrainDetails trainDetails;

    public class BoardingPoint {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class BookingDate {
    }

    public class CityDetails {
        @SerializedName("DestinationCity")
        @Expose
        public DestinationCity destinationCity;
        @SerializedName("SourceCity")
        @Expose
        public SourceCity sourceCity;
    }

    public class DepartureDetails {
        @SerializedName("ArrivalTime")
        @Expose
        public String arrivalTime;
        @SerializedName("DepartureTime")
        @Expose
        public String departureTime;
        @SerializedName("Duration")
        @Expose
        public String duration;
    }

    public class DestinationCity {
        @SerializedName("Code")
        @Expose
        public String code;
        @SerializedName("Name")
        @Expose
        public String name;
    }

    public class DestinationDoj {
        @SerializedName("Epoch")
        @Expose
        public Long epoch;
        @SerializedName("FormattedDate")
        @Expose
        public String formattedDate;
    }

    public class FareDetails {
    }

    public class PassengerDetails {
        @SerializedName("Count")
        @Expose
        public Long count;
        @SerializedName("PassengerStatus")
        @Expose
        public List<Passengerstatus> passengerStatus = null;
    }

    public class Passengerstatus {
        @SerializedName("Berth")
        @Expose
        public Long berth;
        @SerializedName("BookingBerthNo")
        @Expose
        public String bookingBerthNo;
        @SerializedName("BookingCoachId")
        @Expose
        public String bookingCoachId;
        @SerializedName("BookingStatus")
        @Expose
        public String bookingStatus;
        @SerializedName("BookingStatusIndex")
        @Expose
        public String bookingStatusIndex;
        @SerializedName("BookingStatusNew")
        @Expose
        public String bookingStatusNew;
        @SerializedName("Coach")
        @Expose
        public String coach;
        @SerializedName("ConfirmTktStatus")
        @Expose
        public String confirmTktStatus;
        @SerializedName("CurrentBerthNo")
        @Expose
        public String currentBerthNo;
        @SerializedName("CurrentCoachId")
        @Expose
        public String currentCoachId;
        @SerializedName("CurrentStatus")
        @Expose
        public String currentStatus;
        @SerializedName("CurrentStatusIndex")
        @Expose
        public String currentStatusIndex;
        @SerializedName("CurrentStatusNew")
        @Expose
        public String currentStatusNew;
        @SerializedName("Number")
        @Expose
        public Long number;
    }

    public class PnrDetails {
        @SerializedName("Class")
        @Expose
        public String _class;
        @SerializedName("AlertEligible")
        @Expose
        public Boolean alertEligible;
        @SerializedName("AlertSet")
        @Expose
        public Boolean alertSet;
        @SerializedName("BookingDate")
        @Expose
        public BookingDate bookingDate;
        @SerializedName("DestinationDoj")
        @Expose
        public DestinationDoj destinationDoj;
        @SerializedName("FareDetails")
        @Expose
        public FareDetails fareDetails;
        @SerializedName("Pnr")
        @Expose
        public String pnr;
        @SerializedName("PnrCurrentStatus")
        @Expose
        public String pnrCurrentStatus;
        @SerializedName("PredictionDetails")
        @Expose
        public PredictionDetails predictionDetails;
        @SerializedName("Quota")
        @Expose
        public String quota;
        @SerializedName("SourceDoj")
        @Expose
        public SourceDoj sourceDoj;
    }

    public class PredictionDetails {
        @SerializedName("PNR_PRED")
        @Expose
        public List<String> pNRPRED = null;
        @SerializedName("PNR_PRED_DISC")
        @Expose
        public String pNRPREDDISC;
        @SerializedName("PNR_PRED_RAC")
        @Expose
        public String pNRPREDRAC;
    }

    public class RatingDetails {
        @SerializedName("CleanlinessRating")
        @Expose
        public Double cleanlinessRating;
        @SerializedName("FoodRating")
        @Expose
        public Double foodRating;
        @SerializedName("PunctualityRating")
        @Expose
        public Double punctualityRating;
        @SerializedName("Rating")
        @Expose
        public Double rating;
        @SerializedName("RatingCount")
        @Expose
        public Long ratingCount;
    }

    public class ReservationUpto {
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("name")
        @Expose
        public String name;
    }

    public class SourceCity {
        @SerializedName("Code")
        @Expose
        public String code;
        @SerializedName("Name")
        @Expose
        public String name;
    }

    public class SourceDoj {
        @SerializedName("Epoch")
        @Expose
        public Long epoch;
        @SerializedName("FormattedDate")
        @Expose
        public String formattedDate;
    }

    public class StationDetails {
        @SerializedName("BoardingPoint")
        @Expose
        public BoardingPoint boardingPoint;
        @SerializedName("ReservationUpto")
        @Expose
        public ReservationUpto reservationUpto;
    }

    public class Train {
        @SerializedName("Name")
        @Expose
        public String name;
        @SerializedName("Number")
        @Expose
        public String number;
    }

    public class TrainDetails {
        @SerializedName("ChartPrepared")
        @Expose
        public Boolean chartPrepared;
        @SerializedName("DepartureDetails")
        @Expose
        public DepartureDetails departureDetails;
        @SerializedName("Train")
        @Expose
        public Train train;
        @SerializedName("TrainCancelledFlag")
        @Expose
        public Boolean trainCancelledFlag;
    }
}
