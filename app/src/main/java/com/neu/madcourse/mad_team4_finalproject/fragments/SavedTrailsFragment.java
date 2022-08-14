package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.neu.madcourse.mad_team4_finalproject.adapters.GenericAdapter;
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailsViewModel;
import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentSavedTrailsBinding;
import com.neu.madcourse.mad_team4_finalproject.databinding.VerticalTrailViewsBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

import java.util.ArrayList;

public class SavedTrailsFragment extends Fragment {
    /* The Fragment log tag */
    private final String LOG_TAG = SavedTrailsFragment.class.getSimpleName();

    /* The corresponding Activity context */
    private Context mActivityContext;

    /* The Fragment layout binding reference */
    private FragmentSavedTrailsBinding mBinding;

    /* The Generic adapter reference */
    private GenericAdapter<SavedTrail> mAdapter;

    /* The ViewModel database reference */
    private SavedTrailsViewModel mDBViewModel;

    /* The Base Utils reference */
    private BaseUtils mBaseUtils;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentSavedTrailsBinding.inflate(getLayoutInflater(), container, false);

        // Set the activity context
        mActivityContext = requireActivity();

        // Instantiate the view model
        mDBViewModel = new ViewModelProvider(
                this,
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(SavedTrailsViewModel.class);

        // Show the "No saved trails" label
        mBinding.labelNoSavedTrails.setVisibility(View.VISIBLE);
        mBinding.groupSavedTrails.setVisibility(View.INVISIBLE);

        // Instantiate the adapter
        mAdapter = new GenericAdapter<>(mActivityContext, new ArrayList<>(), VerticalTrailViewsBinding.inflate(getLayoutInflater()));
        // Set the recycler view parameters
        mBinding.recyclerViewSavedTrails.setLayoutManager(new LinearLayoutManager(mActivityContext));
        mBinding.recyclerViewSavedTrails.setAdapter(mAdapter);

        // Instantiate the base utils reference
        mBaseUtils = new BaseUtils(mActivityContext);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load the saved trails
        loadSavedTrails();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load the saved trails
        loadSavedTrails();
    }

    /* Helper method to load the saved trails */
    private void loadSavedTrails() {
        // Extract the saved trails from the database
        mDBViewModel.getSavedTrails().observe(getViewLifecycleOwner(), savedTrails -> {
            if (savedTrails.size() > 0) {
                mAdapter.updateDataList(savedTrails);

                // Hide the "No saved trails" label
                mBinding.labelNoSavedTrails.setVisibility(View.INVISIBLE);
                mBinding.groupSavedTrails.setVisibility(View.VISIBLE);
            } else {
                // Show the "No saved trails" label
                mBinding.labelNoSavedTrails.setVisibility(View.VISIBLE);
                mBinding.groupSavedTrails.setVisibility(View.INVISIBLE);
            }
        });
    }
}