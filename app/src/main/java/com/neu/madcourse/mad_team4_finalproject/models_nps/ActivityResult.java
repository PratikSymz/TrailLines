package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ActivityResult implements Serializable {
    @SerializedName("total")
    private String dataCount;
    @SerializedName("data")
    private List<Activity> activityList;
    @SerializedName("limit")
    private String pageLimit;
    @SerializedName("start")
    private String pageStart;

    public String getDataCount() {
        return dataCount;
    }


    public String getPageLimit() {
        return pageLimit;
    }

    public String getPageStart() {
        return pageStart;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }
}
