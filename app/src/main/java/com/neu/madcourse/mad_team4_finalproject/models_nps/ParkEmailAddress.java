package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

public class ParkEmailAddress {
    public String emailAddress;
    @SerializedName("description")
    public String emailDescription;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailDescription() {
        return emailDescription;
    }
}
