package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)