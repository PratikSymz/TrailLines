package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.FriendRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
     TextView fullName;
     ImageView profilePic;
     Button acceptButton, denyButton;
     ProgressBar progressBar;

    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.full_name_textview);
        profilePic = itemView.findViewById(R.id.profile_pic_imageview);
        acceptButton = itemView.findViewById(R.id.accept_request_button);
        denyButton = itemView.findViewById(R.id.deny_request_button);
        progressBar = itemView.findViewById(R.id.request_progress_bar);
    }
    public void bindData(FriendRequest friendRequest){
        fullName.setText(friendRequest.getFullName());
        profilePic.getDrawable();
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL + "/" + friendRequest.getProfilePicUrl());
    }
}
