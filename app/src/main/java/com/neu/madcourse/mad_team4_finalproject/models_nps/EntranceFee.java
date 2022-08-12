package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EntranceFee implements Serializable {
    public int cost;
    public String description;
    @SerializedName("title")
    public String feeInfo;

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getFeeInfo() {
        return feeInfo;
    }
}
