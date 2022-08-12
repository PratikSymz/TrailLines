package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ExploreActivityIconAdapter;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.squareup.picasso.Picasso;

public class ExploreActivityIconViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
//    private Context context;
    public RecyclerView childRecyclerView;


    public ExploreActivityIconViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.exploreActivityIcon);
//        this.context = context;


    }

    public void bindData(Integer iconId){
//        idText.setText(activity.getRecordId());
//        nameText.setText(activity.getName());
        Picasso.get().load(iconId).into(imageView);
//        Glide.with(mContext)
//                .load(park.getImageList().get(0).getUrl())
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .placeholder(R.drawable.ic_downloading)
//                .error(R.drawable.ic_error)
//                .into(trailImage);


    }
}
