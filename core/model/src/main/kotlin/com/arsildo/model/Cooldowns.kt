package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class Cooldowns(
    val images: Int? = null,
    val replies: Int? = null,
    val threads: Int? = null
)