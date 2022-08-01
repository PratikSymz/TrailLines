package com.neu.madcourse.mad_team4_finalproject.AppMainScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityMainScreenBinding;

public class MainScreen extends AppCompatActivity {

    private ActivityMainScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}