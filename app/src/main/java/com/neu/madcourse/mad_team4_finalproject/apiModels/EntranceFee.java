package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

public class EntranceFee {
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
