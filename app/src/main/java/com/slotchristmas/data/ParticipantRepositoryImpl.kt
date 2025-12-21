package com.slotchristmas.data

import com.slotchristmas.R
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.repository.ParticipantRepository

class ParticipantRepositoryImpl : ParticipantRepository {

    override fun getParticipants(): List<Participant> = listOf(
        Participant(id = "1", name = "Kristel", photoResId = R.drawable.kiki),
        Participant(id = "2", name = "Quentin", photoResId = R.drawable.tintin),
        Participant(id = "3", name = "Véro", photoResId = R.drawable.vero),
        Participant(id = "4", name = "Mario", photoResId = R.drawable.mario),
        Participant(id = "5", name = "Alicia", photoResId = R.drawable.alicia),
        Participant(id = "6", name = "Max", photoResId = R.drawable.max),
        Participant(id = "7", name = "Michèle", photoResId = R.drawable.michele),
        Participant(id = "8", name = "Claudine", photoResId = R.drawable.claudine),
        Participant(id = "9", name = "Eva", photoResId = R.drawable.eva),
        Participant(id = "10", name = "Manu", photoResId = R.drawable.manu)
    )
}
