package com.slotchristmas.ui.slot

import com.slotchristmas.domain.model.GiftCount
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.model.SpinResult

data class SlotUiState(
    // Participant lists
    val allParticipants: List<Participant> = emptyList(),      // For chooser reel (never changes)
    val activeReceivers: List<Participant> = emptyList(),      // For receiver reel (shrinks)

    // Spin state
    val isSpinning: Boolean = false,
    val spinPhase: SpinPhase = SpinPhase.IDLE,

    // Target indices for animation
    val targetChooserIndex: Int = 0,
    val targetReceiverIndex: Int = 0,
    val targetGiftCountIndex: Int = 0,

    // Results
    val lastResult: SpinResult? = null,
    val spinHistory: List<SpinResult> = emptyList(),

    // Visual state
    val backgroundResId: Int = 0,
    val festiveMessage: String = "",

    // Audio state
    val isMuted: Boolean = false,

    // Game state
    val isGameOver: Boolean = false
)

enum class SpinPhase {
    IDLE,
    SPINNING_UP,
    SUSTAINED_SPIN,
    REEL1_STOPPING,
    REEL2_STOPPING,
    REEL3_STOPPING,
    CELEBRATION,
    COMPLETE
}
