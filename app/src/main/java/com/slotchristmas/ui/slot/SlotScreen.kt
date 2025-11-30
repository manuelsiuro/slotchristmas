package com.slotchristmas.ui.slot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slotchristmas.ui.components.AnimatedBackground
import com.slotchristmas.ui.components.FestiveMessage
import com.slotchristmas.ui.components.GameOverOverlay
import com.slotchristmas.ui.components.ReceiverPanel
import com.slotchristmas.ui.components.ReelStrip
import com.slotchristmas.ui.components.ResultDisplay
import com.slotchristmas.ui.components.SpinButton
import com.slotchristmas.ui.components.VolumeControl
import com.slotchristmas.ui.components.effects.JackpotCelebration

@Composable
fun SlotScreen(
    viewModel: SlotViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 1: Animated background
        AnimatedBackground(
            backgroundResId = uiState.backgroundResId,
            modifier = Modifier.fillMaxSize()
        )

        // Layer 2: Main content (landscape row layout)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left panel: Receiver management + Spin button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(180.dp)
                    .padding(end = 8.dp)
            ) {
                ReceiverPanel(
                    receivers = uiState.activeReceivers,
                    onRemove = viewModel::removeReceiver,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Spin button (larger, in left column)
                SpinButton(
                    onClick = viewModel::spin,
                    enabled = !uiState.isSpinning && !uiState.isGameOver && uiState.activeReceivers.isNotEmpty(),
                    isSpinning = uiState.isSpinning,
                    size = 100.dp
                )
            }

            // Center: Slot machine + Festive message
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Slot machine reels
                ReelStrip(
                    uiState = uiState
                )

                /*Spacer(modifier = Modifier.height(12.dp))

                // Festive message below slot machine
                FestiveMessage(
                    message = uiState.festiveMessage
                )*/
            }

            // Right panel: Result display & controls (same width as left)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(180.dp)
                    .padding(start = 8.dp)
            ) {
                ResultDisplay(
                    result = uiState.lastResult,
                    modifier = Modifier.weight(1f).width(180.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                VolumeControl(
                    isMuted = uiState.isMuted,
                    onToggleMute = viewModel::toggleMute
                )
            }
        }

        // Layer 4: Jackpot celebration overlay
        JackpotCelebration(
            isActive = uiState.spinPhase == SpinPhase.CELEBRATION,
            modifier = Modifier.fillMaxSize()
        )

        // Layer 5: Game over overlay
        if (uiState.isGameOver) {
            GameOverOverlay(onReset = viewModel::resetGame)
        }
    }
}
