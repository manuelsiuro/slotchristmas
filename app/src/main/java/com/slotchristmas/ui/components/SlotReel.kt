package com.slotchristmas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.animation.ReelAnimationController
import com.slotchristmas.domain.model.Participant
import com.slotchristmas.ui.components.effects.SelectionFrame
import com.slotchristmas.ui.components.reel.ReelConfig
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ReelBackground
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SlotReel(
    items: List<Participant>,
    controller: ReelAnimationController,
    label: String,
    modifier: Modifier = Modifier,
    visibleItemCount: Int = ReelConfig.VISIBLE_ITEMS,
    itemHeight: Dp = ReelConfig.ITEM_SLOT_HEIGHT
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    val centerSlotTopPx = with(density) { ReelConfig.CENTER_SLOT_TOP_Y.toPx() }
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

        // Reel container - NO contentAlignment! Items positioned absolutely from top
        Box(
            modifier = Modifier
                .width(ReelConfig.CONTAINER_WIDTH)
                .height(ReelConfig.CONTAINER_HEIGHT)
                .clip(RoundedCornerShape(16.dp))
                .background(ReelBackground)
                .border(3.dp, ChristmasGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .clipToBounds()
            // NOTE: No contentAlignment here - items use absolute positioning
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

                    // Calculate ABSOLUTE Y position from container top
                    val yPositionPx by remember(index, controller.normalizedOffset) {
                        derivedStateOf {
                            calculateItemYPosition(
                                index = index,
                                normalizedOffset = controller.normalizedOffset,
                                itemHeightPx = itemHeightPx,
                                totalHeightPx = totalHeightPx,
                                centerSlotTopPx = centerSlotTopPx
                            )
                        }
                    }

                    // Calculate alpha for fade effect at edges
                    val alpha = calculateAlpha(yPositionPx, centerSlotTopPx, itemHeightPx)

                    // Item positioned ABSOLUTELY using offset modifier
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(x = 0, y = yPositionPx.roundToInt()) }
                            .fillMaxWidth()
                            .height(itemHeight)
                            .graphicsLayer { this.alpha = alpha },
                        contentAlignment = Alignment.Center  // Center content WITHIN item slot
                    ) {
                        CircularParticipantImage(
                            participant = item,
                            size = ReelConfig.ITEM_CONTENT_SIZE
                        )
                    }
                }
            }

            // SelectionFrame at exact center, sized to match content
            SelectionFrame(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(ReelConfig.SELECTION_FRAME_SIZE)
            )
        }
    }
}

/**
 * Calculate ABSOLUTE Y position from container top.
 *
 * Invariant: When normalizedOffset = targetIndex * itemHeightPx,
 *            item at targetIndex has yPosition = centerSlotTopPx (perfectly centered)
 *
 * @param index The item's index in the list
 * @param normalizedOffset Current scroll offset (0 to totalHeightPx)
 * @param itemHeightPx Height of one item slot in pixels
 * @param totalHeightPx Total height of all items (itemHeightPx * itemCount)
 * @param centerSlotTopPx Y position where centered item's top edge should be
 */
private fun calculateItemYPosition(
    index: Int,
    normalizedOffset: Float,
    itemHeightPx: Float,
    totalHeightPx: Float,
    centerSlotTopPx: Float
): Float {
    // When normalizedOffset = targetIndex * itemHeightPx, item at targetIndex is at centerSlotTopPx
    var yPosition = centerSlotTopPx + (index * itemHeightPx) - normalizedOffset

    // Wrap around for infinite scroll effect
    val viewportBuffer = itemHeightPx * 2
    while (yPosition < -viewportBuffer) {
        yPosition += totalHeightPx
    }
    while (yPosition > totalHeightPx - viewportBuffer) {
        yPosition -= totalHeightPx
    }

    return yPosition
}

/**
 * Calculate alpha (transparency) based on distance from center.
 * Items at center are fully opaque, items at edges are faded.
 */
private fun calculateAlpha(
    yPositionPx: Float,
    centerSlotTopPx: Float,
    itemHeightPx: Float
): Float {
    val distanceFromCenter = abs(yPositionPx - centerSlotTopPx)
    return (1f - (distanceFromCenter / (itemHeightPx * 1.5f))).coerceIn(0.3f, 1f)
}
