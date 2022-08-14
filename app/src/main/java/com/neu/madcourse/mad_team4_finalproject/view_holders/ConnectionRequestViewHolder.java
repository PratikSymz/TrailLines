package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemConnectionRequestBinding;
import com.neu.madcourse.mad_team4_finalproject.interfaces.ItemRemoveListener;
import com.neu.madcourse.mad_team4_finalproject.models.ConnectionRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.NotificationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectionRequestViewHolder extends RecyclerView.ViewHolder {
    /* The activity context */
    private Context mContext;

    /* The Item View binder */
    private ItemConnectionRequestBinding mBinding;

    /* The current Connection request instance */
    private ConnectionRequest mConnRequest;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Network utils reference */
    private NetworkUtils mNetworkUtils;

    /* The Notification utils reference */
    private NotificationUtils mNotificationUtils;

    /* The Firebase storage reference */
    private StorageReference mFirebaseStorage;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Firebase Database reference -> "users" */
    private DatabaseReference mUsersDatabaseRef;

    /* The Firebase Database reference -> "chats" */
    private DatabaseReference mChatsDatabaseRef;

    /* The Recycler view item remove listener interface instance */
    private ItemRemoveListener mListener;

    public ConnectionRequestViewHolder(@NonNull Context context,
                                       @NonNull ItemConnectionRequestBinding itemBinding,
                                       @NonNull ItemRemoveListener listener) {
        super(itemBinding.getRoot());

        // Set the activity context
        mContext = context;

        // Set the item layout view binding
        mBinding = itemBinding;

        // Instantiate the base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the network utils reference
        mNetworkUtils = new NetworkUtils(mContext);

        // Instantiate the notification utils reference
        mNotificationUtils = new NotificationUtils(mContext);

        // Instantiate the Firebase storage reference
        mFirebaseStorage = FirebaseStorage.getInstance().getReference();

        // Instantiate the Firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Instantiate the Firebase user reference
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Instantiate the Firebase "friend_requests" user database reference
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO);

        // Instantiate the Firebase "chats" user database reference
        mChatsDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.ChatKeys.KEY_TLO);

        // Instantiate the item remove listener
        mListener = listener;
    }

    /**
     * Helper method to bind Connection request instance information to the Item Views
     *
     * @param connectionRequest The connection request instance
     */
    public void bindData(ConnectionRequest connectionRequest, int position) {
        // Set the ConnectionRequest instance
        mConnRequest = connectionRequest;

        // Set the user's name
        mBinding.viewConnUserName.setText(mConnRequest.getName());

        // Get the profile image storage reference
        StorageReference imageReference = mFirebaseStorage.child(
                formatFileName(mConnRequest.getProfileImageFileName())
        );

        // Load the user's profile picture into the item view
        imageReference.getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(mContext)
                        .load(uri)
                        .placeholder(R.drawable.ic_profile_tag_account)
                        .error(R.drawable.ic_profile_tag_account)
                        .into(mBinding.viewConnProfileImage)
        );

        // accept button code
        mBinding.viewConnButtonAccept.setOnClickListener(view -> {
            // Show the progress bar
            mBinding.viewConnProgressBar.setVisibility(View.VISIBLE);
            // Hide both the accept and deny buttons
            mBinding.viewConnButtonAccept.setVisibility(View.INVISIBLE);
            mBinding.viewConnButtonDeny.setVisibility(View.INVISIBLE);

            // Extract the userID of the requester
            String userID = connectionRequest.getUserID();

            // Create a message push request on the database reference
            DatabaseReference messagePushRef = mChatsDatabaseRef
                    .child(mFirebaseUser.getUid())
                    .child(userID)
                    .child(Constants.ChatKeys.MessageKeys.KEY_TLO)
                    .push();

            // Extract the message push ID
            String pushID = messagePushRef.getKey();
            assert pushID != null;

            // The message

            // Create a chat reference between the two users when the user accepts the connection request
            // in FORWARD and REVERSE directions
            // [FORWARD]
            mChatsDatabaseRef
                    .child(mFirebaseUser.getUid())
                    .child(userID)
                    .child(Constants.ChatKeys.MessageKeys.KEY_TLO)
                    .child(pushID)
                    .updateChildren(getDataMap(pushID, mFirebaseUser.getUid()))     // TODO: Check this
                    .addOnCompleteListener(taskChat -> {
                        if (taskChat.isSuccessful()) {
                            // [REVERSE]
                            mChatsDatabaseRef
                                    .child(userID)
                                    .child(mFirebaseUser.getUid())
                                    .child(Constants.ChatKeys.MessageKeys.KEY_TLO)
                                    .child(pushID)
                                    .updateChildren(getDataMap(pushID, mFirebaseUser.getUid()))     // TODO: Check this
                                    .addOnCompleteListener(reverseTaskChat -> {
                                        if (reverseTaskChat.isSuccessful()) {
                                            // Now, set the request status of the requester and requestee
                                            // as accepted in FORWARD and REVERSE directions
                                            // [FORWARD]
                                            mUsersDatabaseRef
                                                    .child(mFirebaseUser.getUid())
                                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                                                    .child(userID)
                                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                                                    .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_ACCEPTED)
                                                    .addOnCompleteListener(taskFriendRequest -> {
                                                        if (taskFriendRequest.isSuccessful()) {
                                                            // [REVERSE]
                                                            mUsersDatabaseRef
                                                                    .child(userID)
                                                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                                                                    .child(mFirebaseUser.getUid())
                                                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                                                                    .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_ACCEPTED)
                                                                    .addOnCompleteListener(reverseTaskFriendRequest -> {
                                                                        if (reverseTaskFriendRequest.isSuccessful()) {
                                                                            // Hide the progress bar
                                                                            mBinding.viewConnProgressBar.setVisibility(View.INVISIBLE);
                                                                            // Show the accept and deny buttons
                                                                            mBinding.viewConnButtonAccept.setVisibility(View.VISIBLE);
                                                                            mBinding.viewConnButtonDeny.setVisibility(View.VISIBLE);

                                                                            // Create a notification
                                                                            String title = "Friend Request Accepted";
                                                                            String message = "Friend request accepted by " + mFirebaseUser.getDisplayName();
                                                                            mNotificationUtils.sendNotification(mContext, title, message, userID);
                                                                        } else {
                                                                            handleAcceptException(Objects.requireNonNull(reverseTaskFriendRequest.getException()));
                                                                        }
                                                                    });
                                                        } else {
                                                            handleAcceptException(Objects.requireNonNull(taskFriendRequest.getException()));
                                                        }
                                                    });
                                        } else {
                                            handleAcceptException(Objects.requireNonNull(reverseTaskChat.getException()));
                                        }
                                    });
                        } else {
                            handleAcceptException(Objects.requireNonNull(taskChat.getException()));
                        }
                    });

            // Remove item from the adapter
            mListener.removeItem(position);
        });

        /* Set the Deny request button onClick action */
        mBinding.viewConnButtonDeny.setOnClickListener(view -> {
            // Show the progress bar
            mBinding.viewConnProgressBar.setVisibility(View.VISIBLE);
            // Hide both the accept and deny buttons
            mBinding.viewConnButtonAccept.setVisibility(View.INVISIBLE);
            mBinding.viewConnButtonDeny.setVisibility(View.INVISIBLE);

            // Extract the userID of the requester
            String userID = connectionRequest.getUserID();

            // Setting the request status of the requester as denied
            // in FORWARD direction -> current user - friend [DENIED]
            mUsersDatabaseRef
                    .child(mFirebaseUser.getUid())
                    .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                    .child(userID)
                    .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                    .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_DENIED)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update the request status in the REVERSE direction inside the other user's
                            // "friend_request" directory
                            mUsersDatabaseRef
                                    .child(userID)
                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                                    .child(mFirebaseUser.getUid())
                                    .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                                    .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_DENIED)
                                    .addOnCompleteListener(reverseTask -> {
                                        if (reverseTask.isSuccessful()) {
                                            mBaseUtils.showToast("Request denied successfully!", Toast.LENGTH_SHORT);
                                            // Hide the progress bar
                                            mBinding.viewConnProgressBar.setVisibility(View.INVISIBLE);
                                            // Show the accept and deny buttons
                                            mBinding.viewConnButtonAccept.setVisibility(View.VISIBLE);
                                            mBinding.viewConnButtonDeny.setVisibility(View.VISIBLE);

                                            // Create a notification for friend request denied
                                            String title = "Friend Request Denied";
                                            String message = "Friend request denied by " + mFirebaseUser.getDisplayName();
                                            mNotificationUtils.sendNotification(mContext, title, message, userID);
                                        }
                                        if (!reverseTask.isSuccessful()) {
                                            handleDenyException(Objects.requireNonNull(reverseTask.getException()));
                                        }
                                    });
                        } else {
                            handleDenyException(Objects.requireNonNull(task.getException()));
                        }
                    });

            // Remove item from the adapter
            mListener.removeItem(position);
        });
    }

    /**
     * Helper method to handle exceptions when accepts button on the friend request tab does not function
     *
     * @param exception The Firebase task exception
     */
    private void handleAcceptException(Exception exception) {
        mBaseUtils.showToast("Friend Request acceptation not performed: " + exception.getMessage(), Toast.LENGTH_SHORT);
        // Hide the progress bar
        mBinding.viewConnProgressBar.setVisibility(View.INVISIBLE);
        // Show the accept and deny buttons
        mBinding.viewConnButtonAccept.setVisibility(View.VISIBLE);
        mBinding.viewConnButtonDeny.setVisibility(View.VISIBLE);
    }

    /**
     * Helper method to handle exceptions when deny button on the friend request tab does not function
     *
     * @param exception The Firebase task exception
     */
    private void handleDenyException(Exception exception) {
        mBaseUtils.showToast("Deny Request Failed! " + exception.getMessage(), Toast.LENGTH_SHORT);
        // Hide the progress bar
        mBinding.viewConnProgressBar.setVisibility(View.INVISIBLE);
        // Show the accept and deny buttons
        mBinding.viewConnButtonAccept.setVisibility(View.VISIBLE);
        mBinding.viewConnButtonDeny.setVisibility(View.VISIBLE);
    }

    /* Helper method to generate an empty message data map */
    private Map<String, Object> getDataMap(String pushID, String from) {
        // Create a hash map of data
        Map<String, Object> dataMap = new HashMap<>();
        // Add the message ID
        dataMap.put(Constants.ChatKeys.MessageKeys.KEY_MESSAGE_ID, pushID);
        // Add the message "from" user ID
        dataMap.put(Constants.ChatKeys.MessageKeys.KEY_FROM, from);
        // Add the message content
        dataMap.put(Constants.ChatKeys.MessageKeys.KEY_CONTENT, "Let's begin messaging!");
        // Add the message data type
        dataMap.put(Constants.ChatKeys.MessageKeys.KEY_DATA_TYPE, Constants.ChatKeys.MessageKeys.DATA_TYPE_TEXT);
        // Add the message timestamp
        dataMap.put(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP, ServerValue.TIMESTAMP);

        return dataMap;
    }

    /* Helper method to convert filename into "images" database reference */
    private String formatFileName(String fileName) {
        return String.format(
                "%s/%s", Constants.FirebaseStorageKeys.KEY_IMAGES, fileName
        );
    }
}
