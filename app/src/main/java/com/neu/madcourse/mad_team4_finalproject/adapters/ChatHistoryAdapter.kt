package com.neu.madcourse.mad_team4_finalproject.adapters;

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.neu.madcourse.mad_team4_finalproject.databinding.ItemChatHistoryAltBinding
import com.neu.madcourse.mad_team4_finalproject.models.ChatHistory
import com.neu.madcourse.mad_team4_finalproject.view_holders.ChatHistoryViewHolder

class ChatHistoryAdapter(
    @NonNull context: Context,
    @NonNull chatHistoryList: List<ChatHistory>
) : RecyclerView.Adapter<ChatHistoryViewHolder>() {

    /* The Activity context */
    private val mContext: Context = context

    /* The Chat history item list */
    private var mChatHistoryList: List<ChatHistory> = chatHistoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHistoryViewHolder {
        // Inflate the item view
        val mItemBinding: ItemChatHistoryAltBinding = ItemChatHistoryAltBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHistoryViewHolder(mContext, mItemBinding)
    }

    override fun onBindViewHolder(holder: ChatHistoryViewHolder, position: Int) {
        // Get the current chat history instance
        val chatHistory: ChatHistory = mChatHistoryList[position]
        // Set the chat history view field values
        holder.bindChatHistoryData(chatHistory)
    }

    override fun getItemCount(): Int {
        return mChatHistoryList.size
    }

    /* Helper method to update the adapter list */
    fun updateDataList(chatHistoryList: List<ChatHistory>) {
        // Update the data list
        mChatHistoryList = chatHistoryList
        // Notify the adapter
        notifyDataSetChanged()
    }
}
