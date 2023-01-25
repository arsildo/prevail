package com.arsildo.prevail.utils

import java.util.Locale

fun countryCodeToEmoji(countryCode: String): String {
    return countryCode
        .uppercase(Locale.US)
        .map { char -> Character.codePointAt("$char", 0) - 0x41 + 0x1F1E6 }
        .map { codePoint -> Character.toChars(codePoint) }
        .joinToString(separator = "") { charArray -> String(charArray) }
}