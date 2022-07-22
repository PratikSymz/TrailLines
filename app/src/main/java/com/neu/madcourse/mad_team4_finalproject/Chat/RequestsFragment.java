package com.neu.madcourse.mad_team4_finalproject.Chat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.FriendRequestAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentRequestsBinding;
import com.neu.madcourse.mad_team4_finalproject.models.FriendRequest;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment {
    // creating a binding view variable
    private FragmentRequestsBinding mBinding;
    private FriendRequestAdapter friendRequestAdapter;
    private List<FriendRequest> friendRequestList;
    // creating an object to get user friend request from firebase and profile picture
    private DatabaseReference databaseReferenceRequest, databaseReferenceUsersPhoto;
    // created a firebase user object to get the current user
    private FirebaseUser currentUser;
    // creating a base utils object for toast
    private BaseUtils mBaseUtils;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRequestsBinding.inflate(inflater, container, false);
        // binding the widget set
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.requestRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendRequestList = new ArrayList<>();
        friendRequestAdapter = new FriendRequestAdapter(getActivity(), friendRequestList);
        mBinding.requestRecyclerview.setAdapter(friendRequestAdapter);

        // getting the current user using the application through firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //TODO: Run this code by Pratik
        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO).child(currentUser.getUid())
                .child(Constants.UserKeys.FriendRequests.KEY_TLO);
        mBinding.friendRequestAppearenceTextview.setVisibility(View.VISIBLE);
        mBinding.progressbar.getRoot().setVisibility(View.VISIBLE);
        databaseReferenceRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                friendRequestList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        String requestStatus = Objects.requireNonNull(dataSnapshot
                                .child(Constants.UserKeys.FriendRequests.KEY_REQUEST_STATUS).getValue()).toString();
                        if (requestStatus.equals(Constants.REQUEST_STATUS_SENT)) {
                            String userID = dataSnapshot.getKey();
                            databaseReferenceRequest.child(Objects.requireNonNull(userID)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                    String currentUserName = Objects.requireNonNull(datasnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_FIRST_NAME).getValue()).toString()
                                            + Objects.requireNonNull(datasnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_LAST_NAME).getValue()).toString();
                                    String profilePicture = "";
                                    if (dataSnapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL).getValue() != null) {
                                      FriendRequest friendRequest = new FriendRequest(userID, currentUserName, profilePicture);
                                      friendRequestList.add(friendRequest);
                                      friendRequestAdapter.notifyDataSetChanged();
                                      mBinding.friendRequestAppearenceTextview.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                                    mBaseUtils.showToast(getString(R.string.failed_friend_request, error.getMessage()), Toast.LENGTH_SHORT);
                                    mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
                mBaseUtils.showToast(getString(R.string.failed_friend_request, error.getMessage()), Toast.LENGTH_SHORT);
                mBinding.progressbar.getRoot().setVisibility(View.INVISIBLE);
            }
        });
    }
}