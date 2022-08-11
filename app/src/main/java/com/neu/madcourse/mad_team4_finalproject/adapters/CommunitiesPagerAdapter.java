package com.neu.madcourse.mad_team4_finalproject.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.neu.madcourse.mad_team4_finalproject.chatfragments.ChatHistoryFragment;
import com.neu.madcourse.mad_team4_finalproject.chatfragments.FindFriendsFragment;
import com.neu.madcourse.mad_team4_finalproject.chatfragments.ConnectionRequestsFragment;

/**
 * This class is created to show the contents of the tab when clicked
 * the recycler view will be shown using the adapter class
 * the tabs will be the direct source to show the recycler view contents
 */
public class CommunitiesPagerAdapter extends FragmentPagerAdapter {
    private TabLayout tabLayout;

    public CommunitiesPagerAdapter(@NonNull FragmentManager fm, int behavior, TabLayout tabLayout) {
        super(fm, behavior);
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatHistoryFragment();
            case 1:
                return new ConnectionRequestsFragment();
            case 2:
                return new FindFriendsFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(int position) {
        // Set the titles of the tabs for the TabLayout
        switch (position) {
            case 0:
                return "Chats";
            case 1:
                return "Requests";
            case 2:
                return "Find";
            default:
                return "";
        }
    }
}
