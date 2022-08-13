package com.neu.madcourse.mad_team4_finalproject.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationInterceptor {
    private final static String BASE_URL = "https://fcm.googleapis.com/fcm/send/";

    public NotificationEndpoint getInterceptor() {
        Retrofit interceptor = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return interceptor.create(NotificationEndpoint.class);
    }
}
