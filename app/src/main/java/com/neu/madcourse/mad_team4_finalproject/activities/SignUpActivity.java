package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.FirebaseConsole.ConsoleOutlook;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySignUpBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = SignUpActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivitySignUpBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Firebase user reference create new firebase users and store the data */
    private FirebaseUser firebaseUser;

    /* The USERS database reference creating an object for database reference class */
    private DatabaseReference mUsersDatabaseRef;

    /* The Firebase storage reference */
    private StorageReference fileStorage;


    private Uri localUri, serverUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the Firebase storage reference
        fileStorage = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Method to update the user profile picture from the phone storage during the sign up process
     * This method also will ask user permission to access local image storage for image upload
     *
     * @param view
     */
    public void imageUpload(View view) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);
        }
    }

    /**
     * Method to seek permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

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

    /**
     * This override method will set profile image from the local device
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                localUri = Objects.requireNonNull(data).getData();
                mBinding.viewProfilePicture.setImageURI(localUri);
            }
        }
    }

    /**
     * Method to update profile picture from the local system and update storage console on firebase
     */
    private void picAndNameUpdate() {
        String file = firebaseUser.getUid() + ".jpg";
        final StorageReference reference = fileStorage.child("images/" + file);
        mBinding.progressbar.getRoot().setVisibility(View.VISIBLE);
        reference.putFile(localUri).addOnCompleteListener(task -> {
            mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    serverUri = uri;
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(Objects.requireNonNull(mBinding.viewInputName.getText()).toString()
                                    .trim()).setPhotoUri(serverUri).build();
                    firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            String userID = firebaseUser.getUid();

                            // Initializing the database reference class
                            mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().
                                    child(Constants.UserKeys.KEY_TLO);

                            // Initializing a new hashmap to update the database reference accordingly
                            Map<String, String> personalInfoMap = new HashMap<>();
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_FIRST_NAME, Objects.requireNonNull(mBinding.viewInputName.getText()).toString().trim());
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID, Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim());
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS, String.valueOf(true));
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL, serverUri.getPath());
                            personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PRIVATE_PROFILE, String.valueOf(false));

                            mUsersDatabaseRef.child(userID)
                                    .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                                    .setValue(personalInfoMap).addOnCompleteListener(task2 -> {
                                        mBaseUtils.showToast(getString(R.string.sign_up_success), Toast.LENGTH_SHORT);
                                        startActivity(new Intent(mContext, LoginActivity.class));
                                    });
                        } else {
                            mBaseUtils.showToast(
                                    String.format("Update Failed! : %s", task1.getException()),
                                    Toast.LENGTH_SHORT);
                        }
                    });
                });
            }
        });
    }

    /**
     * Method to update the name of user after signing up in the firebase console
     * The Firebase console will have the userID which will have children as: Name, email, profile picture
     */
    private void updateUserName() {
        mBinding.progressbar.getRoot().setVisibility(View.VISIBLE);
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(Objects.requireNonNull(mBinding.viewInputName.getText()).toString()
                        .trim()).build();
        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(task -> {
            mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                String userID = firebaseUser.getUid();

                // Initializing the database reference class
                mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().
                        child(Constants.UserKeys.KEY_TLO);

                // Initializing a new hashmap to update the database reference accordingly
                Map<String, String> personalInfoMap = new HashMap<>();
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_FIRST_NAME, Objects.requireNonNull(mBinding.viewInputName.getText()).toString().trim());
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID, Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim());
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS, String.valueOf(true));
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL, " ");
                personalInfoMap.put(Constants.UserKeys.PersonalInfoKeys.KEY_PRIVATE_PROFILE, String.valueOf(false));
                mBinding.progressbar.getRoot().setVisibility(View.VISIBLE);
                mUsersDatabaseRef.child(userID)
                        .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                        .setValue(personalInfoMap).addOnCompleteListener(task1 -> {
                            mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                            mBaseUtils.showToast(getString(R.string.sign_up_success), Toast.LENGTH_SHORT);
                            startActivity(new Intent(mContext, LoginActivity.class));
                        });

            } else {
                mBaseUtils.showToast(
                        String.format("Update Failed! : %s", task.getException()),
                        Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * The onClick method to initiate the signup procedure to the Firebase Auth
     * server and verifying the user credentials for new user accounts creations
     *
     * @param view The login button view
     */
    public void signUpButton(View view) {
        String name = Objects.requireNonNull(mBinding.viewInputName.getText()).toString().trim();
        String email = Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mBinding.viewInputPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(mBinding.viewInputConfirmPassword.getText())
                .toString().trim();

        if (!mBaseUtils.isEmpty(name) && !mBaseUtils.isEmpty(email) && mBaseUtils.validateEmail(email)
                && !mBaseUtils.isEmpty(password) && !mBaseUtils.isEmpty(confirmPassword)
                && password.equals(confirmPassword)) {
            mBinding.progressbar.getRoot().setVisibility(View.VISIBLE);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                    /* creating a user profile */
                    firebaseUser = firebaseAuth.getCurrentUser();
                    if (localUri != null) {
                        picAndNameUpdate();
                    } else {
                        updateUserName();
                    }

                } else {
                    mBaseUtils.showToast(
                            String.format(getString(R.string.signup_fail), task.getException()),
                            Toast.LENGTH_SHORT);
                }
            });
        }

        if (mBaseUtils.isEmpty(name)) {
            mBinding.viewInputName.setError(getString(R.string.invalid_name));
        }
        if (mBaseUtils.isEmpty(email)) {
            mBinding.viewInputEmail.setError(getString(R.string.invalid_email));
        }
        if (mBaseUtils.isEmpty(password)) {
            mBinding.viewInputPassword.setError(getString(R.string.invalid_password));
        }
        if (mBaseUtils.isEmpty(confirmPassword)) {
            mBinding.viewInputConfirmPassword.setError(getString(R.string.invalid_password));
        }
        if (!mBaseUtils.validateEmail(email)) {
            mBinding.viewInputEmail.setError(getString(R.string.invalid_email));
        }
        if (!password.equals(confirmPassword)) {
            mBinding.viewInputConfirmPassword.setError(getString(R.string.password_not_match));
        }
    }
}