package com.slotchristmas.ui.components.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.ChristmasWhite
import kotlin.math.sqrt

/**
 * Applies an animated candy cane border with rotating red and white diagonal stripes.
 *
 * The stripes continuously animate to create the illusion of spinning/rotating candy cane.
 *
 * @param borderWidth The thickness of the border
 * @param cornerRadius The corner radius of the border shape
 * @param stripeWidth The width of each individual stripe
 * @param redColor The red color for candy cane stripes
 * @param whiteColor The white color for candy cane stripes
 * @param animationDurationMs Duration for one complete stripe cycle animation
 */
fun Modifier.candyCaneBorder(
    borderWidth: Dp = 16.dp,
    cornerRadius: Dp = 24.dp,
    stripeWidth: Dp = 12.dp,
    redColor: Color = ChristmasRed,
    whiteColor: Color = ChristmasWhite,
    animationDurationMs: Int = 1500
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "candyCane")

    // Animate phase offset from 0 to stripeWidth*2 (one full red+white stripe cycle)
    val stripeWidthPx = stripeWidth.value
    val phaseOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = stripeWidthPx * 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDurationMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "stripePhase"
    )

    this.drawWithContent {
        // Draw candy cane border BEHIND content
        drawCandyCaneStripes(
            borderWidth = borderWidth.toPx(),
            cornerRadius = cornerRadius.toPx(),
            stripeWidth = stripeWidthPx,
            phaseOffset = phaseOffset,
            redColor = redColor,
            whiteColor = whiteColor
        )

        // Draw content on top
        drawContent()
    }
}

/**
 * Draws the candy cane striped border within the border region only.
 */
private fun DrawScope.drawCandyCaneStripes(
    borderWidth: Float,
    cornerRadius: Float,
    stripeWidth: Float,
    phaseOffset: Float,
    redColor: Color,
    whiteColor: Color
) {
    val width = size.width
    val height = size.height

    // Create outer rounded rectangle path
    val outerPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(0f, 0f, width, height),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        )
    }

    // Create inner rounded rectangle path (content area)
    val innerCornerRadius = (cornerRadius - borderWidth).coerceAtLeast(0f)
    val innerPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    borderWidth,
                    borderWidth,
                    width - borderWidth,
                    height - borderWidth
                ),
                cornerRadius = CornerRadius(innerCornerRadius, innerCornerRadius)
            )
        )
    }

    // Create border-only path by subtracting inner from outer
    val borderPath = Path().apply {
        op(outerPath, innerPath, PathOperation.Difference)
    }

    // Clip to the border region and draw stripes
    clipPath(borderPath) {
        // Draw white base
        drawRect(color = whiteColor)

        // Draw diagonal red stripes with animated offset
        drawDiagonalStripes(
            color = redColor,
            stripeWidth = stripeWidth,
            phaseOffset = phaseOffset
        )
    }
}

/**
 * Draws diagonal stripes at 45 degrees with animated phase offset.
 */
private fun DrawScope.drawDiagonalStripes(
    color: Color,
    stripeWidth: Float,
    phaseOffset: Float
) {
    val diagonal = sqrt(size.width * size.width + size.height * size.height)
    val stripeSpacing = stripeWidth * 2  // Red + white alternating

    // Calculate how many stripes needed to cover the diagonal
    val numStripes = ((diagonal / stripeSpacing) + 4).toInt()

    // Draw stripes rotated 45 degrees
    rotate(degrees = 45f, pivot = Offset(size.width / 2, size.height / 2)) {
        // Start position adjusted for phase animation
        val startOffset = -diagonal - stripeSpacing + (phaseOffset % stripeSpacing)

        for (i in 0 until numStripes * 2) {
            val x = startOffset + i * stripeSpacing
            drawRect(
                color = color,
                topLeft = Offset(x, -diagonal),
                size = Size(stripeWidth, diagonal * 3)
            )
        }
    }
}
