package com.arsildo.prevail.utils

import java.util.Locale
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

fun countryCodeToEmoji(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char -> Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6 }
        .map { codePoint -> Character.toChars(codePoint) }
        .joinToString(separator = "") { charArray -> String(charArray) }
}