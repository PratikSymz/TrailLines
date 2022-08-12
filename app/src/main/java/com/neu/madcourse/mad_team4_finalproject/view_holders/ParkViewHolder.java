package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.squareup.picasso.Picasso;

public class ParkViewHolder extends RecyclerView.ViewHolder {

    private TextView trailName;
    private ShapeableImageView trailImage;
    private TextView trailInfo;
    private TextView trailRating;
    public RecyclerView childRecyclerView;


    public ParkViewHolder(@NonNull View itemView) {
        super(itemView);
        trailName = itemView.findViewById(R.id.view_trail_name);
        trailImage = itemView.findViewById(R.id.view_trail_image);
        trailInfo = itemView.findViewById(R.id.view_trail_info);
        trailRating = itemView.findViewById(R.id.view_review);
        childRecyclerView = itemView.findViewById(R.id.trail_activties);
    }

    public void bindData(Park activity) {
        trailName.setText(activity.getFullName());
        trailInfo.setText(activity.getDescription().substring(0, 100) + "...");
        trailRating.setText("N/A");
        Picasso.get().load(activity.getImageList().get(0).getUrl()).into(trailImage);
    }
}
