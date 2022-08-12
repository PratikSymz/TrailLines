package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParkEmailAddress implements Serializable {
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
