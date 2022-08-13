package com.neu.madcourse.mad_team4_finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.activities.AppSettingsPage;
import com.neu.madcourse.mad_team4_finalproject.activities.ChangePasswordActivity;
import com.neu.madcourse.mad_team4_finalproject.activities.ContactUsPage;
import com.neu.madcourse.mad_team4_finalproject.activities.EditProfileActivity;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentProfileBinding;
import com.neu.madcourse.mad_team4_finalproject.models.User;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class ProfileFragment extends Fragment {
    /* The Fragment Log Tag */
    private final String LOG_TAG = ProfileFragment.class.getSimpleName();

    /* The Activity context */
    private Context mActivityContext;

    /* The Fragment layout view binding reference */
    private FragmentProfileBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Firebase Database reference -> "users" */
    private DatabaseReference mUsersDatabaseRef;

    /* The Firebase Storage reference */
    private StorageReference mFirebaseStorage;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);

        // Set the activity context
        mActivityContext = requireActivity();

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mActivityContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Instantiate the firebase database reference -> "user"
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO);

        // Instantiate the firebase storage reference
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Show the profile progress bar
        mBinding.viewProfileProgress.setVisibility(View.VISIBLE);
        mBinding.groupViewsProfile.setVisibility(View.INVISIBLE);

        /* Load the user profile information */
        loadUserInformation();

        /* Setup the profile tag list items */
        setupProfileTags();
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Load the user profile information */
        loadUserInformation();
    }

    /* Helper method to load user info */
    private void loadUserInformation() {
        // Load user info from Firebase
        mUsersDatabaseRef.child(mFirebaseUser.getUid())
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot personalInfoSnapshot) {
                        // Extract user information from snapshot
                        User user = personalInfoSnapshot.getValue(User.class);
                        assert user != null;

                        // Set the information on the views
                        // Profile image
                        Glide.with(mActivityContext)
                                .load(mFirebaseUser.getPhotoUrl())
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .placeholder(R.drawable.ic_person)
                                .error(R.drawable.ic_error)
                                .into(mBinding.viewProfileImage);

                        // Profile name
                        mBinding.viewProfileFullname.setText(user.getName());

                        // Profile email
                        mBinding.viewProfileEmail.setText(user.getEmailID());

                        // Add the onClick action to the edit button
                        mBinding.viewEditButton.setOnClickListener(view -> {
                            Intent intent = new Intent(mActivityContext, EditProfileActivity.class);
                            startActivity(intent);
                        });

                        // Hide the profile progress bar
                        mBinding.viewProfileProgress.setVisibility(View.INVISIBLE);
                        mBinding.groupViewsProfile.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mBaseUtils.showToast(
                                "Could not load profile information! Try again later",
                                Toast.LENGTH_SHORT
                        );
                    }
                });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setupProfileTags() {
        // Set the Settings button
        mBinding.viewButtonSettings.viewSectionIcon.setImageDrawable(
                mActivityContext.getDrawable(R.drawable.ic_profile_tag_settings)
        );
        mBinding.viewButtonSettings.viewSectionLabel.setText("Settings");
        mBinding.viewButtonSettings.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(mActivityContext, AppSettingsPage.class);
            startActivity(intent);
        });

        // Set the change password button
        mBinding.viewButtonUpdatePassword.viewSectionIcon.setImageDrawable(
                mActivityContext.getDrawable(R.drawable.ic_profile_tag_account)
        );
        mBinding.viewButtonUpdatePassword.viewSectionLabel.setText("Change Password");
        mBinding.viewButtonUpdatePassword.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(mActivityContext, ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Set the contact us
        mBinding.viewButtonContactUs.viewSectionIcon.setImageDrawable(
                mActivityContext.getDrawable(R.drawable.email)
        );
        mBinding.viewButtonContactUs.viewSectionLabel.setText("Contact Us");
        mBinding.viewButtonContactUs.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(mActivityContext, ContactUsPage.class);
            startActivity(intent);
        });

    }

    /* Helper method to convert filename into "images" database reference */
    private String formatFileName(String fileName) {
        return String.format(
                "%s/%s", Constants.FirebaseStorageKeys.KEY_IMAGES, fileName
        );
    }
}