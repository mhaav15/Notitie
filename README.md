# Notitie

Minimal Android app with a **fullscreen home-screen widget** for reminders. Built for a **Xiaomi 15T Pro** launcher grid of **7 columns × 4 rows**. The widget is the primary UI: swipe to a home screen filled with Notitie, type a reminder, and see every note in the same widget.

Notes are stored **only on the device** (`notes.json` in the app’s private files).

## Widget (7×4)

The app widget requests **7×4 cells** (`targetCellWidth=7`, `targetCellHeight=4`) and can be resized.

Android widgets cannot host a reliable `EditText` inside `RemoteViews`. Tapping **Typ een herinnering…** / **Opslaan** opens a compact overlay: type the reminder and save with one tap. The scrollable list of **all notes** stays in the widget, with done (○ / ✓) and **Verwijderen** on each row.

## Xiaomi / HyperOS / MIUI — add the widget

1. Install `Notitie.apk` and open **Notitie** once (Android requirement before some launchers list the widget).
2. Go to an **empty home-screen page** (or add a new page).
3. **Long-press** an empty area of the home screen.
4. Tap **Widgets**.
5. Find **Notitie** and tap / drag it onto that page.
6. **Long-press the widget** and **resize** it until it fills the page (**7×4** on a 7-column × 4-row grid).
7. Tap the input bar, type a reminder, tap **Opslaan**. All notes appear in the same widget; scroll if the list is long. Tap ○/✓ to mark done, **Verwijderen** to delete.

If the widget is missing from the picker: Settings → Apps → **Notitie** → make sure the app is not restricted; open the app once more and try Widgets again.

## Download

- **GitHub Release:** https://github.com/mhaav15/Notitie/releases/tag/v1.0.0 (`Notitie.apk`)
- **Actions** artifact on the `Android` workflow
- Build locally: `./gradlew assembleRelease` → `app/build/outputs/apk/release/`

## Build

Requires JDK 17 and the Android SDK (compile SDK 35).

```bash
export ANDROID_HOME=/path/to/android-sdk
./gradlew testDebugUnitTest assembleRelease
```

The release keystore lives at `app/keystore/notitie-release.jks` so a personal installable APK can be built without extra secrets. Replace it if you publish under your own identity.

## Why native Kotlin (not Flutter)

Flutter `home_widget` cannot offer reliable in-widget text entry. This project uses a **Kotlin App Widget + RemoteViews** list (`ListView` / `RemoteViewsService`) and a one-tap **QuickAdd** overlay for typing.
