package com.neu.madcourse.mad_team4_finalproject.models_nps;

import java.io.Serializable;

public class HolidayHours implements Serializable {
    public String sunday;
    public String monday;
    public String tuesday;
    public String wednesday;
    public String thursday;
    public String friday;
    public String saturday;

    public String getSunday() {
        return sunday;
    }

    public String getMonday() {
        return monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getFriday() {
        return friday;
    }

    public String getSaturday() {
        return saturday;
    }
}
