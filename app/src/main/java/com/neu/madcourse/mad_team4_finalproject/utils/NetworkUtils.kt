package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

class NetworkUtils(context: Context) {
    /* The Log tag */
    private val LOG_TAG = NetworkUtils::class.java.simpleName

    /* The Activity context using the utilities */
    private val mContext: Context = context

    /* The Firebase Auth reference */
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /* The Firebase User reference */
    private val mFirebaseUser: FirebaseUser = mFirebaseAuth.currentUser!!

    /* The Firebase Root database reference */
    private val mRootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

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

    /* Helper method to check whether the user has an active internet connection */
    fun isConnected(): Boolean {
        // Get the connectivity manager reference
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the network capabilities reference
        val networkCapabilities = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && isOnline(networkCapabilities)
    }

    /* Helper method to check whether the user has active network adapters */
    private fun isOnline(networkCapabilities: NetworkCapabilities): Boolean {
        return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    }

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
    fun sendNotifications(context: Context, title: String, message: String, userId: String) {
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
                        mEndpoint.sendNotification(mNotification).enqueue(object : retrofit2.Callback<JSONObject> {
                            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                                mBaseUtils.showToast(
                                    "Notification sent successfully",
                                    Toast.LENGTH_SHORT
                                )
                            }

                            override fun onFailure(call: Call<JSONObject>, throwable: Throwable) {
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

}