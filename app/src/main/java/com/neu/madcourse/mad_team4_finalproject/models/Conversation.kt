package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class Conversation(
    @PropertyName(Constants.ChatKeys.MessageKeys.KEY_FROM)
    val fromID: String? = null,
    @PropertyName(Constants.ChatKeys.MessageKeys.KEY_CONTENT)
    val content: String? = null,
    @PropertyName(Constants.ChatKeys.MessageKeys.KEY_MESSAGE_ID)
    val message_id: String? = null,
    @PropertyName(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP)
    val timestamp: Long? = null,
    @PropertyName(Constants.ChatKeys.MessageKeys.KEY_DATA_TYPE)
    val dataType: String? = null
)
