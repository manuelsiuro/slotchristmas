package com.slotchristmas.ui.components.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasGreen
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.ChristmasWhite
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Animation modes for Christmas string lights.
 */
enum class LightAnimationMode {
    /** Lights randomly twinkle with fade in/out effects */
    TWINKLING,
    /** Lights chase sequentially around the border (marquee effect) */
    CHASE,
    /** All lights pulse in synchronized brightness */
    PULSING,
    /** Lights cycle through colors over time */
    COLOR_CYCLING
}

/**
 * Represents a single light bulb with its position and properties.
 */
private data class LightBulb(
    val position: Offset,
    val baseColorIndex: Int,
    val randomSeed: Float
)

/**
 * Applies an animated Christmas string lights border with colorful bulbs
 * that automatically cycles through different animation modes.
 *
 * @param lightCount Number of lights around the border
 * @param colors List of colors for the lights (cycles through)
 * @param bulbRadius Radius of each light bulb
 * @param glowRadius Radius of the glow effect around each bulb
 * @param borderInset How far from the edge the lights are placed
 * @param cornerRadius Corner radius matching the component's shape
 * @param modeCycleDurationMs Duration in ms for each animation mode before switching
 * @param animationSpeed Speed multiplier for animations (1.0 = normal)
 */
fun Modifier.christmasLightsBorder(
    lightCount: Int = 24,
    colors: List<Color> = listOf(ChristmasRed, ChristmasGreen, ChristmasGold, ChristmasWhite),
    bulbRadius: Dp = 6.dp,
    glowRadius: Dp = 12.dp,
    borderInset: Dp = 10.dp,
    cornerRadius: Dp = 16.dp,
    modeCycleDurationMs: Int = 8000,
    animationSpeed: Float = 1.0f
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "christmasLights")

    // Mode cycling animation (cycles through 4 modes)
    val modeCycleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (modeCycleDurationMs * 4 / animationSpeed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "modeCycle"
    )

    // Animation progress within current mode (0 to 1)
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (2000 / animationSpeed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "lightProgress"
    )

    // Pre-generate random seeds for twinkling consistency
    val randomSeeds = remember { List(lightCount) { Random.nextFloat() } }

    this.drawWithContent {
        // Draw content first
        drawContent()

        // Calculate current mode
        val currentMode = LightAnimationMode.entries[modeCycleProgress.toInt() % 4]

        // Calculate light positions
        val bulbs = calculateLightPositions(
            size = size,
            lightCount = lightCount,
            borderInset = borderInset.toPx(),
            cornerRadius = cornerRadius.toPx(),
            randomSeeds = randomSeeds
        )

        // Draw each light bulb
        bulbs.forEachIndexed { index, bulb ->
            val (brightness, colorIndex) = calculateLightState(
                mode = currentMode,
                lightIndex = index,
                lightCount = lightCount,
                progress = animationProgress,
                bulb = bulb
            )

            val color = colors[colorIndex % colors.size]

            drawLightBulb(
                center = bulb.position,
                color = color,
                brightness = brightness,
                bulbRadius = bulbRadius.toPx(),
                glowRadius = glowRadius.toPx()
            )
        }
    }
}

/**
 * Calculates the positions of light bulbs around a rounded rectangle border.
 */
private fun calculateLightPositions(
    size: Size,
    lightCount: Int,
    borderInset: Float,
    cornerRadius: Float,
    randomSeeds: List<Float>
): List<LightBulb> {
    val effectiveWidth = size.width - 2 * borderInset
    val effectiveHeight = size.height - 2 * borderInset
    val effectiveCornerRadius = (cornerRadius - borderInset).coerceAtLeast(0f)

    // Calculate perimeter segments
    val straightTop = effectiveWidth - 2 * effectiveCornerRadius
    val straightSide = effectiveHeight - 2 * effectiveCornerRadius
    val cornerArc = (PI.toFloat() * effectiveCornerRadius / 2f)

    val totalPerimeter = 2 * straightTop + 2 * straightSide + 4 * cornerArc

    val bulbs = mutableListOf<LightBulb>()
    val spacing = totalPerimeter / lightCount

    for (i in 0 until lightCount) {
        val distance = i * spacing
        val position = getPointOnRoundedRect(
            distance = distance,
            size = size,
            borderInset = borderInset,
            cornerRadius = effectiveCornerRadius,
            straightTop = straightTop,
            straightSide = straightSide,
            cornerArc = cornerArc
        )

        bulbs.add(
            LightBulb(
                position = position,
                baseColorIndex = i % 4,
                randomSeed = randomSeeds[i]
            )
        )
    }

    return bulbs
}

/**
 * Gets a point along the rounded rectangle perimeter at the given distance.
 */
private fun getPointOnRoundedRect(
    distance: Float,
    size: Size,
    borderInset: Float,
    cornerRadius: Float,
    straightTop: Float,
    straightSide: Float,
    cornerArc: Float
): Offset {
    var remaining = distance
    val left = borderInset
    val top = borderInset
    val right = size.width - borderInset
    val bottom = size.height - borderInset

    // Top edge (left to right)
    if (remaining < straightTop) {
        return Offset(left + cornerRadius + remaining, top)
    }
    remaining -= straightTop

    // Top-right corner
    if (remaining < cornerArc) {
        val angle = -PI.toFloat() / 2 + (remaining / cornerArc) * (PI.toFloat() / 2)
        return Offset(
            right - cornerRadius + cornerRadius * cos(angle),
            top + cornerRadius + cornerRadius * sin(angle)
        )
    }
    remaining -= cornerArc

    // Right edge (top to bottom)
    if (remaining < straightSide) {
        return Offset(right, top + cornerRadius + remaining)
    }
    remaining -= straightSide

    // Bottom-right corner
    if (remaining < cornerArc) {
        val angle = 0f + (remaining / cornerArc) * (PI.toFloat() / 2)
        return Offset(
            right - cornerRadius + cornerRadius * cos(angle),
            bottom - cornerRadius + cornerRadius * sin(angle)
        )
    }
    remaining -= cornerArc

    // Bottom edge (right to left)
    if (remaining < straightTop) {
        return Offset(right - cornerRadius - remaining, bottom)
    }
    remaining -= straightTop

    // Bottom-left corner
    if (remaining < cornerArc) {
        val angle = PI.toFloat() / 2 + (remaining / cornerArc) * (PI.toFloat() / 2)
        return Offset(
            left + cornerRadius + cornerRadius * cos(angle),
            bottom - cornerRadius + cornerRadius * sin(angle)
        )
    }
    remaining -= cornerArc

    // Left edge (bottom to top)
    if (remaining < straightSide) {
        return Offset(left, bottom - cornerRadius - remaining)
    }
    remaining -= straightSide

    // Top-left corner
    val angle = PI.toFloat() + (remaining / cornerArc) * (PI.toFloat() / 2)
    return Offset(
        left + cornerRadius + cornerRadius * cos(angle),
        top + cornerRadius + cornerRadius * sin(angle)
    )
}

/**
 * Calculates the brightness and color index for a light based on the current animation mode.
 */
private fun calculateLightState(
    mode: LightAnimationMode,
    lightIndex: Int,
    lightCount: Int,
    progress: Float,
    bulb: LightBulb
): Pair<Float, Int> {
    return when (mode) {
        LightAnimationMode.TWINKLING -> {
            // Each bulb fades independently with random phase
            val phase = bulb.randomSeed * 2 * PI.toFloat()
            val frequency = 0.8f + bulb.randomSeed * 0.4f
            val brightness = 0.3f + 0.7f * ((sin(progress * frequency * 2 * PI.toFloat() + phase) + 1f) / 2f)
            Pair(brightness, bulb.baseColorIndex)
        }

        LightAnimationMode.CHASE -> {
            // Moving window of lit bulbs
            val tailLength = 4
            val activePosition = (progress * lightCount).toInt()
            val distance = ((lightIndex - activePosition + lightCount) % lightCount).let {
                if (it > lightCount / 2) lightCount - it else it
            }
            val brightness = if (distance < tailLength) {
                1f - (distance.toFloat() / tailLength) * 0.7f
            } else {
                0.2f
            }
            Pair(brightness, bulb.baseColorIndex)
        }

        LightAnimationMode.PULSING -> {
            // All bulbs pulse together
            val brightness = 0.4f + 0.6f * ((sin(progress * 2 * PI.toFloat()) + 1f) / 2f)
            Pair(brightness, bulb.baseColorIndex)
        }

        LightAnimationMode.COLOR_CYCLING -> {
            // Colors shift around the border
            val colorShift = (progress * 4).toInt()
            val colorIndex = (bulb.baseColorIndex + colorShift) % 4
            Pair(0.9f, colorIndex)
        }
    }
}

/**
 * Draws a single light bulb with glow effect.
 */
private fun DrawScope.drawLightBulb(
    center: Offset,
    color: Color,
    brightness: Float,
    bulbRadius: Float,
    glowRadius: Float
) {
    // Outer glow (radial gradient)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = brightness * 0.5f),
                color.copy(alpha = brightness * 0.2f),
                Color.Transparent
            ),
            center = center,
            radius = glowRadius * brightness.coerceAtLeast(0.5f)
        ),
        radius = glowRadius,
        center = center
    )

    // Bulb body
    drawCircle(
        color = color.copy(alpha = 0.3f + brightness * 0.7f),
        radius = bulbRadius,
        center = center
    )

    // Specular highlight (glass reflection effect)
    if (brightness > 0.5f) {
        drawCircle(
            color = Color.White.copy(alpha = (brightness - 0.5f) * 0.8f),
            radius = bulbRadius * 0.35f,
            center = Offset(
                center.x - bulbRadius * 0.25f,
                center.y - bulbRadius * 0.3f
            )
        )
    }
}
