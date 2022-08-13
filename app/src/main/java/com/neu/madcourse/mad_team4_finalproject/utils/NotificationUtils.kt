package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.neu.madcourse.mad_team4_finalproject.retrofit.NotificationEndpoint
import com.neu.madcourse.mad_team4_finalproject.retrofit.NotificationInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class NotificationUtils(context: Context) {
    /* The Log tag */
    private val LOG_TAG = NotificationUtils::class.java.simpleName

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* The Firebase Auth reference */
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /* The Firebase User reference */
    private val mFirebaseUser: FirebaseUser = mFirebaseAuth.currentUser!!

    /* The Firebase Root database reference */
    private var mRootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    /* The Firebase Token database reference */
    private var mTokenRef: DatabaseReference =
        mRootRef.child(Constants.Tokens.KEY_TLO).child(mFirebaseUser.uid)

    /* The Token map reference */
    private val mTokenMap = HashMap<String, String>()

    /* The Notification object reference */
    private lateinit var mNotification: JSONObject

    /* The Notification meta-data object reference */
    private lateinit var mNotificationData: JSONObject

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Notification endpoint reference */
    private val mEndpoint: NotificationEndpoint = NotificationInterceptor().interceptor

    /* Method to update device token when user sign in or sign out */
    fun updateDeviceToken(context: Context, token: String) {
        mTokenMap[Constants.Tokens.DEVICE_TOKEN] = token
        mTokenRef.setValue(mTokenMap).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                mBaseUtils.showToast("Failed to save device token!", Toast.LENGTH_SHORT)
            }
        }
    }

    /* Method to send notifications whenever a message or friend request is sent from the chat */
    fun sendNotification(context: Context, title: String, message: String, userId: String) {
        // Get the new token database reference
        mTokenRef = mRootRef.child(Constants.Tokens.KEY_TLO).child(userId)
        mTokenRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(Constants.Tokens.DEVICE_TOKEN).value != null) {
                    val deviceToken: String =
                        snapshot.child(Constants.Tokens.DEVICE_TOKEN).value.toString()

                    try {
                        mNotificationData.put(Constants.NOTIFICATION_TITLE, title)
                        mNotificationData.put(Constants.NOTIFICATION_MESSAGE, message)

                        mNotification.put(Constants.NOTIFICATION_TO, deviceToken)
                        mNotification.put(Constants.NOTIFICATION_DATA, mNotificationData)

                        // Send the notification
                        mEndpoint.sendNotification(mNotification)
                            .enqueue(object : retrofit2.Callback<JSONObject> {
                                override fun onResponse(
                                    call: Call<JSONObject>,
                                    response: Response<JSONObject>
                                ) {
                                    mBaseUtils.showToast(
                                        "Notification sent successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                }

                                override fun onFailure(
                                    call: Call<JSONObject>,
                                    throwable: Throwable
                                ) {
                                    mBaseUtils.showToast(
                                        "Failed to send notification",
                                        Toast.LENGTH_SHORT
                                    )
                                }
                            })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(LOG_TAG, error.message)
            }
        })
    }

    /**
     * function to update chat details in firebase and also implement unread message count
     * chat user is another user and current user is me
     */
    fun updateChatDetails(context: Context, currentUserId: String, chatUserID: String, lastMessage: String) {
        mRootRef = FirebaseDatabase.getInstance().reference
        var chatRef: DatabaseReference =
            mRootRef.child(Constants.ChatKeys.KEY_TLO).child(chatUserID)
                .child(currentUserId)

        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var currentCount: String = "0"
                if (snapshot.child(Constants.ChatKeys.MessageKeys.UNREAD_COUNT).value != null) {
                    currentCount =
                        snapshot.child(Constants.ChatKeys.MessageKeys.UNREAD_COUNT).value.toString()
                    val chatMap = HashMap<String, Any>()
                    chatMap.put(
                        Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP,
                        ServerValue.TIMESTAMP
                    )
                    chatMap.put(
                        Constants.ChatKeys.MessageKeys.UNREAD_COUNT,
                        Integer.parseInt(currentCount) + 1
                    )
                    chatMap.put(Constants.ChatKeys.MessageKeys.LAST_MESSAGE, lastMessage)

                    chatMap.put(
                        Constants.ChatKeys.MessageKeys.LAST_MESSAGE_TIME,
                        ServerValue.TIMESTAMP
                    )

                    val chatUserMap = HashMap<String, Any>()
                    chatUserMap.put(
                        Constants.ChatKeys.KEY_TLO + "/" + chatUserID + "/"
                                + currentUserId, chatMap.toString()
                    )
                    /* Update the children in the database rootRef with chatUserMap */
                    mRootRef.updateChildren(
                        chatUserMap,
                        DatabaseReference.CompletionListener() { databaseError: DatabaseError?, databaseReference: DatabaseReference ->
                            if (databaseError != null) {
                                mBaseUtils.showToast("Something went wrong", Toast.LENGTH_SHORT)

                            }
                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                mBaseUtils.showToast("Something went wrong", Toast.LENGTH_SHORT)
            }
        })
    }

    /**
     * method to implement the time ago
     */
    fun getTimeAgo(time: Long): String {
        var mTime: Long = time
        val secondMillis = 1000;
        val minuteMillis: Int = 60 * secondMillis;
        val hourMillis: Int = 60 * minuteMillis;
        val dayMillis: Int = 60 * hourMillis;
        mTime *= 1000
        var now: Long = System.currentTimeMillis()
        if (mTime > now || mTime <= 0) {
            return ""
        }
        val difference: Long = now - mTime
        return if (difference < minuteMillis) {
            "just now"
        } else if (difference < 2 * minuteMillis) {
            "a minute ago"
        } else if (difference < 59 * minuteMillis) {
            difference.div(minuteMillis).toString() + " minutes ago"
        } else if (difference < 90 * minuteMillis) {
            "an hour ago"
        } else if (difference < 24 * hourMillis) {
            difference.div(hourMillis).toString() + " hours ago"
        } else if (difference < 48 * hourMillis) {
            "yesterday"
        } else {
            difference.div(dayMillis).toString() + " days ago"
        }
    }
}