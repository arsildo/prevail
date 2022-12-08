package com.arsildo.prevail.presentation.components.shared

import android.graphics.Typeface
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import java.util.concurrent.TimeUnit

fun getCountryFromCode(country: String) = when (country) {
    "AC" -> "\uD83C\uDDE6\uD83C\uDDE8"
    "AD" -> "\uD83C\uDDE6\uD83C\uDDE9"
    "AE" -> "\uD83C\uDDE6\uD83C\uDDEA"
    "AF" -> "\uD83C\uDDE6\uD83C\uDDEB"
    "AG" -> "\uD83C\uDDE6\uD83C\uDDEC"
    "AI" -> "\uD83C\uDDE6\uD83C\uDDEE"
    "AL" -> "\uD83C\uDDE6\uD83C\uDDF1"
    "AM" -> "\uD83C\uDDE6\uD83C\uDDF2"
    "AO" -> "\uD83C\uDDE6\uD83C\uDDF4"
    "AQ" -> "\uD83C\uDDE6\uD83C\uDDF6"
    "AR" -> "\uD83C\uDDE6\uD83C\uDDF7"
    "AS" -> "\uD83C\uDDE6\uD83C\uDDF8"
    "AT" -> "\uD83C\uDDE6\uD83C\uDDF9"
    "AU" -> "\uD83C\uDDE6\uD83C\uDDFA"
    "AW" -> "\uD83C\uDDE6\uD83C\uDDFC"
    "AX" -> "\uD83C\uDDE6\uD83C\uDDFD"
    "AZ" -> "\uD83C\uDDE6\uD83C\uDDFF"
    else -> ""
}


fun Long.formatMinSec(): String {
    return if (this == 0L) " "
    else if (this < 0L) "Loading... "
    else String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(this)
        )
    )
}

@Composable
fun HtmlText(
    text: String,
    color: Color
) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            setTextColor(color.toArgb())
            typeface = Typeface.DEFAULT_BOLD
        }
    }
    )
}