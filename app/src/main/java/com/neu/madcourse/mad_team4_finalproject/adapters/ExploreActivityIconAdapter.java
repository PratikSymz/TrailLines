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
    private List<Integer> iconIdList;
    private Context mContext;


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
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,
//                false);
//        holder.childRecyclerView.setLayoutManager(layoutManager);
//        holder.childRecyclerView.setHasFixedSize(true);

//        for (Park park : parklist) {
//            boolean flag = false;
//            for (Activity activity: park.getActivityList()) {
//                if (activityCodes.contains(activity.getRecordId())) {
//                    flag = true;
//                }
//
//                switch (activity.getRecordId()) {
//                    case Constants
//                            .ThingsToDoStrings.CAMPING:
//                        exploreActivityIconIds.add(R.drawable.ic_camping);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.CANYONEERING:
//                    case Constants
//                            .ThingsToDoStrings.CAVING:
//                    case Constants
//                            .ThingsToDoStrings.CLIMBING:
//                    case Constants
//                            .ThingsToDoStrings.HIKING:
//                        exploreActivityIconIds.add(R.drawable.ic_hiking);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.SCUBA_DIVING:
//                    case Constants
//                            .ThingsToDoStrings.SNORKELING:
//                        exploreActivityIconIds.add(R.drawable.ic_scuba);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.BIKING:
//                        exploreActivityIconIds.add(R.drawable.ic_biking);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.BOATING:
//                    case Constants
//                            .ThingsToDoStrings.PADDLING:
//                        exploreActivityIconIds.add(R.drawable.ic_boating);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.FISHING:
//                        exploreActivityIconIds.add(R.drawable.ic_fishing);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.SKIING:
//                        exploreActivityIconIds.add(R.drawable.ic_skiing);
//                        break;
//                    case Constants
//                            .ThingsToDoStrings.SURFING:
//                    case Constants
//                            .ThingsToDoStrings.WATER_SKIING:
//                        exploreActivityIconIds.add(R.drawable.ic_swimming);
//                        break;
//                }
//
//            }
//
//        ExploreActivityIconAdapter childRecyclerViewAdapter = new ExploreActivityIconAdapter(arrayList,holder.childRecyclerView.getContext());
//
//        exploreActivityIconAdapter = new ExploreActivityIconAdapter(exploreActivityIconIds, getActivity());
//        mBinding.setLayoutManager(layoutManager);
//        mBinding.horizontalTrailRecyclerView.setAdapter(activityAdxapter);
    }

    @Override
    public int getItemCount() {
        return iconIdList.size();
    }
}
