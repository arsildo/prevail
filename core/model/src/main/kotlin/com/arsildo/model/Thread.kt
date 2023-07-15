package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class Thread(
    val bumplimit: Int? = null,
    val capcode: String? = null,
    val closed: Int? = null,
    val com: String? = null,
    val ext: String? = null,
    val filename: String? = null,
    val fsize: Int? = null,
    val h: Int,
    val imagelimit: Int? = null,
    val images: Int? = null,
    val last_modified: Int? = null,
    val last_replies: List<LastReply>? = null,
    val md5: String? = null,
    val name: String? = null,
    val no: Int,
    val now: String? = null,
    val omitted_images: Int? = null,
    val omitted_posts: Int? = null,
    val replies: Int? = null,
    val resto: Int? = null,
    val semantic_url: String? = null,
    val sticky: Int? = null,
    val sub: String? = null,
    val tim: Long? = null,
    val time: Int? = null,
    val tn_h: Int,
    val tn_w: Int,
    val trip: String? = null,
    val w: Int
)