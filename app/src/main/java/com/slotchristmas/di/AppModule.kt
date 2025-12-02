package com.slotchristmas.di

import android.content.Context
import com.slotchristmas.audio.AudioManager
import com.slotchristmas.data.ParticipantRepositoryImpl
import com.slotchristmas.data.api.BlagueApiService
import com.slotchristmas.domain.repository.ParticipantRepository
import com.slotchristmas.ui.slot.SlotViewModel

object AppModule {

    private var participantRepository: ParticipantRepository? = null
    private var audioManager: AudioManager? = null
    private var blagueApiService: BlagueApiService? = null

    private fun provideParticipantRepository(): ParticipantRepository {
        return participantRepository ?: ParticipantRepositoryImpl().also {
            participantRepository = it
        }
    }

    private fun provideAudioManager(context: Context): AudioManager {
        return audioManager ?: AudioManager(context.applicationContext).also {
            audioManager = it
        }
    }

    private fun provideBlagueApiService(): BlagueApiService {
        return blagueApiService ?: BlagueApiService().also {
            blagueApiService = it
        }
    }

    fun provideSlotViewModel(context: Context): SlotViewModel {
        return SlotViewModel(
            participantRepository = provideParticipantRepository(),
            audioManager = provideAudioManager(context),
            blagueApiService = provideBlagueApiService()
        )
    }
}
