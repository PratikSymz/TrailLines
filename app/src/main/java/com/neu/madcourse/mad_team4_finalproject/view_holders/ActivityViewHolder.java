package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.squareup.picasso.Picasso;

public class ActivityViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText;
    private ShapeableImageView imageView;

    public ActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.trail_type_textview);
        imageView = itemView.findViewById(R.id.trail_type_image_view);

    }

    public void bindData(Activity activity){
//        idText.setText(activity.getRecordId());
        nameText.setText(activity.getName());
//        Picasso.get().load(activity.).into(mBinding.viewProfilePicture)

        switch (activity.getName()) {
            case Constants
                    .ThingsToDoStrings.CAMPING:
                Picasso.get().load(Constants.ThingsToDoImageIds.CAMPING).into(imageView);
            break;
            case Constants
                    .ThingsToDoStrings.CANYONEERING:
                Picasso.get().load(Constants.ThingsToDoImageIds.CANYONEERING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.CAVING:
                Picasso.get().load(Constants.ThingsToDoImageIds.CAVING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.CLIMBING:
                Picasso.get().load(Constants.ThingsToDoImageIds.CLIMBING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.SCUBA_DIVING:
                Picasso.get().load(Constants.ThingsToDoImageIds.SCUBA_DIVING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.BIKING:
                Picasso.get().load(Constants.ThingsToDoImageIds.BIKING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.BOATING:
                Picasso.get().load(Constants.ThingsToDoImageIds.BOATING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.FISHING:
                Picasso.get().load(Constants.ThingsToDoImageIds.FISHING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.HIKING:
                Picasso.get().load(Constants.ThingsToDoImageIds.HIKING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.PADDLING:
                Picasso.get().load(Constants.ThingsToDoImageIds.PADDLING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.SKIING:
                Picasso.get().load(Constants.ThingsToDoImageIds.SKIING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.SNORKELING:
                Picasso.get().load(Constants.ThingsToDoImageIds.SNORKELLING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.SURFING:
                Picasso.get().load(Constants.ThingsToDoImageIds.SURFING).into(imageView);
                break;
            case Constants
                    .ThingsToDoStrings.WATER_SKIING:
                Picasso.get().load(Constants.ThingsToDoImageIds.WATER_SKIING).into(imageView);
                break;
        }
    }
}
