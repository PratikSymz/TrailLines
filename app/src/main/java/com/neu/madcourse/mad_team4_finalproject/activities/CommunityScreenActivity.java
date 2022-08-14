package com.neu.madcourse.mad_team4_finalproject.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.CommunitiesPagerAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityCommunityScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class CommunityScreenActivity extends AppCompatActivity {
    /* The Activity layout view binding reference */
    private ActivityCommunityScreenBinding mBinding;
    private CommunitiesPagerAdapter chatAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean pressAgain = false;
    private BaseUtils mBaseUtils;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate the activity layout view binding
        mBinding = ActivityCommunityScreenBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        // Set the layout root view
        setContentView(mBinding.getRoot());
        tabLayout = mBinding.viewChatTabs;
        viewPager = mBinding.viewChatPages;
        mBaseUtils = new BaseUtils(this);
        setViewPage();

        /* code for updating user online status */
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO)
                .child(firebaseUser.getUid())
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO);

        // if the user is online and then show online
        mDatabaseReference
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS)
                .setValue(true);

        // if the user is offline or disconnected from the server then show offline
        mDatabaseReference
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS)
                .onDisconnect()
                .setValue(false);
    }

    /**
     * This method is to add the tabs in the layout and set it to the chat adapter
     * When the tab is clicked or highlighted it will show the respective recycler view
     */
    private void setViewPage() {
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.chat_tab));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.request_tab));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.find_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // initialize chat adapter to call the adapter and create the fragment with the tabs
        chatAdapter = new CommunitiesPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mBinding.viewChatTabs);
        viewPager.setAdapter(chatAdapter);
        mBinding.viewChatTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    /**
     * on back press method to handle the logistics of the app structure.
     * We want to exit the chat but not the app altogether
     */
    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() > 0) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        } else {
            if (pressAgain) {
                // TODO this needs to be updated once the explore screen is done
                finish();
            } else {
                pressAgain = true;
                mBaseUtils.showToast(getString(R.string.press_again_exit), Toast.LENGTH_SHORT);
                Handler handler = new Handler();
                handler.postDelayed(() -> pressAgain = false, 2000);
            }
        }
    }
}