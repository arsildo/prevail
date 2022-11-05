package com.arsildo.prevail.logic.network.models.threads

import com.google.gson.annotations.SerializedName

data class Thread(
    @SerializedName("bumplimit")
    val bumpLimit: Int,
    @SerializedName("capcode")
    val capCode: String,
    val closed: Int,
    val com: String?,
    val ext: String,
    val filename: String,
    @SerializedName("fsize")
    val fileSize: Int,
    val h: Int,
    @SerializedName("imagelimit")
    val imageLimit: Int,
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