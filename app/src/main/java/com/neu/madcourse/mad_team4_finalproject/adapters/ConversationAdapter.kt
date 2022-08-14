package com.neu.madcourse.mad_team4_finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemConversationBinding
import com.neu.madcourse.mad_team4_finalproject.models.Conversation
import com.neu.madcourse.mad_team4_finalproject.view_holders.ConversationViewHolder

class ConversationAdapter(
    @NonNull context: Context,
    @NonNull conversationList: List<Conversation>
) : RecyclerView.Adapter<ConversationViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The Conversation item list */
    private val mConversationList: List<Conversation> = conversationList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        // Inflate the item view
        val mItemBinding: ItemConversationBinding = ItemConversationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(mContext, mItemBinding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        // Get the current conversation instance
        val conversation: Conversation = mConversationList[position]
        // Set the chat history view field values
        holder.bindConversationData(conversation)
    }

    override fun getItemCount(): Int {
        return mConversationList.size
    }
}