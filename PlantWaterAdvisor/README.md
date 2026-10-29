# Plant Water Advisor

A small Android app that estimates how many cups of water a plant needs from a soil-dryness score. The estimate is produced on the phone with TensorFlow Lite.

This mini project is based on the structure of [NumberGuessingAppTflite](https://github.com/Uzair390-Del/NumberGuessingAppTflite.git). That repository loads `linear.tflite`, passes one float in, and shows one float out. Plant Water Advisor keeps that pattern and uses it for a plant-watering score.

## Model

`app/src/main/assets/water.tflite` is the reference one-layer dense model, with the weight set to 2.0 and the bias set to 0.0.

```
cups = (2.0 * dryness) + 0.0
```

Dryness is a number from 0 to 10. Example: dryness 4 returns 8.0 cups. A normal conversion does the same job: train or set a Keras `Dense` layer, then write it with `TFLiteConverter`. This file keeps the reference graph and only changes those two numbers.

## Prerequisites

- Android Studio with Android SDK 35
- JDK 17 (Android Studio’s bundled JDK is fine)
- A phone or emulator running Android 7.0 (API 24) or newer

No server, API key, or internet connection is required after the Gradle dependencies are downloaded.

## Run

1. Open the `PlantWaterAdvisor` folder in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device.
4. Enter a dryness value from 0 to 10.
5. Tap **Predict water**.

| Dryness | Expected result |
| --- | --- |
| 0 | 0.0 cups |
| 4 | 8.0 cups |
| 10 | 20.0 cups |
| blank, text, or a value outside 0–10 | an error message, no crash |

## Project layout

```
app/src/main/java/com/plantwise/wateradvisor/MainActivity.java
app/src/main/res/layout/activity_main.xml
app/src/main/assets/water.tflite
app/build.gradle
```

`MainActivity` maps the model into a `MappedByteBuffer`, creates an `Interpreter`, builds a `float[1]` input and a `float[1][1]` output, calls `interpreter.run`, and writes the result into the result `TextView`.

## Acknowledgement

Original reference repository: https://github.com/Uzair390-Del/NumberGuessingAppTflite.git

On-device inference uses the TensorFlow Lite Android library (`org.tensorflow:tensorflow-lite:2.16.1`).
