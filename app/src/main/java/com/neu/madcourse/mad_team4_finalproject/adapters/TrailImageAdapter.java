package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neu.madcourse.mad_team4_finalproject.R;

import java.util.List;

public class TrailImageAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> imageIds;
    private final String parkName;

    public TrailImageAdapter(Context c, List<String> imageIds1, String parkName) {
        context = c;
        this.imageIds = imageIds1;
        this.parkName = parkName;
    }

    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public String getItem(int position) {
        return imageIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_explore_image, parent,false);
            ImageView icon = view.findViewById(R.id.icon); // get the reference of ImageView
            Glide.with(context)
                    .load(imageIds.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.ic_downloading)
                    .error(R.drawable.ic_error)
                    .into(icon);
        }


        return view;
    }
}
