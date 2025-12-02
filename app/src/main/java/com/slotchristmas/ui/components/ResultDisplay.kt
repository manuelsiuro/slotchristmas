package com.slotchristmas.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.domain.model.SpinResult
import com.slotchristmas.ui.components.effects.christmasLightsBorder
import com.slotchristmas.ui.theme.BackgroundOverlay
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasGreen
import com.slotchristmas.ui.theme.LobsterFont
import com.slotchristmas.ui.theme.TextPrimary

@Composable
fun ResultDisplay(
    result: SpinResult?,
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
            text = "Résultat",
            color = ChristmasGold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = LobsterFont,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = result != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            result?.let { spinResult ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chooser
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularParticipantImage(
                            participant = spinResult.chooser,
                            size = 40.dp,
                            borderWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            /*Text(
                                text = "Gives:",
                                color = TextPrimary.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )*/
                            Text(
                                text = spinResult.chooser.name,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Text(
                        text = "↓",
                        color = ChristmasGold,
                        fontSize = 20.sp
                    )

                    // Gift count
                    Text(
                        text = "${spinResult.giftCount.value} Cadeau${if (spinResult.giftCount.value > 1) "x" else ""}",
                        color = if (spinResult.giftCount.isJackpot) ChristmasGold else ChristmasGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "↓",
                        color = ChristmasGold,
                        fontSize = 20.sp
                    )

                    // Receiver
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularParticipantImage(
                            participant = spinResult.receiver,
                            size = 40.dp,
                            borderWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            /*Text(
                                text = "Receives:",
                                color = TextPrimary.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )*/
                            Text(
                                text = spinResult.receiver.name,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (spinResult.giftCount.isJackpot) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "JACKPOT!",
                            color = ChristmasGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (result == null) {
            Text(
                text = "Spin to see\nthe result!",
                color = TextPrimary.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
