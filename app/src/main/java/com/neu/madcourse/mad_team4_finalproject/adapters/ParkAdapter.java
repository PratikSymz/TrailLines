package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Activity;
import com.neu.madcourse.mad_team4_finalproject.models_nps.Park;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ParkViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ParkAdapter extends RecyclerView.Adapter<ParkViewHolder> {
    private List<Park> parkList;
    private Context mContext;
    private List<Integer> activityIconIds;

    public ParkAdapter(List<Park> activityList, Context mContext) {
        this.parkList = activityList;
        this.mContext = mContext;
        this.activityIconIds = new ArrayList<>();
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
        this.activityIconIds = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setHasFixedSize(true);

        for (Activity activity : park.getActivityList()) {
            switch (activity.getRecordId()) {
                case Constants
                        .ThingsToDoCodes.CAMPING_CODE:
                    activityIconIds.add(R.drawable.ic_camping);
                    break;
                case Constants
                        .ThingsToDoCodes.CANYONEERING_CODE:
                    activityIconIds.add(R.drawable.ic_canyon);
                    break;
                case Constants
                        .ThingsToDoCodes.CAVING_CODE:
                    activityIconIds.add(R.drawable.ic_cave);
                    break;
                case Constants
                        .ThingsToDoCodes.CLIMBING_CODE:
                    activityIconIds.add(R.drawable.ic_climbing);
                break;
                case Constants
                        .ThingsToDoCodes.HIKING_CODE:
                    activityIconIds.add(R.drawable.ic_hiking);
                    break;
                case Constants
                        .ThingsToDoCodes.SCUBA_DIVING_CODE:
                    activityIconIds.add(R.drawable.ic_scuba);
                    break;
                case Constants
                        .ThingsToDoCodes.SNORKELING_CODE:
                    activityIconIds.add(R.drawable.ic_snorkelling);
                    break;
                case Constants
                        .ThingsToDoCodes.BIKING_CODE:
                    activityIconIds.add(R.drawable.ic_biking);
                    break;
                case Constants
                        .ThingsToDoCodes.BOATING_CODE:
                    activityIconIds.add(R.drawable.ic_boating);
                    break;
                case Constants
                        .ThingsToDoCodes.PADDLING_CODE:
                    activityIconIds.add(R.drawable.ic_kayaking);
                    break;
                case Constants
                        .ThingsToDoCodes.FISHING_CODE:
                    activityIconIds.add(R.drawable.ic_fishing);
                    break;
                case Constants
                        .ThingsToDoCodes.SKIING_CODE:
                    activityIconIds.add(R.drawable.ic_skiing);
                    break;
                case Constants
                        .ThingsToDoCodes.SURFING_CODE:
                    activityIconIds.add(R.drawable.ic_surfing);
                break;
                case Constants
                        .ThingsToDoCodes.WATER_SKIING_CODE:
                    activityIconIds.add(R.drawable.ic_swimming);
                    break;
            }
        }

        ExploreActivityIconAdapter exploreActivityIconAdapter = new ExploreActivityIconAdapter(activityIconIds,holder.childRecyclerView.getContext());
        holder.childRecyclerView.setAdapter(exploreActivityIconAdapter);
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
        notifyDataSetChanged();
    }
}
