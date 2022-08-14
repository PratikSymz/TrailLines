package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.activities.TrailDetailActivity;
import com.neu.madcourse.mad_team4_finalproject.interfaces.ActivityOnClickListener;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.models_nps.ParkResult;
import com.neu.madcourse.mad_team4_finalproject.retrofit.NPSEndpoints;
import com.neu.madcourse.mad_team4_finalproject.retrofit.NPSInterceptor;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityViewHolder extends RecyclerView.ViewHolder implements ActivityOnClickListener {
    private final TextView nameText;
    private final ShapeableImageView imageView;
    private final Context mContext;
    private final View mItemView;
    private final List<Park> mParkList = new ArrayList<>();
    private final ActivityOnClickListener mListener;

    public ActivityViewHolder(@NonNull View itemView, Context context, ActivityOnClickListener listener) {
        super(itemView);
        nameText = itemView.findViewById(R.id.trail_type_textview);
        imageView = itemView.findViewById(R.id.trail_type_image_view);
        mContext = context;
        mItemView = itemView;
        mListener = listener;


    }

    public void bindData(Activity activity) {
        nameText.setText(activity.getName());

        mItemView.setOnClickListener(v -> mListener.onActivityClick(activity.getRecordId()));

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
            case Constants.ThingsToDoStrings.ALL:
                Glide.with(mContext)
                        .load(Constants.ThingsToDoImageIds.ALL)
                        .placeholder(R.drawable.ic_downloading)
                        .error(R.drawable.ic_error)
                        .into(imageView);
                break;
        }


    }

    @Override
    public void onActivityClick(String activityCode) {

    }

}
