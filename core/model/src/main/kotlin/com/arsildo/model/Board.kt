package com.arsildo.model

import androidx.annotation.Keep

@Keep
data class Board(
    val board: String,
    val board_flags: BoardFlags? = null,
    val bump_limit: Int,
    val code_tags: Int? = null,
    val cooldowns: Cooldowns,
    val country_flags: Int? = null,
    val custom_spoilers: Int? = null,
    val image_limit: Int? = null,
    val is_archived: Int? = null,
    val math_tags: Int? = null,
    val max_comment_chars: Int? = null,
    val max_filesize: Int? = null,
    val max_webm_duration: Int? = null,
    val max_webm_filesize: Int? = null,
    val meta_description: String? = null,
    val min_image_height: Int? = null,
    val min_image_width: Int? = null,
    val oekaki: Int? = null,
    val pages: Int? = null,
    val per_page: Int? = null,
    val require_subject: Int? = null,
    val sjis_tags: Int? = null,
    val spoilers: Int? = null,
    val text_only: Int? = null,
    val title: String,
    val user_ids: Int? = null,
    val webm_audio: Int? = null,
    val ws_board: Int
)