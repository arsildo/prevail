package com.arsildo.prevail.logic.network.models.boards

import androidx.annotation.Keep

@Keep
data class Cooldowns(
    val images: Int,
    val replies: Int,
    val threads: Int
)