package com.neu.madcourse.mad_team4_finalproject.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityTrailDetailBinding
import com.neu.madcourse.mad_team4_finalproject.view_pagers.TrailDetailPagerAdapter

class TrailDetailActivity : AppCompatActivity() {
    companion object {
        val TAB_TITLES: List<String> = listOf("Overview", "Reviews")
    }

    /* The Activity Log Tag */
    private val LOG_TAG: String = TrailDetailActivity::class.java.simpleName

    /* The Activity context */
    private lateinit var mContext: Context

    /* The Activity layout view binding reference */
    private lateinit var mBinding: ActivityTrailDetailBinding

    /* The Detail Fragment pager adapter */
    private lateinit var mPagerAdapter: TrailDetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity context
        mContext = this

        // Instantiate the activity layout view binding
        mBinding = ActivityTrailDetailBinding.inflate(layoutInflater)
        // Set the layout root view
        setContentView(mBinding.root)

        // Instantiate the view pager
        mPagerAdapter = TrailDetailPagerAdapter(mContext as TrailDetailActivity)

        // Set the view pager adapter
        mBinding.segmentOverviewReviews.viewPagerOverviewReviews.adapter = mPagerAdapter

        // Set the view pager tab titles
        TabLayoutMediator(mBinding.segmentOverviewReviews.viewTabMoreDetail, mBinding.segmentOverviewReviews.viewPagerOverviewReviews) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }
}