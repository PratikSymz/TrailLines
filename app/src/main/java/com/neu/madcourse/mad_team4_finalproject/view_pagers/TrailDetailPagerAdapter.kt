package com.neu.madcourse.mad_team4_finalproject.view_pagers

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.neu.madcourse.mad_team4_finalproject.fragments.TrailOverviewFragment
import com.neu.madcourse.mad_team4_finalproject.fragments.TrailReviewsFragment
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park

/**
 * This class is created to show the contents of the tab when clicked
 * the recycler view will be shown using the adapter class
 * the tabs will be the direct source to show the recycler view contents
 */
class TrailDetailPagerAdapter(@NonNull fragmentActivity: FragmentActivity, @NonNull park: Park)
    : FragmentStateAdapter(fragmentActivity) {

    /* The Park reference */
    private val mPark = park

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrailOverviewFragment(mPark)
            1 -> TrailReviewsFragment(mPark)
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

}