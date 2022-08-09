package com.neu.madcourse.mad_team4_finalproject.models

data class ChatHistory(
    val userID: String,
    val name: String,
    val profilePictureFileName: String,
    val unreadCount: String,
    val lastMessage: String,
    val lastMessageTimestamp: String
)
