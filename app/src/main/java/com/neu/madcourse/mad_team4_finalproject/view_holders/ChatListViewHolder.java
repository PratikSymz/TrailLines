package com.neu.madcourse.mad_team4_finalproject.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.madcourse.mad_team4_finalproject.R;
import com.neu.madcourse.mad_team4_finalproject.models.ChatModel;

public class ChatListViewHolder extends RecyclerView.ViewHolder {
    private TextView fullName, lastMessage, lastMessageTime, unreadMessageCount;
    private LinearLayout linearLayoutChatList;
    private ImageView profileUrl;

    public ChatListViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayoutChatList = itemView.findViewById(R.id.chat_linear_layout);
        fullName = itemView.findViewById(R.id.text_view_fullname);
        lastMessage = itemView.findViewById(R.id.last_message_textview);
        lastMessageTime = itemView.findViewById(R.id.time_text_view);
        unreadMessageCount = itemView.findViewById(R.id.un_read_message_count);
        profileUrl = itemView.findViewById(R.id.chat_profile_picture);

    }

    public void bindData(ChatModel chatModel){
        fullName.setText(chatModel.getFullName());
        profileUrl.getDrawable();
    }
}
