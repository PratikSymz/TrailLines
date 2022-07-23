package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.FriendRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    TextView fullName;
    ImageView profilePic;
    Button acceptButton, denyButton;
    ProgressBar progressBar;
    DatabaseReference databaseReferenceFriendRequest;
    FirebaseUser currentUser;
    private BaseUtils mBaseUtils;

    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        fullName = itemView.findViewById(R.id.full_name_textview);
        profilePic = itemView.findViewById(R.id.profile_pic_imageview);
        acceptButton = itemView.findViewById(R.id.accept_request_button);
        denyButton = itemView.findViewById(R.id.deny_request_button);
        progressBar = itemView.findViewById(R.id.request_progress_bar);
    }

    public void bindData(FriendRequest friendRequest) {
        fullName.setText(friendRequest.getFullName());
        profilePic.getDrawable();
        databaseReferenceFriendRequest = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.FriendRequests.KEY_TLO);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                acceptButton.setVisibility(View.INVISIBLE);
                denyButton.setVisibility(View.INVISIBLE);

                String userID = friendRequest.getUserID();
                // setting the request status as null to delete the record
                databaseReferenceFriendRequest
                        .child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseReferenceFriendRequest
                                            .child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        mBaseUtils.showToast("Deny Request Failed!", Toast.LENGTH_SHORT);
                                                    }
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    acceptButton.setVisibility(View.VISIBLE);
                                                    denyButton.setVisibility(View.VISIBLE);
                                                }
                                            });
                                } else {
                                    mBaseUtils.showToast("Deny Request Failed!", Toast.LENGTH_SHORT);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    acceptButton.setVisibility(View.VISIBLE);
                                    denyButton.setVisibility(View.VISIBLE);
                                }
                            }
                        });

            }
        });
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL + "/" + friendRequest.getProfilePicUrl());
    }
}
