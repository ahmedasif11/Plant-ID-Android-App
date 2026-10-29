# Plant-ID-Android-App

Assignment No. 2 — Plant Water Advisor. A mini Android app that predicts cups of water from a soil-dryness score using TensorFlow Lite on the device.

Reference repository: https://github.com/Uzair390-Del/NumberGuessingAppTflite.git

## Repository layout

```
.
├── PlantWaterAdvisor/          Android Studio project
│   ├── app/                    App module, UI, and water.tflite
│   ├── gradle/                 Gradle wrapper and version catalog
│   └── .gitignore              Android build and IDE ignores
├── reports/                    Theory, practical, and code-snippet PDFs
├── screenshots/                Home, result, and invalid-input screens
└── PlantWaterAdvisor.zip       Zipped Android Studio project
```

| Deliverable | Location |
| --- | --- |
| Android Studio project | `PlantWaterAdvisor/` |
| Zipped project | `PlantWaterAdvisor.zip` |
| Theory report | `reports/Assignment-No-2-Theory.pdf` |
| Practical report | `reports/Practical-Implementation-Report.pdf` |
| Code snippets | `reports/Code-Snippets.pdf` |
| Screenshots | `screenshots/` |
| How to run | `PlantWaterAdvisor/README.md` |

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

The on-device model is one dense layer: `cups = 2 * dryness`, with bias 0. Fill in the roll number and course on the report title pages before submitting.
