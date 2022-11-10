package com.arsildo.prevail.logic.network.models.boards

import androidx.annotation.Keep

@Keep
data class Board(
    val board: String,
    val board_flags: BoardFlags,
    val bump_limit: Int,
    val code_tags: Int,
    val cooldowns: Cooldowns,
    val country_flags: Int,
    val custom_spoilers: Int,
    val image_limit: Int,
    val is_archived: Int,
    val math_tags: Int,
    val max_comment_chars: Int,
    val max_filesize: Int,
    val max_webm_duration: Int,
    val max_webm_filesize: Int,
    val meta_description: String,
    val min_image_height: Int,
    val min_image_width: Int,
    val oekaki: Int,
    val pages: Int,
    val per_page: Int,
    val require_subject: Int,
    val sjis_tags: Int,
    val spoilers: Int,
    val text_only: Int,
    val title: String,
    val user_ids: Int,
    val webm_audio: Int,
    val ws_board: Int
)