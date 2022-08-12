package com.neu.madcourse.mad_team4_finalproject.retrofit;

import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ThingsToDoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NPSEndpoints {
    @GET("parks")
    Call<ParkResult> getParkResults(@Query("api_key") String apiKey, @Query("stateCode") String stateCode);

    @GET("thingstodo")
    Call<ThingsToDoResult> getThingsToDoResults(@Query("api_key") String apiKey);

    @GET("activities")
    Call<ActivityResult> getActivityResults(@Query("api_key") String apiKey);

    @GET("parks")
    Call<ParkResult> getActivityParkResults(@Query("api_key") String apiKey, @Query("stateCode") String stateCode, @Query("q") String activityId);

}
