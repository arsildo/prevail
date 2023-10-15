package com.arsildo.media

import com.arsildo.media.MediaEndpoints.IMAGE_MEDIA
import java.util.regex.Pattern

private object MediaEndpoints {
    const val IMAGE_MEDIA = "https://i.4cdn.org"
}

fun getImageUrl(
    board: String,
    imageId: Long?,
    extension: String?,
): String {
    return "$IMAGE_MEDIA/$board/$imageId$extension"
}

fun findBoard(input: String): String? {
    val regex = Regex(pattern = "/\\w+/")
    val matchResult = regex.find(input)
    return matchResult?.value
}