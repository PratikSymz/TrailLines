package com.neu.madcourse.mad_team4_finalproject.models

import com.neu.madcourse.mad_team4_finalproject.models_nps.Park
import java.io.Serializable

data class Explore(
    val park: Park,
    val reviewStat: ReviewStat
) : Serializable
