package com.neu.madcourse.mad_team4_finalproject.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.CustomFragmentListRowBinding
import com.neu.madcourse.mad_team4_finalproject.models.SavedTrails
import com.neu.madcourse.mad_team4_finalproject.view_holders.TrailViewHolder

class TrailDatabaseAdapter(@NonNull context: Context,
@NonNull savedTrailList: List<SavedTrails>) : RecyclerView.Adapter<TrailViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context
    private var savedTrailsList: List<SavedTrails> = savedTrailList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val mItemBinding: CustomFragmentListRowBinding = CustomFragmentListRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailViewHolder(mContext, mItemBinding)
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        // Get the current conversation instance
        val savedTrails: SavedTrails = savedTrailsList[position]
        holder.bindConversationData(savedTrails)


    }

    override fun getItemCount(): Int {
        return savedTrailsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(savedTrailsList: List<SavedTrails>){
        this.savedTrailsList = savedTrailsList
        notifyDataSetChanged()
    }
}