package com.slotchristmas.config

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.slotchristmas.R

object AssetConfig {
    // Audio
    @RawRes
    val BACKGROUND_MUSIC = R.raw.christmas_music

    // Static background (single image, no rotation)
    val BACKGROUNDS: List<Int> = listOf(R.drawable.bg_christmas_0)

    // Background change interval in milliseconds (unused with static bg)
    const val BACKGROUND_CHANGE_INTERVAL_MS = 30_000L

    fun randomBackground(): Int = R.drawable.bg_christmas_0

    fun nextBackground(current: Int): Int = R.drawable.bg_christmas_0
}
