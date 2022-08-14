package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.view_holders.HikeDetailActivityIconViewHolder;

import java.util.List;

public class HikeDetailActivityIconAdapter extends RecyclerView.Adapter<HikeDetailActivityIconViewHolder> {
    private List<Integer> iconIdList;
    private final Context mContext;


    public HikeDetailActivityIconAdapter(List<Integer> iconIdList, Context mContext) {
        this.iconIdList = iconIdList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HikeDetailActivityIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hike_detail_activities, parent, false);
        return new HikeDetailActivityIconViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeDetailActivityIconViewHolder holder, int position) {
        Integer iconId = iconIdList.get(position);
        holder.bindData(iconId);
    }

    @Override
    public int getItemCount() {
        return iconIdList.size();
    }

    /* Helper method to update the adapter list */
    public void updateDataList(List<Integer> iconIdList) {
        // Update the data list
        this.iconIdList = iconIdList;
        // Notify the adapter
        notifyDataSetChanged();
    }

}
