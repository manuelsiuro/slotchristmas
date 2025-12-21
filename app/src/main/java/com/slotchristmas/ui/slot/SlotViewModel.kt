package com.slotchristmas.ui.slot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slotchristmas.audio.AudioManager
import com.slotchristmas.audio.SoundEffect
import com.slotchristmas.config.AssetConfig
import com.slotchristmas.data.api.BlagueApiService
import com.slotchristmas.domain.model.GiftCount
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.model.SpinResult
import com.slotchristmas.domain.repository.ParticipantRepository
import com.slotchristmas.util.FestiveMessages
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SlotChristmas"

class SlotViewModel(
    private val participantRepository: ParticipantRepository,
    private val audioManager: AudioManager,
    private val blagueApiService: BlagueApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SlotUiState())
    val uiState: StateFlow<SlotUiState> = _uiState.asStateFlow()

    private var punchlineJob: Job? = null

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

        // Fetch initial joke from API
        fetchNewJoke()

        // Start background music
        audioManager.startMusic()
    }

    private fun fetchNewJoke() {
        // Cancel any pending punchline reveal
        punchlineJob?.cancel()

        viewModelScope.launch {
            try {
                val blague = blagueApiService.getRandomBlague()
                _uiState.update { state ->
                    state.copy(
                        jokeSetup = blague.blague,
                        jokePunchline = blague.reponse,
                        showPunchline = false
                    )
                }
                Log.d(TAG, "Fetched joke: ${blague.blague}")

                // Schedule punchline reveal after 5 seconds
                punchlineJob = launch {
                    delay(5000)
                    _uiState.update { it.copy(showPunchline = true) }
                    Log.d(TAG, "Showing punchline: ${blague.reponse}")
                }
            } catch (e: Exception) {
                // Fallback to static message if API fails
                Log.e(TAG, "Failed to fetch joke: ${e.message}")
                val fallback = FestiveMessages.random()
                _uiState.update { state ->
                    state.copy(
                        jokeSetup = fallback,
                        jokePunchline = "",
                        showPunchline = false
                    )
                }
            }
        }
    }

    fun spin() {
        val currentState = _uiState.value
        if (currentState.isSpinning || currentState.activeReceivers.isEmpty()) return

        viewModelScope.launch {
            // Play random button click sound
            audioManager.playRandomButtonClick()

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
            Log.d(TAG, "Reel 1 (GIVES) stopped - Giver: ${chooser.name}")

            // Reel 2 stops 500ms later
            delay(500)
            _uiState.update { it.copy(spinPhase = SpinPhase.REEL2_STOPPED) }
            audioManager.playSfx(SoundEffect.REEL_STOP)
            Log.d(TAG, "Reel 2 (RECEIVES) stopped - Receiver: ${receiver.name}")

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
            Log.d(TAG, "Last Result: ${chooser.name} gives ${giftCount.value} gift(s) to ${receiver.name}")

            // Check for jackpot or Grinch (special celebrations)
            if (giftCount.isJackpot || giftCount.isGrinch) {
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
            Log.d(TAG, "Receivers list after spin: ${updatedReceivers.map { it.name }}")

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

            // Fetch new joke for next display
            fetchNewJoke()
        }
    }

    fun removeReceiver(participantId: String) {
        _uiState.update { state ->
            val updatedReceivers = state.activeReceivers.filter { it.id != participantId }
            Log.d(TAG, "Receivers list changed (manual removal): ${updatedReceivers.map { it.name }}")
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
