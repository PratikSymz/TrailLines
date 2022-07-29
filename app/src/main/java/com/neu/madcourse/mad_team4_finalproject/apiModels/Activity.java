package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

public class Activity {
    @SerializedName("id")
    public String recordId;
    public String name;

    public String getRecordId() {
        return recordId;
    }

    public String getName() {
        return name;
    }
}
