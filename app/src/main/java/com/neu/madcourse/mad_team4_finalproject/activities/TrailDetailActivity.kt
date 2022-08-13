package com.neu.madcourse.mad_team4_finalproject.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.adapters.HikeDetailActivityIconAdapter
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailsViewModel
import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityTrailDetailBinding
import com.neu.madcourse.mad_team4_finalproject.models.Explore
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
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

    /* The ViewModel database reference */
    private lateinit var mDBViewModel: SavedTrailsViewModel

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

    /* The Detail Fragment pager adapter */
    private lateinit var mPagerAdapter: TrailDetailPagerAdapter

    /* The Intent Park model reference */
    private lateinit var mExplore: Explore

    /* The Save status of the trail item */
    private var mSaved: Boolean = false

    /* Empty DB ID */
    private val EMPTY_KEY = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity context
        mContext = this

        // Instantiate the activity layout view binding
        mBinding = ActivityTrailDetailBinding.inflate(layoutInflater)
        // Set the layout root view
        setContentView(mBinding.root)

        // Instantiate the view model
        mDBViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[SavedTrailsViewModel::class.java]

        // Instantiate the Base utils reference
        mBaseUtils = BaseUtils(mContext)

        // Retrieve the Park information from the intent
        mExplore = intent.getSerializableExtra("explore_details") as Explore

        // Set the park information on the detail layout views
        setDetailInformation(mExplore);

        // Instantiate the view pager
        mPagerAdapter = TrailDetailPagerAdapter(mContext as TrailDetailActivity, mExplore.park)

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

        for (activity in mExplore.park.activityList) {
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

        // Create the saved trail reference
        // Extract the park and review stat instances
        val park = mExplore.park
        val reviewStat = mExplore.reviewStat

        // Create the saved trail instance
        val savedTrail = SavedTrail(
            EMPTY_KEY,
            park.parkID,
            park.imageList[0].url,
            park.shortName,
            park.fullName,
            park.designation,
            String.format(
                "%s, %s, %s %s",
                park.addressList[0].line1,
                park.addressList[0].city,
                park.addressList[0].stateCode,
                park.addressList[0].postalCode
            ),
            reviewStat.totalReviewers,
            reviewStat.totalStars,
            park.description,
            park.latitude,
            park.longitude,
            park.weatherInfo,
            "",
            String.format(
                Constants.MapKeys.FORMAT,
                park.latitude,
                park.longitude,
                Constants.MapKeys.API_KEY
            )
        )

        /* Set the back button onClick action */
        mBinding.viewCloseDetail.setOnClickListener { finish() }

        /* Check if the trail already exists in the database */
        mDBViewModel.isTrailSaved(park.parkID).observe(this) { saved ->
            mSaved = if (saved) {
                // Set the bookmark fill icon
                mBinding.viewSaveDetail.setImageDrawable(
                    AppCompatResources.getDrawable(mContext, R.drawable.ic_bookmark_fill)
                )
                // Set the saved status as true
                true
            } else {
                // Set the bookmark empty icon
                mBinding.viewSaveDetail.setImageDrawable(
                    AppCompatResources.getDrawable(mContext, R.drawable.ic_bookmark_empty)
                )
                // Set the saved status as false
                false
            }
        }

        /* Set the save button onClick action [DATABASE] */
        mBinding.viewSaveDetail.setOnClickListener {
            if (!mSaved) {
                saveTrailData(savedTrail)
            } else {
                deleteTrailData(savedTrail)
            }
        }
    }

    /* Helper method to set the park information on the detail views */
    private fun setDetailInformation(explore: Explore) {
        // Extract the park and review stat instances
        val park = explore.park
        val reviewStat = explore.reviewStat

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

        // Set the review stat information
        if (reviewStat.totalReviewers > 0) {
            val avgRating = round(reviewStat.totalStars / reviewStat.totalReviewers, 1)
            mBinding.viewDetailRating.text =
                String.format("%s (%s)", avgRating, reviewStat.totalReviewers)
        } else {
            mBinding.viewDetailRating.text = String.format("%s (%s)", 0.0, 0)
        }

        // TODO: REMOVE Difficulty level
    }

    /* Helper method to save trail data in the database */
    private fun saveTrailData(savedTrail: SavedTrail) {
        // Add data to the database
        mDBViewModel.saveTrail(savedTrail)
        // Show toast
        mBaseUtils.showToast("Added " + savedTrail.fullName + " to your saved list!", Toast.LENGTH_LONG)

        // Set the bookmark fill icon
        mBinding.viewSaveDetail.setImageDrawable(
            AppCompatResources.getDrawable(mContext, R.drawable.ic_bookmark_fill)
        )
        // Set the saved status as true
        mSaved = true
    }

    /* Helper method to delete trail data from the database */
    private fun deleteTrailData(savedTrail: SavedTrail) {
        // Build an alert dialog box
        AlertDialog.Builder(mContext)
            // Positive button
            .setPositiveButton("Yes") { _, _ ->
                // Delete the trail reference
                mDBViewModel.deleteTrailByID(savedTrail.trailID)
                // Show toast
                mBaseUtils.showToast(
                    "Successfully removed trail: ${savedTrail.shortName}!",
                    Toast.LENGTH_SHORT
                )

                // Set the bookmark empty icon
                mBinding.viewSaveDetail.setImageDrawable(
                    AppCompatResources.getDrawable(mContext, R.drawable.ic_bookmark_empty)
                )
                // Set the saved status as false
                mSaved = false
            }

            // Negative button
            .setNegativeButton("No") { _, _ -> }

            .setTitle("Delete trail: ${savedTrail.shortName}?")
            .setMessage("Are your sure you want to delete trail: ${savedTrail.shortName}?")
            .create()
            .show()
    }

    /* Reference: https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place */
    private fun round(value: Double, precision: Int): Double {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale).toDouble() / scale
    }
}