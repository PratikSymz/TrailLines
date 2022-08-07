package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemSelectedImageBinding

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

        // Set the onClick action
        // mBinding.root.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        TODO()
    }
}