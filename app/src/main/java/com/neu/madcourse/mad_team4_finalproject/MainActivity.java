package com.neu.madcourse.mad_team4_finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityMainBinding;
import com.neu.madcourse.mad_team4_finalproject.fragments.ExploreScreenFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        replaceFragment(new ExploreScreenFragment());

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.explore);

        mBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
            case R.id.explore:
                replaceFragment(new ExploreScreenFragment());
                break;
//
//            case R.id.community:
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
//                return true;
//
//            case R.id.saved_trails:
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
//                return true;
//
//            case R.id.profile:
////                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
//                return true;
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

//    ExploreScreenFragment exploreScreenFragment = new ExploreScreenFragment();


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.explore:
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, exploreScreenFragment).commit();
//                return true;
////
////            case R.id.community:
//////                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
////                return true;
////
////            case R.id.saved_trails:
//////                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
////                return true;
////
////            case R.id.profile:
//////                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
////                return true;
//        }
//        return false;
//    }

}