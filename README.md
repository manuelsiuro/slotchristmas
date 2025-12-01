# SlotChristmas - Christmas Gift Exchange Slot Machine

A festive Android slot machine app for Christmas gift exchanges built with Kotlin and Jetpack Compose.

## Features

- **3-Reel Slot Machine**: Chooser, Receiver, and Gift Count (1-4)
- **Polished Animations**: Spring physics, exponential decay, smooth 60fps scrolling
- **Audio System**: Background music + sound effects (MediaPlayer + SoundPool)
- **Jackpot Celebrations**: Particle effects when someone gets 4 gifts
- **Landscape Mode**: Optimized for horizontal display
- **Receiver Management**: Remove participants who have received all their gifts
- **Christmas String Lights Border**: Animated multicolor lights around panels with 4 cycling modes (Twinkling, Chase, Pulsing, Color Cycling)
- **Festive Typography**: Lobster font from Google Fonts for a friendly, holiday feel

## Project Structure

```
app/src/main/
├── java/com/slotchristmas/
│   ├── MainActivity.kt              # Entry point
│   ├── SlotChristmasApp.kt          # Application class
│   ├── animation/
│   │   ├── AnimationConfig.kt       # Animation timing constants
│   │   └── ReelAnimationController.kt # 4-phase reel animation
│   ├── audio/
│   │   ├── AudioManager.kt          # Music + SFX handling
│   │   └── SoundEffect.kt           # Sound effect enum
│   ├── config/
│   │   └── AssetConfig.kt           # Asset resource mappings
│   ├── data/
│   │   └── ParticipantRepositoryImpl.kt # Participant data source
│   ├── di/
│   │   └── AppModule.kt             # Manual dependency injection
│   ├── domain/
│   │   ├── model/
│   │   │   ├── GiftCount.kt         # Gift count with weighted random
│   │   │   ├── Participant.kt       # Participant data class
│   │   │   └── SpinResult.kt        # Spin result data
│   │   └── repository/
│   │       └── ParticipantRepository.kt # Repository interface
│   ├── ui/
│   │   ├── components/
│   │   │   ├── AnimatedBackground.kt    # Cycling backgrounds
│   │   │   ├── CircularParticipantImage.kt # Circular photo display
│   │   │   ├── FestiveMessage.kt        # Bottom festive messages
│   │   │   ├── GameOverOverlay.kt       # Game complete screen
│   │   │   ├── GiftCountReel.kt         # Number reel (1-4)
│   │   │   ├── ReceiverPanel.kt         # Side panel with receivers
│   │   │   ├── ReelStrip.kt             # 3-reel container
│   │   │   ├── ResultDisplay.kt         # Spin result display
│   │   │   ├── SlotReel.kt              # Single reel component
│   │   │   ├── SpinButton.kt            # Animated spin button
│   │   │   ├── VolumeControl.kt         # Mute toggle
│   │   │   └── effects/
│   │   │       ├── CandyCaneBorder.kt    # Animated candy cane border
│   │   │       ├── ChristmasLightsBorder.kt # String lights border effect
│   │   │       ├── JackpotParticles.kt  # Confetti particle system
│   │   │       └── SelectionFrame.kt    # Pulsing gold frame
│   │   ├── slot/
│   │   │   ├── SlotScreen.kt            # Main screen composable
│   │   │   ├── SlotUiState.kt           # UI state data class
│   │   │   └── SlotViewModel.kt         # Business logic
│   │   └── theme/
│   │       ├── Color.kt                 # Color definitions
│   │       ├── Theme.kt                 # Material3 theme
│   │       └── Type.kt                  # Typography
│   └── util/
│       └── FestiveMessages.kt           # Random message generator
└── res/
    ├── drawable/                        # Placeholder graphics
    ├── font/                            # Custom fonts (Lobster)
    ├── raw/                             # Audio files (empty placeholders)
    └── values/                          # Colors, strings, themes
```

## Remaining Tasks

### High Priority

1. **Add Real Audio Files**

   Replace empty placeholder files in `res/raw/`:
   - `christmas_music.mp3` - Background music loop
   - `sfx_spin_start.ogg` - Spin button press sound
   - `sfx_spinning_loop.ogg` - Reel spinning sound
   - `sfx_reel_stop.ogg` - Reel landing sound
   - `sfx_jackpot.ogg` - 4-gift celebration sound
   - `sfx_button_click.ogg` - UI click sound

2. **Add Real Participant Photos**

   Replace placeholder drawables in `res/drawable/`:
   - `participant_1.xml` through `participant_10.xml` (currently colored circles)
   - Add actual circular photos of your party participants
   - Update `AssetConfig.kt` with correct resource IDs

3. **Add Christmas Background Images**

   Replace gradient placeholders:
   - `bg_christmas_1.xml` through `bg_christmas_5.xml`
   - Use actual Christmas-themed photos

### Medium Priority

4. **Customize Participant List**

   Edit `data/ParticipantRepositoryImpl.kt`:
   ```kotlin
   private val participants = listOf(
       Participant(1, "Mom", R.drawable.photo_mom, 2),
       Participant(2, "Dad", R.drawable.photo_dad, 2),
       // Add your actual participants...
   )
   ```

5. **Tune Animation Timing**

   Edit `animation/AnimationConfig.kt` to adjust:
   - `spinUpDurationMs` - Acceleration phase (default: 300ms)
   - `sustainedSpinDurationMs` - Main spin duration (default: 2000ms)
   - `maxVelocity` - Top spinning speed (default: 15000f)
   - `landingStiffness` - Bounce stiffness (default: 800f)

6. **Customize Gift Rarity**

   Edit `domain/model/GiftCount.kt`:
   ```kotlin
   enum class GiftCount(val value: Int, val weight: Int) {
       ONE(1, 40),    // 40% chance
       TWO(2, 35),    // 35% chance
       THREE(3, 20),  // 20% chance
       FOUR(4, 5)     // 5% chance (jackpot!)
   }
   ```

### Low Priority

7. **Add Festive Messages**

   Edit `util/FestiveMessages.kt` to add more messages

8. **Customize Colors**

   Edit `ui/theme/Color.kt` for your color scheme

## Troubleshooting

### Build Errors

1. **"gradle-wrapper.jar not found"**
   - Run `gradle wrapper` in terminal
   - Or File → Invalidate Caches and Restart

2. **"SDK not found"**
   - Create `local.properties` with:
     ```
     sdk.dir=/Users/YOUR_USERNAME/Library/Android/sdk
     ```

3. **Compose compiler issues**
   - Ensure Kotlin version matches Compose compiler version
   - Current: Kotlin 1.9.22, Compose BOM 2024.02.00

### Runtime Issues

1. **No sound**
   - Check audio files exist in `res/raw/`
   - Verify device is not muted
   - Check AudioManager logs in Logcat

2. **Images not showing**
   - Verify drawable resources exist
   - Check resource IDs in `AssetConfig.kt`

## Architecture

- **UI Layer**: Jetpack Compose with Material3
- **State Management**: ViewModel + StateFlow
- **Animation**: Compose Animatable with spring/exponentialDecay
- **Audio**: MediaPlayer (music) + SoundPool (SFX)
- **DI**: Manual injection via AppModule (no Hilt overhead)
- **Image Loading**: Coil with CircleCropTransformation

## License

Private project for Christmas party use.
