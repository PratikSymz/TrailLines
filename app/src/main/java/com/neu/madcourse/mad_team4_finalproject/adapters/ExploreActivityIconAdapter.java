package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ExploreActivityIconViewHolder;

import java.util.List;

public class ExploreActivityIconAdapter extends RecyclerView.Adapter<ExploreActivityIconViewHolder> {
    private final List<Integer> iconIdList;
    private final Context mContext;


    public ExploreActivityIconAdapter(List<Integer> iconIdList, Context mContext) {
        this.iconIdList = iconIdList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ExploreActivityIconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_explore_activities, parent, false);
        return new ExploreActivityIconViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreActivityIconViewHolder holder, int position) {
        Integer iconId = iconIdList.get(position);
        holder.bindData(iconId);
    }

    @Override
    public int getItemCount() {
        return iconIdList.size();
    }
}
