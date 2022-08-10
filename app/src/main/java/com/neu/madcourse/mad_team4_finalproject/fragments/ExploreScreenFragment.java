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
import com.neu.madcourse.mad_team4_finalproject.adapters.ParkAdapter;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentExploreScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.retrofit_interfaces.NPSEndpoints;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
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
    private ParkAdapter parkAdapter;
    List<Activity> activityList;
    List<Park> parklist;
    private final String TAG = ExploreScreenFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentExploreScreenBinding.inflate(getLayoutInflater(), container, false);
        activityList = new ArrayList<>();
        parklist = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        activityAdapter = new ActivityAdapter(activityList, getActivity());
        mBinding.horizontalTrailRecyclerView.setLayoutManager(layoutManager);
        mBinding.horizontalTrailRecyclerView.setAdapter(activityAdapter);

        LinearLayoutManager layoutManagerForParks = new LinearLayoutManager(getActivity());
        parkAdapter = new ParkAdapter(parklist, getActivity());
        mBinding.verticalTrailRecyclerView.setLayoutManager(layoutManagerForParks);
        mBinding.verticalTrailRecyclerView.setAdapter(parkAdapter);

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

        npsEndpoints.getParkResults("gRfVnZYb1bHwKtboVOQUS1kgFpP4243lIiYCY51I", "MA").enqueue(new Callback<ParkResult>() {
            @Override
            public void onResponse(Call<ParkResult> call, Response<ParkResult> response) {
                assert response.body() != null;
                parklist = response.body().getParkList();

                List<String> activityCodes = List.of(Constants.ThingsToDoCodes.CAMPING_CODE, Constants.ThingsToDoCodes.CAMPING_CODE,
                        Constants.ThingsToDoCodes.CAMPING_CODE, Constants.ThingsToDoCodes.CANYONEERING_CODE,
                        Constants.ThingsToDoCodes.CAVING_CODE, Constants.ThingsToDoCodes.BOATING_CODE,
                        Constants.ThingsToDoCodes.BIKING_CODE, Constants.ThingsToDoCodes.CLIMBING_CODE,
                        Constants.ThingsToDoCodes.SCUBA_DIVING_CODE, Constants.ThingsToDoCodes.SKIING_CODE,
                        Constants.ThingsToDoCodes.SNORKELING_CODE, Constants.ThingsToDoCodes.SURFING_CODE,
                        Constants.ThingsToDoCodes.WATER_SKIING_CODE, Constants.ThingsToDoCodes.FISHING_CODE,
                        Constants.ThingsToDoCodes.PADDLING_CODE, Constants.ThingsToDoCodes.HIKING_CODE);

                List<Park> filteredParkList = new ArrayList<>();
                for (Park park : parklist) {
                    boolean flag = false;
                    for (Activity activity: park.getActivityList()) {
                        if (activityCodes.contains(activity.getRecordId())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        filteredParkList.add(park);
                    }
                }

                parkAdapter = new ParkAdapter(filteredParkList, getActivity());
                mBinding.verticalTrailRecyclerView.setAdapter(parkAdapter);
                Log.d(TAG, response.body().getDataCount());
            }

            @Override
            public void onFailure(Call<ParkResult> call, Throwable t) {
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