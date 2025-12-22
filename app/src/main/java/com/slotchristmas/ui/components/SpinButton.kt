package com.slotchristmas.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slotchristmas.R
import com.slotchristmas.ui.components.effects.christmasLightsBorder
import com.slotchristmas.ui.theme.SpinButtonBorder
import com.slotchristmas.ui.theme.SpinButtonDisabled
import com.slotchristmas.ui.theme.SpinButtonGradientBottom
import com.slotchristmas.ui.theme.SpinButtonGradientMiddle
import com.slotchristmas.ui.theme.SpinButtonGradientTop

@Composable
fun SpinButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isSpinning: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Calculate ring thickness (12% of total size) and inner circle size
    val ringThickness = size * 0.12f
    val innerSize = size - (ringThickness * 2)

    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            isPressed -> 0.92f
            else -> 1f
        },
        animationSpec = spring(stiffness = 400f),
        label = "buttonScale"
    )

    // Pulsing animation for Santa image when spinning
    val infiniteTransition = rememberInfiniteTransition(label = "santaPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Outer Box (burgundy ring)
    Box(
        modifier = modifier
            .scale(scale)
            .size(size)
            .then(
                if (isSpinning) {
                    Modifier.christmasLightsBorder(
                        lightCount = 12,
                        cornerRadius = size / 2,
                        borderInset = 4.dp,
                        bulbRadius = 4.dp,
                        glowRadius = 8.dp
                    )
                } else {
                    Modifier
                }
            )
            .shadow(
                elevation = if (enabled) 8.dp else 4.dp,
                shape = CircleShape,
                spotColor = if (enabled) SpinButtonBorder else Color.Gray
            )
            .clip(CircleShape)
            .background(if (enabled) SpinButtonBorder else SpinButtonDisabled)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner Box (gold circle)
        Box(
            modifier = Modifier
                .size(innerSize)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.5f),
                    ambientColor = Color.Black.copy(alpha = 0.3f)
                )
                .clip(CircleShape)
                .background(
                    brush = if (enabled) {
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to SpinButtonGradientTop,      // Bright highlight at top
                                0.35f to SpinButtonGradientMiddle,  // Rich gold transition
                                0.65f to SpinButtonGradientMiddle,  // Sustained mid-tone
                                1.0f to SpinButtonGradientBottom    // Deep bronze shadow
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                SpinButtonDisabled.copy(alpha = 0.7f),
                                SpinButtonDisabled
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            // Content: Santa image when spinning, empty when idle (matching reference)
            if (isSpinning) {
                Image(
                    painter = painterResource(id = R.drawable.spin_in_progress),
                    contentDescription = "Spinning...",
                    modifier = Modifier
                        .scale(pulseScale)
                        .size(innerSize - 8.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            // No text when idle - clean gold surface matching reference image
        }
    }
}
