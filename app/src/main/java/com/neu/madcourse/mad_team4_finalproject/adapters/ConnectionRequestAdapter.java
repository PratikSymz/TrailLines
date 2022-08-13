package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.databinding.ItemConnectionRequestBinding;
import com.neu.madcourse.mad_team4_finalproject.interfaces.ItemRemoveListener;
import com.neu.madcourse.mad_team4_finalproject.models.ConnectionRequest;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ConnectionRequestViewHolder;

import java.util.List;

public class ConnectionRequestAdapter extends RecyclerView.Adapter<ConnectionRequestViewHolder> {
    private Context mContext;
    private List<ConnectionRequest> connectionRequestList;
    /* The Recycler view item remove listener interface instance */
    private ItemRemoveListener mListener;

    public ConnectionRequestAdapter(Context mContext, List<ConnectionRequest> friendRequestList, ItemRemoveListener listener) {
        this.mContext = mContext;
        this.connectionRequestList = friendRequestList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ConnectionRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConnectionRequestBinding itemBinding =
                ItemConnectionRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ConnectionRequestViewHolder(mContext, itemBinding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionRequestViewHolder holder, int position) {
        ConnectionRequest friendRequest = connectionRequestList.get(position);
        holder.bindData(friendRequest, position);
    }

    @Override
    public int getItemCount() {
        return connectionRequestList.size();
    }

    /* Helper method to update the adapter list */
    public void updateDataList(List<ConnectionRequest> connectionRequests) {
        // Update the data list
        this.connectionRequestList = connectionRequests;
        // Notify the adapter
        notifyDataSetChanged();
    }
}
