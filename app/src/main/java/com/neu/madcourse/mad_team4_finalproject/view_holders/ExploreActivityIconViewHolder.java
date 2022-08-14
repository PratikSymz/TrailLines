package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ExploreActivityIconAdapter;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.squareup.picasso.Picasso;

public class ExploreActivityIconViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final Context mContext;

    public ExploreActivityIconViewHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        imageView = itemView.findViewById(R.id.exploreActivityIcon);
        this.mContext = mContext;


    }

    public void bindData(Integer iconId){
        Glide.with(mContext)
                .load(iconId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_downloading)
                .error(R.drawable.ic_error)
                .into(imageView);


    }
}
