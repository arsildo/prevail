package com.arsildo.prevail.utils

import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat


@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    htmlText: String,
    onClick: () -> Unit = {},
    color: Color = MaterialTheme.colorScheme.secondary
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextColor(color.toArgb())
                typeface = Typeface.DEFAULT_BOLD
                setTextIsSelectable(true)
                setOnClickListener { onClick() }
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { it.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}