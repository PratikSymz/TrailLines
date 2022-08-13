package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Park implements Serializable {
    @SerializedName("activities")
    private List<Activity> activityList;
    @SerializedName("addresses")
    private List<Address> addressList;
    @SerializedName("contacts")
    private ParkContact parkContact;
    private String designation;
    //    @SerializedName("entranceFees")
//    private List<EntranceFee> entranceFeeList;
//    @SerializedName("entrancePasses")
//    private List<EntrancePass> entrancePassList;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("id")
    private String parkID;
    private String latitude;
    private String longitude;
    @SerializedName("images")
    private List<Image> imageList;
    @SerializedName("name")
    private String shortName;
    //    @SerializedName("operatingHours")
//    private List<ParkOperationsHours> parkOperationsHoursList;
    private String states;
    private String url;
    private String weatherInfo;
    private String description;

    public List<Activity> getActivityList() {
        return activityList;
    }

//    public List<Address> getAddressList() {
//        return addressList;
//    }

    public ParkContact getParkContact() {
        return parkContact;
    }

    public String getDesignation() {
        return designation;
    }
//
//    public List<EntranceFee> getEntranceFeeList() {
//        return entranceFeeList;
//    }
//
//    public List<EntrancePass> getEntrancePassList() {
//        return entrancePassList;
//    }

    public String getFullName() {
        return fullName;
    }

    public String getParkID() {
        return parkID;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public String getShortName() {
        return shortName;
    }

//    public List<ParkOperationsHours> getParkOperationsHoursList() {
//        return parkOperationsHoursList;
//    }

    public String getStates() {
        return states;
    }

    public String getUrl() {
        return url;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public String getDescription() {
        return description;
    }
}
