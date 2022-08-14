package com.neu.madcourse.mad_team4_finalproject.models

import com.google.firebase.database.PropertyName
import com.neu.madcourse.mad_team4_finalproject.utils.Constants

data class User(
    @get:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_NAME)
    @set:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_NAME)
    var name: String = "",
    @get:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID)
    @set:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_EMAIL_ID)
    var emailID: String = "",
    @get:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS)
    @set:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_ONLINE_STATUS)
    var isOnline: Boolean = false,
    @get:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_PRIVATE_PROFILE)
    @set:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_PRIVATE_PROFILE)
    var isPrivateProfile: Boolean = false,
    @get:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL)
    @set:PropertyName(Constants.UserKeys.PersonalInfoKeys.KEY_PROFILE_URL)
    var profilePictureFileName: String = ""
)
