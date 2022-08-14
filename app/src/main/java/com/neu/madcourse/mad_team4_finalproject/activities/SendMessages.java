package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySendMessagesBinding;

public class SendMessages extends AppCompatActivity {
    private ActivitySendMessagesBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messages);
        mBinding = ActivitySendMessagesBinding.inflate(getLayoutInflater());
        mBinding.getRoot();
    }
}