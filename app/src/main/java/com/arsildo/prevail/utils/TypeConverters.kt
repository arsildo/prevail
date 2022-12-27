package com.arsildo.prevail.utils

import java.util.concurrent.TimeUnit

fun Long.formatMinSec(): String {
    return if (this <= 0L) ""
    else String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
        )
    )
}