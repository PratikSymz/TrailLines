package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

public class Activity {
    @SerializedName("id")
    public String activityId;
    public String name;
//    public Integer imageId;

    public String getRecordId() {
        return activityId;
    }

    public String getName() {
        return name;
    }

//    public Integer getImageId() {
//        return imageId;
//    }

//    public void setImageId(Integer imageId) {
//        this.imageId = imageId;
//    }

}
