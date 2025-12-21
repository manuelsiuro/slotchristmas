package com.slotchristmas.domain.model

import kotlin.random.Random

enum class GiftCount(val value: Int, val isJackpot: Boolean = false, val isGrinch: Boolean = false) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4, isJackpot = true),
    GRINCH(10, isGrinch = true);  // 10 gifts = 1 from each of 10 players

    companion object {
        // Weighted random: 1=34%, 2=32%, 3=23%, 4=5% (Jackpot), GRINCH=6%
        fun weightedRandom(): GiftCount {
            val rand = Random.nextInt(100)
            return when {
                rand < 34 -> ONE       // 0-33 (34%)
                rand < 66 -> TWO       // 34-65 (32%)
                rand < 89 -> THREE     // 66-88 (23%)
                rand < 94 -> FOUR      // 89-93 (5% Jackpot)
                else -> GRINCH         // 94-99 (6% Grinch)
            }
        }

        fun fromIndex(index: Int): GiftCount {
            return entries[index.coerceIn(0, entries.size - 1)]
        }
    }
}
