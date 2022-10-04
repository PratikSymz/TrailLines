package com.neu.madcourse.mad_team4_finalproject.activities;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.madcourse.mad_team4_finalproject.adapters.TrailImageAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityTrailImageBinding;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Image;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrailImageActivity extends AppCompatActivity {

    private ActivityTrailImageBinding mBinding;
    private TrailImageAdapter mAdapter;
    private Park mPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTrailImageBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPark = (Park) getIntent().getSerializableExtra("park_details");

        List<String> IMAGE_IDS = new ArrayList<>();
        if (mPark.getImageList() != null || !mPark.getImageList().isEmpty()) {
            IMAGE_IDS = mPark.getImageList().stream().map(Image::getUrl).collect(Collectors.toList());
        }

        // Extract the grid view
        String parkName = mPark.getFullName();
        GridView gridView = mBinding.gridview;
        // Instantiate the image adapter
        mAdapter = new TrailImageAdapter(this.getBaseContext(), IMAGE_IDS, parkName);
        // Set the adapter
        gridView.setAdapter(mAdapter);

        mBinding.textImage.setText(mPark.getFullName() + " Image Gallery");

        mBinding.imageButton.setOnClickListener(v -> finish());
    }
}