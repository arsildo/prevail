package com.arsildo.prevail.data.models

import androidx.annotation.Keep

@Keep
data class Cooldowns(
    val images: Int,
    val replies: Int,
    val threads: Int
)