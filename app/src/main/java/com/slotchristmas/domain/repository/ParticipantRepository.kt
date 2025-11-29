package com.slotchristmas.domain.repository

import com.slotchristmas.domain.model.Participant

interface ParticipantRepository {
    fun getParticipants(): List<Participant>
}
