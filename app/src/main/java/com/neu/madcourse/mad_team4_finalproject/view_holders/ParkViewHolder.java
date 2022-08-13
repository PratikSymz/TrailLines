package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.activities.TrailDetailActivity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;

public class ParkViewHolder extends RecyclerView.ViewHolder {
    private TextView trailName;
    private ShapeableImageView trailImage;
    private TextView trailInfo;
    private TextView trailRating;
    public RecyclerView childRecyclerView;
    private Context mContext;
    private View mItemView;

    public ParkViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        trailName = itemView.findViewById(R.id.view_trail_name);
        trailImage = itemView.findViewById(R.id.view_trail_image);
        trailInfo = itemView.findViewById(R.id.view_trail_info);
        childRecyclerView = itemView.findViewById(R.id.trail_activties);
        trailRating = itemView.findViewById(R.id.view_park_rating);
        mContext = context;
        mItemView = itemView;
    }

    public void bindData(Park park) {
        trailName.setText(park.getFullName());
        trailInfo.setText(park.getAddressList().get(0).getLine1());
        trailRating.setText("0.0");

        Glide.with(mContext)
                .load(park.getImageList().get(0).getUrl())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(350, 200)
                .placeholder(R.drawable.ic_downloading)
                .error(R.drawable.ic_error)
                .into(trailImage);

        Log.d("pvh", park.getImageList().get(0).getUrl());
        /* Set the onClick action for every item view on the parks list */
        mItemView.setOnClickListener(view -> {
            Intent detailIntent = new Intent(mContext, TrailDetailActivity.class);
            detailIntent.putExtra("park_details", park);
            mContext.startActivity(detailIntent);
        });
    }
}
