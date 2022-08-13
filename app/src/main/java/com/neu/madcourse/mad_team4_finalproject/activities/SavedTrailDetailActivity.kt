package com.neu.madcourse.mad_team4_finalproject.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrailsViewModel
import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivitySavedTrailDetailBinding
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

class SavedTrailDetailActivity : AppCompatActivity() {
    /* The Activity Log Tag */
    private val LOG_TAG: String = SavedTrailDetailActivity::class.java.simpleName

    /* The Activity context */
    private lateinit var mContext: Context

    /* The Activity layout view binding reference */
    private lateinit var mBinding: ActivitySavedTrailDetailBinding

    /* The ViewModel database reference */
    private lateinit var mDBViewModel: SavedTrailsViewModel

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

    /* The Saved trail model reference */
    private lateinit var mSavedTrail: SavedTrail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the activity context
        mContext = this

        // Instantiate the activity layout view binding
        mBinding = ActivitySavedTrailDetailBinding.inflate(layoutInflater)
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
        mSavedTrail = intent.getSerializableExtra("saved_trail_details") as SavedTrail

        // Set the park information on the detail layout views
        setDetailInformation(mSavedTrail);

        /* Set the back button onClick action */
        mBinding.viewCloseDetail.setOnClickListener { finish() }

        // Set the bookmark fill icon
        mBinding.viewUnsaveDetail.setImageDrawable(
            AppCompatResources.getDrawable(mContext, R.drawable.ic_bookmark_fill)
        )

        /* Set the save button onClick action [DATABASE] */
        mBinding.viewUnsaveDetail.setOnClickListener {
            deleteTrailData(mSavedTrail)
        }
    }

    /* Helper method to set the saved trail information on the detail views */
    private fun setDetailInformation(savedTrail: SavedTrail) {
        // Load the park image
        Glide.with(mContext)
            .load(savedTrail.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_downloading)
            .error(R.drawable.ic_error)
            .into(mBinding.viewSavedDetailImage)

        // Set the park name
        mBinding.viewSavedDetailFullName.text = savedTrail.fullName

        // Set the park designation
        mBinding.viewSavedDetailDesignation.text = savedTrail.designation

        // Set the park location
        mBinding.viewSavedDetailLocation.text = savedTrail.address

        // Set the review stat information
        if (savedTrail.numReviewers > 0) {
            val avgRating = round(savedTrail.totalRatings / savedTrail.numReviewers, 1)
            mBinding.viewSavedDetailRating.text =
                String.format("%s (%s)", avgRating, savedTrail.numReviewers)
        } else {
            mBinding.viewSavedDetailRating.text = String.format("%s (%s)", 0.0, 0)
        }

        // Set the description
        mBinding.segmentSavedOverview.viewOverviewDescription.text = savedTrail.description

        val mapUrl = String.format(
            Constants.MapKeys.FORMAT,
            savedTrail.latitude,
            savedTrail.longitude,
            Constants.MapKeys.API_KEY
        )
        Log.d(LOG_TAG, mapUrl);
        Glide.with(mContext)
            .load(mapUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.ic_downloading)
            .error(R.drawable.ic_error)
            .into(mBinding.segmentSavedOverview.viewOverviewMap)
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

                finish()
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