package com.arsildo.prevail.data.models

import androidx.annotation.Keep

@Keep
data class ThreadPosts(
    val posts: List<Post>
)