package com.neu.madcourse.mad_team4_finalproject.Explore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentExploreScreenBinding;

public class ExploreScreenFragment extends Fragment {
    private FragmentExploreScreenBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentExploreScreenBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }
}