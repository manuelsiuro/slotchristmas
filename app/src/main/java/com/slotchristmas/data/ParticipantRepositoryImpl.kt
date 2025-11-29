package com.slotchristmas.data

import com.slotchristmas.R
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.domain.repository.ParticipantRepository

class ParticipantRepositoryImpl : ParticipantRepository {

    override fun getParticipants(): List<Participant> = listOf(
        Participant(id = "1", name = "Person 1", photoResId = R.drawable.participant_1),
        Participant(id = "2", name = "Person 2", photoResId = R.drawable.participant_2),
        Participant(id = "3", name = "Person 3", photoResId = R.drawable.participant_3),
        Participant(id = "4", name = "Person 4", photoResId = R.drawable.participant_4),
        Participant(id = "5", name = "Person 5", photoResId = R.drawable.participant_5),
        Participant(id = "6", name = "Person 6", photoResId = R.drawable.participant_6),
        Participant(id = "7", name = "Person 7", photoResId = R.drawable.participant_7),
        Participant(id = "8", name = "Person 8", photoResId = R.drawable.participant_8),
        Participant(id = "9", name = "Person 9", photoResId = R.drawable.participant_9),
        Participant(id = "10", name = "Person 10", photoResId = R.drawable.participant_10)
    )
}
