package com.neu.madcourse.mad_team4_finalproject.models_nps;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ParkContact implements Serializable {
    @SerializedName("phoneNumbers")
    public List<ParkContactInfo> phoneNumbersList;
    @SerializedName("emailAddresses")
    public List<ParkEmailAddress> emailAddressesList;

    public List<ParkContactInfo> getPhoneNumbersList() {
        return phoneNumbersList;
    }

    public List<ParkEmailAddress> getEmailAddressesList() {
        return emailAddressesList;
    }
}
