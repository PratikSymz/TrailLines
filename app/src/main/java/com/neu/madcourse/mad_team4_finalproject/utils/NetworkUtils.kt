package com.neu.madcourse.mad_team4_finalproject.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject

class NetworkUtils(context: Context) {
    /* The Activity context using the utilities */
    private val mContext: Context = context
    private lateinit var rootReference: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private val tokenHashMap = HashMap<String, String>()
    private lateinit var mBaseUtils: BaseUtils
    private lateinit var notification: JSONObject
    private lateinit var notificationData: JSONObject


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
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        rootReference = FirebaseDatabase.getInstance().reference
        databaseReference = rootReference.child(Constants.Tokens.KEY_TLO).child(firebaseUser.uid)
        tokenHashMap[Constants.Tokens.DEVICE_TOKEN] = token
        databaseReference.setValue(tokenHashMap).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                mBaseUtils.showToast("Failed to save device token!", Toast.LENGTH_SHORT)
            }
        }
    }

    /* Method to send notifications whenever a message or friend request is sent from the chat */
    fun sendNotifications(context: Context, title: String, message: String, userId: String) {
        rootReference = FirebaseDatabase.getInstance().reference
        databaseReference = rootReference.child(Constants.Tokens.KEY_TLO).child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(Constants.Tokens.DEVICE_TOKEN).value != null) {
                    val deviceToken: String =
                        snapshot.child(Constants.Tokens.DEVICE_TOKEN).value.toString()

                    try {
                        notificationData.put(Constants.NOTIFICATION_TITLE, title)
                        notificationData.put(Constants.NOTIFICATION_MESSAGE, message)

                        notification.put(Constants.NOTIFICATION_TO, deviceToken)
                        notification.put(Constants.NOTIFICATION_DATA, notificationData)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                error.message
            }

        })
    }

}