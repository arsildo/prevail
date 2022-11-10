package com.arsildo.prevail.logic.network.models.threads

import androidx.annotation.Keep

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)