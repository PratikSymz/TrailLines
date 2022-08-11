package com.neu.madcourse.mad_team4_finalproject.fragments;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ChatHistoryAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentChatHistoryBinding;
import com.neu.madcourse.mad_team4_finalproject.models.ChatHistory;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class ChatHistoryFragment extends Fragment {
    private FragmentChatHistoryBinding mBinding;

    // creating a base utils object for toast
    private BaseUtils mBaseUtils;

    // declaring chatListAdapter class
    private ChatHistoryAdapter chatHistoryAdapter;

    // declaring List of chatModel
    private List<ChatHistory> chatHistoryList = new ArrayList<>();

    // creating a database reference object for chats and users
    private DatabaseReference chatDatabaseReference, userDatabaseReference;

    // creating a firebaseUser class to get current user information
    private FirebaseUser currentUser;

    // Creating a child event listener object to refresh the list everytime a new message is sent
    private ChildEventListener childEventListener;

    // Creating a query object for firebase
    private Query query;

    // Creating a list of all userId
    List<String> userIDList;

    public ChatHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatHistoryBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Instantiate the adapter
        chatHistoryAdapter = new ChatHistoryAdapter(requireActivity(), new ArrayList<>(chatHistoryList));
        // Show the progress view while loading data
        mBinding.chatProgressBar.getRoot().setVisibility(View.VISIBLE);

        // creating a new linear layout manager and reversing the layout
        // Reversing the layout so that the last message sent will be the first message seen in the view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // reversing the layout so that last message sent will be seen as first
        linearLayoutManager.setReverseLayout(true);
        // setting the stack from end as true
        linearLayoutManager.setStackFromEnd(true);

        //Initiating the userIDList
        userIDList = new ArrayList<>();

        // Set the adapter and the layout manager
        mBinding.recyclerViewHistory.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewHistory.setAdapter(chatHistoryAdapter);

        // connecting firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.KEY_TLO);
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.ChatKeys.KEY_TLO).child(currentUser.getUid());
        // query = chatDatabaseReference.orderByChild(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP); // TODO: Check if it works
        chatDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    // Hide the progress bar
                    mBinding.chatProgressBar.getRoot().setVisibility(View.INVISIBLE);
                }

                for (DataSnapshot child : snapshot.getChildren()) {
                    updateChatList(snapshot, true, child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // declaring child listener to make sure fragment is destroyed when we are not using
        // and reopened when we use it
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // calling the helper method
                updateChatList(snapshot, true, snapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // changed the value of isNewRecord to false here
                updateChatList(snapshot, false, snapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                updateChatList(snapshot, true, snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                updateChatList(snapshot, true, snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // adding the child listener to the query
        query.addChildEventListener(childEventListener);
    }

    /**
     * A helper method to update the child event listener
     * This method will set the recycler view adapter with new chatModelList and check for message record as well
     *
     * @param dataSnapshot
     * @param isNewRecord
     * @param userID
     */
    @SuppressLint("NotifyDataSetChanged")
    private void updateChatList(DataSnapshot dataSnapshot, boolean isNewRecord, String userID) {
        mBinding.chatProgressBar.getRoot().setVisibility(View.INVISIBLE);

        final String lastMessage, lastMessageTime, unreadMessageCount;
        // TODO: these strings data structures are updated later
        // updating the last message - if its not null get the value or else set to null
        if (dataSnapshot.child(Constants.ChatKeys.MessageKeys.LAST_MESSAGE).getValue() != null) {
            lastMessage = Objects.requireNonNull(dataSnapshot.child(Constants.ChatKeys.MessageKeys.LAST_MESSAGE).getValue()).toString();
        } else {
            lastMessage = "";
        }
        // same thing for this one as well
        if (dataSnapshot.child(Constants.ChatKeys.MessageKeys.LAST_MESSAGE_TIME).getValue() != null) {
            lastMessageTime = Objects.requireNonNull(dataSnapshot.child(Constants.ChatKeys.MessageKeys.LAST_MESSAGE_TIME).getValue()).toString();
        } else {
            lastMessageTime = "";
        }

        unreadMessageCount = dataSnapshot.child(Constants.ChatKeys.MessageKeys.UNREAD_COUNT)
                .getValue() == null ? "0" :
                Objects.requireNonNull(dataSnapshot.child(Constants.ChatKeys.MessageKeys.UNREAD_COUNT)
                        .getValue()).toString();

        userDatabaseReference.child(userID).child(Constants.UserKeys.PersonalInfoKeys.KEY_TLO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = Objects.requireNonNull(snapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_NAME).getValue()).toString();
                        String photo = Objects.requireNonNull(snapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL)
                                .getValue()).toString();

                        // adding these data to the chatModelList
                        ChatHistory chatHistory = new ChatHistory(userID, name, photo, unreadMessageCount, lastMessage, lastMessageTime);
                        /* wrote a conditional statement to update list only its isNew record then add directly */
                        if (isNewRecord) {
                            chatHistoryList.add(chatHistory);
                            userIDList.add(userID);
                        } else {
                            //TODO ask Pratik about this!
                            int clickedUser = userIDList.indexOf(userID);
                            chatHistoryList.set(clickedUser, chatHistory);
                        }
                        chatHistoryAdapter.updateDataList(chatHistoryList);
                        chatHistoryAdapter.notifyDataSetChanged();
                        mBinding.chatProgressBar.getRoot().setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mBaseUtils.showToast(getString(R.string.failed_chat_list, error.getMessage()), Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * Over ride method called to stop listening to child listener
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (query != null) query.removeEventListener(childEventListener);
    }
}