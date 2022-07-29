package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

public class EntrancePass {
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
