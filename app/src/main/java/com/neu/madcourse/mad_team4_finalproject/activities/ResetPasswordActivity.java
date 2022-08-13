package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityResetPasswordBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = ResetPasswordActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityResetPasswordBinding mBinding;

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
        mBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Set the Reset password button onClick action
        mBinding.viewButtonResetPassword.setOnClickListener(view -> initiatePasswordReset());

        // Set the Close button onClick action
        mBinding.viewButtonClose.setOnClickListener(view -> closeResetScreen());
    }

    /* Helper method to initiate the user's password reset implementation by connecting to the Firebase Auth server */
    private void initiatePasswordReset() {
        // Extract the user's input
        String email = Objects.requireNonNull(mBinding.viewEmailAddress.getText()).toString();

        // All fields valid
        if (!mBaseUtils.isEmpty(email) && mBaseUtils.isValidEmail(email)) {
            // Send the password reset request
            mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                // Hide the password reset view
                mBinding.viewGroupResetAction.setVisibility(View.INVISIBLE);
                // Show the post reset action view
                mBinding.viewGroupResetMessage.setVisibility(View.VISIBLE);

                // Show the post action message
                if (task.isSuccessful()) {
                    mBinding.viewResetInstructions.setText(getString(R.string.reset_instructions, email));
                    // Start a countdown timer for 60s after sending the reset email before the user can retry
                    new CountDownTimer(60000, 10000) {
                        @Override
                        public void onTick(long remainingTime) {
                            // Update the Retry remaining time
                            mBinding.viewButtonRetry.setText(
                                    getString(R.string.resend_timer,
                                            String.valueOf(remainingTime / 1000))
                            );

                            // While ticking, disable the onClickListener for the Retry button
                            mBinding.viewButtonRetry.setOnClickListener(null);
                        }

                        @Override
                        public void onFinish() {
                            // Once timer has been finished, restore the Retry button action
                            mBinding.viewButtonRetry.setText(getString(R.string.retry));
                            // Set the onClick action - Bring back the Reset password layout
                            mBinding.viewButtonRetry.setOnClickListener(view -> {
                                // Hide the password reset view
                                mBinding.viewGroupResetAction.setVisibility(View.VISIBLE);
                                // Show the post reset action view
                                mBinding.viewGroupResetMessage.setVisibility(View.INVISIBLE);
                            });
                        }
                    }.start();
                } else {
                    mBinding.viewResetInstructions.setText(getString(R.string.email_sent_failed, task.getException()));
                    // Exception encountered, restore the Retry button action
                    mBinding.viewButtonRetry.setText(getString(R.string.retry));
                    // Set the onClick action - Bring back the Reset password layout
                    mBinding.viewButtonRetry.setOnClickListener(view -> {
                        // Hide the password reset view
                        mBinding.viewGroupResetAction.setVisibility(View.VISIBLE);
                        // Show the post reset action view
                        mBinding.viewGroupResetMessage.setVisibility(View.INVISIBLE);
                    });
                }
            });
        }

        // IF email input empty or invalid format
        if (mBaseUtils.isEmpty(email) || !mBaseUtils.isValidEmail(email)) {
            mBinding.viewEmailAddress.setError(getString(R.string.invalid_email));
        }
    }

    private void closeResetScreen() {
        finish();
    }
}