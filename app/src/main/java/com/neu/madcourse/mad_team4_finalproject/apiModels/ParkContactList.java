package com.neu.madcourse.mad_team4_finalproject.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ParkContactList {
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
