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
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<FindFriend> mFindFriendsList;

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

        // Instantiate the find friends list
        mFindFriendsList = new ArrayList<>();

        // Set the recycler view parameters
        mBinding.viewRequestRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.viewRequestRecyclerView.setEmptyView(mBinding.labelFindFriends);
        // Instantiate the recycler view adapter
        mAdapter = new FindFriendAdapter(mContext, mFindFriendsList, position -> {
            // Remove the user from the find friends list
            mFindFriendsList.remove(position);
            mAdapter.updateDataList(mFindFriendsList);
        });

        // TODO: Fix this
        // Set the adapter to the recycler view
        mBinding.viewRequestRecyclerView.setAdapter(mAdapter);

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

                    // If userID is the same as the currently logged on user's ID
                    if (userID.equals(mFirebaseUser.getUid())) {
                        continue;
                    }

                    // Check if the name field is not null
                    if (personalInfoSnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_NAME).getValue() != null) {
                        // Extract the user's fields
                        String name = Objects.requireNonNull(personalInfoSnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_NAME).getValue()).toString();
                        String profileImageUrl = Objects.requireNonNull(personalInfoSnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL).getValue()).toString();

                        // Check the currently existing friend request to extract their request status
                        mFriendRequestsDatabaseRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        findFriend = new FindFriend(name, profileImageUrl, userID, true);
                                    } else {
                                        // Create the new FindFriend model reference
                                        findFriend = new FindFriend(name, profileImageUrl, userID, false);
                                    }
                                } else {
                                    /* There is not FriendRequest directory under the userID, so
                                    create a new model instance with "isRequestSent" as False */
                                    findFriend = new FindFriend(name, profileImageUrl, userID, false);
                                }

                                // Add to the list
                                mFindFriendsList.add(findFriend);

                                // Update the adapter
                                mAdapter.updateDataList(mFindFriendsList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Hide the progress bar
                                mBinding.viewProgressBar.getRoot().setVisibility(View.INVISIBLE);
                            }
                        });

                        // Hide the progress screen
                        mBinding.viewProgressBar.getRoot().setVisibility(View.INVISIBLE);
                    }
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