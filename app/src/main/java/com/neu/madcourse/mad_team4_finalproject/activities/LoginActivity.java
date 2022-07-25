package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityLoginBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Network utils reference */
    private NetworkUtils mNetworkUtils;

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
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the Network utils reference
        mNetworkUtils = new NetworkUtils(mContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        /* FACEBOOK Login */
        // TODO: Initialize the Facebook login button

        // Set the login button onClick action
        applyListener(mBinding.viewSegmentLogin.viewButtonLogin, childView -> {
            if (mNetworkUtils.isConnected()) {
                initiateUserLogin();
            } else {
                // Launch the no active connection screen
                startActivity(new Intent(mContext, ConnectionActivity.class));
            }
        });

        // Set the sign up button onClick action
        applyListener(mBinding.viewSegmentLogin.viewLinkSignup, childView -> signUp());

        // Set the forgot password onClick action
        applyListener(mBinding.viewSegmentLogin.viewLinkForgotPassword, childView -> resetPassword());
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // Check if the user is currently logged in to the app
//        // This piece of code will not be executed if the user has already logged out
//        if (mFirebaseUser != null) {
//            startActivity(new Intent(mContext, ChatScreenActivity.class));
//            finish();
//        }
//    }

    /**
     * The onClick method to initiate the user login procedure by connecting to the Firebase Auth
     * server and verifying the user credentials
     */
    public void initiateUserLogin() {
        // Extract the input email
        String email = Objects.requireNonNull(mBinding.viewSegmentLogin.viewInputEmail.getText()).toString().trim();
        // Extract the input password
        String password = Objects.requireNonNull(mBinding.viewSegmentLogin.viewInputPassword.getText()).toString().trim();

        // Connect to the Firebase Auth server
        if (!mBaseUtils.isEmpty(email) && !mBaseUtils.isEmpty(password)) {
            // Show the progress bar while signing in
            mBinding.viewProgressBar.getRoot().setVisibility(View.VISIBLE);
            // Sign in the user with Firebase auth
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Hide the progress bar
                    mBinding.viewProgressBar.getRoot().setVisibility(View.INVISIBLE);
                    mBaseUtils.showToast("Login Successful!", Toast.LENGTH_SHORT);
                    // TODO: subject to change to main app screen (explore tab)
                    Intent intent = new Intent(mContext, ChatScreenActivity.class);
                    startActivity(intent);
                } else {
                    mBaseUtils.showToast(
                            String.format("Login Failed: %s", task.getException()),
                            Toast.LENGTH_SHORT);
                }
            });
        }

        if (email.equals("")) {
            mBinding.viewSegmentLogin.viewInputEmail.setError(getString(R.string.login_empty_email));
        }

        if (password.equals("")) {
            mBinding.viewSegmentLogin.viewInputPassword.setError(getString(R.string.login_empty_password));
        }
    }

    /**
     * The onClick method to take users to the sign up page
     */
    private void signUp() {
        startActivity(new Intent(mContext, SignUpActivity.class));
    }

    /* The onClick method to take users to the reset password page */
    private void resetPassword() {
        startActivity(new Intent(mContext, ResetPasswordActivity.class));
    }

    private static void applyListener(View child, View.OnClickListener listener) {
        if (child == null)
            return;

        if (child instanceof ViewGroup) {
            applyListener((ViewGroup) child, listener);
        } else {
            child.setOnClickListener(listener);
        }
    }

    private static void applyListener(ViewGroup parent, View.OnClickListener listener) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                applyListener((ViewGroup) child, listener);
            } else {
                applyListener(child, listener);
            }
        }
    }
}