package com.neu.madcourse.mad_team4_finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemSelectedImageBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.VerticalTrailViewsBinding
import com.neu.madcourse.mad_team4_finalproject.view_holders.GenericViewHolder

class GenericAdapter<T>(
    @NonNull context: Context,
    @NonNull dataModelList: List<T>,
    @NonNull itemBinding: ViewBinding
) : RecyclerView.Adapter<GenericViewHolder<T>>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The data model item list */
    private var mDataModelList: List<T> = dataModelList

    /* The Item layout view binding reference */
    private var mItemBinding: ViewBinding = itemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        if (mItemBinding::class.java.isAssignableFrom(ItemSelectedImageBinding::class.java)) {
            mItemBinding = ItemSelectedImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else if (mItemBinding::class.java.isAssignableFrom(VerticalTrailViewsBinding::class.java)) {
            mItemBinding = VerticalTrailViewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        return GenericViewHolder(mContext, mItemBinding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        // Get the data model instance
        val dataModel: T = mDataModelList[position]
        // Set the item view field values
        holder.bindData(dataModel)
    }

    override fun getItemCount(): Int {
        return mDataModelList.size
    }

    /* Helper method to update the adapter list */
    fun updateDataList(dataModelList: List<T>) {
        // Update the data list
        mDataModelList = dataModelList
        // Notify the adapter
        notifyItemRangeChanged(0, mDataModelList.size)
    }
}