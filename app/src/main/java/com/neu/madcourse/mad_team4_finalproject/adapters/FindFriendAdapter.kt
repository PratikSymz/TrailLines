package com.neu.madcourse.mad_team4_finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemFriendRequestBinding
import com.neu.madcourse.mad_team4_finalproject.models.FindFriend
import com.neu.madcourse.mad_team4_finalproject.view_holders.FindFriendViewHolder

class FindFriendAdapter(
    @NonNull context: Context,
    @NonNull friendsList: List<FindFriend>
) : RecyclerView.Adapter<FindFriendViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The Find friend item list */
    private val mFriendsList: List<FindFriend> = friendsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindFriendViewHolder {
        // Inflate the item view
        val mItemBinding: ItemFriendRequestBinding = ItemFriendRequestBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FindFriendViewHolder(mContext, mItemBinding)
    }

    override fun onBindViewHolder(holder: FindFriendViewHolder, position: Int) {
        // Get the current find friend instance
        val findFriend: FindFriend = mFriendsList[position]
        // Set the find friend view field values
        holder.bindFindFriendData(findFriend)
    }

    override fun getItemCount(): Int {
        return mFriendsList.size
    }
}