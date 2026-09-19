# Notitie

Minimal Android app with **two fullscreen home-screen widgets**. Notes stay **on the device**.

| Picker-naam | Wat het is |
| --- | --- |
| **Notitie** | Lijst herinneringen: snel toevoegen, scrollen, gedaan / verwijderen |
| **Notitieblok** | Leeg papier: vrij typen (meerdere regels), automatisch opslaan |

Beide widgets vragen bij plaatsing een **volledige pagina** (`minWidth`/`minHeight` ≈ 7×6 cellen). HyperOS gebruikt die minimummaat als eerste formaat — daarom landt het niet meer als 2×2. Past het raster niet, dan klempt de launcher naar het grootste beschikbare vak (bijv. 4×6 of 7×4).

Android `RemoteViews` kan geen betrouwbare `EditText` in een widget. Daarom:

- **Notitie:** tik op het invoerveld → overlay → typ + **Opslaan**
- **Notitieblok:** tik op het papier → fullscreen blad → typ vrij; tekst komt terug op het widget

## Xiaomi / HyperOS

1. Installeer `Notitie.apk` en open **Notitie** één keer.
2. Zet het **startscherm-raster zo groot mogelijk**: lang indrukken op het startscherm → instellingen / **Rasterindeling**. Voor een volle pagina: **7×4** of **4×6**, niet een klein 4-koloms raster als je 7×4 wilt.
3. **Verwijder** een eerder geplaatst klein (2×2) widget — een update verandert de maat van een bestaand widget niet.
4. Lang indrukken → **Widgets** → **Notitie** of **Notitieblok** → op een **lege pagina** zetten.
5. Het widget moet de pagina vullen. Blijft het klein: raster op max, widget weghalen, opnieuw toevoegen.

Slepen om te verkleinen kan als HyperOS hendels toont; dat is optioneel.

## Download

- **GitHub Release:** https://github.com/mhaav15/Notitie/releases/tag/v1.2.0 (`Notitie.apk`)
- **Actions** artifact op de `Android` workflow
- Lokaal: `./gradlew assembleRelease`

## Build

JDK 17, compile SDK 35.

```bash
export ANDROID_HOME=/path/to/android-sdk
./gradlew testDebugUnitTest assembleRelease
```
