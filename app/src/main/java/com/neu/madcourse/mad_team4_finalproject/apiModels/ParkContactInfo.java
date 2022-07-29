package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

public class ParkContactInfo {
    public String phoneNumber;
    @SerializedName("description")
    public String contactDescription;
    @SerializedName("extension")
    public String phoneExtension;
    @SerializedName("type")
    public String phoneType;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public String getPhoneType() {
        return phoneType;
    }
}
