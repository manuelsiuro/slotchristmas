package com.slotchristmas.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.SpinButtonDisabled
import com.slotchristmas.ui.theme.SpinButtonGradientEnd
import com.slotchristmas.ui.theme.SpinButtonGradientStart

@Composable
fun SpinButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isSpinning: Boolean,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 120.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            isPressed -> 0.92f
            else -> 1f
        },
        animationSpec = spring(stiffness = 400f),
        label = "buttonScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) SpinButtonGradientStart else SpinButtonDisabled,
        label = "buttonColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (enabled) ChristmasGold else Color.Gray,
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .size(size)
            .shadow(
                elevation = if (enabled) 12.dp else 4.dp,
                shape = CircleShape,
                spotColor = if (enabled) ChristmasRed else Color.Gray
            )
            .clip(CircleShape)
            .background(
                brush = if (enabled) {
                    Brush.verticalGradient(
                        colors = listOf(SpinButtonGradientStart, SpinButtonGradientEnd)
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(SpinButtonDisabled, SpinButtonDisabled)
                    )
                }
            )
            .border(4.dp, borderColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isSpinning) "..." else "SPIN!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
