package com.slotchristmas.domain.model

import androidx.annotation.DrawableRes

data class Participant(
    val id: String,
    val name: String,
    @DrawableRes val photoResId: Int,
    val remainingGiftsToReceive: Int = 40
) {
    val canReceiveGifts: Boolean
        get() = remainingGiftsToReceive > 0
}
