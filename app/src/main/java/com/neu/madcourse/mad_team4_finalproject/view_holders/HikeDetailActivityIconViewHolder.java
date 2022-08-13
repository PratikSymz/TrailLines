package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.squareup.picasso.Picasso;

public class HikeDetailActivityIconViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private Context mContext;

    public HikeDetailActivityIconViewHolder(@NonNull View itemView, Context mContext) {
        super(itemView);
        imageView = itemView.findViewById(R.id.hikeDetailActivityIcon);
        this.mContext = mContext;
    }

    public void bindData(Integer iconId){
        Picasso.get().load(iconId).into(imageView);
        Glide.with(mContext)
                .load(iconId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_downloading)
                .error(R.drawable.ic_error)
                .into(imageView);
    }
}
