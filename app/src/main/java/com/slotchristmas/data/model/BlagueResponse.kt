package com.slotchristmas.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BlagueResponse(
    val blague: String,
    val reponse: String
)
