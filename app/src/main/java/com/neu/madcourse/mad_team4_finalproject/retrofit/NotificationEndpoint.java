package com.neu.madcourse.mad_team4_finalproject.retrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationEndpoint {
    @Headers({
            "Authorization: key=AIzaSyAy87ZeFt-N4VRxz0InXqcDIv2hRO3MIBk",
            "Sender: id=707222154198",
            "Content-Type: application/json"
    })
    @POST("send")
    Call<JSONObject> sendNotification(@Body JSONObject notification);
}
