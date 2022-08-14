package com.neu.madcourse.mad_team4_finalproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.neu.madcourse.mad_team4_finalproject.activities.AddReviewActivity
import com.neu.madcourse.mad_team4_finalproject.adapters.GenericAdapter
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentTrailReviewsBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemReviewBinding
import com.neu.madcourse.mad_team4_finalproject.models.ReviewMessage
import com.neu.madcourse.mad_team4_finalproject.models.ReviewStat
import com.neu.madcourse.mad_team4_finalproject.models.User
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants


class TrailReviewsFragment(park: Park) : Fragment() {
    /* The Fragment Log Tag */
    private val LOG_TAG: String = TrailReviewsFragment::class.java.simpleName

    /* The Activity context */
    private lateinit var mContext: Context

    /* The Fragment layout view binding reference */
    private lateinit var mBinding: FragmentTrailReviewsBinding

    /* The Base utils reference */
    private lateinit var mBaseUtils: BaseUtils

    /* The Network utils reference */
    // private val mNetworkUtils: NetworkUtils? = null

    /* The Firebase Database reference -> "users" */
    private lateinit var mUsersDatabaseRef: DatabaseReference

    /* The Firebase Database reference -> "reviews" */
    private lateinit var mReviewsDatabaseRef: DatabaseReference

    /* The Firebase Auth service reference */
    private lateinit var mFirebaseAuth: FirebaseAuth

    /* The Firebase User reference */
    private lateinit var mFirebaseUser: FirebaseUser

    /* The Recycler view adapter reference */
    private lateinit var mAdapter: GenericAdapter<ReviewMessage>

    /* The Review instance set reference */
    private var mReviewsList = HashSet<ReviewMessage>()

    /* The Park model reference */
    private val mPark = park

    /* The Park ID reference */
    private var mParkID: String = mPark.parkID

    /* The Trail ID reference */
    private var mTrailID: String = "102"

    /* The selected images list reference */
    // private val mFindFriendsList: List<FindFriend>? = null

    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?): View {

        // Set the activity context
        mContext = requireActivity()

        // Inflate the layout for this fragment
        mBinding = FragmentTrailReviewsBinding.inflate(inflater, container, false)
        // Return the root view
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instantiate the Base utils reference
        mBaseUtils = BaseUtils(mContext)

        // Instantiate the Network utils reference
        // mNetworkUtils = NetworkUtils(mContext)

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance()

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.currentUser!!

        // Instantiate the firebase database reference -> "user"
        mUsersDatabaseRef = FirebaseDatabase.getInstance().reference
            .child(Constants.UserKeys.KEY_TLO)

        // Instantiate the firebase database reference -> "reviews"/parkID
        mReviewsDatabaseRef = FirebaseDatabase.getInstance().reference
            .child(Constants.ReviewKeys.KEY_TLO)
            .child(mTrailID)    // TODO: Check

        /* Set the selected images recycler view parameters */
        // Set the layout manager
        val layoutManager = LinearLayoutManager(mContext)
        mBinding.recyclerViewReviews.layoutManager = layoutManager

        // Instantiate the adapter class
        mAdapter = GenericAdapter(
            mContext, mReviewsList.toList(),
            ItemReviewBinding.inflate(layoutInflater)
        )

        // Set the adapter on the recycler view
        mBinding.recyclerViewReviews.adapter = mAdapter

        // Add the onClick action for the "Add review button"
        mBaseUtils.applyListener(mBinding.viewAddReview.root) {
            val intent: Intent = Intent(mContext, AddReviewActivity::class.java)
            intent.putExtra("park_details", mPark)
            startActivity(intent)
        }

        /* Load the stats and reviews corresponding to the given trail ID */
        loadStatsAndReviews()
    }

    override fun onResume() {
        super.onResume()

        /* Load the stats and reviews corresponding to the given trail ID */
        loadStatsAndReviews()
    }

    /* Helper method to load the stats and reviews corresponding to the given trail ID */
    private fun loadStatsAndReviews() {
        /* Load the "reviews" stats reference for the given Trail ID */
        mReviewsDatabaseRef.child(mTrailID).child(Constants.ReviewKeys.ReviewStats.KEY_TLO)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(statsSnapshot: DataSnapshot) {
                    // Extract the review stats
                    val reviewStats: ReviewStat? = statsSnapshot.getValue(ReviewStat::class.java)
                    // Show the review stats on the views
                    showReviewStats(reviewStats)
                }

                override fun onCancelled(error: DatabaseError) {
                    mBaseUtils.showToast("Failed loading review stats!", Toast.LENGTH_SHORT)
                }
            })

        /* Load the "reviews" messages reference for the given Trail ID */
        mReviewsDatabaseRef.child(mTrailID).child(Constants.ReviewKeys.ReviewMessages.KEY_TLO)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(reviewsSnapshot: DataSnapshot) {
                    // Clear the adapter
                    mAdapter.updateDataList(mutableListOf())

                    // Clear the review set
                    mReviewsList.clear()

                    // Iterate through all the user IDs and extract their review and personal information
                    reviewsSnapshot.children.forEach {
                        // Extract the userID
                        val userID: String = it.key!!
                        // Extract the review information
                        val review: ReviewMessage = it.getValue(ReviewMessage::class.java)!!

                        // Extract the user personal information from the firebase database
                        mUsersDatabaseRef.child(userID)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(userSnapshot: DataSnapshot) {
                                    // Extract the personal information from the snapshot
                                    val user: User = userSnapshot
                                        .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                                        .getValue(User::class.java)!!

                                    // Add the user information to the Review model class
                                    review.user = user

                                    // Add the review item to the reviews list
                                    mReviewsList.add(review)

                                    // Update the adapter item list
                                    mAdapter.updateDataList(mReviewsList.toList())
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // TODO: Show error appropriately
                                    mBaseUtils.showToast(
                                        "Unable to load reviews",
                                        Toast.LENGTH_SHORT
                                    )
                                }

                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mBaseUtils.showToast("Unable to load reviews", Toast.LENGTH_SHORT)
                }
            })
    }

    /* Helper method to show review stats on the layout views */
    private fun showReviewStats(reviewStats: ReviewStat?) {
        if (reviewStats == null) {
            // Set total rating as 0
            mBinding.viewReviewCard.viewReviewsAverage.text = "0.0"

            // Set average stars as 0
            mBinding.viewReviewsRating.rating = 0F

            // Set the "Excellent" progress and number of reviews as 0
            mBinding.viewProgressExcellent.progress = 0
            mBinding.viewNumRatingExcellent.text = "0"

            // Set the "Great" progress and number of reviews as 0
            mBinding.viewProgressGreat.progress = 0
            mBinding.viewNumRatingGreat.text = "0"

            // Set the "Average" progress and number of reviews as 0
            mBinding.viewProgressAverage.progress = 0
            mBinding.viewNumRatingAverage.text = "0"

            // Set the "Poor" progress and number of reviews as 0
            mBinding.viewProgressPoor.progress = 0
            mBinding.viewNumRatingPoor.text = "0"

            // Set the "Terrible" progress and number of reviews as 0
            mBinding.viewProgressTerrible.progress = 0
            mBinding.viewNumRatingTerrible.text = "0"
        } else {
            // Calculate the average star rating [out of 5]
            val averageRating: Double = (reviewStats.totalStars!! / reviewStats.totalReviewers!!)

            // Set total rating
            mBinding.viewReviewCard.viewReviewsAverage.text = round(averageRating, 1).toString()

            // Set average stars
            mBinding.viewReviewsRating.rating = round(averageRating, 1).toFloat()

            // Set the "Excellent" progress and number of reviews
            mBinding.viewProgressExcellent.progress =
                ((reviewStats.numExcellent!! * 100) / reviewStats.totalReviewers!!).toInt()
            mBinding.viewNumRatingExcellent.text = reviewStats.numExcellent!!.toString()

            // Set the "Great" progress and number of reviews
            mBinding.viewProgressGreat.progress =
                ((reviewStats.numGreat!! * 100) / reviewStats.totalReviewers!!).toInt()
            mBinding.viewNumRatingGreat.text = reviewStats.numGreat!!.toString()

            // Set the "Average" progress and number of reviews
            mBinding.viewProgressAverage.progress =
                ((reviewStats.numAverage!! * 100) / reviewStats.totalReviewers!!).toInt()
            mBinding.viewNumRatingAverage.text = reviewStats.numAverage!!.toString()

            // Set the "Poor" progress and number of reviews
            mBinding.viewProgressPoor.progress =
                ((reviewStats.numPoor!! * 100) / reviewStats.totalReviewers!!).toInt()
            mBinding.viewNumRatingPoor.text = reviewStats.numPoor!!.toString()

            // Set the "Terrible" progress and number of reviews
            mBinding.viewProgressTerrible.progress =
                ((reviewStats.numTerrible!! * 100) / reviewStats.totalReviewers!!).toInt()
            mBinding.viewNumRatingTerrible.text = reviewStats.numTerrible!!.toString()
        }
    }

    /* Reference: https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place */
    private fun round(value: Double, precision: Int): Double {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale).toDouble() / scale
    }
}