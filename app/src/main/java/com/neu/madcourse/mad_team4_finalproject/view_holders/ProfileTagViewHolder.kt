package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemProfileTagBinding
import com.neu.madcourse.mad_team4_finalproject.models.ProfileTag
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils

class ProfileTagViewHolder (
    @NonNull context: Context,
    @NonNull itemBinding: ItemProfileTagBinding,
) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ItemProfileTagBinding = itemBinding

    /* The current Profile Tag instance */
    private lateinit var mProfileTag: ProfileTag

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /** Helper method to bind Profile Tag instance information to the Item Views
     * @param profileTag The profile tag instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindProfileTagData(profileTag: ProfileTag) {
        // Set the Profile instance
        mProfileTag = profileTag

        // Set the profile item icon
        mBinding.viewSectionIcon.setImageResource(
            mBaseUtils.extractDrawableID(profileTag.profileIconIdentifier)
        )

        // Set the profile item label
        mBinding.viewSectionLabel.text = profileTag.profileLabel

        // Set the onClick action
        mBinding.root.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val intent = Intent(mContext, Class.forName(mProfileTag.intent))
        mContext.startActivity(intent)
    }
}