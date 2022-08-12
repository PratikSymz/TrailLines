package com.neu.madcourse.mad_team4_finalproject.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ActivityAdapter;
import com.neu.madcourse.mad_team4_finalproject.adapters.ParkAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentExploreScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.interfaces.LocationResultListener;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ActivityResult;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.retrofit.NPSEndpoints;
import com.neu.madcourse.mad_team4_finalproject.retrofit.NPSInterceptor;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    /* The Location Utils reference */
    private LocationUtils mLocationUtils;

    /* The Retrofit Endpoints client reference */
    private final NPSEndpoints mEndpoints = new NPSInterceptor().getInterceptor();

    /* The Location request reference */
    private LocationRequest mLocationRequest;

    /* The Location provider client */
    private FusedLocationProviderClient mLocationClient;

    /* The Current Location reference */
    private Location mLocation = null;

    /* The Firebase Database reference -> "reviews" */
    private DatabaseReference mReviewsDatabaseRef;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Location permission request code */
    private final int REQUEST_CODE_LOCATION = 101;

    /* The Resolution permission request code */
    private final int REQUEST_CODE_RESOLUTION = 102;

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

        // Instantiate the location utils reference
        mLocationUtils = new LocationUtils(mActivityContext);

        // Instantiate the location request
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setNumUpdates(1);

        // Instantiate the location client
        mLocationClient = LocationServices.getFusedLocationProviderClient(mActivityContext);

        // Show the park list progress bar
        mBinding.verticalTrailRecyclerView.setVisibility(View.INVISIBLE);
        mBinding.groupParkProgress.setVisibility(View.VISIBLE);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);

        /* The Search box on query change location extraction */
        mBinding.holderSearchBox.setStartIconOnClickListener(view -> {
            String query = Objects.requireNonNull(mBinding.viewSearchQuery.getText()).toString();
            Address address = mLocationUtils.getLocationFromQuery(query);
            String location = (address != null) ? address.getAdminArea() : "null";
            Log.d(TAG, location);
        });

        /* Set the onClick action for the filter button */
        mBinding.filterButton.setOnClickListener(view -> {
            FilterBottomSheetDialog bottomSheet = new FilterBottomSheetDialog();
            bottomSheet.show(requireActivity().getSupportFragmentManager(), bottomSheet.getTag());
        });

        // Load the activity information
        initiateActivityCallback();

        // Load the park information and retrieve the current location
        getCurrentLocation(location -> {
            // Extract the location
            mLocation = location;
            // Load the park information
            initiateParksCallback(mLocation);
            // Hide the park list progress bar
            mBinding.verticalTrailRecyclerView.setVisibility(View.VISIBLE);
            mBinding.groupParkProgress.setVisibility(View.INVISIBLE);
        });
    }

    /* Helper method to initiate the "Activity list" endpoint callback */
    private void initiateActivityCallback() {
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
    }

    /* Helper method to initiate the "Park list" endpoint callback */
    private void initiateParksCallback(@NonNull Location location) {
        Address currentLocationAddress = mLocationUtils.getLocationFromCoordinates(location);
        if (currentLocationAddress == null) {
            mBaseUtils.showToast("Current location not found!", Toast.LENGTH_SHORT);
        } else {
            // Extract the state code
            String stateCode = mBaseUtils.extractStateCode(currentLocationAddress.getAdminArea());
            if (stateCode == null) {
                mBaseUtils.showToast("Invalid location!", Toast.LENGTH_SHORT);
            } else {
                mEndpoints.getParkResults(Constants.Retrofit.API_KEY, stateCode).enqueue(new Callback<>() {
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
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private void getCurrentLocation(LocationResultListener resultListener) {
        final Location[] location = {null};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permissions are granted
            if (ContextCompat.checkSelfPermission(mActivityContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mActivityContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Check GPS enabled status
                if (mLocationUtils.isGPSEnabled()) {
                    // Make the location request
                    mLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);

                            if (locationResult.getLocations().size() > 0) {
                                location[0] = locationResult.getLocations().get(0);
                                LocationServices.getFusedLocationProviderClient(mActivityContext)
                                        .removeLocationUpdates(this);

                                // Update the listener
                                resultListener.onLocationResult(location[0]);
                                Log.d(TAG, location[0].toString());
                            }
                        }
                    }, Looper.getMainLooper());
                } else {
                    mLocationUtils.enableGPS(mLocationRequest);
                }
            } else {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION
                );
            }
        }
    }

    /* Override method to check and request permissions */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION && grantResults.length > 0) {
            if (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (mLocationUtils.isGPSEnabled()) {
                    getCurrentLocation(currentLocation -> {
                        // Extract the location
                        mLocation = currentLocation;
                        // Load the park information
                        initiateParksCallback(mLocation);
                        // Hide the park list progress bar
                        mBinding.verticalTrailRecyclerView.setVisibility(View.VISIBLE);
                        mBinding.groupParkProgress.setVisibility(View.INVISIBLE);
                    });
                } else {
                    mLocationUtils.enableGPS(mLocationRequest);
                }
            } else {
                mBaseUtils.showToast(getString(R.string.permission_required), Toast.LENGTH_SHORT);
            }
        }
    }

    /* Override method to retrieve current location */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION) {
            getCurrentLocation(currentLocation -> {
                // Extract the location
                mLocation = currentLocation;
                // Load the park information
                initiateParksCallback(mLocation);
                // Hide the park list progress bar
                mBinding.verticalTrailRecyclerView.setVisibility(View.VISIBLE);
                mBinding.groupParkProgress.setVisibility(View.INVISIBLE);
            });

        }
    }
}