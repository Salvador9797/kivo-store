# Proposed update to F-Droid RFP #4646

Copy the text below into https://gitlab.com/fdroid/rfp/-/work_items/4646. This file is an upstream preparation document, not evidence that the update was posted or approved.

---

Upstream update, 2026-10-28:

Kivo has moved from `app.kivo.store` to **`app.kivo.market`**. Please assess this new application ID; it is a distinct install and should not be treated as an update to the old package.

- Source: https://github.com/Salvador9797/kivo-store
- Current version: **0.9.13**, versionCode **20**.
- Tag: `v0.9.13`; source commit: `d727802a023d21e96a2d1ac74b388431305c8fd6`.
- APK: https://github.com/Salvador9797/kivo-store/releases/download/v0.9.13/Kivo-0.9.13.apk
- APK SHA-256: `7a589dac0ac7759564fdc468b198bb0f1656ffcd197e00d17979bd5f4d998e20`.
- Developer certificate SHA-256: `9678c2392788e6c890d5555d0bfcf94a054c319575fe7599aa384f45316b9cc2`. Developer-key reuse or reproducibility is not requested.
- Gradle and manifest agree on application ID, versionName and versionCode. Java namespace remains `app.kivo.store` with fully qualified Android component names.
- JDK 17, Gradle 8.9, AGP 8.7.3, SDK 35, minSdk 26.
- Build: `bash gradlew :app:assembleRelease --no-daemon`. No signing credentials are needed for this unsigned release build. The downloadable developer APK uses the separate SDK/ECJ builder.

The wrapper JAR SHA-256 is `498495120a03b9a6ab5d155f5de3c8f0d986a449153702fb80fc80e134484f17`, matching https://services.gradle.org/distributions/gradle-8.9-wrapper.jar.sha256. The properties pin the binary distribution SHA-256 `d725d707bfabd4dfdc958c624003b3c80accc03f7037b5122c4b1d0ef15cecab`, matching https://services.gradle.org/distributions/gradle-8.9-bin.zip.sha256. Please reassess the historical `insecure-gradlew` label; upstream does not claim authority to remove F-Droid review labels.

Debug compilation and automated Java/UI tests have passed on GitHub Actions. Upstream CI is being extended to compile the unsigned release variant and explicitly verify the wrapper checksums; the corresponding run must pass before this additional check is considered verified. This is not a build in F-Droid's environment, and bit-for-bit reproducibility is not claimed.

The application now also connects to IzzyOnDroid and has an experimental itch.io integration. GitHub and itch.io authentication are optional; web games and downloads may rely on proprietary network services or non-free third-party content. Please assess NonFreeNet and whether a separate build flavor or other changes are necessary. Automatic synchronization does not remove the need for this review.

Original code and Kivo icon: GPL-3.0-or-later. Third-party notices remain in THIRD_PARTY_NOTICES.md. An artwork-specific license audit is still outstanding; overall inclusion compliance is not marked complete. The new illustrative design previews under docs/previews are explicitly not device screenshots. Existing fastlane screenshots show an earlier UI and need replacement with verified current screenshots.

The project is still newly public and does not establish three months of public development. Please advise on eligibility and remaining packaging requirements. The package change, checksums and build improvements do not resolve this age requirement or imply approval.

Privacy policy: https://github.com/Salvador9797/kivo-store/blob/main/PRIVACY.md
