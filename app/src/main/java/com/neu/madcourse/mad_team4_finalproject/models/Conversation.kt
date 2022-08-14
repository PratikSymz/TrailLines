package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class Conversation(
    @get:PropertyName(Constants.ChatKeys.MessageKeys.KEY_FROM)
    @set:PropertyName(Constants.ChatKeys.MessageKeys.KEY_FROM)
    var fromID: String = "",
    @get:PropertyName(Constants.ChatKeys.MessageKeys.KEY_CONTENT)
    @set:PropertyName(Constants.ChatKeys.MessageKeys.KEY_CONTENT)
    var content: String = "",
    @get:PropertyName(Constants.ChatKeys.MessageKeys.KEY_MESSAGE_ID)
    @set:PropertyName(Constants.ChatKeys.MessageKeys.KEY_MESSAGE_ID)
    var message_id: String = "",
    @get:PropertyName(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP)
    @set:PropertyName(Constants.ChatKeys.MessageKeys.KEY_TIMESTAMP)
    var timestamp: Long = 0L,
    @get:PropertyName(Constants.ChatKeys.MessageKeys.KEY_DATA_TYPE)
    @set:PropertyName(Constants.ChatKeys.MessageKeys.KEY_DATA_TYPE)
    var dataType: String = ""
)
