# DriveTap

DriveTap is a small Android + Android Auto app for triggering user-configured HTTP endpoints from a car-safe interface.

The phone app lets you configure up to 10 taps. Each tap can send a `GET` or `POST` request, use Basic Auth, optionally send credentials as `user` / `pass` parameters, include additional form/query variables, and choose a color and Android Auto icon. The Android Auto view exposes those configured taps as large grid actions.

Repository: [github.com/danielnavarrogomez/drivetap](https://github.com/danielnavarrogomez/drivetap)

## Features

- Configure up to 10 endpoint actions on the phone.
- Supports `GET` and `POST`.
- Supports Basic Auth, similar to `curl -u USER:PASS`.
- Can send credentials as `user` and `pass` variables.
- Supports extra variables as `VAR=VALUE` lines.
- Per-action color and predefined Android Auto icon.
- Android Auto integration using the AndroidX Car App Library IoT category.
- Localized phone UI for many European languages.

## Android Auto Notes

DriveTap is implemented as an AndroidX Car App Library template app in the `IOT` category. Android Auto controls most of the in-car layout and visual styling, so the app uses compliant templates and generated action icons rather than custom in-car views.

For local testing, use Google's Android Auto Desktop Head Unit:

```bash
cd /usr/local/share/android-commandlinetools/extras/google/auto
./desktop-head-unit --usb
```

## Build

Install Android Studio or the Android SDK command-line tools, then build the debug APK:

```bash
./gradlew assembleDebug
```

The APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Install on a connected phone:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Development Setup

This project currently uses:

- Android Gradle Plugin 8.7.3
- AndroidX Car App Library 1.7.0
- compileSdk 35
- minSdk 26
- Java 17 source compatibility

If using command-line tools only, make sure `ANDROID_HOME` / `ANDROID_SDK_ROOT` point to your Android SDK and that platform/build tools for API 35 are installed.

## Security

This is an early prototype. Endpoint credentials are currently stored in app preferences for simplicity. Before broad distribution, migrate credential storage to AndroidX Security / encrypted preferences and review privacy notices for user-provided endpoint data.

## License

DriveTap is released under the MIT License. See [LICENSE](LICENSE).
