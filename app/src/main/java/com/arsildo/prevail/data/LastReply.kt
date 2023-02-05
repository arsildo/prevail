package com.arsildo.prevail.data

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Stable
@Keep
data class LastReply(
    val capcode: String,
    val com: String,
    val ext: String,
    val filename: String,
    val fsize: Int,
    val h: Int,
    val md5: String,
    val name: String,
    val no: Int,
    val now: String,
    val resto: Int,
    val tim: Long,
    val time: Int,
    val tn_h: Int,
    val tn_w: Int,
    val trip: String,
    val w: Int
)