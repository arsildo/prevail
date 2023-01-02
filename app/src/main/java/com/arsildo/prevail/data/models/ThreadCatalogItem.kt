package com.arsildo.prevail.data.models

import androidx.annotation.Keep

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)