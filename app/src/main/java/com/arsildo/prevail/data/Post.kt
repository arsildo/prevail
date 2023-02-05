package com.arsildo.prevail.data

import androidx.annotation.Keep
import androidx.compose.runtime.Stable
import com.google.gson.annotations.SerializedName

@Stable
@Keep
data class ThreadPosts(
    val posts: List<Post>
)

@Stable
@Keep
data class Post(
    val com: String?,
    val sub: String?,
    @SerializedName("ext")
    val mediaType: String?,
    @SerializedName("filename")
    val fileName: String,
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
    val mediaID: Long,
    @SerializedName("time")
    val timeStamp: Int,
    @SerializedName("w")
    val mediaWidth: Int,
    @SerializedName("h")
    val mediaHeight: Int
)