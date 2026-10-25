# Validación 0.9.6 alpha

Doce suites DOM: incluye formulario visible antes de iniciar sesión, acceso directo, borrador local, permisos locales independientes de error remoto, conservación del foco al actualizar opiniones/presupuesto, ruta de publicación, actualización tras detectar instalación, procedencia y aviso parcial de traducción, escape de HTML y Ver original.

TranslationReviewsTest JVM: segmentación real en bytes UTF-8, no dividir emojis, conservación del texto, límite por caracteres Unicode, respuestas válidas, cuota agotada, errores del proveedor y elegibilidad exacta por paquete/certificado a través de distintas fuentes.

Prueba HTTP real de MyMemory con texto público de ejemplo: HTTP200/responseStatus200, respuesta española y quotaFinished=false. Prueba de lectura real de Issues de Salvador9797/kivo-comunidad: HTTP200 y array válido (0 elementos). No se ha publicado ninguna opinión de prueba.

Chromium/Playwright: distribución en claro y oscuro a 320/360/412/768 px, tarjetas, acceso a ficha y formulario a 390px, botón Comentar y calificar, foco en el comentario y ausencia de desbordamiento.

Compilación Android API35/min26; firma v2/v3 con el certificado previo. No se ha ejecutado en un teléfono físico ni completado un envío real entre dos cuentas. Las pruebas HTTP del entorno no sustituyen una prueba de red en Android.


## 0.9.8-alpha

- Lectura parcial del APK público 0.9.7 real: paquete app.kivo.store, versionCode 16, minSdk 26; 72,177 bytes transferidos de 9,381,151 por HTTPS.
- Prueba de transporte real con token de prueba deliberadamente inválido: HTTP 401 recuperado mediante consulta pública anónima HTTP 200. No se usó ni revocó un token de usuario real para esta prueba.
- Pruebas JVM de límites ZIP/XML, manifiestos almacenados y comprimidos, corrupción, truncamiento, límites de tamaño, dominios de redirección y separación entre consultas públicas y perfil autenticado.
- Pruebas DOM de los estados comprobando/error/instalada/actualizable/ausente, cambio de asset, versión instalada más reciente, SDK incompatible y ausencia de vinculación manual.
- Compilación SDK 35 y firma de desarrollo conservada. La consulta PackageManager y el ciclo completo de instalación todavía requieren confirmación en un teléfono físico; no se afirma una prueba física completa desde este entorno.
