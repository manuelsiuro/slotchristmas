package com.slotchristmas.animation

data class AnimationConfig(
    val spinUpDuration: Int = 300,           // ms to reach full speed
    val sustainedSpinDuration: Int = 2000,   // ms at full speed (varies per reel)
    val maxVelocity: Float = 15000f,         // pixels per second
    val bounceDamping: Float = 0.55f,        // Spring damping ratio (0.5 = medium bounce)
    val bounceStiffness: Float = 300f,       // Spring stiffness
    val reelStopDelay: Long = 400            // ms delay between reel stops
) {
    companion object {
        // Configs for each reel with staggered sustained spin times
        val REEL_1 = AnimationConfig(sustainedSpinDuration = 2000)
        val REEL_2 = AnimationConfig(sustainedSpinDuration = 2400)
        val REEL_3 = AnimationConfig(sustainedSpinDuration = 2800)
    }
}
