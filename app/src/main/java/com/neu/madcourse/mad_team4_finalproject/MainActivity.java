package com.neu.madcourse.mad_team4_finalproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityMainBinding;
import com.neu.madcourse.mad_team4_finalproject.fragments.ExploreScreenFragment;
import com.neu.madcourse.mad_team4_finalproject.fragments.SavedTrailsFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        replaceFragment(new ExploreScreenFragment());

        mBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.explore:
                    replaceFragment(new ExploreScreenFragment());
                    break;

                case R.id.community:
                    replaceFragment(new CommunityLinkFragment());
                    return true;

                case R.id.saved_trails:
                    replaceFragment(new SavedTrailsFragment());
                    break;

                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    return true;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

}