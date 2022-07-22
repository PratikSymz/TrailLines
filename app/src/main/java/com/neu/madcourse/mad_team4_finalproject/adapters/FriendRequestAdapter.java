package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.FriendRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.view_holders.FriendRequestViewHolder;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> {
    private Context mContext;
    private List<FriendRequest> friendRequestList;

    public FriendRequestAdapter(Context mContext, List<FriendRequest> friendRequestList) {
        this.mContext = mContext;
        this.friendRequestList = friendRequestList;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.friend_request_layout_file, parent
        ,false);
        return new FriendRequestViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        FriendRequest friendRequest = friendRequestList.get(position);
        holder.bindData(friendRequest);
        // TODO: this needs to be checked again
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL + "/" + friendRequest.getProfilePicUrl());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).placeholder(R.drawable.blank_profile_picture)
                        .error(R.drawable.blank_profile_picture).into((ImageView) holder.itemView.findViewById(R.id.profile_pic_imageview));
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }
}
