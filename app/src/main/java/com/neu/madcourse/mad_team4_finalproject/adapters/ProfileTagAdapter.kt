package com.neu.madcourse.mad_team4_finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemProfileTagBinding
import com.neu.madcourse.mad_team4_finalproject.models.ProfileTag
import com.neu.madcourse.mad_team4_finalproject.view_holders.ProfileTagViewHolder

class ProfileTagAdapter(
    @NonNull context: Context,
    @NonNull profileTags: List<ProfileTag>
) : RecyclerView.Adapter<ProfileTagViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The Profile Tag item list */
    private val mProfileTags: List<ProfileTag> = profileTags

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTagViewHolder {
        // Inflate the item view
        val mItemBinding: ItemProfileTagBinding = ItemProfileTagBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileTagViewHolder(parent.context, mItemBinding)
    }

    override fun onBindViewHolder(holder: ProfileTagViewHolder, position: Int) {
        // Get the current profile tag
        val profileTag: ProfileTag = mProfileTags[position]
        // Set the profile tag view field values
        holder.bindProfileTagData(profileTag)
    }

    override fun getItemCount(): Int {
        return mProfileTags.size
    }
}
