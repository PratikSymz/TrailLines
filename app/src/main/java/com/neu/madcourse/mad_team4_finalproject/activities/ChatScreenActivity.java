package com.neu.madcourse.mad_team4_finalproject.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.adapters.ConversationAdapter;
import com.neu.madcourse.mad_team4_finalproject.databinding.ActivityChatScreenBinding;
import com.neu.madcourse.mad_team4_finalproject.models.Conversation;
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils;
import com.neu.madcourse.mad_team4_finalproject.utils.Constants;
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatScreenActivity extends AppCompatActivity {
    /* The Activity Log Tag */
    private final String LOG_TAG = ChatScreenActivity.class.getSimpleName();

    /* The Activity context */
    private Context mContext;

    /* The Activity layout view binding reference */
    private ActivityChatScreenBinding mBinding;

    /* The Base utils reference */
    private BaseUtils mBaseUtils;

    /* The Network utils reference */
    private NetworkUtils mNetworkUtils;

    /* The Firebase Database reference */
    private DatabaseReference mRootDatabaseRef;

    /* The Firebase Database reference -> "messages" */
    private DatabaseReference mMessageDatabaseRef;

    /* The Firebase Auth service reference */
    private FirebaseAuth mFirebaseAuth;

    /* The Firebase User reference */
    private FirebaseUser mFirebaseUser;

    /* The Other user's ID reference */
    private String mOtherUserID;

    /* The Conversation adapter instance */
    private ConversationAdapter mAdapter;

    /* The Conversation model list instance */
    private List<Conversation> mConversationList = new ArrayList<>();

    /* The Conversation Child listener */
    private ChildEventListener mConversationListener;

    /* The current page instance */
    private int mCurrentPage = 1;

    /* The conversation list limit per page */
    private final int RECORDS_PER_PAGE_LIMIT = 30;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the activity context
        mContext = this;

        // Extract the other user's ID from the intent
        if (getIntent().hasExtra("user_id")) {
            mOtherUserID = getIntent().getStringExtra("user_id");
        }

        // Instantiate the activity layout view binding
        mBinding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        // Set the layout root view
        setContentView(mBinding.getRoot());

        // Instantiate the Base utils reference
        mBaseUtils = new BaseUtils(mContext);

        // Instantiate the Network utils reference
        mNetworkUtils = new NetworkUtils(mContext);

        // Instantiate the firebase database reference - "users"
        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();

        // Instantiate the firebase auth service reference
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged in user
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        assert mFirebaseUser != null;

        // Add the onClick action for the send message button
        mBaseUtils.applyListener(mBinding.viewSegmentMessage.viewButtonSend, view -> {
            if (view.getId() == mBinding.viewSegmentMessage.viewButtonSend.getId()) {
                // Check for active internet connection
                if (mNetworkUtils.isConnected()) {
                    // Create a message push request on the database reference
                    DatabaseReference messagePushRef = mRootDatabaseRef.child(Constants.ChatKeys.KEY_TLO)
                            .child(mFirebaseUser.getUid())
                            .child(mOtherUserID)
                            .child(Constants.ChatKeys.MessageKeys.KEY_TLO)
                            .push();

                    // Extract the message push ID
                    String pushID = messagePushRef.getKey();

                    // Call the send message method
                    sendMessage(
                            pushID,
                            Objects.requireNonNull(mBinding.viewSegmentMessage.viewMessage.getText()).toString().trim(),
                            Constants.ChatKeys.MessageKeys.DATA_TYPE_TEXT
                    );
                } else {
                    mBaseUtils.showToast(getString(R.string.no_internet), Toast.LENGTH_SHORT);
                }
            }
        });

        // Instantiate the conversation adapter
        mAdapter = new ConversationAdapter(mContext, mConversationList);
        // Set the adapter and layout manager on the recycler view
        mBinding.recyclerViewConversation.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerViewConversation.setAdapter(mAdapter);

        // Load the messages from the server
        loadConversationHistory();
        // Scroll to most recent message
        mBinding.recyclerViewConversation.scrollToPosition(mConversationList.size() - 1);

        // Set the onRefresh action on the scroll view
        mBinding.viewSwipeRefreshConversation.setOnRefreshListener(() -> {
            // Increment current page
            mCurrentPage += 1;
            // reload the next messages
            loadConversationHistory();
        });
    }

    /* Helper method to record the message sent from one user to the other */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void sendMessage(String pushID, String message, String dataType) {
        try {
            if (!mBaseUtils.isEmpty(message)) {
                // Create a hash map of data
                Map<String, Object> dataMap = new HashMap<>();
                // Add the message ID
                dataMap.put(Constants.ChatKeys.MessageKeys.KEY_MESSAGE_ID, pushID);
                // Add the message "from" user ID
                dataMap.put(Constants.ChatKeys.MessageKeys.KEY_FROM, mFirebaseUser.getUid());
                // Add the message content
                dataMap.put(Constants.ChatKeys.MessageKeys.KEY_CONTENT, message);
                // Add the message data type
                dataMap.put(Constants.ChatKeys.MessageKeys.KEY_DATA_TYPE, dataType);
                // Add the message timestamp
                dataMap.put(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP, ServerValue.TIMESTAMP);

                // The current user's database reference -> TODO: Check if it works
                String mCurrentUserRef = String.format("%s/%s/%s/%s/%s",
                        Constants.ChatKeys.KEY_TLO, mFirebaseUser.getUid(), mOtherUserID, Constants.ChatKeys.MessageKeys.KEY_TLO, pushID
                );

                // The other user's database reference -> TODO: Check if it works
                String mOtherUserRef = String.format("%s/%s/%s/%s/%s",
                        Constants.ChatKeys.KEY_TLO, mOtherUserID, mFirebaseUser.getUid(), Constants.ChatKeys.MessageKeys.KEY_TLO, pushID
                );

                // Create a hash map of message ID database references
                Map<String, Object> chatDataMap = new HashMap<>();
                // Add message data for the current user [FORWARD]
                chatDataMap.put(mCurrentUserRef, dataMap);
                // Add message data for the other user [REVERSE]
                chatDataMap.put(mOtherUserRef, dataMap);

                // RESET the chat box text
                mBinding.viewSegmentMessage.viewMessage.setText("");

                // Update the root database reference with the chat object
                mRootDatabaseRef.updateChildren(chatDataMap, (databaseError, reference) -> {
                    if (databaseError != null) {
                        mBaseUtils.showToast(
                                getString(R.string.failed_to_send_message, databaseError.getMessage()),
                                Toast.LENGTH_SHORT
                        );
                    } else {
                        mBaseUtils.showToast(
                                getString(R.string.message_sent_successfully),
                                Toast.LENGTH_SHORT
                        );

                        // Create a notification
                        String title = "New message";
                        mNetworkUtils.sendNotification(mContext, title, message, mOtherUserID);
                    }
                });
            }

        } catch (Exception exception) {
            mBaseUtils.showToast(
                    getString(R.string.failed_to_send_message, exception.getMessage()),
                    Toast.LENGTH_SHORT
            );
        }
    }

    /* Helper method to load the conversation history between the users from the server */
    private void loadConversationHistory() {
        // Clear the current conversation list
        mConversationList.clear();

        // Instantiate the "message" database reference
        mMessageDatabaseRef = mRootDatabaseRef
                .child(Constants.ChatKeys.KEY_TLO)
                .child(mFirebaseUser.getUid())
                .child(mOtherUserID)
                .child(Constants.ChatKeys.MessageKeys.KEY_TLO);

        // Query over the message list
        Query messageQuery = mMessageDatabaseRef.limitToLast(mCurrentPage * RECORDS_PER_PAGE_LIMIT);

        // Attach a listener to the query
        if (mConversationListener != null) {
            // Remove the listener
            messageQuery.removeEventListener(mConversationListener);
        }

        // Initialize the listener
        mConversationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Conversation conversation = snapshot.getValue(Conversation.class);
                // Add to the list
                mConversationList.add(conversation);
                // Update the adapter
                mAdapter.notifyItemInserted(mConversationList.size() - 1);
                // Scroll recycler view to the most recent message
                mBinding.recyclerViewConversation.scrollToPosition(mConversationList.size() - 1);

                // Set the swipe refreshing to FALSE -> Loaded new data
                mBinding.viewSwipeRefreshConversation.setRefreshing(false);
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
                // Set the swipe refreshing to FALSE -> Error
                mBinding.viewSwipeRefreshConversation.setRefreshing(false);
            }
        };

        // Add listener to the query
        messageQuery.addChildEventListener(mConversationListener);
    }
}