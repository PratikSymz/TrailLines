package com.neu.madcourse.mad_team4_finalproject.view_holders;


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.neu.madcourse.mad_team4_finalproject.R
import com.neu.madcourse.mad_team4_finalproject.activities.ChatScreenActivity
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemChatHistoryAltBinding
import com.neu.madcourse.mad_team4_finalproject.models.ChatHistory
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

class ChatHistoryViewHolder(
    @NonNull context: Context,
    @NonNull itemBinding: ItemChatHistoryAltBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ItemChatHistoryAltBinding = itemBinding

    /* The current ChatHistory instance */
    private lateinit var mChatHistory: ChatHistory

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Firebase storage reference */
    private val mFirebaseStorage: StorageReference = FirebaseStorage.getInstance().reference

    /** Helper method to bind Chat history instance information to the Item Views
     * @param chatHistory The chat history instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindChatHistoryData(chatHistory: ChatHistory) {
        // Set the ChatHistory instance
        mChatHistory = chatHistory

        // Set the user's name into the item view
        mBinding.viewProfileName.text = chatHistory.name

        // Set the last message
        mBinding.viewLastSeenMessage.text = chatHistory.lastMessage

        // Set the unread message count
        mBinding.viewUnreadCount.text = chatHistory.unreadCount

        // Set the last message timestamp
        mBinding.viewTimeLastSent.text = chatHistory.lastMessageTimestamp

        // Get the profile image storage reference
        val imageReference: StorageReference = mFirebaseStorage.child(
            formatFileName(mChatHistory.profilePictureFileName)
        )

        // Load the user's profile picture into the item view
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(mContext)
                .load(uri)
                .placeholder(R.drawable.ic_profile_tag_account)
                .error(R.drawable.ic_profile_tag_account)
                .into(mBinding.viewProfilePicture)
        }

        // Set the item onClick listener
        mBinding.root.setOnClickListener {
            val intent: Intent = Intent(mContext, ChatScreenActivity::class.java)
            // Pass the userID of the other user
            intent.putExtra("user_id", mChatHistory.userID)
            mContext.startActivity(intent)

        }
        if (!mChatHistory.unreadCount.equals("0")) {
            mBinding.viewUnreadCount.visibility = View.VISIBLE
            mBinding.viewUnreadCount.text = chatHistory.unreadCount
        } else {
            mBinding.viewUnreadCount.visibility = View.INVISIBLE
        }

        // check the length of the last message and then assign it to new String variable
        if (mBinding.viewLastSeenMessage.length() > 30) {
            val lastMessage: String = chatHistory.lastMessage.substring(0, 30)
            mBinding.viewLastSeenMessage.text = lastMessage
        }
        if (mBinding.viewTimeLastSent.text == null) {
            mBinding.viewTimeLastSent.text = ""
        }


    }

    /* Helper method to convert filename into "images" database reference */
    private fun formatFileName(fileName: String): String {
        return String.format(
            "%s/%s", Constants.FirebaseStorageKeys.KEY_IMAGES, fileName
        )
    }
}