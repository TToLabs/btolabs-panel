# CONTINUAR — Panel BToLabs

> Antes de seguir: lee completo `~/.claude/CLAUDE.md` (reglas generales) y usa la bóveda de Obsidian del proyecto Grupo VL (`../Grupo VL/boveda/`, empezar por `Índice.md` y `Diagnóstico mes cero.md`).

Actualizado: 7-oct-2026.

## Qué es
Panel interno de trabajo de Alberto (BToLabs), **solo lectura**: diagnóstico de hoy (mes cero) de las webs y redes de Grupo VL, estado de accesos, datos por confirmar y descargas (PDF y CSV). Web y APK de Android con el mismo `docs/index.html`.

## Estructura
- `docs/index.html`: toda la app (HTML, CSS y JS en un archivo, sin dependencias externas; funciona sin internet dentro de la APK).
- `docs/datos.json`: **los datos**. Para actualizar el panel se edita este archivo y se publica: la web y la APK lo leen al abrir (la APK pide `https://ttolabs.github.io/btolabs-panel/datos.json`; sin red usa la última copia guardada o la que trae adentro).
- `android/`: proyecto Capacitor 6 (`cl.btolabs.panel`, webDir `www`, generado por `scripts/preparar-www.js`). Pide permiso de notificaciones (regla general). `localStorage` envuelto en try/catch.

## Decisiones de Alberto (no re-litigar)
- Solo lectura: Alberto lee y descarga; Claude actualiza `datos.json`.
- **Sin actualizador de APK** (excepción explícita a la regla general, 7-oct-2026): la estructura del panel no va a cambiar; lo que cambia son los datos, que se leen en línea.
- **Sin contraseña** para ver el panel, por ahora. Ningún dato guarda contraseñas: los accesos se describen (plataforma, estado, rol, cuenta usada).
- Más adelante Alberto contratará un **hosting propio** y se manejará todo en línea: basta con copiar `docs/` y cambiar `DATOS_REMOTOS` y `WEB` en `index.html`.

## Estado (7-oct-2026)
- Web probada en local (`python -m http.server 8766 --directory docs`): las 6 secciones sin errores de consola, sin desborde a 375 px, CSV correcto (separador `;`, BOM), PDF generado con Chrome headless, 3 páginas carta.
- APK debug compilada: `android/app/build/outputs/apk/debug/app-debug.apk` (3,8 MB). Trae `assets/public/index.html` y `datos.json`, plugin de notificaciones y los permisos INTERNET y POST_NOTIFICATIONS. Después se instaló y probó en el teléfono (ver abajo).
- Publicado: ver "Cambios del 7-oct (tarde)".

## Cambios del 7-oct (tarde)
- **Publicado** con autorización de Alberto: repo público `TToLabs/btolabs-panel`, Pages desde `/docs` → https://ttolabs.github.io/btolabs-panel/ . APK descargable: `/panel-btolabs.apk`.
- **Ícono** con el logo de Grupo VL (la web solo lo tiene en 106×91; recortado y al 80 % de la capa frontal). Fuentes en `assets/`; se regenera con `npx capacitor-assets generate --android --iconBackgroundColor "#ffffff"`.
- **Estilo ejecutivo** (pedido de Alberto): siempre fondo blanco, sin modo oscuro, azul marino y grises. Menú en dos grupos: "Informe para gerencia" (Resumen ejecutivo, Webs, LinkedIn, Descargas) y "Uso interno" (Accesos, Por confirmar). El resumen ya no muestra el estado de los accesos.
- APK instalada y probada en el teléfono de Alberto (Samsung A23, adb `R5CTC0T1W4W`): abre, lee los datos y no hay errores en logcat. Pages entrega `datos.json` con CORS `*`.
- **Decisión de Alberto (7-oct): este es SU panel.** Hoy contiene el diagnóstico de mes cero. Sobre esta misma base se agregan el checklist mensual, las guías por ítem, la vista en vivo (solo para él) y el informe semanal y mensual para la gerencia.

## Cambios del 8-oct
- **Sección nueva "Plan de mejora"** (en "Informe para gerencia"): hallazgos verificados con sus causas, orden recomendado (Primero / Después / Tras la reunión / Según el contrato), tabla de problemas y soluciones, y los **costos para Grupo VL** (honorarios y herramientas de terceros). Datos en `docs/datos.json` clave `plan` (incluye `plan.costos`).
- **Decisión de Alberto (8-oct):** las mejoras de "Primero" y "Después" van incluidas en el plan mensual; no se cobran aparte ni por horas. El valor de $90.000 por sitio sigue sin confirmar.
- Precios de terceros consultados el 8-oct-2026: WP Rocket (wp-rocket.me) verificado; ShortPixel solo por sitios de comparación (no se pudo leer su página oficial); Smush Pro sin precio.
- Paseo guiado con voz y subtítulos: scripts en la carpeta temporal del equipo (no están en el repo). Ver `Grupo VL/boveda/Plan técnico y costos.md` para el detalle.
- **La APK instalada en el teléfono es la anterior**: no muestra "Plan de mejora" hasta reinstalar la nueva (`docs/panel-btolabs.apk`). El teléfono no estaba conectado por adb.

## Pendientes
0. Reinstalar la APK en el teléfono (bajarla de la web o por adb).
1. Agregar al panel: checklist mensual, guías por ítem, vista en vivo (solo Alberto) e informe semanal y mensual para la gerencia.
2. Cargar los números de LinkedIn cuando Alberto los cuente.
3. Después de la reunión: actualizar accesos y datos confirmados.

## Cómo actualizar datos
Editar `docs/datos.json` → commit → push. La APK no se recompila.
Recompilar la APK solo si cambia `index.html`: `npm run sync && cd android && gradlew.bat assembleDebug`. Después copiar la APK a `docs/panel-btolabs.apk` y hacer push.

## Trampas
- **Capacitor empaqueta `www/`, no `docs/`.** `npm run sync` arma `www/` desde `docs/` sin los `.apk`. Si se empaqueta `docs/` directo, cada APK lleva adentro la anterior: pasó el 7-oct (21,4 MB en vez de 7,5 MB).
- La API de PageSpeed sin clave tiene cuota 0 (error 429). Las mediciones se hicieron con Lighthouse local: `npx -y lighthouse@12 <url> [--preset=desktop]`.
- La carga del elemento principal (LCP) de grupovl.cl en celular varió entre 41,6 y 115,8 s en 2 corridas; se publica la menor.
- Chrome headless para PDF necesita `--user-data-dir` propio si el Chrome de Alberto está abierto.
