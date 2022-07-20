package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.madcourse.mad_team4_finalproject.FirebaseConsole.ConsoleOutlook;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySignUpBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

import java.util.HashMap;
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
    /* create new firebase users and store the data */
    private FirebaseUser firebaseUser;
    /* creating an object for database reference class */
    private DatabaseReference databaseReference;

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
    }

    /**
     * Method to update the name of user after signing up in the firebase console
     * The Firebase console will have the userID which will have children as: Name, email, profile picture
     */

    private void updateUserName() {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(Objects.requireNonNull(mBinding.viewInputName.getText()).toString()
                        .trim()).build();
        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String userID = firebaseUser.getUid();
                    /* initializing the database reference class here */
                    databaseReference = FirebaseDatabase.getInstance().getReference().
                            child(ConsoleOutlook.userProfile);
                    // initializing a new hashmap to update the console outlook accordingly
                    HashMap<String, String> stringHashMap = new HashMap<>();
                    stringHashMap.put(ConsoleOutlook.firstName, Objects.requireNonNull(mBinding.viewInputName.getText()).toString().trim());
                    stringHashMap.put(ConsoleOutlook.userEmail, Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim());
                    stringHashMap.put(ConsoleOutlook.onlineStatus, String.valueOf(true));
                    stringHashMap.put(ConsoleOutlook.userPhoto, " ");
                    stringHashMap.put(ConsoleOutlook.privateProfile, String.valueOf(false));

                    databaseReference.child(userID).child("personal_info").setValue(stringHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mBaseUtils.showToast(getString(R.string.sign_up_success), Toast.LENGTH_SHORT);
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    });

                } else {
                    mBaseUtils.showToast(
                            String.format("Update Failed! : %s", task.getException()),
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * The onClick method to initiate the signup procedure to the Firebase Auth
     * server and verifying the user credentials for new user accounts creations
     *
     * @param v The login button view
     */

    public void signUpButton(View v) {
        String name = Objects.requireNonNull(mBinding.viewInputName.getText()).toString().trim();
        String email = Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mBinding.viewInputPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(mBinding.viewInputConfirmPassword.getText())
                .toString().trim();

        if (!mBaseUtils.isEmpty(name) && !mBaseUtils.isEmpty(email) && mBaseUtils.validateEmail(email)
                && !mBaseUtils.isEmpty(password) && !mBaseUtils.isEmpty(confirmPassword)
                && password.equals(confirmPassword)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    /* creating a user profile */
                    firebaseUser = firebaseAuth.getCurrentUser();
                    updateUserName();
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