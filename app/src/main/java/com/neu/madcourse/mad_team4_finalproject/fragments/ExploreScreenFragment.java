package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.neu.madcourse.mad_team4_finalproject.adapters.ActivityAdapter;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentExploreScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.retrofit_interfaces.NPSEndpoints;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExploreScreenFragment extends Fragment {
    private FragmentExploreScreenBinding mBinding;
    private ActivityAdapter activityAdapter;
    List<Activity> activityList;
    private final String TAG = ExploreScreenFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentExploreScreenBinding.inflate(getLayoutInflater(), container, false);
        activityList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        activityAdapter = new ActivityAdapter(activityList, getActivity());
        mBinding.horizontalTrailRecyclerView.setLayoutManager(layoutManager);
        mBinding.horizontalTrailRecyclerView.setAdapter(activityAdapter);
        NPSEndpoints npsEndpoints = getRetrofitClient();

        mBinding.filterButton.setOnClickListener(v -> {
            FilterBottomSheetDialog bottomSheet = new FilterBottomSheetDialog();
            bottomSheet.show(getActivity().getSupportFragmentManager(), bottomSheet.getTag());
        });


        npsEndpoints.getActivityResults().enqueue(new Callback<ActivityResult>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ActivityResult> call, @NonNull Response<ActivityResult> response) {
                assert response.body() != null;
                List<String> activities = List.of(Constants.ThingsToDoStrings.CAMPING, Constants.ThingsToDoStrings.CAMPING,
                        Constants.ThingsToDoStrings.CAMPING, Constants.ThingsToDoStrings.CANYONEERING,
                        Constants.ThingsToDoStrings.CAVING, Constants.ThingsToDoStrings.BOATING,
                        Constants.ThingsToDoStrings.BIKING, Constants.ThingsToDoStrings.CLIMBING,
                        Constants.ThingsToDoStrings.SCUBA_DIVING, Constants.ThingsToDoStrings.SKIING,
                        Constants.ThingsToDoStrings.SNORKELING, Constants.ThingsToDoStrings.SURFING,
                        Constants.ThingsToDoStrings.WATER_SKIING, Constants.ThingsToDoStrings.FISHING,
                        Constants.ThingsToDoStrings.PADDLING, Constants.ThingsToDoStrings.HIKING);

                activityList = response.body().getActivityList();
                List<Activity> filteredActivityList = activityList.stream().filter(activity -> activities.contains(activity.getName())).collect(Collectors.toList());
                activityAdapter = new ActivityAdapter(filteredActivityList, getActivity());
                mBinding.horizontalTrailRecyclerView.setAdapter(activityAdapter);
                Log.d(TAG, response.body().getDataCount());
            }

            @Override
            public void onFailure(@NonNull Call<ActivityResult> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
        return mBinding.getRoot();
    }

    /**
     * calling the retrofit from this method
     */
    public NPSEndpoints getRetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://developer.nps.gov/api/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(NPSEndpoints.class);
    }
}