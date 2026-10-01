# wear-bridge-examples

Wear OS example apps built on [`wearbridge-sdk`](https://github.com/0xberkay/wearbridge-sdk).
Shizuku-style power tools, ported to the wrist via the consent-first WearBridge broker.

> **Requires:** [WearBridge Manager](https://play.google.com/store/apps/details?id=com.zbd.wearbridge)
> on the watch. Each app binds `com.zbd.wearbridge`, asks on-watch consent
> (package name + SHA-256 shown), then runs typed tasks. No root, no raw shell.

## Apps

| App | Package | WearBridge task | SDK |
|---|---|---|---|
| [wear-perm-list](examples/wear-perm-list) | `com.zbd.wearbridge.examples.permlist` | `core.*`, `adb.dumpsys` live · `privacy.perm.*` planned | 1.0.0 |
| [wear-dpi-switcher](examples/wear-dpi-switcher) | `com.zbd.wearbridge.examples.dpi` | `display.density.set/reset` (200/240/320) | 1.1.0 planned |
| [wear-custom-animator](examples/wear-custom-animator) | `com.zbd.wearbridge.examples.animator` | `ui.anim.set` (off/0.5x/1x/2x) | 1.1.0 planned |

`perm-list` runs today on SDK 1.0.0. DPI + animator UIs are wired and log honestly
(`TASK_NOT_ALLOWED` until SDK 1.1.0 exposes the typed tasks already present in the
manager's Pro catalog).

## Build

```bash
./gradlew assembleDebug   # all 3 APKs
./gradlew test            # unit tests
```

APKs: `examples/<name>/build/outputs/apk/debug/*-debug.apk`.
Release APKs ship via GitHub Releases on every `v*` tag (see `release.yml`).

## Template for the next example

1. Copy `examples/wear-perm-list` → `examples/wear-<name>`.
2. `settings.gradle.kts` → `include(":examples:wear-<name>")`.
3. `build.gradle.kts`: `namespace`, `applicationId=com.zbd.wearbridge.examples.<name>`, `versionCode`.
4. Manifest keeps `BIND_API` + `<package android:name="com.zbd.wearbridge" />` queries.
5. Reuse `data/WearBridgeClient.kt` as-is; extend `MainScreenViewModel` with task calls.
