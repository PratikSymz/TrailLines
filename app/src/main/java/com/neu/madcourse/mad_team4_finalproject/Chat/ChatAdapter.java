package com.neu.madcourse.mad_team4_finalproject.Chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.neu.madcourse.mad_team4_finalproject.R;

/**
 * This class is created to show the contents of the tab when clicked
 * the recycler view will be shown using the adapter class
 * the tabs will be the direct source to show the recycler view contents
 */
public class ChatAdapter extends FragmentPagerAdapter {
    private TabLayout tabLayout;

    public ChatAdapter(@NonNull FragmentManager fm, int behavior, TabLayout tabLayout) {
        super(fm, behavior);
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new RequestsFragment();
            case 2:
                return new FindFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabLayout.getTabCount();
    }

}
