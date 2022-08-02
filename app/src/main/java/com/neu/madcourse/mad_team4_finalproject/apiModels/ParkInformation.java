package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkInformation {
    public String fullName;
    @SerializedName("id")
    public String parkID;
    public String latitude;
    public String longitude;
    @SerializedName("images")
    private List<Image> imageList;
    @SerializedName("name")
    private String shortName;
    // TODO address, activities, description, directionUrl, parkID,

    public String getFullName() {
        return fullName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getParkID() {
        return parkID;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public String getShortName() {
        return shortName;
    }
}
