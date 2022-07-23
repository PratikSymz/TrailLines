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
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.FriendRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.Objects;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    TextView fullName;
    ImageView profilePic;
    Button acceptButton, denyButton;
    ProgressBar progressBar;
    DatabaseReference databaseReferenceFriendRequest, databaseReferenceChats;
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

    /**
     * bind data will take care of the onBindViewHolder
     * @param friendRequest
     */
    public void bindData(FriendRequest friendRequest) {
        fullName.setText(friendRequest.getFullName());
        profilePic.getDrawable();
        databaseReferenceFriendRequest = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.FriendRequests.KEY_TLO);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.Chats.KEY_TLO);
        // accept button code
        acceptButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.INVISIBLE);
            denyButton.setVisibility(View.INVISIBLE);
            String userID = friendRequest.getUserID();
            //TODO Create another user reference for reverse friend request.........
            databaseReferenceChats.child(currentUser.getUid()).child(userID).child(Constants.UserKeys.Chats.TIME_STAMP)
                    .setValue(ServerValue.TIMESTAMP).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            databaseReferenceChats.child(userID).child(currentUser.getUid()).child(Constants.UserKeys.Chats.TIME_STAMP)
                                    .setValue(ServerValue.TIMESTAMP).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            databaseReferenceFriendRequest.child(currentUser.getUid()).child(userID).child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS)
                                                    .setValue(Constants.ACCEPT_FRIEND_REQUEST).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task1) {
                                                            if (task1.isSuccessful()) {
                                                                databaseReferenceFriendRequest.child(userID).child(currentUser.getUid()).child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS)
                                                                        .setValue(Constants.ACCEPT_FRIEND_REQUEST).addOnCompleteListener(task11 -> {
                                                                            if (task11.isSuccessful()) {
                                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                                acceptButton.setVisibility(View.VISIBLE);
                                                                                denyButton.setVisibility(View.VISIBLE);
                                                                            } else {
                                                                                handleAcceptException(Objects.requireNonNull(task11.getException()));
                                                                            }
                                                                        });
                                                            } else {
                                                                handleAcceptException(Objects.requireNonNull(task1.getException()));
                                                            }
                                                        }
                                                    });

                                        } else {
                                            handleAcceptException(Objects.requireNonNull(task1.getException()));
                                        }
                                    });
                            ;
                        } else {
                            handleAcceptException(Objects.requireNonNull(task.getException()));
                        }
                    });
        });
        // deny button code
        denyButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.INVISIBLE);
            denyButton.setVisibility(View.INVISIBLE);

            String userID = friendRequest.getUserID();
            // setting the request status as null to delete the record
            //TODO Create another user reference for reverse friend request.......
            databaseReferenceFriendRequest.child(currentUser.getUid()).child(userID)
                    .child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS).setValue("denied").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            databaseReferenceFriendRequest.child(userID).child(currentUser.getUid())
                                    .child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS).setValue("denied").addOnCompleteListener(task12 -> {
                                        if (!task12.isSuccessful()) {
                                            handleDenyException(Objects.requireNonNull(task12.getException()));
                                        }
                                    });
                        } else {
                            handleDenyException(Objects.requireNonNull(task.getException()));
                        }
                    });

        });
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                .child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL + "/" + friendRequest.getProfilePicUrl());
    }

    /**
     * A method to handle exception when deny button on the friend request tab does not function
     *
     * @param exception
     */

    private void handleDenyException(Exception exception) {
        mBaseUtils.showToast("Deny Request Failed!".trim() + exception.getMessage(), Toast.LENGTH_SHORT);
        progressBar.setVisibility(View.INVISIBLE);
        acceptButton.setVisibility(View.VISIBLE);
        denyButton.setVisibility(View.VISIBLE);
    }


    /**
     * A method to handle exception when accept button on the friend request tab does not function
     *
     * @param exception
     */
    private void handleAcceptException(Exception exception) {
        mBaseUtils.showToast("Friend Request acceptation not performed: ".trim() + exception.getMessage(), Toast.LENGTH_SHORT);
        progressBar.setVisibility(View.INVISIBLE);
        acceptButton.setVisibility(View.VISIBLE);
        denyButton.setVisibility(View.VISIBLE);
    }
}
