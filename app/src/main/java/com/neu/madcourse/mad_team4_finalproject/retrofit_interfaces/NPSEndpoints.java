package com.neu.madcourse.mad_team4_finalproject.retrofit_interfaces;

import com.neu.madcourse.mad_team4_finalproject.apiModels.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.apiModels.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.apiModels.ThingsToDoResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NPSEndpoints {
    @GET("parks")
    Call<ParkResult> getParkResults();

    @GET("thingstodo")
    Call<ThingsToDoResult> getThingsToDoResults();

//    @Headers("api_key: " + "gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I")
    @GET("activities?api_key=gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I")
    Call<ActivityResult> getActivityResults();
}
