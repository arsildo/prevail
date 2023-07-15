package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class ThreadCatalog(
    val page: Int,
    val threads: List<Thread>
)