package com.arsildo.prevail.logic.network.models.threadList

import androidx.annotation.Keep

@Keep
data class Thread(
    val bumplimit: Int,
    val capcode: String,
    val closed: Int,
    val com: String? = null,
    val ext: String ? = null,
    val filename: String,
    val fsize: Int,
    val country: String?,
    val country_name: String?,
    val h: Int,
    val imagelimit: Int,
    val images: Int,
    val last_modified: Int,
    val last_replies: List<LastReply>,
    val md5: String,
    val name: String,
    val no: Int,
    val now: String,
    val omitted_images: Int,
    val omitted_posts: Int,
    val replies: Int,
    val resto: Int,
    val semantic_url: String,
    val sticky: Int,
    val sub: String,
    val tim: Long,
    val time: Int,
    val tn_h: Int,
    val tn_w: Int,
    val w: Int
)