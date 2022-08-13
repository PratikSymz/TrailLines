package com.neu.madcourse.mad_team4_finalproject.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityEditProfileBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** This activity is launched when the user clicks on the Edit button on the profile activity
 * in order to edit their name, email and profile picture
 */
public class EditProfileActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = EditProfileActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityEditProfileBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Firebase Database reference */
    private DatabaseReference mUsersDatabaseRef;

    /* The Firebase Storage reference */
    private StorageReference mFirebaseStorage;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Profile picture URI reference */
    private Uri mServerUri, mLocalUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the firebase database reference - "users"
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().
                child(Constants.UserKeys.KEY_TLO);

        // Instantiate the firebase storage reference
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Check if the current user exists
        if (mFirebaseUser != null) {
            // Populate the views with the user's last saved information
            mBinding.viewUpdateName.setText(mFirebaseUser.getDisplayName());
            mBinding.viewUpdateEmail.setText(mFirebaseUser.getEmail());

            // Get the profile picture uri
            mServerUri = mFirebaseUser.getPhotoUrl();
            if (mServerUri != null) {
                Glide.with(mContext)
                        .load(mServerUri)
                        .placeholder(R.drawable.ic_profile_tag_account)
                        .error(R.drawable.ic_profile_tag_account)
                        .into(mBinding.viewProfileImage);
            }
        }

        // Set the profile image onClick menu popup action
        mBinding.viewProfileImage.setOnClickListener(this::updateImage);

        // Set the save button onClick action
        mBinding.viewButtonSaveChanges.setOnClickListener(view -> {
            // Extract the updated information
            String updatedName = Objects.requireNonNull(mBinding.viewUpdateName.getText()).toString();
            String updatedEmail = Objects.requireNonNull(mBinding.viewUpdateEmail.getText()).toString();
           // String updatePrivacyStatus = Objects.requireNonNull(mBinding.viewPrivateSwitch.getText()).toString();

            // Update firebase server information
            if (!mBaseUtils.isEmpty(updatedName) && !mBaseUtils.isEmpty(updatedEmail) &&
                    mBaseUtils.isValidEmail(updatedEmail)) {
                if ((mLocalUri != null)) {
                    updateLoginAndImageInfo(updatedName, updatedEmail);
                } else {
                    updateLoginInfo(updatedName, updatedEmail);
                }
            }

            if (mBaseUtils.isEmpty(updatedName)) {
                mBinding.viewUpdateName.setError(getString(R.string.invalid_name));
            }

            if (mBaseUtils.isEmpty(updatedEmail)) {
                mBinding.viewUpdateEmail.setError(getString(R.string.invalid_email));
            }
        });
    }

    /**
     * Helper method to update the personal information of the current user in the firebase database
     *  @param name  The user's updated full name
     * @param email The user's updated email address
     */
    private void updateLoginInfo(String name, String email) {
        // Get the profile change request reference
        final UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();

        // Update the user profile on firebase
        mFirebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(updateTask -> {
            if (updateTask.isSuccessful()) {
                // Get the current user's ID
                String userID = mFirebaseUser.getUid();

                // Initializing a new hashmap to update the database reference accordingly
                Map<String, String> personalInfoMap = new HashMap<>();
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_NAME, name);
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID, email);

                // Update the database reference JSON
                mUsersDatabaseRef.child(userID)
                        .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                        .setValue(personalInfoMap).addOnCompleteListener(task -> {
                            finish();
                        });
            } else {
                mBaseUtils.showToast(
                        String.format("Update Failed: %s", updateTask.getException()),
                        Toast.LENGTH_SHORT
                );
            }
        });
    }

    /**
     * Helper method to update the personal information of the current user in the firebase database
     * The profile image is selected from the local system and updated in the firebase storage
     *  @param name  The user's updated full name
     * @param email The user's updated email address
     */
    private void updateLoginAndImageInfo(String name, String email) {
        // Get the filename
        String fileName = String.format("%s.jpg", mFirebaseUser.getUid());

        // Get the images storage reference from the firebase storage
        final StorageReference imageRef = mFirebaseStorage.child(String.format("images/%s", fileName));

        // Upload the new profile picture
        imageRef.putFile(mLocalUri).addOnCompleteListener(imageTask -> {
            if (imageTask.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Update the last saved server uri
                    mServerUri = uri;

                    // Get the profile change request reference
                    final UserProfileChangeRequest profileChangeRequest =
                            new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(mServerUri)
                                    .build();

                    // Update the user profile on firebase
                    mFirebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            // Get the current user's ID
                            String userID = mFirebaseUser.getUid();

                            // Initializing a new hashmap to update the database reference accordingly
                            Map<String, String> personalInfoMap = new HashMap<>();
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_NAME, name);
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID, email);
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL, mServerUri.getPath());

                            // Update the database reference JSON
                            mUsersDatabaseRef.child(userID)
                                    .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                                    .setValue(personalInfoMap).addOnCompleteListener(task -> {
                                        finish();
                                    });
                        } else {
                            mBaseUtils.showToast(
                                    String.format("Update Failed: %s", updateTask.getException()),
                                    Toast.LENGTH_SHORT
                            );
                        }
                    });
                });
            }
        });
    }

    /* Helper method to launch the image selector options popup for the user to update their profile picture */
    @SuppressLint("NonConstantResourceId")
    public void updateImage(View view) {
        // If there is no image on the server
        if (mServerUri == null) {
            loadImagePicker();
        } else {
            // Load a popup menu with selection options
            PopupMenu popupMenu = new PopupMenu(mContext, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_image, popupMenu.getMenu());
            // Add an onItemClickListener
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_change_picture:
                        loadImagePicker();
                        return true;

                    case R.id.menu_item_remove_picture:
                        removeImage();
                        return true;
                }

                return false;
            });

            popupMenu.show();
        }
    }

    /* Helper method to remove the user's profile picture from the server */
    private void removeImage() {
        // Build a profile change request
        // Get the profile change request reference
        final UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest.Builder()
                        .setPhotoUri(null)
                        .build();

        // Update the user profile on firebase
        mFirebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(updateTask -> {
            if (updateTask.isSuccessful()) {
                // Get the current user's ID
                String userID = mFirebaseUser.getUid();

                // Initializing a new hashmap to update the database reference accordingly
                Map<String, String> personalInfoMap = new HashMap<>();
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL, "");

                // Update the database reference JSON
                mUsersDatabaseRef.child(userID)
                        .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                        .setValue(personalInfoMap).addOnCompleteListener(task -> {
                            mBaseUtils.showToast(getString(R.string.remove_picture_successful), Toast.LENGTH_SHORT);
                        });
            } else {
                mBaseUtils.showToast(
                        String.format("Update Failed: %s", updateTask.getException()),
                        Toast.LENGTH_SHORT
                );
            }
        });
    }

    /**
     * Helper method to update the user's profile picture from local storage.
     * Method asks user permission to access local image storage for image upload
     */
    private void loadImagePicker() {
        // Check the storage permissions
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }
    }

    /* Predefined method to seek permissions */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            } else {
                mBaseUtils.showToast(getString(R.string.permission_required), Toast.LENGTH_SHORT);
            }
        }
    }

    /* Helper method to set the profile image from the local device */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mLocalUri = Objects.requireNonNull(data).getData();
                mBinding.viewProfileImage.setImageURI(mLocalUri);
            }
        }
    }
}