# Release builds in this mini project do not shrink code.
# If minify is turned on later, keep the TensorFlow Lite interpreter classes.
-keep class org.tensorflow.lite.** { *; }
