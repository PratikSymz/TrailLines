package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.activities.SavedTrailDetailActivity
import com.neu.madcourse.mad_team4_finalproject.database.models.SavedTrail
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemSelectedImageBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.VerticalTrailViewsBinding

class GenericViewHolder<T>(
    @NonNull context: Context,
    @NonNull itemBinding: ViewBinding
) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ViewBinding = itemBinding

    /* The current data model instance */
    private var mDataModel: T? = null

    /** Helper method to bind data model instance information to the Item Views
     * @param dataModel The data model instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindData(dataModel: T) {
        // Set the data model instance
        mDataModel = dataModel

        /* Check the instance of the data model and bind data to the views accordingly */

        // IF data model class is URI -> review "selected images" list
        if (Uri::class.java.isAssignableFrom(mDataModel!!::class.java)) {
            val binding: ItemSelectedImageBinding = mBinding as ItemSelectedImageBinding
            binding.viewSelectedImage.setImageURI(mDataModel as Uri)
        }
        // IF data model class is SavedTrail -> saved trails list in DB
        else if (SavedTrail::class.java.isAssignableFrom(mDataModel!!::class.java)) {
            val binding: VerticalTrailViewsBinding = mBinding as VerticalTrailViewsBinding

            // The data model
            val savedTrail = mDataModel as SavedTrail

            // Set the view information
            binding.viewTrailName.text = savedTrail.fullName
            binding.viewTrailInfo.text = savedTrail.address

            if (savedTrail.numReviewers > 0) {
                val avgRating =
                    round(savedTrail.totalRatings / savedTrail.numReviewers, 1)
                binding.viewParkRating.text = avgRating.toString()
            } else {
                binding.viewParkRating.text = "0.0"
            }

            Glide.with(mContext)
                .load(savedTrail.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(350, 200)
                .placeholder(R.drawable.ic_downloading)
                .error(R.drawable.ic_error)
                .into(binding.viewTrailImage)

            /* Set the onClick action for every item view on the parks list */
            binding.root.setOnClickListener {
                val savedDetailIntent = Intent(mContext, SavedTrailDetailActivity::class.java)
                savedDetailIntent.putExtra("saved_trail_details", savedTrail)
                mContext.startActivity(savedDetailIntent)
            }
        }
    }

    /* Reference: https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place */
    private fun round(value: Double, precision: Int): Double {
        val scale = Math.pow(10.0, precision.toDouble()).toInt()
        return Math.round(value * scale).toDouble() / scale
    }

    override fun onClick(view: View?) {
        TODO()
    }
}