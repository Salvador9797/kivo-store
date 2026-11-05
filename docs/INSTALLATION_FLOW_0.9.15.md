# Kivo 0.9.15 — instalación directa y refuerzo de la interfaz

## Cambio principal

Al tocar descargar una app de GitHub, F-Droid o IzzyOnDroid, la solicitud queda marcada para instalar. Cuando DownloadManager confirma que terminó, Kivo conserva la validación de tamaño, hash disponible, identidad, versión, arquitectura y certificado y abre la confirmación del instalador de Android. No cambia a la sección Descargas ni exige pulsar de nuevo Comprobar e instalar.

El receiver en primer plano y el sondeo comparten una cola que elimina duplicados. Una sola verificación/confirmación ocupa el turno. Los APK de itch.io y los guardados usan la misma cola de confirmaciones, evitando que sobrescriban el APK pendiente de otra descarga.

Si la app queda en segundo plano, no fuerza pantallas sobre otras apps: espera a volver a Kivo. Una notificación de descarga correcta enlaza a la validación/instalación de ese archivo, con ID y nonce comprobados contra el registro local. Las notificaciones de error llevan a los detalles para reintentar. Los APK anteriores a esta versión no empiezan a instalarse sin una nueva acción del usuario.

El primer intento se registra antes de verificar. Cancelar, denegar el permiso o un APK inválido no provoca un bucle automático. El APK sigue disponible para un reintento explícito. Si Android mata el proceso entre ese registro y la apertura del instalador, el usuario puede continuar desde la notificación o Descargas; no se promete una transacción exactamente una vez entre procesos. No hay instalación silenciosa ni permisos adicionales.

## Otras correcciones incluidas

- Abrir permanece disponible cuando existe Actualizar.
- Alias ARMv7, ARM64 y x86 se interpretan consistentemente en la recomendación web y nativa.
- El acceso normal a GitHub solicita el catálogo sin escritura; public_repo se ofrece solo al entrar a publicar reseñas, con explicación y cancelación. No revoca permisos de autorizaciones anteriores ni elimina el alcance amplio de la integración actual de reseñas. Las cuentas ya conectadas pueden revocar autorizaciones en GitHub.
- Los scripts ejecutables de la interfaz están autorizados mediante hashes CSP; se retira unsafe-inline de script-src y los manejadores de error inline.
- app.js y lucide.js son las fuentes canónicas de los scripts incluidos. `python3 tools/sync_ui.py` actualiza index.html y hashes; `--check` detecta desajustes en CI.
- Se elimina un listener repetido y se centraliza User-Agent de la versión.
- Versión preparada: 0.9.15, versionCode 22, app.kivo.market, Android 8/API 26 mínimo y targetSdk 35.

## Validación

Pruebas de interfaz: `npm ci --ignore-scripts && npm test`. La suite incluye rechazo de la variante ARMv7 en un equipo ARM64 únicamente, hashes CSP de los scripts reales, fallback de imágenes, acceso de cuenta y conservación de pantalla durante transferencias. JSDOM no aplica la política CSP: se verifica su integridad, no se afirma una prueba de bloqueo en WebView real.

Pruebas de cola y selección nativa: compilar y ejecutar `tests/InstallQueueTest.java` y `tests/AbiNamesTest.java` con sus clases correspondientes. Se prueban FIFO, pausa/reanudación, deduplicación, turno ocupado, exclusión de fallidos/cancelados/instalados y reintento explícito.

Se comprobaron además suites existentes de versiones instaladas, recuperación de descargas, programación de catálogo, actualizaciones, fuentes y juegos. El código Android se compiló contra android.jar API 35, se convirtió a DEX y se empaquetó/alineó un APK sin firma con herramientas SDK 35. La compilación sin firma es reproducible con `ANDROID_SDK_ROOT=/ruta/sdk python3 tools/build_unsigned.py` y JDK 17+.

**Pendiente de dispositivo real:** descarga y confirmación de instalación en Samsung/Android, abrir ajustes de instalación por primera vez, cancelar el instalador, varias descargas, bloqueo de pantalla y muerte de proceso. Las pruebas de cola no sustituyen la integración con Package Installer. También queda pendiente autorizar GitHub realmente y confirmar scopes con el proveedor.

## Publicación

El APK generado sin firma sirve para validar el build; no es una actualización instalable. Para distribuir una actualización sobre 0.9.14 hay que firmar con la clave original y validar su huella antes de publicar. No se ha creado una nueva clave ni sustituido la firma del producto.

## Resto de la auditoría

Esta entrega prioriza instalación directa y correcciones acotadas. Siguen pendientes: rotación de certificados con linaje verificado en todos los flujos; comparación de permisos por versión; mediciones de arranque/memoria/datos en dispositivo; preferencias y exportación de colección; mejoras de privacidad de traducción y una integración de reseñas con permisos más restringidos. Mantener nuevas apps y lanzamientos visibles para todos.
