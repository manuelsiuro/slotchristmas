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

    // Selected results (set at spin start, displayed when reel stops)
    val selectedChooser: Participant? = null,
    val selectedReceiver: Participant? = null,
    val selectedGiftCount: GiftCount? = null,

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
    SPINNING,       // All reels cycling
    REEL1_STOPPED,  // First reel stopped, others still spinning
    REEL2_STOPPED,  // First two stopped, third still spinning
    REEL3_STOPPED,  // All stopped
    CELEBRATION,    // Jackpot celebration
    COMPLETE
}
