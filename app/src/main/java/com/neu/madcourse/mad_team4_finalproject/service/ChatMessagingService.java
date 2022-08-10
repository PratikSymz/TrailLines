package com.neu.madcourse.mad_team4_finalproject.service;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class ChatMessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        NetworkUtils mNetworkUtils = new NetworkUtils(this);
        mNetworkUtils.updateDeviceToken(this, token);
    }
}