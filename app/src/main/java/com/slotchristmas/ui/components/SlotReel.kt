package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.animation.ReelAnimationController
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.ui.components.effects.SelectionFrame
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ReelBackground
import kotlin.math.abs

@Composable
fun SlotReel(
    items: List<Participant>,
    controller: ReelAnimationController,
    label: String,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = 3,
    itemHeight: Dp = 90.dp
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val totalItems = items.size
    val totalHeightPx = itemHeightPx * totalItems

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Label
        Text(
            text = label,
            color = ChristmasGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Reel container
        Box(
            modifier = Modifier
                .width(itemHeight + 20.dp)
                .height(itemHeight * visibleItemCount)
                .clip(RoundedCornerShape(16.dp))
                .background(ReelBackground)
                .border(3.dp, ChristmasGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            if (items.isNotEmpty()) {
                // Calculate which items are visible
                val visibleIndices by remember(controller.normalizedOffset, totalItems) {
                    derivedStateOf {
                        if (totalItems == 0) emptyList()
                        else {
                            val centerIndex = ((controller.normalizedOffset / itemHeightPx).toInt()) % totalItems
                            (-2..visibleItemCount + 1).map { offset ->
                                ((centerIndex + offset) % totalItems + totalItems) % totalItems
                            }.distinct()
                        }
                    }
                }

                visibleIndices.forEach { index ->
                    val item = items.getOrNull(index) ?: return@forEach

                    val yOffset by remember(index, controller.normalizedOffset) {
                        derivedStateOf {
                            val normalizedOffset = controller.normalizedOffset
                            var baseY = index * itemHeightPx - normalizedOffset

                            // Wrap around for circular effect
                            while (baseY < -itemHeightPx * 1.5f) {
                                baseY += totalHeightPx
                            }
                            while (baseY > totalHeightPx - itemHeightPx * 0.5f) {
                                baseY -= totalHeightPx
                            }

                            // Center the items in the viewport
                            baseY + (itemHeightPx * visibleItemCount / 2) - (itemHeightPx / 2)
                        }
                    }

                    // Calculate alpha for fade effect at edges
                    val viewportCenter = itemHeightPx * visibleItemCount / 2
                    val distanceFromCenter = abs(yOffset - viewportCenter + itemHeightPx / 2)
                    val alpha = (1f - (distanceFromCenter / (itemHeightPx * 1.5f))).coerceIn(0.3f, 1f)

                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                translationY = yOffset
                                this.alpha = alpha
                            }
                            .size(itemHeight)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularParticipantImage(
                            participant = item,
                            size = itemHeight - 16.dp
                        )
                    }
                }
            }

            // Selection frame overlay
            SelectionFrame(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(itemHeight + 4.dp)
            )
        }
    }
}
