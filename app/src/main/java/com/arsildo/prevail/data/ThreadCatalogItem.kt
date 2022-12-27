package com.arsildo.prevail.data

import androidx.annotation.Keep

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)