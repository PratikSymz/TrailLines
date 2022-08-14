package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.adapters.GenericAdapter
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemReviewBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemSelectedImageBinding
import com.neu.madcourse.mad_team4_finalproject.models.ReviewMessage
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class GenericViewHolder<T>(
    @NonNull context: Context,
    @NonNull itemBinding: ViewBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    /* The Log tag */
    private val LOG_TAG = GenericViewHolder::class.java.simpleName

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ViewBinding = itemBinding

    /* The current data model instance */
    private var mDataModel: T? = null

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Network utils reference */
    private val mNetworkUtils: NetworkUtils = NetworkUtils(mContext)

    /* The Firebase storage reference */
    private val mFirebaseStorage: StorageReference = FirebaseStorage.getInstance().reference

    /* The Review images list reference */
    private val mReviewImageList = HashSet<Uri>()

    /* The Review images recycler view reference */
    private val mAdapter: GenericAdapter<Uri> = GenericAdapter(
        mContext, mReviewImageList.toList(),
        ItemSelectedImageBinding.inflate(LayoutInflater.from(mContext))
    )

    /** Helper method to bind data model instance information to the Item Views
     * @param dataModel The data model instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindData(dataModel: T) {
        // Set the data model instance
        mDataModel = dataModel

        // IF data model class is URI -> review "selected images" list
        if (Uri::class.java.isAssignableFrom(mDataModel!!::class.java)) {
            // Instantiate the binding
            val binding: ItemSelectedImageBinding = mBinding as ItemSelectedImageBinding
            // Set the view information
            Glide.with(mContext)
                .load(mDataModel as Uri)
                .placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(binding.viewSelectedImage)
        }

        // IF data model class is REVIEW -> list of reviews
        if (ReviewMessage::class.java.isAssignableFrom(mDataModel!!::class.java)) {
            // Instantiate the binding
            val binding: ItemReviewBinding = mBinding as ItemReviewBinding
            // Set the view information
            val review: ReviewMessage = mDataModel as ReviewMessage

            // Load the user name
            mBinding.viewReviewUserName.text = review.user!!.name

            // Load the user's profile picture into the item view
            Glide.with(mContext)
                .load(review.user!!.profilePictureFileName)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(binding.viewReviewUserImage)

            // Load the user's rating
            mBinding.viewReviewUserRating.text = review.rating.toString()

            // Load the rating bar value
            mBinding.viewReviewUserRatingBar.rating = review.rating!!

            // Load the review timestamp
            mBinding.viewReviewUserTimestamp.text = formatTimestamp(review.timestamp!!)

            // Load the review title
            mBinding.viewReviewTitle.text = String.format("\"%s\"", review.reviewTitle)

            // Load the review message
            if (!mBaseUtils.isEmpty(review.reviewMessage)) {
                mBinding.viewReviewBody.text = review.reviewMessage
                mBinding.viewReviewBody.visibility = View.VISIBLE
            } else {
                mBinding.viewReviewBody.visibility = View.GONE
            }

            // Set the layout manager
            val layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = RecyclerView.HORIZONTAL
            mBinding.recyclerViewReviewImages.layoutManager = layoutManager

            // Set the adapter on the recycler view
            mBinding.recyclerViewReviewImages.adapter = mAdapter

            // Load the review images
            if (review.reviewImages != null && review.reviewImages!!.isNotEmpty()) {
                mBinding.recyclerViewReviewImages.visibility = View.VISIBLE
                // Convert the list of "URI Paths" to URIs
                review.reviewImages!!.forEach {
                    mReviewImageList.add(Uri.parse(it))
                }

                // Update the adapter
                mAdapter.updateDataList(mReviewImageList.toList())
            } else {
                mBinding.recyclerViewReviewImages.visibility = View.GONE
            }

            // Load the recommendation status
            // Create a Spannable string
            if (review.recommendationStatus != null) {
                // Show the recommendation label
                mBinding.viewReviewRecommendationLabel.visibility = View.VISIBLE
                // Extract the status
                val status = review.recommendationStatus as Boolean

                // Recommended
                if (status) {
                    val rSpannable = SpannableString(
                        String.format(
                            "\"%s recommends this place\"",
                            review.user!!.name
                        )
                    )

                    rSpannable.setSpan(
                        ForegroundColorSpan(mContext.getColor(R.color.blue)),
                        review.user!!.name.length + 1,
                        review.user!!.name.length + 2 + "recommends".length,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )

                    mBinding.viewReviewRecommendationLabel.text = rSpannable
                } else {    // Not recommended
                    val nrSpannable =
                        SpannableString(
                            String.format(
                                "\"%s does not recommends this place\"",
                                review.user!!.name
                            )
                        )

                    nrSpannable.setSpan(
                        ForegroundColorSpan(mContext.getColor(R.color.red)),
                        review.user!!.name.length + 1,
                        review.user!!.name.length + 2 + "does not recommends".length,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                    )

                    mBinding.viewReviewRecommendationLabel.text = nrSpannable
                }
            }
        }
    }

    /* Helper method to format timestamp in "ago" format */
    @SuppressLint("SimpleDateFormat")
    private fun formatTimestamp(timestamp: Long): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val now = System.currentTimeMillis()
            val ago =
                DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.MINUTE_IN_MILLIS)
            return ago.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /* Helper method to convert filename into "images" database reference */
    private fun formatFileName(fileName: String): String {
        return String.format(
            "%s/%s", Constants.FirebaseStorageKeys.KEY_IMAGES, fileName
        )
    }
}