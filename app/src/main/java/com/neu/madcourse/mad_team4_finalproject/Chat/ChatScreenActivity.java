package com.neu.madcourse.mad_team4_finalproject.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;

public class ChatScreenActivity extends AppCompatActivity {
    /* The Activity layout view binding reference */
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate the activity layout view binding
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());
    }
}