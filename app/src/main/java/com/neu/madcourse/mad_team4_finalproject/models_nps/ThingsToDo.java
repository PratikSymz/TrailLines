package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThingsToDo {
    @SerializedName("title")
    public String activityTitle;
    public String location;
    public String shortDescription;
    public String longDescription;
    public String locationDescription;
    public String petsDescription;
    public String arePetsPermitted;
    public String durationDescription;
    @SerializedName("activityDescription")
    public String trailDescription;
    @SerializedName("url")
    public String parkActivityUrl;
    public String duration; // this more of an estimation!
    @SerializedName("tags")
    public List<String> otherActivities;

    public String getLocation() {
        return location;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getPetsDescription() {
        return petsDescription;
    }

    public String getArePetsPermitted() {
        return arePetsPermitted;
    }

    public String getDurationDescription() {
        return durationDescription;
    }

    public String getTrailDescription() {
        return trailDescription;
    }

    public String getParkActivityUrl() {
        return parkActivityUrl;
    }

    public String getDuration() {
        return duration;
    }

    public List<String> getOtherActivities() {
        return otherActivities;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getActivityTitle() {
        return activityTitle;
    }
}
