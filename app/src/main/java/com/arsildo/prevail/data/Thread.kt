package com.arsildo.prevail.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class ThreadCatalog : ArrayList<ThreadCatalogItem>()

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)


@Keep
data class Thread(
    val bumplimit: Int,
    val capcode: String?,
    val closed: Int,
    val com: String? = null,
    @SerializedName("ext")
    val mediaType: String?,
    @SerializedName("filename")
    val fileName: String,
    @SerializedName("fsize")
    val fileSize: Int,
    val country: String?,
    val country_name: String?,
    val imagelimit: Int,
    val images: Int,
    val last_modified: Int,
    val last_replies: List<LastReply>,
    val name: String,
    val no: Int,
    val now: String,
    val omitted_images: Int,
    val omitted_posts: Int,
    val replies: Int,
    val resto: Int,
    val semantic_url: String,
    val sticky: Int,
    val sub: String? = null,
    @SerializedName("tim")
    val mediaID: Long,
    @SerializedName("time")
    val timeStamp: Int,
    @SerializedName("w")
    val mediaWidth: Int,
    @SerializedName("h")
    val mediaHeight: Int
)