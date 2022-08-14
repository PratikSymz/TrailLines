package com.neu.madcourse.mad_team4_finalproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityConnectionBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

public class ConnectionActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = ConnectionActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityConnectionBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Network utils reference */
    private NetworkUtils mNetworkUtils;

    /* The Connectivity manager network callback reference */
    private ConnectivityManager.NetworkCallback mNetworkCallback;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityConnectionBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the Network utils reference
        mNetworkUtils = new NetworkUtils(mContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mNetworkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    // Close the activity
                    finish();
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                }
            };

            // Register network callback with the connectivity manager
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            // Register the callback
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(),
                    mNetworkCallback
            );
        }

        // Set the retry connection button onClick action
        mBinding.viewConnectionRetry.setOnClickListener(view -> retryConnection());

        // Set the close screen button onClick action
        mBinding.viewCloseScreen.setOnClickListener(view -> closeScreen());
    }

    private void retryConnection() {
        // Show the progress bar
        mBinding.viewProgressBarConnection.setVisibility(View.VISIBLE);
        // Check the internet connection
        if (mNetworkUtils.isConnected()) {
            finish();
        } else {
            // Wait for 1 second and hide the progress bar in the meantime
            new Handler().postDelayed(() ->
                            mBinding.viewProgressBarConnection.setVisibility(View.INVISIBLE),
                    1000
            );
        }
    }

    private void closeScreen() {
        // Close the app if no internet connection, else close the current activity
        finishAffinity();
    }
}