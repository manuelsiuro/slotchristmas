# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Coil image loading
-keep class coil.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
