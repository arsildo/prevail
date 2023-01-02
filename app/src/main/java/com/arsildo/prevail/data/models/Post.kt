package com.arsildo.prevail.data.models

import androidx.annotation.Keep

@Keep
data class Post(
    val bumplimit: Int,
    val com: String?,
    val sub: String?,
    val ext: String?,
    val filename: String,
    val fsize: Int,
    val country: String?,
    val country_name: String?,
    val h: Int,
    val imagelimit: Int,
    val images: Int,
    val md5: String,
    val name: String,
    val no: Int,
    val now: String,
    val replies: Int,
    val resto: Int,
    val semantic_url: String,
    val tim: Long,
    val time: Int,
    val tn_h: Int,
    val tn_w: Int,
    val unique_ips: Int,
    val w: Int
)