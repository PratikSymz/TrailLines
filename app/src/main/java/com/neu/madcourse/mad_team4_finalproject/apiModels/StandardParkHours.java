package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

public class StandardParkHours {
    @SerializedName("sunday")
    public String sundayHours;
    @SerializedName("monday")
    public String mondayHours;
    @SerializedName("tuesday")
    public String tuesdayHours;
    @SerializedName("wednesday")
    public String wednesdayHours;
    @SerializedName("thursday")
    public String thursdayHours;
    @SerializedName("friday")
    public String fridayHours;
    @SerializedName("saturday")
    public String saturdayHours;

    public String getSundayHours() {
        return sundayHours;
    }

    public String getMondayHours() {
        return mondayHours;
    }

    public String getTuesdayHours() {
        return tuesdayHours;
    }

    public String getWednesdayHours() {
        return wednesdayHours;
    }

    public String getThursdayHours() {
        return thursdayHours;
    }

    public String getFridayHours() {
        return fridayHours;
    }

    public String getSaturdayHours() {
        return saturdayHours;
    }
}
