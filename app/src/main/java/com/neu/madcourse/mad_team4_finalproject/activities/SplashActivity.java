package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySplashBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

public class SplashActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = SplashActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivitySplashBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        mBaseUtils.applyListener(mBinding.holderGetStarted.getRoot(), view -> {
            //TODO add shared preference
        });
    }


}