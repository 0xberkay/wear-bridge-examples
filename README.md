# wear-bridge-examples

Wear OS example apps built on [`wearbridge-sdk`](https://github.com/0xberkay/wearbridge-sdk).
Shizuku-style power tools, ported to the wrist via the consent-first WearBridge broker.

> **Requires:** [WearBridge Manager](https://play.google.com/store/apps/details?id=com.zbd.wearbridge)
> on the watch. Each app binds `com.zbd.wearbridge`, asks on-watch consent
> (package name + SHA-256 shown), then runs typed tasks. No root, no raw shell.

## Apps

| App | Package | WearBridge task | SDK |
|---|---|---|---|
| [wear-perm-list](examples/wear-perm-list) | `com.zbd.wearbridge.examples.permlist` | `core.*`, `adb.dumpsys` live · `privacy.perm.*` planned | 1.1.0 |
| [wear-dpi-switcher](examples/wear-dpi-switcher) | `com.zbd.wearbridge.examples.dpi` | `display.density.set/reset` (200/240/280/320) | 1.1.0 |
| [wear-custom-animator](examples/wear-custom-animator) | `com.zbd.wearbridge.examples.animator` | `ui.anim.set` (off/0.5x/1x/2x) | 1.1.0 |

`perm-list` currently shows bridge diagnostics, not installed-app permissions.
DPI and animator require the updated manager's typed-task catalog and a running shell server.
SDK 1.1.0 opens the manager's per-execution confirmation from the foreground example app.
Grant access first, choose a preset, then select **Allow once** in WearBridge.
Unanswered confirmations expire after 60 seconds.

## Build

These apps resolve `io.github.0xberkay:wearbridge-sdk:1.1.0` from Maven Central.
The single SDK artifact includes the protocol classes and AIDL interfaces.

All three apps use **Compose for Wear OS Material 3**, with `AppScaffold`,
`ScreenScaffold`, rotary-enabled `TransformingLazyColumn`, transforming list buttons,
black backgrounds and single-column layouts. Shared watch UI lives in `common/`.

```bash
./gradlew assembleDebug   # all 3 APKs
./gradlew test            # unit tests
```

APKs: `examples/<name>/build/outputs/apk/debug/*-debug.apk`.
Release APKs ship via GitHub Releases on every `v*` tag (see `release.yml`).

## Download and install

Download the three signed APKs from [GitHub Releases](https://github.com/0xberkay/wear-bridge-examples/releases).
Install the desired APK on the watch with `adb install <file.apk>`.
If replacing a locally debug-signed build, uninstall that example first because its
certificate differs from the release certificate. Then connect and grant access again.
DPI and animator also ask for **Allow once** before each change.

For signed local release builds, set `EXAMPLES_KEYSTORE_FILE`,
`EXAMPLES_KEYSTORE_PASSWORD`, `EXAMPLES_KEY_ALIAS`, and `EXAMPLES_KEY_PASSWORD`,
then run `./gradlew assembleRelease`. GitHub Actions reads these from repository secrets.

## Template for the next example

1. Copy `examples/wear-perm-list` → `examples/wear-<name>`.
2. `settings.gradle.kts` → `include(":examples:wear-<name>")`.
3. `build.gradle.kts`: `namespace`, `applicationId=com.zbd.wearbridge.examples.<name>`, `versionCode`.
4. Manifest keeps `BIND_API` + `<package android:name="com.zbd.wearbridge" />` queries.
5. Reuse `data/WearBridgeClient.kt` as-is; extend `MainScreenViewModel` with task calls.
