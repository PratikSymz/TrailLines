package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.madcourse.mad_team4_finalproject.MainActivity;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = SplashActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivitySplashBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        new Handler().postDelayed(() -> {
            //This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }, 1500);
    }
}
