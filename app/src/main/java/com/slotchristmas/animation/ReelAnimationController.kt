package com.slotchristmas.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

class ReelAnimationController(
    private val itemHeightPx: Float,
    private val itemCount: Int,
    private val config: AnimationConfig = AnimationConfig()
) {
    val offsetAnimatable: Animatable<Float, AnimationVector1D> = Animatable(0f)

    private val totalHeight: Float
        get() = itemHeightPx * itemCount

    val normalizedOffset: Float
        get() {
            val total = totalHeight
            return ((offsetAnimatable.value % total) + total) % total
        }

    val currentIndex: Int
        get() = ((normalizedOffset / itemHeightPx).toInt()) % itemCount

    val velocity: Float
        get() = offsetAnimatable.velocity

    suspend fun spinAndLandOn(targetIndex: Int) {
        // Phase 1: Spin Up (acceleration)
        val spinUpTarget = offsetAnimatable.value + (config.maxVelocity * config.spinUpDuration / 1000f)
        offsetAnimatable.animateTo(
            targetValue = spinUpTarget,
            animationSpec = tween(
                durationMillis = config.spinUpDuration,
                easing = FastOutSlowInEasing
            )
        )

        // Phase 2: Sustained Spin (constant velocity)
        val sustainedTarget = offsetAnimatable.value + (config.maxVelocity * config.sustainedSpinDuration / 1000f)
        offsetAnimatable.animateTo(
            targetValue = sustainedTarget,
            animationSpec = tween(
                durationMillis = config.sustainedSpinDuration,
                easing = LinearEasing
            )
        )

        // Phase 3: Deceleration (exponential decay)
        try {
            offsetAnimatable.animateDecay(
                initialVelocity = config.maxVelocity * 0.6f,
                animationSpec = exponentialDecay(
                    frictionMultiplier = 1.5f,
                    absVelocityThreshold = 100f
                )
            )
        } catch (e: Exception) {
            // Animation might be interrupted
        }

        // Phase 4: Bounce Landing (spring physics)
        val finalOffset = calculateLandingOffset(targetIndex)
        offsetAnimatable.animateTo(
            targetValue = finalOffset,
            animationSpec = spring(
                dampingRatio = config.bounceDamping,
                stiffness = config.bounceStiffness
            )
        )
    }

    suspend fun snapToIndex(index: Int) {
        val targetOffset = index * itemHeightPx
        offsetAnimatable.snapTo(targetOffset)
    }

    private fun calculateLandingOffset(targetIndex: Int): Float {
        val total = totalHeight
        val currentCycles = (offsetAnimatable.value / total).toInt()
        // Add one more cycle to ensure we always spin forward
        return (currentCycles + 1) * total + (targetIndex * itemHeightPx)
    }

    fun reset() {
        // Reset is handled via snapToIndex
    }
}
