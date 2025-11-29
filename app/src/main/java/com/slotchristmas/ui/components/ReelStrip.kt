package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.slotchristmas.animation.AnimationConfig
import com.slotchristmas.animation.ReelAnimationController
import com.slotchristmas.domain.model.GiftCount
import com.slotchristmas.ui.slot.SlotUiState
import com.slotchristmas.ui.slot.SpinPhase
import com.slotchristmas.ui.theme.BackgroundOverlay
import com.slotchristmas.ui.theme.ChristmasGold

@Composable
fun ReelStrip(
    uiState: SlotUiState,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { 90.dp.toPx() }

    // Create animation controllers for each reel
    val chooserController = remember {
        ReelAnimationController(
            itemHeightPx = itemHeightPx,
            itemCount = 10, // Max participants
            config = AnimationConfig.REEL_1
        )
    }

    val receiverController = remember {
        ReelAnimationController(
            itemHeightPx = itemHeightPx,
            itemCount = 10, // Max participants
            config = AnimationConfig.REEL_2
        )
    }

    val giftCountController = remember {
        ReelAnimationController(
            itemHeightPx = itemHeightPx,
            itemCount = GiftCount.entries.size,
            config = AnimationConfig.REEL_3
        )
    }

    // Handle spin animation
    LaunchedEffect(uiState.spinPhase) {
        when (uiState.spinPhase) {
            SpinPhase.SPINNING_UP, SpinPhase.SUSTAINED_SPIN -> {
                // All reels start spinning together
                launch {
                    chooserController.spinAndLandOn(uiState.targetChooserIndex)
                }
                launch {
                    receiverController.spinAndLandOn(uiState.targetReceiverIndex)
                }
                launch {
                    giftCountController.spinAndLandOn(uiState.targetGiftCountIndex)
                }
            }
            else -> { /* Other phases handled by timing in ViewModel */ }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(BackgroundOverlay)
            .border(4.dp, ChristmasGold, RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reel 1: Chooser
            SlotReel(
                items = uiState.allParticipants,
                controller = chooserController,
                label = "GIVES"
            )

            // Reel 2: Receiver
            SlotReel(
                items = uiState.activeReceivers,
                controller = receiverController,
                label = "RECEIVES"
            )

            // Reel 3: Gift Count
            GiftCountReel(
                controller = giftCountController
            )
        }
    }
}
