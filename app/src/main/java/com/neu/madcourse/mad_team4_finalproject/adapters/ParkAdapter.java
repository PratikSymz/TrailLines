package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ParkViewHolder;

import java.util.List;

public class ParkAdapter extends RecyclerView.Adapter<ParkViewHolder> {
    private List<Park> parkList;
    private Context mContext;

    public ParkAdapter(List<Park> activityList, Context mContext) {
        this.parkList = activityList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ParkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vertical_trail_views, parent, false);
        return new ParkViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkViewHolder holder, int position) {
        Park park = parkList.get(position);
        holder.bindData(park);
    }

    @Override
    public int getItemCount() {
        return parkList.size();
    }

    /* Helper method to update the adapter list */
    public void updateDataList(List<Park> parkList) {
        // Update the data list
        this.parkList = parkList;
        // Notify the adapter
        notifyItemRangeChanged(0, this.parkList.size());
    }
}
