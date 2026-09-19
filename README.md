# Notitie

Minimal Android app with **two home-screen widgets**. Made for a **Xiaomi 15T Pro** (grid 7 columns × 4 rows), but **you choose the size**.

Alles blijft **lokaal** op het toestel.

## Twee widgets

| Picker-naam | Wat het is |
| --- | --- |
| **Notitie** | Lijst herinneringen: snel toevoegen, scrollen, gedaan / verwijderen |
| **Notitieblok** | Leeg notitieblok: vrij typen (meerdere regels), automatisch opslaan |

Beide zijn **vrij te vergroten en verkleinen**. Aanbevolen startmaat is **7×4** (één startscherm-pagina). Kleinere maten werken ook, bijvoorbeeld **2×2**, **4×2**, **4×4**, **5×4**, en alles wat de launcher tussen min en max toestaat.

Android `RemoteViews` kan geen betrouwbare `EditText` in een widget zetten. Daarom:

- **Notitie:** tik op het invoerveld → compact overlay → typ + **Opslaan**
- **Notitieblok:** tik op het papier → fullscreen notitieblok dat er hetzelfde uitziet → typ vrij; tekst staat daarna weer op het widget

## Formaat kiezen (Xiaomi / HyperOS / MIUI)

1. Installeer `Notitie.apk` en open **Notitie** één keer.
2. Houd een leeg startscherm **ingedrukt**.
3. Tik op **Widgets**.
4. Kies **Notitie** of **Notitieblok** en sleep het op het scherm.
5. **Houd het widget ingedrukt** tot de **hendels** (hoekpunten) verschijnen.
6. **Sleep de hendels** naar het formaat dat je wilt — 2×2 tot een volledige pagina (7×4).

Als het widget in de kiezer ontbreekt: Instellingen → Apps → **Notitie** → niet beperken; open de app nog eens.

## Download

- **GitHub Release:** https://github.com/mhaav15/Notitie/releases/tag/v1.1.0 (`Notitie.apk`)
- **Actions** artifact op de `Android` workflow
- Lokaal: `./gradlew assembleRelease` → `app/build/outputs/apk/release/`

## Build

JDK 17 en Android SDK (compile SDK 35).

```bash
export ANDROID_HOME=/path/to/android-sdk
./gradlew testDebugUnitTest assembleRelease
```

De release-keystore staat in `app/keystore/notitie-release.jks`. Vervang die als je onder een eigen identiteit publiceert.
