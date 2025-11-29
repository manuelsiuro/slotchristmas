package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.ui.theme.BackgroundOverlay
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasRed

@Composable
fun VolumeControl(
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isMuted) ChristmasRed.copy(alpha = 0.8f) else BackgroundOverlay)
            .clickable(onClick = onToggleMute),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isMuted) "🔇" else "🔊",
            fontSize = 24.sp
        )
    }
}
