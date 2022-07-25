package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.databinding.ItemConnectionRequestBinding;
import com.neu.madcourse.mad_team4_finalproject.models.ConnectionRequest;
import com.neu.madcourse.mad_team4_finalproject.view_holders.ConnectionRequestViewHolder;

import java.util.List;

public class ConnectionRequestAdapter extends RecyclerView.Adapter<ConnectionRequestViewHolder> {
    private Context mContext;
    private List<ConnectionRequest> friendRequestList;

    public ConnectionRequestAdapter(Context mContext, List<ConnectionRequest> friendRequestList) {
        this.mContext = mContext;
        this.friendRequestList = friendRequestList;
    }

    @NonNull
    @Override
    public ConnectionRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConnectionRequestBinding itemBinding =
                ItemConnectionRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ConnectionRequestViewHolder(mContext, itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionRequestViewHolder holder, int position) {
        ConnectionRequest friendRequest = friendRequestList.get(position);
        holder.bindData(friendRequest);
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }
}
