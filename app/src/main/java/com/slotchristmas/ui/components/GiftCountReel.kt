package com.slotchristmas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slotchristmas.R
import com.slotchristmas.domain.model.GiftCount
import com.slotchristmas.ui.components.effects.SelectionFrame
import com.slotchristmas.ui.components.reel.ReelConfig
import com.slotchristmas.ui.theme.ChristmasGold
import com.slotchristmas.ui.theme.ReelBackground
import kotlinx.coroutines.delay

@Composable
fun GiftCountReel(
    isSpinning: Boolean,
    result: GiftCount?,
    modifier: Modifier = Modifier
) {
    val giftCounts = GiftCount.entries

    // State for the currently displayed gift count during spinning
    var displayedGiftCount by remember { mutableStateOf(giftCounts.first()) }

    // Rapid cycling effect when spinning
    LaunchedEffect(isSpinning) {
        if (isSpinning) {
            while (true) {
                displayedGiftCount = giftCounts.random()
                delay(80) // ~12 FPS cycling speed
            }
        }
    }

    // When stopped, show the result; when spinning, show cycling item
    val itemToShow = if (isSpinning) displayedGiftCount else result

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Label
        Text(
            text = "Combien ?",
            color = ChristmasGold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Reel container
        Box(
            modifier = Modifier
                .width(ReelConfig.CONTAINER_WIDTH)
                .height(ReelConfig.CONTAINER_HEIGHT)
                .clip(RoundedCornerShape(16.dp))
                .background(ReelBackground)
                /*.border(3.dp, ChristmasGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))*/,
            contentAlignment = Alignment.Center
        ) {
            // Display single centered item
            itemToShow?.let { giftCount ->
                GiftCountItem(
                    giftCount = giftCount,
                    size = ReelConfig.ITEM_CONTENT_SIZE
                )
            }

            // Selection frame overlay
            SelectionFrame(
                modifier = Modifier.size(ReelConfig.SELECTION_FRAME_SIZE)
            )
        }
    }
}

private fun GiftCount.toDrawableRes(): Int = when (this) {
    GiftCount.ONE -> R.drawable.cadeaux_1
    GiftCount.TWO -> R.drawable.cadeaux_2
    GiftCount.THREE -> R.drawable.cadeaux_3
    GiftCount.FOUR -> R.drawable.cadeaux_4
    GiftCount.GRINCH -> R.drawable.grinch
}

@Composable
private fun GiftCountItem(
    giftCount: GiftCount,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = giftCount.toDrawableRes()),
            contentDescription = "${giftCount.value} cadeaux",
            modifier = Modifier.size(size),
            contentScale = ContentScale.Fit
        )
    }
}
