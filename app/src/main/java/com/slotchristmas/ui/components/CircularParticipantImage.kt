package com.slotchristmas.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slotchristmas.R
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.ui.theme.ChristmasGold

@Composable
fun CircularParticipantImage(
    participant: Participant,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    borderColor: Color = ChristmasGold,
    borderWidth: Dp = 3.dp
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(participant.photoResId)
            .crossfade(true)
            .placeholder(R.drawable.participant_placeholder)
            .error(R.drawable.participant_placeholder)
            .build(),
        contentDescription = participant.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
    )
}
