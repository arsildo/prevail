package com.arsildo.prevail.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class ThreadPosts(
    val posts: List<Post>
)

@Keep
data class Post(
    val com: String?,
    val sub: String?,
    @SerializedName("ext")
    val fileExtension: String?,
    val filename: String,
    @SerializedName("fsize")
    val fileSize: Int,
    val country: String?,
    val country_name: String?,
    val images: Int,
    val name: String,
    val no: Int,
    val now: String,
    val replies: Int,
    val semantic_url: String,
    @SerializedName("tim")
    val mediaId: Long,
    @SerializedName("time")
    val timeStamp: Int,
    @SerializedName("w")
    val mediaWidth: Int,
    @SerializedName("h")
    val mediaHeight: Int
)