package com.slotchristmas.ui.components.effects

import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasGreen
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.ChristmasWhite
import kotlin.random.Random

data class Particle(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val startRotation: Float,
    val rotationSpeed: Float,
    val size: Float,
    val color: Color,
    var x: Float = startX,
    var y: Float = startY,
    var rotation: Float = startRotation,
    var alpha: Float = 1f
)

@Composable
fun JackpotCelebration(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 100,
    durationMs: Long = 3000
) {
    var particles by remember { mutableStateOf(emptyList<Particle>()) }
    var startTime by remember { mutableLongStateOf(0L) }

    // Generate particles when celebration starts
    LaunchedEffect(isActive) {
        if (isActive) {
            startTime = System.currentTimeMillis()
            particles = generateParticles(particleCount)
        } else {
            particles = emptyList()
        }
    }

    // Animation loop
    LaunchedEffect(isActive) {
        if (!isActive) return@LaunchedEffect

        while (isActive) {
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed > durationMs) break

            val progress = elapsed / durationMs.toFloat()
            val gravity = 500f

            particles = particles.map { particle ->
                particle.copy(
                    x = particle.startX + particle.velocityX * progress,
                    y = particle.startY + particle.velocityY * progress + 0.5f * gravity * progress * progress,
                    rotation = particle.startRotation + particle.rotationSpeed * progress * 360f,
                    alpha = (1f - progress).coerceIn(0f, 1f)
                )
            }
            delay(16) // ~60fps
        }
    }

    if (isActive && particles.isNotEmpty()) {
        Canvas(modifier = modifier) {
            particles.forEach { particle ->
                rotate(particle.rotation, pivot = Offset(particle.x, particle.y)) {
                    drawCircle(
                        color = particle.color.copy(alpha = particle.alpha),
                        radius = particle.size,
                        center = Offset(particle.x, particle.y)
                    )
                }
            }
        }
    }
}

private fun generateParticles(count: Int): List<Particle> {
    val colors = listOf(
        ChristmasRed,
        ChristmasGreen,
        ChristmasGold,
        ChristmasWhite
    )

    return (0 until count).map { i ->
        Particle(
            id = i,
            startX = Random.nextFloat() * 1920f,  // Approximate screen width
            startY = -50f,  // Start above screen
            velocityX = Random.nextFloat() * 400f - 200f,  // -200 to 200
            velocityY = Random.nextFloat() * 300f + 100f,  // 100 to 400
            startRotation = Random.nextFloat() * 360f,
            rotationSpeed = Random.nextFloat() * 2f - 1f,  // -1 to 1 rotations
            size = Random.nextFloat() * 10f + 5f,  // 5 to 15
            color = colors.random()
        )
    }
}
