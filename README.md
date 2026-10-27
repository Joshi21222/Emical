# EmiCal

A premium Android EMI calculator built with Kotlin and Jetpack Compose.

## Features
- Monthly EMI estimate based on principal, annual interest rate, and tenure
- Total interest and total repayment summary
- Principal-versus-interest breakdown
- Dark navy interface with mint accents
- Responsive input fields and a simple bottom navigation
- Automated debug APK build with GitHub Actions

## Build locally
Requirements: JDK 17, Android SDK (API 35), and Gradle 8.9.

```bash
gradle assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Download an APK from GitHub
1. Open the repository's **Actions** tab.
2. Select **Build EmiCal APK** and open the latest successful run.
3. Download the `EmiCal-debug-apk` artifact.
4. Extract the ZIP to find `app-debug.apk`.

The app provides estimates; actual lender EMI and fees may vary.
