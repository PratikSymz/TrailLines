package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityAppSettingsPageBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class AppSettingsPage extends AppCompatActivity {
    /* The Activity layout view binding reference */
    private ActivityAppSettingsPageBinding mBinding;
    /* The Activity context */
    private Context mContext;
    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;
    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;
    /* The USERS database reference creating an object for database reference class */
    private DatabaseReference mUsersDatabaseRef;
    /* The Base utils reference */
    private BaseUtils mBaseUtils;
    /* The Firebase user reference create new firebase users and store the data */
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate the activity layout view binding
        mBinding = ActivityAppSettingsPageBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());
        mContext = this;
        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mBinding.viewLogoutButton.setOnClickListener(view -> {
            logout();
        });
        mBinding.viewForgotPasswordText.setOnClickListener(view -> {
            forgotPassword();
        });
        mBinding.viewPrivateProfileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProfileStatus(isChecked);
            }
        });
    }

    public void logout() {
        // Set the logout button onClick action
        // Also set the token removal when the user logs out of the chat app
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        mUsersDatabaseRef = rootReference.child(Constants.Tokens.KEY_TLO).child(mFirebaseUser.getUid());
        mUsersDatabaseRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseAuth.signOut();
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                } else {
                    mBaseUtils.showToast("Something went wrong", Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void forgotPassword() {
        startActivity(new Intent(mContext, ResetPasswordActivity.class));
        finish();
    }

    public void updateProfileStatus(boolean status) {
        String userID = firebaseUser.getUid();
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUsersDatabaseRef.child(Constants.UserKeys.KEY_TLO)
                .child(userID).child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PRIVATE_PROFILE).setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mBaseUtils.showToast("Private Profile status updated!", Toast.LENGTH_SHORT);
                        } else {
                            mBaseUtils.showToast("Task failed", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}