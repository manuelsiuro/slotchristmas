package com.slotchristmas.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.ui.theme.BackgroundOverlay
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ChristmasGreen
import com.slotchristmas.ui.theme.ChristmasRed
import com.slotchristmas.ui.theme.LobsterFont

@Composable
fun FestiveMessage(
    jokeSetup: String,
    jokePunchline: String,
    showPunchline: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundOverlay)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Always show the joke setup (blague)
            AnimatedContent(
                targetState = jokeSetup,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "jokeSetup"
            ) { text ->
                Text(
                    text = text,
                    color = Color.White, //ChristmasGold
                    fontSize = 18.sp,
                    fontFamily = LobsterFont,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                            color = ChristmasRed.copy(alpha = 0.8f),
                            offset = Offset(2f, 2f),
                            blurRadius = 6f
                        )
                    )
                )
            }

            // Show punchline after delay
            AnimatedContent(
                targetState = showPunchline && jokePunchline.isNotEmpty(),
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "punchlineVisibility"
            ) { show ->
                if (show) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = jokePunchline,
                            color = Color.White, //ChristmasGreen
                            fontSize = 16.sp,
                            fontFamily = LobsterFont,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                    }
                } else {
                    // Empty spacer to maintain layout
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }
        }
    }
}
