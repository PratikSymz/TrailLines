package com.neu.madcourse.mad_team4_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;
    private TextInputEditText textEmailId, textPassword;
    private String email, password;

    /* The Activity layout view binding reference */
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Instantiate the activity layout view binding
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());
    }


    /**
     * @return none
     * @param v
     * This is the button login method
     */
    public void loginButton(View v){
       //email =  mbinding.id.getText().toString().trim()
        // password =  mbinding.id.getText().toString().trim()
        if (email.equals("")){
            //mbinding.id.setError(getString(R.string.empty_email)
        }
        else if (password.equals("")){
            //mbinding.id.setError(getString(R.string.empty_password)
        } else{

        }
    }
}