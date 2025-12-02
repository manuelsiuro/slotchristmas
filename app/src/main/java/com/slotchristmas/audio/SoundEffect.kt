package com.slotchristmas.audio

import androidx.annotation.RawRes
import com.slotchristmas.R
// https://pixabay.com/sound-effects/search/christmas/
enum class SoundEffect(@RawRes val resId: Int) {
    SPIN_START(R.raw.sfx_spin_start),
    SPINNING_LOOP(R.raw.sfx_spinning_loop),
    REEL_STOP(R.raw.sfx_reel_stop),
    JACKPOT(R.raw.sfx_jackpot),
    BUTTON_CLICK(R.raw.sfx_button_click)
}
