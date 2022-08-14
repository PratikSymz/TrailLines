package com.neu.madcourse.mad_team4_finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemFriendRequestBinding
import com.neu.madcourse.mad_team4_finalproject.interfaces.ItemRemoveListener
import com.neu.madcourse.mad_team4_finalproject.models.FindFriend
import com.neu.madcourse.mad_team4_finalproject.view_holders.FindFriendViewHolder

class FindFriendAdapter(
    @NonNull context: Context,
    @NonNull friendsList: MutableList<FindFriend>,
    listener: ItemRemoveListener
) : RecyclerView.Adapter<FindFriendViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The Find friend item list */
    private var mFriendsList: List<FindFriend> = friendsList

    /* The Recycler view item remove listener interface instance */
    private val mListener: ItemRemoveListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindFriendViewHolder {
        // Inflate the item view
        val mItemBinding: ItemFriendRequestBinding = ItemFriendRequestBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FindFriendViewHolder(mContext, mItemBinding, mListener)
    }

    override fun onBindViewHolder(holder: FindFriendViewHolder, position: Int) {
        // Get the current find friend instance
        val findFriend: FindFriend = mFriendsList[position]
        // Set the find friend view field values
        holder.bindFindFriendData(findFriend, position)
    }

    override fun getItemCount(): Int {
        return mFriendsList.size
    }

    /* Helper method to update the adapter list */
    fun updateDataList(friendsList: List<FindFriend>) {
        // Update the data list
        mFriendsList = friendsList
        // Notify the adapter
        notifyDataSetChanged()
    }
}