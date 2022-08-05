package com.neu.madcourse.mad_team4_finalproject.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityReviewScreenBinding;

public class ReviewScreen extends AppCompatActivity {
    private ActivityReviewScreenBinding mBinding;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityReviewScreenBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mBinding.viewNotRecommended.labelRecommendStatus.setText("Not Recommended");
        mBinding.viewNotRecommended.iconRecommendStatus.setImageResource(R.drawable.thumbs_down);
        mBinding.viewNotRecommended.getRoot().setBackgroundTintList(getResources().getColorStateList(R.color.background_tint));
    }
}