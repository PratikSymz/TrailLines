package com.neu.madcourse.mad_team4_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;

import java.util.Objects;

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
     * This is the button login method.
     * When the login button on the activity is clicked this method is called
     */
    public void loginButton(View v){
       email =  Objects.requireNonNull(mBinding.viewInputEmail.getText()).toString().trim();
       password =  Objects.requireNonNull(mBinding.viewInputPassword.getText()).toString().trim();
        if (email.equals("")){
            mBinding.viewInputEmail.setError(getString(R.string.empty_email));
        }
        else if (password.equals("")){
            mBinding.viewInputPassword.setError(getString(R.string.empty_password));
        } else{
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(mContext, "Login Successful!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContext, "Login Failed : " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}