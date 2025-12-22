package com.slotchristmas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slotchristmas.R
import com.slotchristmas.ui.slot.SlotUiState
import com.slotchristmas.ui.slot.SpinPhase

@Composable
fun ReelStrip(
    uiState: SlotUiState,
    onSpin: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine which reels are still spinning based on phase
    val reel1Spinning = uiState.spinPhase == SpinPhase.SPINNING
    val reel2Spinning = uiState.spinPhase in listOf(SpinPhase.SPINNING, SpinPhase.REEL1_STOPPED)
    val reel3Spinning = uiState.spinPhase in listOf(SpinPhase.SPINNING, SpinPhase.REEL1_STOPPED, SpinPhase.REEL2_STOPPED)

    // Check if Grinch was rolled - show Grinch in all reels when stopped
    val isGrinchResult = uiState.selectedGiftCount?.isGrinch == true

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background frame image
        Image(
            painter = painterResource(id = R.drawable.reel_strip_background),
            contentDescription = "Slot machine frame",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        // Reels positioned in center content area
        Row(
            modifier = Modifier
                .padding(top = 60.dp, bottom = 50.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reel 1: Chooser (shows Grinch when Grinch is rolled)
            SlotReel(
                items = uiState.allParticipants,
                isSpinning = reel1Spinning,
                result = uiState.selectedChooser,
                showLabel = false,
                showGrinch = isGrinchResult
            )

            // Reel 2: Receiver (shows Grinch when Grinch is rolled)
            SlotReel(
                items = uiState.activeReceivers,
                isSpinning = reel2Spinning,
                result = uiState.selectedReceiver,
                showLabel = false,
                showGrinch = isGrinchResult
            )

            // Reel 3: Gift Count
            GiftCountReel(
                isSpinning = reel3Spinning,
                result = uiState.selectedGiftCount,
                showLabel = false
            )
        }

        // SpinButton overlaid on yellow circle at bottom
        SpinButton(
            onClick = onSpin,
            enabled = !uiState.isSpinning && !uiState.isGameOver && uiState.activeReceivers.isNotEmpty(),
            isSpinning = uiState.isSpinning,
            size = 80.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 8.dp)
        )
    }
}
