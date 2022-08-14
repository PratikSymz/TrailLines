package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParkContactInfo implements Serializable {
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
