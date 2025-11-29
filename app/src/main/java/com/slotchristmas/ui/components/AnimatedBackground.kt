package com.slotchristmas.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slotchristmas.R

@Composable
fun AnimatedBackground(
    backgroundResId: Int,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = backgroundResId,
        animationSpec = tween(durationMillis = 1000),
        modifier = modifier,
        label = "backgroundCrossfade"
    ) { resId ->
        if (resId != 0) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(resId)
                    .crossfade(true)
                    .build(),
                contentDescription = "Christmas background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback to a solid color or placeholder
            Image(
                painter = painterResource(id = R.drawable.bg_christmas_1),
                contentDescription = "Christmas background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
