package com.neu.madcourse.mad_team4_finalproject.chatfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.neu.madcourse.mad_team4_finalproject.adapters.FindFriendAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentFindFriendsBinding;
import com.neu.madcourse.mad_team4_finalproject.models.FindFriend;
import com.neu.madcourse.mad_team4_finalproject.models.User;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Finding hiking user for message request fragment screen
 * Shows the list of publicly available users on the app to send a message request to
 */
public class FindFriendsFragment extends Fragment {
    /* The Fragment Log Tag */
    private final String LOG_TAG = FindFriendsFragment.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Fragment layout view binding reference */
    private FragmentFindFriendsBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Network utils reference */
    private NetworkUtils mNetworkUtils;

    /* The Firebase Database reference -> "users" */
    private DatabaseReference mUsersDatabaseRef;

    /* The Firebase Database reference -> "users"/userID/"friend_requests" */
    private DatabaseReference mFriendRequestsDatabaseRef;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Recycler view adapter reference */
    private FindFriendAdapter mAdapter;

    /* The find friends instance list reference */
    private Set<FindFriend> mFindFriendsList = new HashSet<>();
    ;

    /* Required empty public constructor */
    public FindFriendsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the activity context
        mContext = getActivity();

        // Inflate the layout for this fragment
        mBinding = FragmentFindFriendsBinding.inflate(inflater, container, false);
        // Return the root view
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the Network utils reference
        mNetworkUtils = new NetworkUtils(mContext);

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Instantiate the firebase database reference -> "user"
        mUsersDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO);

        // Instantiate the firebase database reference -> "users"/userID/"friend_requests"
        mFriendRequestsDatabaseRef = mUsersDatabaseRef
                .child(mFirebaseUser.getUid())
                .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO);

        // Set the recycler view parameters
        mBinding.viewRequestRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // Instantiate the recycler view adapter
        mAdapter = new FindFriendAdapter(mContext, new ArrayList<>(mFindFriendsList), position -> {
            // Remove the user from the find friends list
//            mFindFriendsList.remove(position);
//            mAdapter.updateDataList(mFindFriendsList);
        });

        // Set the adapter to the recycler view
        mBinding.viewRequestRecyclerView.setAdapter(mAdapter);
        // Hide the recycler view
        mBinding.viewRequestRecyclerView.setVisibility(View.INVISIBLE);

        // Hide the find friends label
        mBinding.labelFindFriends.setVisibility(View.INVISIBLE);

        // Show the progress screen
        mBinding.viewProgressBar.getRoot().setVisibility(View.VISIBLE);

        // Retrieve the list of users from the firebase server and update the recycler view
        initFindFriendsList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initFindFriendsList() {
        // Query the list of publicly available users on the firebase server
        Query userQuery = mUsersDatabaseRef.orderByChild(Constants.UserKeys.PersonalInfoKeys.KEY_NAME);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /* snapshot -> contains list of all the users */
                // Clear the existing list
                mFindFriendsList.clear();

                // Iterate through all the users
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Extract the user's ID
                    String userID = userSnapshot.getKey();
                    assert userID != null;

                    // The PersonalInfo snapshot
                    DataSnapshot personalInfoSnapshot = userSnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO);
                    // Get the user information
                    User user = personalInfoSnapshot.getValue(User.class);
                    assert (user != null);

                    // If userID is the same as the currently logged on user's ID
                    if (userID.equals(mFirebaseUser.getUid())) {
                        continue;
                    }

                    // If the user has set the private profile status as true
                    if (user.isPrivateProfile()) {
                        continue;
                    }

                    // Check if the user has already responded to or sent the friend request from or to the logged in user
                    String requestStatus = userSnapshot
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                            .child(mFirebaseUser.getUid())
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                            .getValue(String.class);

                    // Check if the logged in user has sent a request before
                    if (requestStatus != null) {
                        // If responded before or sent request before, skip the user
                        if (requestStatus.contains(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_ACCEPTED)
                                || requestStatus.contains(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_DENIED)
                                || requestStatus.contains(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_SENT)) {
                            continue;
                        }
                    }

                    // Check the currently existing friend request to extract their request status
                    mFriendRequestsDatabaseRef.child(userID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // A find friend instance
                                    FindFriend findFriend;

                                    if (snapshot.exists()) {
                                        String requestStatus = Objects.requireNonNull(
                                                snapshot.child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS).getValue()
                                        ).toString();

                                        // Check if the request status is "sent"
                                        if (requestStatus.equals(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_SENT)) {
                                            // Create the new FindFriend model reference
                                            findFriend = new FindFriend(user.getName(), user.getProfilePictureFileName(), userID, true);
                                        } else {
                                            // Create the new FindFriend model reference
                                            findFriend = new FindFriend(user.getName(), user.getProfilePictureFileName(), userID, false);
                                        }
                                    } else {

                                        /* There is no FriendRequest directory under the userID, so create a new model instance with "isRequestSent" as False */
                                        findFriend = new FindFriend(user.getName(), user.getProfilePictureFileName(), userID, false);
                                    }

                                    // Add to the list
                                    mFindFriendsList.add(findFriend);

                                    // Update the adapter
                                    mAdapter.updateDataList(new ArrayList<>(mFindFriendsList));

                                    if (mFindFriendsList.size() > 0) {
                                        // Show the recycler view
                                        mBinding.viewRequestRecyclerView.setVisibility(View.VISIBLE);

                                        // Hide the find friends label
                                        mBinding.labelFindFriends.setVisibility(View.INVISIBLE);
                                    } else {
                                        // Hide the recycler view
                                        mBinding.viewRequestRecyclerView.setVisibility(View.INVISIBLE);

                                        // Hide the find friends label
                                        mBinding.labelFindFriends.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Hide the progress bar
                                    mBaseUtils.showToast("Cannot load friends list at the moment!", Toast.LENGTH_SHORT);
                                }
                            });

                    // Hide the progress screen
                    mBinding.viewProgressBar.getRoot().setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hide the progress screen
                mBinding.viewProgressBar.getRoot().setVisibility(View.INVISIBLE);

                // Show a toast message
                mBaseUtils.showToast(
                        String.format("Failed to fetch friends list: %s", error.getMessage()),
                        Toast.LENGTH_SHORT
                );
            }
        });
    }
}