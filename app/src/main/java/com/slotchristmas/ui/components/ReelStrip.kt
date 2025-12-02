package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.slotchristmas.ui.components.effects.candyCaneBorder
import com.slotchristmas.ui.slot.SlotUiState
import com.slotchristmas.ui.slot.SpinPhase
import com.slotchristmas.ui.theme.BackgroundOverlay

@Composable
fun ReelStrip(
    uiState: SlotUiState,
    modifier: Modifier = Modifier
) {
    // Determine which reels are still spinning based on phase
    val reel1Spinning = uiState.spinPhase == SpinPhase.SPINNING
    val reel2Spinning = uiState.spinPhase in listOf(SpinPhase.SPINNING, SpinPhase.REEL1_STOPPED)
    val reel3Spinning = uiState.spinPhase in listOf(SpinPhase.SPINNING, SpinPhase.REEL1_STOPPED, SpinPhase.REEL2_STOPPED)

    Box(
        modifier = modifier
            .candyCaneBorder(
                borderWidth = 16.dp,
                cornerRadius = 24.dp,
                stripeWidth = 24.dp,
                animationDurationMs = 3000
            )
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BackgroundOverlay),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reel 1: Chooser
            SlotReel(
                items = uiState.allParticipants,
                isSpinning = reel1Spinning,
                result = uiState.selectedChooser,
                label = "Qui ?"
            )

            // Reel 2: Receiver
            SlotReel(
                items = uiState.activeReceivers,
                isSpinning = reel2Spinning,
                result = uiState.selectedReceiver,
                label = "Pour qui ?"
            )

            // Reel 3: Gift Count
            GiftCountReel(
                isSpinning = reel3Spinning,
                result = uiState.selectedGiftCount
            )
        }
    }
}
