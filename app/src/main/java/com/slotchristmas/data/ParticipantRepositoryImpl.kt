package com.slotchristmas.data

import com.slotchristmas.R
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.repository.ParticipantRepository

class ParticipantRepositoryImpl : ParticipantRepository {

    override fun getParticipants(): List<Participant> = listOf(
        Participant(id = "1", name = "Kristel", photoResId = R.drawable.participant_1),
        Participant(id = "2", name = "Quentin", photoResId = R.drawable.participant_2),
        Participant(id = "3", name = "Veronique", photoResId = R.drawable.participant_3),
        Participant(id = "4", name = "Mario", photoResId = R.drawable.participant_4),
        Participant(id = "5", name = "Alicia", photoResId = R.drawable.participant_5),
        Participant(id = "6", name = "Max", photoResId = R.drawable.participant_6),
        Participant(id = "7", name = "Michèle", photoResId = R.drawable.participant_7),
        Participant(id = "8", name = "Claudine", photoResId = R.drawable.participant_8),
        Participant(id = "9", name = "Eva", photoResId = R.drawable.participant_9),
        Participant(id = "10", name = "Manu", photoResId = R.drawable.participant_10)
    )
}
