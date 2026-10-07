# CONTINUAR — Panel BToLabs

> Antes de seguir: lee completo `~/.claude/CLAUDE.md` (reglas generales) y usa la bóveda de Obsidian del proyecto Grupo VL (`../Grupo VL/boveda/`, empezar por `Índice.md` y `Diagnóstico mes cero.md`).

Actualizado: 7-oct-2026.

## Qué es
Panel interno de trabajo de Alberto (BToLabs), **solo lectura**: diagnóstico de hoy (mes cero) de las webs y redes de Grupo VL, estado de accesos, datos por confirmar y descargas (PDF y CSV). Web y APK de Android con el mismo `docs/index.html`.

## Estructura
- `docs/index.html`: toda la app (HTML, CSS y JS en un archivo, sin dependencias externas; funciona sin internet dentro de la APK).
- `docs/datos.json`: **los datos**. Para actualizar el panel se edita este archivo y se publica: la web y la APK lo leen al abrir (la APK pide `https://ttolabs.github.io/btolabs-panel/datos.json`; sin red usa la última copia guardada o la que trae adentro).
- `android/`: proyecto Capacitor 6 (`cl.btolabs.panel`, webDir `docs`). Pide permiso de notificaciones (regla general). `localStorage` envuelto en try/catch.

## Decisiones de Alberto (no re-litigar)
- Solo lectura: Alberto lee y descarga; Claude actualiza `datos.json`.
- **Sin actualizador de APK** (excepción explícita a la regla general, 7-oct-2026): la estructura del panel no va a cambiar; lo que cambia son los datos, que se leen en línea.
- **Sin contraseña** para ver el panel, por ahora. Ningún dato guarda contraseñas: los accesos se describen (plataforma, estado, rol, cuenta usada).
- Más adelante Alberto contratará un **hosting propio** y se manejará todo en línea: basta con copiar `docs/` y cambiar `DATOS_REMOTOS` y `WEB` en `index.html`.

## Estado (7-oct-2026)
- Web probada en local (`python -m http.server 8766 --directory docs`): las 6 secciones sin errores de consola, sin desborde a 375 px, CSV correcto (separador `;`, BOM), PDF generado con Chrome headless, 3 páginas carta.
- APK debug compilada: `android/app/build/outputs/apk/debug/app-debug.apk` (3,8 MB). Trae `assets/public/index.html` y `datos.json`, plugin de notificaciones y los permisos INTERNET y POST_NOTIFICATIONS. **No se probó en un teléfono ni en un emulador.**
- Repo local con commits. **No publicado:** el sistema de permisos bloqueó crear el repo público `TToLabs/btolabs-panel` y activar Pages. Hasta que se publique, la APK usa los datos que trae adentro.

## Pendientes
1. Que Alberto autorice publicar (repo público + Pages), o que defina otro hosting.
2. Instalar la APK en el teléfono y comprobar que abre y lee los datos.
3. Cargar los números de LinkedIn cuando Alberto los cuente.
4. Después de la reunión: actualizar accesos y datos confirmados.

## Cómo actualizar datos
Editar `docs/datos.json` → commit → push (cuando exista el remoto). La APK no se recompila.
Recompilar la APK solo si cambia `index.html`: `npx cap sync android && cd android && gradlew.bat assembleDebug`.

## Trampas
- La API de PageSpeed sin clave tiene cuota 0 (error 429). Las mediciones se hicieron con Lighthouse local: `npx -y lighthouse@12 <url> [--preset=desktop]`.
- La carga del elemento principal (LCP) de grupovl.cl en celular varió entre 41,6 y 115,8 s en 2 corridas; se publica la menor.
- Chrome headless para PDF necesita `--user-data-dir` propio si el Chrome de Alberto está abierto.
