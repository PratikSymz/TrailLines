package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ActivityViewHolder;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityViewHolder> {
    private List<Activity> activityList;
    private Context mContext;

    public ActivityAdapter(List<Activity> activityList, Context mContext) {
        this.activityList = activityList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_information, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.bindData(activity);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
