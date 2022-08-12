package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EntrancePass implements Serializable {
    @SerializedName("cost")
    public int passCost;
    @SerializedName("description")
    public String passDescription;
    @SerializedName("title")
    public String passInfo;

    public int getPassCost() {
        return passCost;
    }

    public String getPassDescription() {
        return passDescription;
    }

    public String getPassInfo() {
        return passInfo;
    }
}
