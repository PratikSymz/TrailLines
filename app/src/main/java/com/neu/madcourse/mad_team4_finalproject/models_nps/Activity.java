package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Activity implements Serializable {
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
