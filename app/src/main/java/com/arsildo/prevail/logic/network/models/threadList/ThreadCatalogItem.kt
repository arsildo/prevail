package com.arsildo.prevail.logic.network.models.threadList

import androidx.annotation.Keep

@Keep
data class ThreadCatalogItem(
    val page: Int,
    val threads: List<Thread>
)