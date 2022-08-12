package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class ActivityViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText;
    private ShapeableImageView imageView;
    private Context mContext;

    public ActivityViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        nameText = itemView.findViewById(R.id.trail_type_textview);
        imageView = itemView.findViewById(R.id.trail_type_image_view);
        mContext = context;
    }

    public void bindData(Activity activity) {
        nameText.setText(activity.getName());

        switch (activity.getName()) {
            case Constants.ThingsToDoStrings.CAMPING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.CAMPING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.CANYONEERING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.CANYONEERING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.CAVING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.CAVING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.CLIMBING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.CLIMBING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.SCUBA_DIVING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.SCUBA_DIVING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.BIKING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.BIKING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.BOATING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.BOATING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.FISHING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.FISHING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.HIKING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.HIKING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.PADDLING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.PADDLING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.SKIING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.SKIING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.SNORKELING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.SNORKELLING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.SURFING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.SURFING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
            case Constants.ThingsToDoStrings.WATER_SKIING:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.WATER_SKIING)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
        }
    }
}
