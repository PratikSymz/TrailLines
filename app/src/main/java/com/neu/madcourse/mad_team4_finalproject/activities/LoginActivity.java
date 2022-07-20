package com.neu.madcourse.mad_team4_finalproject.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

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
    }


    /**
     * The onClick method to initiate the user login procedure by connecting to the Firebase Auth
     * server and verifying the user credentials
     *
     * @param view The login button view
     */
    public void initiateUserLogin(View view) {
        // Extract the input email
        String email = Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim();
        // Extract the input password
        String password = Objects.requireNonNull(mBinding.viewInputPassword.getText()).toString().trim();

        // Connect to the Firebase Auth server
        if (!mBaseUtils.isEmpty(email) && !mBaseUtils.isEmpty(password)) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mBaseUtils.showToast("Login Successful!", Toast.LENGTH_SHORT);
                } else {
                    mBaseUtils.showToast(
                            String.format("Login Failed: %s", task.getException()),
                            Toast.LENGTH_SHORT
                    );
                }
            });
        }

        if (email.equals("")) {
            mBinding.viewInputEmail.setError(getString(R.string.login_empty_email));
        }

        if (password.equals("")) {
            mBinding.viewInputPassword.setError(getString(R.string.login_empty_password));
        }
    }

    /**
     * The onClick method to take users to the sign up page
     *
     * @param view The login button view
     */

    public void signUp(View view){
        startActivity(new Intent(mContext, SignUpActivity.class));
    }
}