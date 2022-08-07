package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkOperationsHours {
    @SerializedName("name")
    public String operationsName;
    public String description;
    @SerializedName("standardHours")
    public List<StandardParkHours> standardParkHoursList;
    @SerializedName("exceptions")
    public List<HolidayHours> holidayHoursList;

    public String getOperationsName() {
        return operationsName;
    }

    public String getDescription() {
        return description;
    }

    public List<StandardParkHours> getStandardParkHoursList() {
        return standardParkHoursList;
    }

    public List<HolidayHours> getHolidayHoursList() {
        return holidayHoursList;
    }
}
