package com.slotchristmas.config

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.slotchristmas.R

object AssetConfig {
    // Audio
    @RawRes
    val BACKGROUND_MUSIC = R.raw.christmas_music

    // Backgrounds (rotate randomly)
    val BACKGROUNDS: List<Int> = listOf(
        R.drawable.bg_christmas_1,
        R.drawable.bg_christmas_2,
        R.drawable.bg_christmas_3,
        R.drawable.bg_christmas_4,
        R.drawable.bg_christmas_5
    )

    // Background change interval in milliseconds
    const val BACKGROUND_CHANGE_INTERVAL_MS = 30_000L

    fun randomBackground(): Int = BACKGROUNDS.random()

    fun nextBackground(current: Int): Int {
        val filtered = BACKGROUNDS.filter { it != current }
        return if (filtered.isNotEmpty()) filtered.random() else BACKGROUNDS.random()
    }
}
