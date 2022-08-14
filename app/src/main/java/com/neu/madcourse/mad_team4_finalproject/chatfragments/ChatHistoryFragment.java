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
import com.neu.madcourse.mad_team4_finalproject.models.User;
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
    private ChatHistoryAdapter onlineChatHistoryAdapter;

    // declaring List of chatModel
    private List<ChatHistory> chatHistoryList = new ArrayList<>();
    private List<ChatHistory> onlineChatHistoryList = new ArrayList<>();

    // creating a database reference object for chats and users
    private DatabaseReference chatDatabaseReference, userDatabaseReference;

    // creating a firebaseUser class to get current user information
    private FirebaseUser currentUser;

    // Creating a child event listener object to refresh the list everytime a new message is sent
    private ChildEventListener childEventListener;

    // Creating a query object for firebase
    private Query query;

    // Creating a list of all userId
    List<String> userIDList = new ArrayList<>();
    List<String> onlineUserIDList = new ArrayList<>();

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
        chatHistoryAdapter = new ChatHistoryAdapter(requireActivity(), chatHistoryList);
        onlineChatHistoryAdapter = new ChatHistoryAdapter(requireActivity(), onlineChatHistoryList);
        // Show the progress view while loading data
        mBinding.chatProgressBar.getRoot().setVisibility(View.VISIBLE);

        // creating a new linear layout manager and reversing the layout
        // Reversing the layout so that the last message sent will be the first message seen in the view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // reversing the layout so that last message sent will be seen as first
        linearLayoutManager.setReverseLayout(true);
        // setting the stack from end as true
        linearLayoutManager.setStackFromEnd(true);

        // creating a new linear layout manager and reversing the layout
        // Reversing the layout so that the last message sent will be the first message seen in the view
        LinearLayoutManager linearLayoutManagerOnline = new LinearLayoutManager(getActivity());
        // reversing the layout so that last message sent will be seen as first
        linearLayoutManagerOnline.setReverseLayout(true);
        // setting the stack from end as true
        linearLayoutManagerOnline.setStackFromEnd(true);

        // Set the adapter and the layout manager
        mBinding.recyclerViewHistory.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewHistory.setAdapter(chatHistoryAdapter);

        mBinding.recyclerViewHistoryOnline.setLayoutManager(linearLayoutManagerOnline);
        mBinding.recyclerViewHistoryOnline.setAdapter(onlineChatHistoryAdapter);

        // connecting firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.UserKeys.KEY_TLO);
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.ChatKeys.KEY_TLO)
                .child(currentUser.getUid());

        query = chatDatabaseReference.orderByChild(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP); // TODO: Check if it works
        chatDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0) {
                    mBinding.recyclerViewHistoryOnline.setVisibility(View.INVISIBLE);
                    mBinding.labelNoneOnline.setVisibility(View.VISIBLE);

                    mBinding.recyclerViewHistory.setVisibility(View.INVISIBLE);
                    mBinding.labelNoChats.setVisibility(View.VISIBLE);
                } else {
                    mBinding.recyclerViewHistoryOnline.setVisibility(View.VISIBLE);
                    mBinding.labelNoneOnline.setVisibility(View.INVISIBLE);

                    mBinding.recyclerViewHistory.setVisibility(View.VISIBLE);
                    mBinding.labelNoChats.setVisibility(View.INVISIBLE);
                }

                // Hide the progress bar
                mBinding.chatProgressBar.getRoot().setVisibility(View.INVISIBLE);

                for (DataSnapshot child : snapshot.getChildren()) {
                    updateChatList(snapshot, false, child.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mBaseUtils.showToast("Cannot load chats at the moment!", Toast.LENGTH_SHORT);
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
        if (dataSnapshot.child(userID).child(Constants.ChatKeys.MessageKeys.LAST_MESSAGE).getValue() != null) {
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
                        // Get the user information
                        User user = snapshot.getValue(User.class);
                        assert user != null;

                        // adding these data to the chatModelList
                        ChatHistory chatHistory = new ChatHistory(userID, user.getName(), user.getProfilePictureFileName(), unreadMessageCount, lastMessage, lastMessageTime);
                        /* wrote a conditional statement to update list only its isNew record then add directly */
                        if (isNewRecord) {
                            if (user.isOnline()) {
                                if (!onlineChatHistoryList.contains(chatHistory)) {
                                    onlineChatHistoryList.add(chatHistory);
                                    onlineUserIDList.add(userID);
                                }
                            } else {
                                if (!chatHistoryList.contains(chatHistory)) {
                                    chatHistoryList.add(chatHistory);
                                    userIDList.add(userID);
                                }
                            }
                        } else {
                            if (user.isOnline()) {
                                if (onlineChatHistoryList.size() > 0){
                                    if (onlineUserIDList.contains(userID)) {
                                        onlineChatHistoryList.set(onlineUserIDList.indexOf(userID), chatHistory);
                                    }
                                }
                            } else {
                                if (chatHistoryList.size() > 0) {
                                    if (userIDList.contains(userID)) {
                                        chatHistoryList.set(userIDList.indexOf(userID), chatHistory);
                                    }
                                }
                            }
                        }

                        if (chatHistoryList.size() > 0) {
                            mBinding.recyclerViewHistory.setVisibility(View.VISIBLE);
                            mBinding.labelNoChats.setVisibility(View.INVISIBLE);
                        } else {
                            mBinding.recyclerViewHistory.setVisibility(View.INVISIBLE);
                            mBinding.labelNoChats.setVisibility(View.VISIBLE);
                        }

                        if (onlineChatHistoryList.size() > 0) {
                            mBinding.recyclerViewHistoryOnline.setVisibility(View.VISIBLE);
                            mBinding.labelNoneOnline.setVisibility(View.INVISIBLE);
                        } else {
                            mBinding.recyclerViewHistoryOnline.setVisibility(View.INVISIBLE);
                            mBinding.labelNoneOnline.setVisibility(View.VISIBLE);
                        }

                        chatHistoryAdapter.updateDataList(chatHistoryList);
                        onlineChatHistoryAdapter.updateDataList(onlineChatHistoryList);

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