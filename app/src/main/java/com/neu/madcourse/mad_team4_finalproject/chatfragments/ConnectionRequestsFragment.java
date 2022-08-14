package com.neu.madcourse.mad_team4_finalproject.chatfragments;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.ValueEventListener;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ConnectionRequestAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentConnectionRequestsBinding;
import com.neu.madcourse.mad_team4_finalproject.models.ConnectionRequest;
import com.neu.madcourse.mad_team4_finalproject.models.User;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: CHECK WITH AMIT
 */
public class ConnectionRequestsFragment extends Fragment {
    // creating a binding view variable
    private FragmentConnectionRequestsBinding mBinding;

    private ConnectionRequestAdapter connectionRequestAdapter;

    private List<ConnectionRequest> connectionRequestList = new ArrayList<>();
    ;

    // creating an object to get user friend request from firebase and profile picture
    private DatabaseReference databaseReferenceRequest, databaseReferenceUsers;

    // created a firebase user object to get the current user
    private FirebaseUser currentUser;

    // creating a base utils object for toast
    private BaseUtils mBaseUtils;

    public ConnectionRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentConnectionRequestsBinding.inflate(inflater, container, false);
        // binding the widget set
        return mBinding.getRoot();
    }

    @Override
    @SuppressLint("NotifyDataSetChanged")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.recyclerViewConnectionRequests.setLayoutManager(new LinearLayoutManager(getActivity()));

        connectionRequestAdapter = new ConnectionRequestAdapter(getActivity(), connectionRequestList, position -> {
            // Remove item from the friendRequestList and update the adapter
            connectionRequestList.remove(position);
            connectionRequestAdapter.notifyItemRemoved(position);
        });

        mBinding.recyclerViewConnectionRequests.setAdapter(connectionRequestAdapter);

        mBaseUtils = new BaseUtils(requireActivity());

        // getting the current user using the application through firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO)
                .child(currentUser.getUid())
                .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO);

        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO);

        mBinding.recyclerViewConnectionRequests.setVisibility(View.INVISIBLE);
        mBinding.labelConnectionRequests.setVisibility(View.INVISIBLE);
        mBinding.viewConnectionRequestsProgress.getRoot().setVisibility(View.VISIBLE);

        databaseReferenceRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                connectionRequestList.clear();
                mBinding.recyclerViewConnectionRequests.setVisibility(View.INVISIBLE);
                mBinding.labelConnectionRequests.setVisibility(View.VISIBLE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        String userID = dataSnapshot.getKey();
                        assert (userID != null);

                        String requestStatus = dataSnapshot
                                .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                                .getValue(String.class);
                        assert (requestStatus != null);

                        // If the request RECEIVED from other user
                        if (requestStatus.contains(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_RECEIVED)) {
                            databaseReferenceUsers
                                    .child(userID)
                                    .child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot personalInfoSnapshot) {
                                            // Get the user information
                                            User user = personalInfoSnapshot.getValue(User.class);
                                            assert user != null;

                                            ConnectionRequest connectionRequest;
                                            if (!mBaseUtils.isEmpty(user.getProfilePictureFileName())) {
                                                connectionRequest = new ConnectionRequest(userID,
                                                        user.getName(),
                                                        user.getProfilePictureFileName());
                                            } else {
                                                connectionRequest = new ConnectionRequest(userID, user.getName(), "");
                                            }

                                            connectionRequestList.add(connectionRequest);
                                            connectionRequestAdapter.updateDataList(connectionRequestList);

                                            if (connectionRequestList.size() > 0) {
                                                mBinding.recyclerViewConnectionRequests.setVisibility(View.VISIBLE);
                                                mBinding.labelConnectionRequests.setVisibility(View.INVISIBLE);
                                            } else {
                                                mBinding.recyclerViewConnectionRequests.setVisibility(View.INVISIBLE);
                                                mBinding.labelConnectionRequests.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mBaseUtils.showToast(getString(R.string.failed_friend_request, error.getMessage()), Toast.LENGTH_SHORT);
                                        }
                                    });
                        }
                    } else {
                        /* There is not FriendRequest directory. Pass an empty list */
                        mBinding.recyclerViewConnectionRequests.setVisibility(View.INVISIBLE);
                        mBinding.labelConnectionRequests.setVisibility(View.VISIBLE);
                    }
                }

                mBinding.viewConnectionRequestsProgress.getRoot().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mBaseUtils.showToast(getString(R.string.failed_friend_request, error.getMessage()), Toast.LENGTH_SHORT);
                mBinding.viewConnectionRequestsProgress.getRoot().setVisibility(View.INVISIBLE);
            }
        });
    }
}