package com.slotchristmas.ui.slot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slotchristmas.audio.AudioManager
import com.slotchristmas.audio.SoundEffect
import com.slotchristmas.config.AssetConfig
import com.slotchristmas.domain.model.GiftCount
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.model.SpinResult
import com.slotchristmas.domain.repository.ParticipantRepository
import com.slotchristmas.util.FestiveMessages
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SlotViewModel(
    private val participantRepository: ParticipantRepository,
    private val audioManager: AudioManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SlotUiState())
    val uiState: StateFlow<SlotUiState> = _uiState.asStateFlow()

    init {
        initializeGame()
    }

    private fun initializeGame() {
        audioManager.initialize()

        val participants = participantRepository.getParticipants()
        _uiState.update { state ->
            state.copy(
                allParticipants = participants,
                activeReceivers = participants.toList(),
                backgroundResId = AssetConfig.randomBackground(),
                festiveMessage = FestiveMessages.random(),
                isGameOver = false,
                lastResult = null,
                spinHistory = emptyList()
            )
        }

        // Start background music
        audioManager.startMusic()
    }

    fun spin() {
        val currentState = _uiState.value
        if (currentState.isSpinning || currentState.activeReceivers.isEmpty()) return

        viewModelScope.launch {
            // Generate random results
            val chooser = currentState.allParticipants.random()
            val receiver = currentState.activeReceivers.random()
            val giftCount = GiftCount.weightedRandom()

            // Update state with selected results and start spinning
            _uiState.update { state ->
                state.copy(
                    isSpinning = true,
                    spinPhase = SpinPhase.SPINNING,
                    selectedChooser = chooser,
                    selectedReceiver = receiver,
                    selectedGiftCount = giftCount
                )
            }

            // Play spin start sound
            audioManager.playSfx(SoundEffect.SPIN_START)

            // Start spinning loop sound after brief delay
            delay(200)
            audioManager.playSfx(SoundEffect.SPINNING_LOOP, loop = true)

            // Reel 1 stops after 2.3 seconds
            delay(2300)
            _uiState.update { it.copy(spinPhase = SpinPhase.REEL1_STOPPED) }
            audioManager.playSfx(SoundEffect.REEL_STOP)

            // Reel 2 stops 500ms later
            delay(500)
            _uiState.update { it.copy(spinPhase = SpinPhase.REEL2_STOPPED) }
            audioManager.playSfx(SoundEffect.REEL_STOP)

            // Reel 3 stops 500ms later
            delay(500)
            audioManager.stopSfx(SoundEffect.SPINNING_LOOP)
            _uiState.update { it.copy(spinPhase = SpinPhase.REEL3_STOPPED) }
            audioManager.playSfx(SoundEffect.REEL_STOP)

            // Create result
            val festiveMessage = FestiveMessages.random()
            val result = SpinResult(
                chooser = chooser,
                receiver = receiver,
                giftCount = giftCount,
                festiveMessage = festiveMessage
            )

            // Check for jackpot
            if (giftCount.isJackpot) {
                _uiState.update { it.copy(spinPhase = SpinPhase.CELEBRATION) }
                audioManager.playSfx(SoundEffect.JACKPOT)
                delay(2000) // Celebration duration
            }

            // Update receiver's remaining gifts
            val updatedReceivers = currentState.activeReceivers.map { participant ->
                if (participant.id == receiver.id) {
                    participant.copy(
                        remainingGiftsToReceive = (participant.remainingGiftsToReceive - giftCount.value).coerceAtLeast(0)
                    )
                } else participant
            }.filter { it.canReceiveGifts }

            // Update final state
            _uiState.update { state ->
                state.copy(
                    isSpinning = false,
                    spinPhase = SpinPhase.COMPLETE,
                    lastResult = result,
                    spinHistory = state.spinHistory + result,
                    activeReceivers = updatedReceivers,
                    backgroundResId = AssetConfig.nextBackground(state.backgroundResId),
                    festiveMessage = festiveMessage,
                    isGameOver = updatedReceivers.isEmpty()
                )
            }

            // Reset phase after a brief moment
            delay(500)
            _uiState.update { it.copy(spinPhase = SpinPhase.IDLE) }
        }
    }

    fun removeReceiver(participantId: String) {
        _uiState.update { state ->
            val updatedReceivers = state.activeReceivers.filter { it.id != participantId }
            state.copy(
                activeReceivers = updatedReceivers,
                isGameOver = updatedReceivers.isEmpty()
            )
        }
    }

    fun toggleMute() {
        val newMuteState = audioManager.toggleMute()
        _uiState.update { it.copy(isMuted = newMuteState) }
    }

    fun resetGame() {
        initializeGame()
    }

    fun onResume() {
        audioManager.resumeMusic()
    }

    fun onPause() {
        audioManager.pauseMusic()
    }

    fun onDestroy() {
        audioManager.release()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
