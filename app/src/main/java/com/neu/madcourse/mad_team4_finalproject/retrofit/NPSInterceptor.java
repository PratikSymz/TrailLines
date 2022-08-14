package com.neu.madcourse.mad_team4_finalproject.retrofit;

import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NPSInterceptor {
    /* Helper method to instantiate the Retrofit client */
    public NPSEndpoints getInterceptor() {
        // Build the client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Retrofit.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(NPSEndpoints.class);
    }
}
