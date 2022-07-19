package com.neu.madcourse.mad_team4_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());
    }
}