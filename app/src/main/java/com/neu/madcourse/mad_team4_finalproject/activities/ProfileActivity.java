package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.neu.madcourse.mad_team4_finalproject.adapters.ProfileTagAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityProfileBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

public class ProfileActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = ProfileActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityProfileBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Setup the profile tag list items
        setupProfileTags();
    }

    private void setupProfileTags() {
        // Setup the Profile Tag adapter
        ProfileTagAdapter mAdapter = new ProfileTagAdapter(mContext, mBaseUtils.getProfileTags());
        // Set the Recycler view parameters

    }
}