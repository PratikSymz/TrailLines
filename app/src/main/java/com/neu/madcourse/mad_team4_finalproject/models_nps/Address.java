package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

public class Address {
    public String line1;
    public String line2;
    public String line3;
    public String city;
    public String stateCode;
    public String postalCode;
    @SerializedName("type")
    public String addressType;

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getCity() {
        return city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddressType() {
        return addressType;
    }
}
