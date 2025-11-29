package com.slotchristmas.ui.components.reel

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Single source of truth for all reel dimensions.
 * All positioning calculations derive from these values.
 *
 * Coordinate System:
 * - Y = 0 is at the TOP of the reel container
 * - Positive Y goes DOWN
 * - When normalizedOffset = index * ITEM_SLOT_HEIGHT_PX, item at that index is centered
 */
object ReelConfig {
    // Core item dimensions
    val ITEM_CONTENT_SIZE: Dp = 74.dp       // Actual visual content size (image/number)
    val ITEM_PADDING: Dp = 8.dp             // Padding around content within slot
    val ITEM_SLOT_HEIGHT: Dp = 90.dp        // Total height of one slot (content + padding)

    // Container dimensions
    const val VISIBLE_ITEMS: Int = 3
    val CONTAINER_HEIGHT: Dp = ITEM_SLOT_HEIGHT * VISIBLE_ITEMS  // 270.dp
    val CONTAINER_WIDTH: Dp = ITEM_SLOT_HEIGHT + 20.dp           // 110.dp

    // Selection frame - sized to visually frame the content
    val SELECTION_FRAME_SIZE: Dp = ITEM_CONTENT_SIZE + 10.dp     // 84.dp - slightly larger than content

    // Positioning constants
    // The Y position (from container top) where a centered item's TOP edge should be
    // For 3 visible items of 90.dp each in a 270.dp container:
    // Center slot starts at (270 - 90) / 2 = 90.dp from top
    val CENTER_SLOT_TOP_Y: Dp = (CONTAINER_HEIGHT - ITEM_SLOT_HEIGHT) / 2  // 90.dp

    // Center of the container (for reference)
    val CONTAINER_CENTER_Y: Dp = CONTAINER_HEIGHT / 2  // 135.dp
}
