package com.neu.madcourse.mad_team4_finalproject.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.activities.LoginActivity;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

import java.util.Objects;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class ChatMessagingService extends FirebaseMessagingService {
    private NotificationCompat.Builder notificationBuilder;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        NetworkUtils mNetworkUtils = new NetworkUtils(this);
        mNetworkUtils.updateDeviceToken(this, token);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getData().get(Constants.NOTIFICATION_TITLE);
        String message = remoteMessage.getData().get(Constants.NOTIFICATION_MESSAGE);

        Intent chatIntent = new Intent(this, LoginActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                chatIntent, PendingIntent.FLAG_ONE_SHOT);
        /* creating a notification manager to manage notification */
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /* code for setting a default sound for notification */
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /* building notification */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID,
                    Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(Constants.CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(this, Constants.CHANNEL_ID);

        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.attachment_bubble);
            notificationBuilder.setColor(getResources().getColor(R.color.hike_dark_green));
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSound);
            notificationBuilder.setContentIntent(pendingIntent);

            /* checking if the notification is a message or a video/image in a condition statement */
            if (Objects.requireNonNull(message).startsWith("https://firebasestorage.")) {
                try {
                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    Glide.with(this).asBitmap()
                            .load(message).into(new CustomTarget<Bitmap>(200, 100) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    bigPictureStyle.bigPicture(resource);
                                    notificationBuilder.setStyle(bigPictureStyle);
                                    notificationManager.notify(123, notificationBuilder.build());
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                } catch (Exception e) {
                    notificationBuilder.setContentText("New notification received");
                }
            } else {
                notificationBuilder.setContentText(message);
                /* notification manager created to manage the chat notifications */
                notificationManager.notify(123, notificationBuilder.build());
            }

            //TODO ask pratik about video 77 12:49 min mark
        }
    }
}