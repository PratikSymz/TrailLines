package com.neu.madcourse.mad_team4_finalproject.view_holders

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemConversationBinding
import com.neu.madcourse.mad_team4_finalproject.models.Conversation
import com.neu.madcourse.mad_team4_finalproject.utils.BaseUtils
import java.text.SimpleDateFormat
import java.util.*

class ConversationViewHolder(
    @NonNull context: Context,
    @NonNull itemBinding: ItemConversationBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    /* The activity context */
    private val mContext: Context = context

    /* The Item View binder */
    private val mBinding: ItemConversationBinding = itemBinding

    /* The current Conversation instance */
    private lateinit var mConversation: Conversation

    /* The Base utils reference */
    private val mBaseUtils: BaseUtils = BaseUtils(mContext)

    /* The Firebase Auth service reference */
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance();

    /* The Firebase User reference */
    private val mFirebaseUser: FirebaseUser = mFirebaseAuth.currentUser!!

    /** Helper method to bind Conversation instance information to the Item Views
     * @param conversation The conversation instance
     */
    @SuppressLint("ClickableViewAccessibility")
    fun bindConversationData(conversation: Conversation) {
        // Set the Conversation instance
        mConversation = conversation

        // Extract the other user's ID
        val fromUserID: String? = mConversation.fromID

        // IF "from" field contains the logged in user's info
        if (fromUserID == mFirebaseUser.uid) {
            // Show the sender bubble
            mBinding.holderSender.visibility = View.VISIBLE
            mBinding.holderReceiver.visibility = View.GONE
            // Set the conversation view information
            mBinding.viewSentMessage.text = mConversation.content
            mBinding.viewSentTimestamp.text = formatTimestamp(mConversation.timestamp)
        } else {
            // Show the sender bubble
            mBinding.holderSender.visibility = View.GONE
            mBinding.holderReceiver.visibility = View.VISIBLE
            // Set the conversation view information
            mBinding.viewReceivedMessage.text = mConversation.content
            mBinding.viewReceivedTimestamp.text = formatTimestamp(mConversation.timestamp)
        }
    }

    /* Helper method to format the message timestamp into simple time */
    @SuppressLint("SimpleDateFormat")
    private fun formatTimestamp(timestamp: Long?): String {
        val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val time: String = dateFormat.format(Date(timestamp!!))
        val timeSplit: List<String> = time.split(" ")
        return timeSplit[1]
    }
}