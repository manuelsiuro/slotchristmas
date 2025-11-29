package com.slotchristmas.domain.model

import kotlin.random.Random

enum class GiftCount(val value: Int, val isJackpot: Boolean = false) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4, isJackpot = true);

    companion object {
        // Weighted random: 1=40%, 2=35%, 3=20%, 4=5%
        fun weightedRandom(): GiftCount {
            val rand = Random.nextInt(100)
            return when {
                rand < 40 -> ONE
                rand < 75 -> TWO
                rand < 95 -> THREE
                else -> FOUR
            }
        }

        fun fromIndex(index: Int): GiftCount {
            return entries[index.coerceIn(0, entries.size - 1)]
        }
    }
}
