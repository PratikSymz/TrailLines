package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.Explore;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ExploreViewHolder;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreViewHolder> {
    List<Explore> exploreList;
    private Context mContext;

    public ExploreAdapter(List<Explore> exploreList, Context mContext) {
        this.exploreList = exploreList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vertical_trail_views, parent, false);
        return new ExploreViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreViewHolder holder, int position) {
        Explore explore = exploreList.get(position);
        holder.bindData(explore);
    }

    @Override
    public int getItemCount() {
        return exploreList.size();
    }

    /* Helper method to update the adapter list */
    public void updateDataList(List<Explore> exploreList) {
        // Update the data list
        this.exploreList = exploreList;
        // Notify the adapter
        notifyItemRangeChanged(0, this.exploreList.size());
    }
}
