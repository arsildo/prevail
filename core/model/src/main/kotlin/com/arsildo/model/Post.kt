package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class Post(
    val capcode: String? = null,
    val closed: Int? = null,
    val com: String? = null,
    val ext: String? = null,
    val filename: String? = null,
    val fsize: Int? = null,
    val h: Int? = null,
    val images: Int? = null,
    val md5: String? = null,
    val name: String? = null,
    val no: Int,
    val now: String? = null,
    val replies: Int? = null,
    val resto: Int? = null,
    val semantic_url: String? = null,
    val sticky: Int? = null,
    val sub: String? = null,
    val tim: Long? = null,
    val time: Int? = null,
    val tn_h: Int? = null,
    val tn_w: Int? = null,
    val unique_ips: Int? = null,
    val w: Int? = null
)