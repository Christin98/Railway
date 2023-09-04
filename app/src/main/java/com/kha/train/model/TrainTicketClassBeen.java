package com.kha.train.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainTicketClassBeen {
    @SerializedName("avlDayList")
    @Expose
    public List<AvlDayList> avlDayList = null;
    @SerializedName("baseFare")
    @Expose
    public String baseFare;
    @SerializedName("bkgCfg")
    @Expose
    public BkgCfg bkgCfg;
    @SerializedName("cateringCharge")
    @Expose
    public String cateringCharge;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("dynamicFare")
    @Expose
    public Double dynamicFare;
    @SerializedName("fuelAmount")
    @Expose
    public String fuelAmount;
    @SerializedName("insuranceCharge")
    @Expose
    public Double insuranceCharge;
    @SerializedName("otherCharge")
    @Expose
    public String otherCharge;
    @SerializedName("reservationCharge")
    @Expose
    public String reservationCharge;
    @SerializedName("serverId")
    @Expose
    public String serverId;
    @SerializedName("serviceTax")
    @Expose
    public String serviceTax;
    @SerializedName("superfastCharge")
    @Expose
    public String superfastCharge;
    @SerializedName("tatkalFare")
    @Expose
    public String tatkalFare;
    @SerializedName("timeStamp")
    @Expose
    public String timeStamp;
    @SerializedName("totalCollectibleAmount")
    @Expose
    public String totalCollectibleAmount;
    @SerializedName("totalConcession")
    @Expose
    public String totalConcession;
    @SerializedName("totalFare")
    @Expose
    public String totalFare;
    @SerializedName("trainName")
    @Expose
    public String trainName;
    @SerializedName("wakeupTime")
    @Expose
    public Long wakeupTime;
    @SerializedName("wpServiceCharge")
    @Expose
    public String wpServiceCharge;
    @SerializedName("wpServiceTax")
    @Expose
    public String wpServiceTax;

    public class AvlDayList {
        @SerializedName("availablityDate")
        @Expose
        public String availablityDate;
        @SerializedName("availablityStatus")
        @Expose
        public String availablityStatus;
        @SerializedName("availablityType")
        @Expose
        public String availablityType;
        @SerializedName("currentBkgFlag")
        @Expose
        public String currentBkgFlag;
        @SerializedName("reason")
        @Expose
        public String reason;
        @SerializedName("reasonType")
        @Expose
        public String reasonType;
    }

    public class BkgCfg {
        @SerializedName("applicableBerthTypes")
        @Expose
        public List<String> applicableBerthTypes = null;
        @SerializedName("bedRollFlagEnabled")
        @Expose
        public String bedRollFlagEnabled;
        @SerializedName("childBerthMandatory")
        @Expose
        public String childBerthMandatory;
        @SerializedName("countryList")
        @Expose
        public String countryList;
        @SerializedName("foodChoiceEnabled")
        @Expose
        public String foodChoiceEnabled;
        @SerializedName("idRequired")
        @Expose
        public String idRequired;
        @SerializedName("lowerBerthApplicable")
        @Expose
        public String lowerBerthApplicable;
        @SerializedName("maxARPDays")
        @Expose
        public String maxARPDays;
        @SerializedName("maxChildAge")
        @Expose
        public String maxChildAge;
        @SerializedName("maxIdCardLength")
        @Expose
        public String maxIdCardLength;
        @SerializedName("maxInfants")
        @Expose
        public String maxInfants;
        @SerializedName("maxNameLength")
        @Expose
        public String maxNameLength;
        @SerializedName("maxPassengerAge")
        @Expose
        public String maxPassengerAge;
        @SerializedName("maxPassengers")
        @Expose
        public String maxPassengers;
        @SerializedName("maxRetentionDays")
        @Expose
        public String maxRetentionDays;
        @SerializedName("minIdCardLength")
        @Expose
        public String minIdCardLength;
        @SerializedName("minNameLength")
        @Expose
        public String minNameLength;
        @SerializedName("minPassengerAge")
        @Expose
        public String minPassengerAge;
        @SerializedName("newTimeTable")
        @Expose
        public String newTimeTable;
        @SerializedName("seniorCitizenApplicable")
        @Expose
        public String seniorCitizenApplicable;
        @SerializedName("srctnwAge")
        @Expose
        public String srctnwAge;
        @SerializedName("srctzTAge")
        @Expose
        public Long srctzTAge;
        @SerializedName("srctznAge")
        @Expose
        public String srctznAge;
        @SerializedName("travelInsuranceFareMsg")
        @Expose
        public String travelInsuranceFareMsg;
        @SerializedName("validIdCardTypes")
        @Expose
        public List<String> validIdCardTypes = null;
    }
}
