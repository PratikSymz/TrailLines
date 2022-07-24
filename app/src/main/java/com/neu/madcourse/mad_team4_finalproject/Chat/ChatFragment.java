package com.neu.madcourse.mad_team4_finalproject.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ChatListAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.FragmentChatBinding;
import com.neu.madcourse.mad_team4_finalproject.models.ChatModel;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private FragmentChatBinding mBinding;
    // creating a base utils object for toast
    private BaseUtils mBaseUtils;
    // declaring chat list recycler view
    private RecyclerView chatListRecyclerView;
    // declaring chatListAdapter class
    private ChatListAdapter chatListAdapter;
    // declaring List of chatModel
    private List<ChatModel> chatModelList;
    // declaring textView to show an emptyChatList content;
    private TextView emptyChatTextView;
    // creating a database reference object for chats and users
    private DatabaseReference chatDatabaseReference, userDatabaseReference;
    // creating a firebaseUser class to get current user information
    private FirebaseUser currentUser;
    // Creating a child event listener object to refresh the list everytime a new message is sent
    private ChildEventListener childEventListener;
    // Creating a query object for firebase
    private Query query;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        mBinding = FragmentChatBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatModelList = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(chatModelList, getActivity());
        /**
         * creating a new linear layout manager and reversing the layout
         * Reversing the layout so that the last message sent will be the first message seen in the view
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // reversing the layout so that last message sent will be seen as first
        linearLayoutManager.setReverseLayout(true);
        // setting the stack from end as true
        linearLayoutManager.setStackFromEnd(true);
        mBinding.chatRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.chatRecyclerview.setAdapter(chatListAdapter);
        // connecting firebase
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.KEY_TLO);
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.UserKeys.Chats.KEY_TLO);
        query = chatDatabaseReference.orderByChild(Constants.UserKeys.Chats.TIME_STAMP);
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

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        // adding the child listener to the query
        query.addChildEventListener(childEventListener);
        mBinding.emptyChatAppearence.setVisibility(View.VISIBLE);
        mBinding.chatProgressBar.getRoot().setVisibility(View.VISIBLE);

    }

    /**
     * A helper method to update the child event listener
     * This method will set the recycler view adapter with new chatModelList and check for message record as well
     *
     * @param ds
     * @param newRecord
     * @param userID
     */
    private void updateChatList(DataSnapshot ds, boolean newRecord, String userID) {
        mBinding.emptyChatAppearence.setVisibility(View.INVISIBLE);
        mBinding.chatProgressBar.getRoot().setVisibility(View.INVISIBLE);

        final String lastMessage, lastMessageTime, unreadMessageCount;
        // TODO: these strings data structures are updated later
        lastMessage = "";
        lastMessageTime = "";
        unreadMessageCount = "";

        userDatabaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_FIRST_NAME +
                        Constants.UserKeys.PersonalInfoKeys.KEY_LAST_NAME).getValue()).toString();
                String photo = Objects.requireNonNull(snapshot.child(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL)
                        .getValue()).toString();
                // adding these data to the chatModelList
                ChatModel chatModel = new ChatModel(userID, name, photo, unreadMessageCount, lastMessage, lastMessageTime);
                chatModelList.add(chatModel);
                chatListAdapter.notifyDataSetChanged();
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
        query.removeEventListener(childEventListener);
    }
}