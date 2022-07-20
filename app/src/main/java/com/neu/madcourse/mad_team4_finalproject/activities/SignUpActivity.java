package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySignUpBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        if (name.equals("")) {
            mBinding.viewInputName.setError(getString(R.string.invalid_name));
        } else if (email.equals("")) {
            mBinding.viewInputEmail.setError(getString(R.string.invalid_email));
        } else if (password.equals("")) {
            mBinding.viewInputPassword.setError(getString(R.string.invalid_password));
        } else if (confirmPassword.equals("")) {
            mBinding.viewInputConfirmPassword.setError(getString(R.string.invalid_password));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.viewInputEmail.setError(getString(R.string.invalid_email));
        } else if (!password.equals(confirmPassword)) {
            mBinding.viewInputConfirmPassword.setError(getString(R.string.password_not_match));
        } else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mBaseUtils.showToast(getString(R.string.sign_up_success), Toast.LENGTH_SHORT);
                    startActivity(new Intent(mContext, LoginActivity.class));
                } else {
                    mBaseUtils.showToast(
                            String.format(getString(R.string.signup_fail), task.getException()),
                            Toast.LENGTH_SHORT
                    );
                }
            });
        }
    }
}