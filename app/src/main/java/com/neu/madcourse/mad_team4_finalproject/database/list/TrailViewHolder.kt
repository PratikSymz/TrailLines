package com.neu.madcourse.mad_team4_finalproject.database.list

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.database.SavedTrails
import com.neu.madcourse.mad_team4_finalproject.databinding.CustomFragmentListRowBinding
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils

class TrailViewHolder(
    @NonNull context: Context,
    @NonNull itemBinding: CustomFragmentListRowBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: CustomFragmentListRowBinding = itemBinding

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The current SavedTrails instance */
    private lateinit var mSavedTrails: SavedTrails

    /** Helper method to bind Conversation instance information to the Item Views
     * @param conversation The conversation instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindConversationData(savedTrails: SavedTrails) {
        // Set the Conversation instance
        mSavedTrails = savedTrails
        mBinding.holderTrailId.text = mSavedTrails.id.toString()
        mBinding.holderTrailImage.text = mSavedTrails.hikeImageUrl.toString()
        mBinding.holderTrailName.text = mSavedTrails.shortName
        mBinding.holderTrailLocation.text = mSavedTrails.location
        mBinding.holderTrailReviews.text = mSavedTrails.numOfReviews.toString()
        mBinding.holderTrailRatings.text = mSavedTrails.ratings.toString()
        mBinding.holderTrailDifficulty.text = mSavedTrails.difficultyLevel
        mBinding.holderTrailElevation.text = mSavedTrails.trailElevation.toString()
        mBinding.holderTrailLength.text = mSavedTrails.trailLength.toString()

    }
}
