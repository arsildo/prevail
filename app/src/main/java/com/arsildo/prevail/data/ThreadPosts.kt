package com.arsildo.prevail.data

import androidx.annotation.Keep

@Keep
data class ThreadPosts(
    val posts: List<Post>
)