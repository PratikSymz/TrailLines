package com.neu.madcourse.mad_team4_finalproject.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.adapters.HikeDetailActivityIconAdapter
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityTrailDetailBinding
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park
import com.neu.madcourse.mad_team4_finalproject.utils.Constants
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

    /* The Intent Park model reference */
    private lateinit var mPark: Park

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity context
        mContext = this

        // Instantiate the activity layout view binding
        mBinding = ActivityTrailDetailBinding.inflate(layoutInflater)
        // Set the layout root view
        setContentView(mBinding.root)

        // Retrieve the Park information from the intent
        mPark = intent.getSerializableExtra("park_details") as Park

        // Set the park information on the detail layout views
        setDetailInformation(mPark);

        // Instantiate the view pager
        mPagerAdapter = TrailDetailPagerAdapter(mContext as TrailDetailActivity, mPark)

        // Set the view pager adapter
        mBinding.segmentOverviewReviews.viewPagerOverviewReviews.adapter = mPagerAdapter

        // Set the view pager tab titles
        TabLayoutMediator(
            mBinding.segmentOverviewReviews.viewTabMoreDetail,
            mBinding.segmentOverviewReviews.viewPagerOverviewReviews
        ) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerViewActivities.layoutManager = layoutManager
        mBinding.recyclerViewActivities.setHasFixedSize(true)

        val activityIconIds = ArrayList<Int>()
        val activityIconAdapter = HikeDetailActivityIconAdapter(activityIconIds, mContext)
        mBinding.recyclerViewActivities.adapter = activityIconAdapter

        for (activity in mPark.activityList) {
            when (activity.recordId) {
                Constants.ThingsToDoCodes.CAMPING_CODE -> activityIconIds.add(R.drawable.ic_camping)
                Constants.ThingsToDoCodes.CANYONEERING_CODE -> activityIconIds.add(R.drawable.ic_canyon)
                Constants.ThingsToDoCodes.CAVING_CODE -> activityIconIds.add(R.drawable.ic_cave)
                Constants.ThingsToDoCodes.CLIMBING_CODE -> activityIconIds.add(R.drawable.ic_climbing)
                Constants.ThingsToDoCodes.HIKING_CODE -> activityIconIds.add(R.drawable.ic_hiking)
                Constants.ThingsToDoCodes.SCUBA_DIVING_CODE -> activityIconIds.add(R.drawable.ic_scuba)
                Constants.ThingsToDoCodes.SNORKELING_CODE -> activityIconIds.add(R.drawable.ic_snorkelling)
                Constants.ThingsToDoCodes.BIKING_CODE -> activityIconIds.add(R.drawable.ic_biking)
                Constants.ThingsToDoCodes.BOATING_CODE -> activityIconIds.add(R.drawable.ic_boating)
                Constants.ThingsToDoCodes.PADDLING_CODE -> activityIconIds.add(R.drawable.ic_kayaking)
                Constants.ThingsToDoCodes.FISHING_CODE -> activityIconIds.add(R.drawable.ic_fishing)
                Constants.ThingsToDoCodes.SKIING_CODE -> activityIconIds.add(R.drawable.ic_skiing)
                Constants.ThingsToDoCodes.SURFING_CODE -> activityIconIds.add(R.drawable.ic_surfing)
                Constants.ThingsToDoCodes.WATER_SKIING_CODE -> activityIconIds.add(R.drawable.ic_swimming)
            }
        }

        activityIconAdapter.updateDataList(activityIconIds)
        /* Set the back button onClick action */
        mBinding.viewCloseDetail.setOnClickListener { finish() }

        /* TODO: Set the save button onClick action [DATABASE] */
    }

    /* Helper method to set the park information on the detail views */
    private fun setDetailInformation(park: Park) {
        // Load the park image
        Glide.with(mContext)
            .load(park.imageList[0].url)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_downloading)
            .error(R.drawable.ic_error)
            .into(mBinding.viewDetailImage)

        // Set the park name (short)
        mBinding.viewDetailFullName.text = park.fullName
        // Set the park name (long)
        mBinding.viewDetailDesignation.text = park.designation

        // Set the park location
        val longAddress: String = String.format(
            "%s, %s, %s %s",
            park.addressList[0].line1,
            park.addressList[0].city,
            park.addressList[0].stateCode,
            park.addressList[0].postalCode
        )

        mBinding.viewDetailLocation.text = longAddress

        // TODO: The Difficulty level and the Review information
    }
}