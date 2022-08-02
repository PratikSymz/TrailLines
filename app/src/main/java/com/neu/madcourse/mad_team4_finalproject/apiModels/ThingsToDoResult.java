package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThingsToDoResult {
    @SerializedName("total")
    private String dataCount;
    @SerializedName("data")
    private List<ThingsToDo> thingsToDoList;
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

    public List<ThingsToDo> getThingsToDoList() {
        return thingsToDoList;
    }
}
