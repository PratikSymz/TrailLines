package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

public class Activity {
    @SerializedName("id")
    public String activityId;
    public String name;

    public String getRecordId() {
        return activityId;
    }
    public String getName() {
        return name;
    }


}
