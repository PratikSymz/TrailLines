package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.neu.madcourse.mad_team4_finalproject.adapters.ActivityAdapter;
import com.neu.madcourse.mad_team4_finalproject.adapters.ParkAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentExploreScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.retrofit_interfaces.NPSEndpoints;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExploreScreenFragment extends Fragment {
    /* The Fragment log tag */
    private final String TAG = ExploreScreenFragment.class.getSimpleName();

    /* The corresponding Activity context */
    private Context mActivityContext;

    /* The Fragment layout binding reference */
    private FragmentExploreScreenBinding mBinding;

    /* The Activities list adapter reference */
    private ActivityAdapter mActivityAdapter;

    /* The Activities list reference */
    private List<Activity> mActivityList = new ArrayList<>();

    /* The Park list adapter reference */
    private ParkAdapter mParkAdapter;

    /* The Park list reference */
    private List<Park> mParkList = new ArrayList<>();

    /* The Base Utils reference */
    private BaseUtils mBaseUtils;

    /* The Retrofit Endpoint interface reference */
    private NPSEndpoints mEndpoints;

    /* The Defined list of activities */
    private final List<String> ACTIVITY_CODES = List.of(
            Constants.ThingsToDoCodes.CAMPING_CODE, Constants.ThingsToDoCodes.BIKING_CODE,
            Constants.ThingsToDoCodes.BOATING_CODE, Constants.ThingsToDoCodes.CANYONEERING_CODE,
            Constants.ThingsToDoCodes.CAVING_CODE, Constants.ThingsToDoCodes.CLIMBING_CODE,
            Constants.ThingsToDoCodes.FISHING_CODE, Constants.ThingsToDoCodes.HIKING_CODE,
            Constants.ThingsToDoCodes.PADDLING_CODE, Constants.ThingsToDoCodes.SCUBA_DIVING_CODE,
            Constants.ThingsToDoCodes.SNORKELING_CODE, Constants.ThingsToDoCodes.SKIING_CODE,
            Constants.ThingsToDoCodes.SURFING_CODE, Constants.ThingsToDoCodes.WATER_SKIING_CODE
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentExploreScreenBinding.inflate(getLayoutInflater(), container, false);

        // Set the activity context
        mActivityContext = requireActivity();

        // Set the recycler view parameters for the "Horizontal" recycler view -> Activities
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(mActivityContext,
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mActivityAdapter = new ActivityAdapter(mActivityList, mActivityContext);
        mBinding.horizontalTrailRecyclerView.setLayoutManager(horizontalLayoutManager);
        mBinding.horizontalTrailRecyclerView.setAdapter(mActivityAdapter);

        // Set the recycler view parameters for the "Vertical" recycler view -> Parks
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(mActivityContext);
        mParkAdapter = new ParkAdapter(mParkList, mActivityContext);
        mBinding.verticalTrailRecyclerView.setLayoutManager(verticalLayoutManager);
        mBinding.verticalTrailRecyclerView.setAdapter(mParkAdapter);

        // Instantiate the base utils reference
        mBaseUtils = new BaseUtils(mActivityContext);
        // Load the state codes map
        mBaseUtils.initStateMap();

        // Instantiate the NPS Endpoints reference
        mEndpoints = getRetrofitClient();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);

        /* The Search box on query change location extraction */
        mBinding.holderSearchBox.setStartIconOnClickListener(view -> {
            Address address = getLocation(Objects.requireNonNull(mBinding.viewSearchQuery.getText()).toString());
            String location = (address != null) ? address.getAdminArea() : "null";
            Log.d(TAG, location);
        });

        /* Set the onClick action for the filter button */
        mBinding.filterButton.setOnClickListener(view -> {
            FilterBottomSheetDialog bottomSheet = new FilterBottomSheetDialog();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
        });

        /* Initiate the "Activity list" endpoint callback */
        mEndpoints.getActivityResults(Constants.Retrofit.API_KEY).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ActivityResult> call, @NonNull Response<ActivityResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extract the Activity results
                    ActivityResult results = response.body();
                    // Extract the activity list data from the response
                    mActivityList = results.getActivityList();

                    // TODO: HOW TO? Filter the list of activities
                    List<Activity> filteredActivityList = mActivityList.stream()
                            .filter(activity -> ACTIVITY_CODES.contains(activity.getRecordId())).collect(Collectors.toList());

                    // Update the adapter list
                    mActivityAdapter.updateDataList(filteredActivityList);
                    Log.d(TAG, results.getDataCount());
                } else {
                    mBaseUtils.showToast(
                            String.format("NPS Activities Response error: %s", response.errorBody()),
                            Toast.LENGTH_SHORT
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<ActivityResult> call, @NonNull Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });

        /* Initiate the "Park list" endpoint callback */
        mEndpoints.getParkResults(Constants.Retrofit.API_KEY, Constants.Retrofit.STATE_MA).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ParkResult> call, @NonNull Response<ParkResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extract the Park results
                    ParkResult results = response.body();
                    // Extract the park list
                    mParkList = results.getParkList();

                    // TODO: How is it being filtered?
                    List<Park> filteredParkList = new ArrayList<>();
                    for (Park park : mParkList) {
                        boolean flag = false;
                        for (Activity activity : park.getActivityList()) {
                            if (ACTIVITY_CODES.contains(activity.getRecordId())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            filteredParkList.add(park);
                        }
                    }

                    // Update the adapter list
                    mParkAdapter.updateDataList(mParkList);
                    Log.d(TAG, results.getDataCount());
                } else {
                    mBaseUtils.showToast(
                            String.format("NPS Parks Response error: %s", response.errorBody()),
                            Toast.LENGTH_SHORT
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<ParkResult> call, @NonNull Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    /**
     * Helper method to extract location coordinates from user input
     *
     * @param inputQuery The location input query from the user
     * @return the state code if the location exists
     */
    private Address getLocation(String inputQuery) {
        // Instantiate the Geocoder
        Geocoder geocoder = new Geocoder(mActivityContext);
        // The Address reference
        Address locationAddress = null;
        try {
            // Extract the list of addresses from the location text
            List<Address> addressList = geocoder.getFromLocationName(inputQuery, 1);
            // Extract the first address
            if (!addressList.isEmpty()) locationAddress = addressList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationAddress;
    }

    /* Helper method to instantiate the Retrofit client */
    public NPSEndpoints getRetrofitClient() {
        // Build the client
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.Retrofit.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(NPSEndpoints.class);
    }
}