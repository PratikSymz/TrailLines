package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityChangePasswordBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = ChangePasswordActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityChangePasswordBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Set the change password onClick action
        mBinding.viewButtonChangePassword.setOnClickListener(view -> initiatePasswordChange());
    }

    /* Helper method to update the user's password on the Firebase auth server */
    private void initiatePasswordChange() {
        // Extract field values
        String password = Objects.requireNonNull(mBinding.viewUpdatePassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(mBinding.viewUpdateConfirmPassword.getText()).toString();

        // All fields valid
        if (!mBaseUtils.isEmpty(password) && !mBaseUtils.isEmpty(confirmPassword) && confirmPassword.equals(password)) {
            if (mFirebaseUser != null) {
                // Initiate the password update task
                mFirebaseUser.updatePassword(password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mBaseUtils.showToast(getString(R.string.password_change_successful), Toast.LENGTH_SHORT);
                        // TODO: Logout the user
                        finish();
                    } else {
                        mBaseUtils.showToast(getString(R.string.something_wrong), Toast.LENGTH_SHORT);
                    }
                });
            }
        }

        if (mBaseUtils.isEmpty(password)) {
            mBinding.viewUpdatePassword.setError(getString(R.string.invalid_password));
        }

        if (mBaseUtils.isEmpty(confirmPassword)) {
            mBinding.viewUpdateConfirmPassword.setError(getString(R.string.invalid_password));
        }

        if (!password.equals(confirmPassword)) {
            mBinding.viewUpdateConfirmPassword.setError(getString(R.string.password_not_match));
        }
    }
}