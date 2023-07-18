package com.arsildo.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
data class LastReply(
    val capcode: String? = null,
    val com: String? = null,
    val ext: String? = null,
    val filename: String? = null,
    val fsize: Int? = null,
    val h: Int? = null,
    val md5: String? = null,
    val name: String? = null,
    val no: Int? = null,
    val now: String? = null,
    val resto: Int? = null,
    val tim: Long? = null,
    val time: Int? = null,
    val tn_h: Int? = null,
    val tn_w: Int? = null,
    val trip: String? = null,
    val w: Int? = null
)