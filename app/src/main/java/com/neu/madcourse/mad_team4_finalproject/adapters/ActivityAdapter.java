package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.interfaces.ActivityOnClickListener;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ActivityViewHolder;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityViewHolder> {
    private List<Activity> activityList;
    private Context mContext;
    private ActivityOnClickListener mListener;

    public ActivityAdapter(List<Activity> activityList, Context context, ActivityOnClickListener listener) {
        this.activityList = activityList;
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_trail_views, parent, false);
        return new ActivityViewHolder(view, mContext, mListener);
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

    /* Helper method to update the adapter list */
    public void updateDataList(List<Activity> activityList) {
        // Update the data list
        this.activityList = activityList;
        // Notify the adapter
        notifyItemRangeChanged(0, this.activityList.size());
    }
}
