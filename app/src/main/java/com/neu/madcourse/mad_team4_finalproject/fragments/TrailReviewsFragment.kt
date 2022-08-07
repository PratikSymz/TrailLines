package com.neu.madcourse.mad_team4_finalproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.neu.madcourse.mad_team4_finalproject.activities.AddReviewActivity
import com.neu.madcourse.mad_team4_finalproject.adapters.GenericAdapter
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentTrailReviewsBinding
import com.neu.madcourse.mad_team4_finalproject.models.Review
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants


class TrailReviewsFragment : Fragment() {
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

    /* The Firebase Database reference -> "users"/userID/"reviews" */
    private lateinit var mReviewsDatabaseRef: DatabaseReference

    /* The Firebase Auth service reference */
    private lateinit var mFirebaseAuth: FirebaseAuth

    /* The Firebase User reference */
    private lateinit var mFirebaseUser: FirebaseUser

    /* The Recycler view adapter reference */
    private lateinit var mAdapter: GenericAdapter<Review>

    /* The Trail ID reference */
    private lateinit var mTrailID: String

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

        // Instantiate the firebase database reference -> "reviews"/trailID
        mReviewsDatabaseRef = FirebaseDatabase.getInstance().reference
            .child(Constants.ReviewKeys.KEY_TLO)
            .child(mTrailID)

        // Add the onClick action for the "Add review button"
        mBaseUtils.applyListener(mBinding.viewAddReview.root) {
            val intent: Intent = Intent(mContext, AddReviewActivity::class.java)
            startActivity(intent)
        }
    }
}