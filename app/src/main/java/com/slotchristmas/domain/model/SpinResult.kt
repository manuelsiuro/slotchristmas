package com.slotchristmas.domain.model

data class SpinResult(
    val chooser: Participant,
    val receiver: Participant,
    val giftCount: GiftCount,
    val festiveMessage: String,
    val timestamp: Long = System.currentTimeMillis()
)
