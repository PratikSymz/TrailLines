package com.neu.madcourse.mad_team4_finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.neu.madcourse.mad_team4_finalproject.Chat.ChatAdapter;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityChatScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityLoginBinding;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;

public class ChatScreenActivity extends AppCompatActivity {
    /* The Activity layout view binding reference */
    private ActivityChatScreenBinding mBinding;
    private ChatAdapter chatAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean pressAgain = false;
    private BaseUtils mBaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate the activity layout view binding
        mBinding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());
        tabLayout = mBinding.mainTab;
        viewPager = mBinding.mainViewPage;
        mBaseUtils = new BaseUtils(this);
        setViewPage();
    }

    /**
     * This method is to add the tabs in the layout and set it to the chat adapter
     * When the tab is clicked or highlighted it will show the respective recycler view
     */
    private void setViewPage(){
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.chat_tab));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.request_tab));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.find_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // initialize chat adapter to call the adapter and create the fragment with the tabs
        chatAdapter = new ChatAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mBinding.mainTab);
        viewPager.setAdapter(chatAdapter);
        mBinding.mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        if (tabLayout.getSelectedTabPosition() > 0){
            tabLayout.selectTab(tabLayout.getTabAt(0));
        } else {
            if (pressAgain){
                // TODO this needs to be updated once the explore screen is done
                finish();
            } else {
                pressAgain = true;
                mBaseUtils.showToast(getString(R.string.press_again_exit), Toast.LENGTH_SHORT);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pressAgain = false;
                    }
                }, 2000);
            }
        }
    }
}