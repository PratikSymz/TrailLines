package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemFriendRequestBinding
import com.neu.madcourse.mad_team4_finalproject.interfaces.ItemRemoveListener
import com.neu.madcourse.mad_team4_finalproject.models.FindFriend
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants
import com.neu.madcourse.mad_team4_finalproject.utils.NetworkUtils

class FindFriendViewHolder(
    @NonNull context: Context,
    @NonNull itemBinding: ItemFriendRequestBinding,
    @NonNull listener: ItemRemoveListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ItemFriendRequestBinding = itemBinding

    /* The current Find friend instance */
    private lateinit var mFindFriend: FindFriend

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Network utils reference */
    private val mNetworkUtils: NetworkUtils = NetworkUtils(mContext)

    /* The Firebase storage reference */
    private val mFirebaseStorage: StorageReference = FirebaseStorage.getInstance().reference

    /* The Firebase Auth service reference */
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /* The Firebase User reference */
    private val mFirebaseUser: FirebaseUser? = mFirebaseAuth.currentUser

    /* The Recycler view item remove listener interface instance */
    private val mListener: ItemRemoveListener = listener

    /* The Firebase Database reference -> "users" */
    private val mUsersDatabaseRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference
            .child(Constants.UserKeys.KEY_TLO)

    /** Helper method to bind Find friend instance information to the Item Views
     * @param findFriend The find friend instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindFindFriendData(findFriend: FindFriend, position: Int) {
        // Set the FindFriend instance
        mFindFriend = findFriend

        // Set the user's name into the item view
        mBinding.viewRequestUserName.text = findFriend.name

        // Get the profile image storage reference
        val imageReference: StorageReference = mFirebaseStorage.child(
            formatFileName(mFindFriend.profileImageFileName)
        )

        // Load the user's profile picture into the item view
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(mContext)
                .load(uri)
                .placeholder(R.drawable.ic_profile_tag_account)
                .error(R.drawable.ic_profile_tag_account)
                .into(mBinding.viewRequestProfileImage)
        }

        // Check if the friend request has already been sent
        if (findFriend.isRequestSent) {
            // Hide the send request button
            mBinding.viewRequestButtonSend.visibility = View.GONE
            // Show the cancel request button
            mBinding.viewRequestButtonCancel.visibility = View.VISIBLE
        } else {
            // Show the send request button
            mBinding.viewRequestButtonSend.visibility = View.VISIBLE
            // Hide the cancel request button
            mBinding.viewRequestButtonCancel.visibility = View.GONE
        }

        /* Set the send friend request button onClick action */
        mBinding.viewRequestButtonSend.setOnClickListener {
            // Hide the send request button
            mBinding.viewRequestButtonSend.visibility = View.GONE
            // Show the progress bar
            mBinding.viewRequestProgressBar.visibility = View.VISIBLE

            // Extract the userID of the friend
            val friendID: String = findFriend.userID

            // Add the friend request in the current user's structure
            mUsersDatabaseRef
                .child(mFirebaseUser!!.uid)
                .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                .child(friendID)
                .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_SENT)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Add the request in the opposite direction inside the other user's
                        // "friend_request" directory
                        mUsersDatabaseRef
                            .child(friendID)
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                            .child(mFirebaseUser.uid)
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                            .setValue(Constants.UserKeys.FriendRequestKeys.REQUEST_STATUS_RECEIVED)
                            .addOnCompleteListener { reverseTask ->
                                if (reverseTask.isSuccessful) {
                                    // Show a toast message
                                    mBaseUtils.showToast(
                                        mContext.getString(R.string.request_sent_successfully),
                                        Toast.LENGTH_SHORT
                                    )

                                    // Hide the send request button
                                    mBinding.viewRequestButtonSend.visibility = View.GONE
                                    // Hide the progress bar
                                    mBinding.viewRequestProgressBar.visibility = View.INVISIBLE
                                    // Show the cancel request button
                                    mBinding.viewRequestButtonCancel.visibility = View.VISIBLE

                                    // Remove item from the find friends list
                                    mListener.removeItem(position)

                                    // Create a notification
                                    val title = "New Friend Request"
                                    val message = "Friend request from " + mFirebaseUser.displayName
                                    mNetworkUtils.sendNotification(mContext, title, message, friendID)
                                } else {
                                    mBaseUtils.showToast(
                                        mContext.getString(
                                            R.string.failed_to_send_request,
                                            reverseTask.exception
                                        ),
                                        Toast.LENGTH_SHORT
                                    )

                                    handleFailedSendRequest()
                                }
                            }
                    } else {
                        mBaseUtils.showToast(
                            mContext.getString(R.string.failed_to_send_request, task.exception),
                            Toast.LENGTH_SHORT
                        )

                        handleFailedSendRequest()
                    }
                }
        }

        // Set the cancel friend request button onClick action
        mBinding.viewRequestButtonCancel.setOnClickListener {
            // Hide the send request button
            mBinding.viewRequestButtonCancel.visibility = View.GONE
            // Show the progress bar
            mBinding.viewRequestProgressBar.visibility = View.VISIBLE

            // Extract the userID of the friend
            val friendID: String = findFriend.userID

            // Add the friend request in the current user's structure
            mUsersDatabaseRef
                .child(mFirebaseUser!!.uid)
                .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                .child(friendID)
                .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                .setValue(null)     // Setting value to "null" deletes the record
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Add the request in the opposite direction inside the other user's
                        // "friend_request" directory
                        mUsersDatabaseRef
                            .child(friendID)
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_TLO)
                            .child(mFirebaseUser.uid)
                            .child(Constants.UserKeys.FriendRequestKeys.KEY_REQUEST_STATUS)
                            .setValue(null)     // Setting value to "null" deletes the record
                            .addOnCompleteListener { reverseTask ->
                                if (reverseTask.isSuccessful) {
                                    // Show a toast message
                                    mBaseUtils.showToast(
                                        mContext.getString(R.string.request_cancelled_successfully),
                                        Toast.LENGTH_SHORT
                                    )

                                    // Hide the send request button
                                    mBinding.viewRequestButtonSend.visibility = View.VISIBLE
                                    // Hide the progress bar
                                    mBinding.viewRequestProgressBar.visibility = View.INVISIBLE
                                    // Show the cancel request button
                                    mBinding.viewRequestButtonCancel.visibility = View.GONE
                                } else {
                                    mBaseUtils.showToast(
                                        mContext.getString(
                                            R.string.failed_to_cancel_request,
                                            reverseTask.exception
                                        ),
                                        Toast.LENGTH_SHORT
                                    )

                                    handleFailedCancelRequest()
                                }
                            }
                    } else {
                        mBaseUtils.showToast(
                            mContext.getString(R.string.failed_to_cancel_request, task.exception),
                            Toast.LENGTH_SHORT
                        )

                        handleFailedCancelRequest()
                    }
                }
        }
    }

    /* Helper method to handle failed send request request exception */
    private fun handleFailedSendRequest() {
        // Show the send request button again
        mBinding.viewRequestButtonSend.visibility = View.VISIBLE
        // Hide the progress bar
        mBinding.viewRequestProgressBar.visibility = View.INVISIBLE
        // Hide the cancel request button
        mBinding.viewRequestButtonCancel.visibility = View.GONE
    }

    /* Helper method to handle failed cancel request request exception */
    private fun handleFailedCancelRequest() {
        // Show the send request button again
        mBinding.viewRequestButtonSend.visibility = View.GONE
        // Hide the progress bar
        mBinding.viewRequestProgressBar.visibility = View.INVISIBLE
        // Hide the cancel request button
        mBinding.viewRequestButtonCancel.visibility = View.VISIBLE
    }

    /* Helper method to convert filename into "images" database reference */
    private fun formatFileName(fileName: String): String {
        return String.format(
            "%s/%s", Constants.FirebaseStorageKeys.KEY_IMAGES, fileName
        )
    }
}