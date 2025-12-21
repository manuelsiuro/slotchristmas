package com.slotchristmas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.R
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.ui.components.effects.SelectionFrame
import com.slotchristmas.ui.components.reel.ReelConfig
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.LobsterFont
import com.slotchristmas.ui.theme.ReelBackground
import kotlinx.coroutines.delay

@Composable
fun SlotReel(
    items: List<Participant>,
    isSpinning: Boolean,
    result: Participant?,
    label: String,
    modifier: Modifier = Modifier,
    showGrinch: Boolean = false
) {
    // State for the currently displayed item during spinning
    var displayedItem by remember { mutableStateOf(items.firstOrNull()) }

    // Keep reference to latest items list for use in coroutine
    val currentItems by rememberUpdatedState(items)

    // Rapid cycling effect when spinning
    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            while (currentItems.isNotEmpty()) {
                displayedItem = currentItems.random()
                delay(80) // ~12 FPS cycling speed
            }
        }
    }

    // When stopped, show the result; when spinning, show cycling item
    val itemToShow = if (isSpinning) displayedItem else result

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Label
        Text(
            text = label,
            color = ChristmasGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = LobsterFont,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Reel container
        Box(
            modifier = Modifier
                .width(ReelConfig.CONTAINER_WIDTH)
                .height(ReelConfig.CONTAINER_HEIGHT)
                .clip(RoundedCornerShape(16.dp))
                .background(ReelBackground)
                /*.border(3.dp, ChristmasGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))*/,
            contentAlignment = Alignment.Center
        ) {
            // Display single centered item (Grinch or participant)
            if (showGrinch && !isSpinning) {
                // Show Grinch image when Grinch is rolled
                Image(
                    painter = painterResource(id = R.drawable.grinch),
                    contentDescription = "Grinch",
                    modifier = Modifier.size(ReelConfig.ITEM_CONTENT_SIZE),
                    contentScale = ContentScale.Fit
                )
            } else {
                // Normal participant display
                itemToShow?.let { participant ->
                    CircularParticipantImage(
                        participant = participant,
                        size = ReelConfig.ITEM_CONTENT_SIZE
                    )
                }
            }

            // Selection frame overlay
            SelectionFrame(
                modifier = Modifier.size(ReelConfig.SELECTION_FRAME_SIZE)
            )
        }
    }
}
