package com.arsildo.prevail.logic.network.models.thread

import androidx.annotation.Keep

@Keep
data class ThreadPosts(
    val posts: List<Post>
)