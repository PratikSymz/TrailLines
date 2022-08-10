package com.neu.madcourse.mad_team4_finalproject.retrofit_interfaces;

import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ThingsToDoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NPSEndpoints {
    @GET("parks")
    Call<ParkResult> getParkResults(@Query("api_key") String apiKey, @Query("stateCode") String stateCode);

    @GET("thingstodo?api_key=gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I")
    Call<ThingsToDoResult> getThingsToDoResults();

//    @Headers("api_key: " + "gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I")
    @GET("activities?api_key=gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I")
    Call<ActivityResult> getActivityResults();
}
