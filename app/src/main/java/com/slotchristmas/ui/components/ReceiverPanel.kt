package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.ui.components.effects.christmasLightsBorder
import com.slotchristmas.ui.theme.BackgroundOverlay
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.TextPrimary

@Composable
fun ReceiverPanel(
    receivers: List<Participant>,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .christmasLightsBorder(
                cornerRadius = 16.dp,
                lightCount = 20,
                borderInset = 8.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundOverlay)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Receivers",
            color = ChristmasGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "(${receivers.size} left)",
            color = TextPrimary.copy(alpha = 0.7f),
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(receivers, key = { it.id }) { participant ->
                ReceiverItem(
                    participant = participant,
                    onRemove = { onRemove(participant.id) }
                )
            }
        }
    }
}

@Composable
private fun ReceiverItem(
    participant: Participant,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularParticipantImage(
                participant = participant,
                size = 36.dp,
                borderWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Remove button
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(ChristmasRed.copy(alpha = 0.8f))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "×",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }


        Text(
            text = participant.name,
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


    }

    /*Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Spacer(modifier = Modifier.width(6.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = participant.name,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${participant.remainingGiftsToReceive} gifts left",
                color = TextPrimary.copy(alpha = 0.6f),
                fontSize = 9.sp
            )
        }

        // Remove button
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(ChristmasRed.copy(alpha = 0.8f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }*/
}
