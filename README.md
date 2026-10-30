# Kivo Store

<img src="fastlane/metadata/android/es-ES/images/icon.png" width="96" alt="Icono de Kivo Store">

Una tienda de aplicaciones para Android que reúne F-Droid, IzzyOnDroid y proyectos de GitHub en una interfaz sencilla.

**Versión: 0.9.14 · Android 8.0 o posterior · app.kivo.market**

## Descarga

El APK firmado se publica en [Releases](https://github.com/Salvador9797/kivo-store/releases). Las instalaciones y actualizaciones requieren la confirmación de Android.

## Cambios en 0.9.14

- Fuentes agrupadas por paquete, con elección de origen y novedades conservadas.
- Sugerencias de nombres y corrección de errores de escritura cuando no hay coincidencias directas.
- Notas y tamaño de las actualizaciones; firmas incompatibles explicadas aparte.
- Mensajes de descarga para conexión, Wi-Fi, almacenamiento y archivos retirados; reintentos controlados.
- Más intentos de obtener iconos oficiales desde GitHub, con caché y respaldo de iniciales.
- Capturas con opción de reintentar, texto legible y controles accesibles.
- Preferencias locales de tema, tamaño del texto, contraste y reducción de movimiento.

El inicio sigue mostrando nuevas aplicaciones y lanzamientos. No se añadió un feed personalizado ni seguimiento para recomendaciones. Las pruebas automatizadas no sustituyen la comprobación de instalación y uso en un dispositivo real.

## Cambios en 0.9.13

- Nueva presentación de Inicio, juegos en cuadrícula y barra de búsqueda renovada.
- Búsqueda por nombres parciales y funciones con términos relacionados en español e inglés.
- Sincronización automática del catálogo al abrir o volver a Kivo: F-Droid e IzzyOnDroid cada 6 horas y consultas limitadas de GitHub cada 12 horas.
- Corrección de la carga inicial de IzzyOnDroid y de filtros que podían ocultar su catálogo.
- Mejoras en la detección de apps instaladas, comparación del APK compatible con el dispositivo y progreso de descargas.
- Integración experimental de juegos de itch.io; disponibilidad y descargas sujetas a su API y a comprobación en dispositivos reales.

### Firma de esta distribución

Esta versión usa una nueva firma. No se instala encima de Kivo 0.9.8: desinstalar la antigua elimina sus datos locales. Las futuras actualizaciones de esta distribución conservarán la nueva firma. Esta distribución utiliza el nuevo paquete `app.kivo.market`, código interno 20, para su publicación en una ficha nueva de APKPure.

## Qué ofrece

- Descubrimiento por necesidades, colecciones, nuevos proyectos, populares y lanzamientos.
- Catálogos de F-Droid y GitHub, búsqueda y estrellas de GitHub.
- Detección local de apps instaladas y comprobación de actualizaciones compatibles.
- Comprobación de integridad, paquete y firma antes de instalar APK compatibles.
- Descripciones traducidas al español con consentimiento previo mediante MyMemory.
- Comentarios públicos y calificaciones de 1 a 5 mediante la cuenta de GitHub y el repositorio de comunidad.

## Vistas previas del nuevo estilo

Inicio, búsqueda por funciones y catálogo de juegos de Kivo 0.9.13.

![Vistas ilustrativas de Inicio, Búsqueda y Juegos](docs/previews/kivo-0.9.13-vistas.png)

Estas imágenes son ilustraciones del diseño, no capturas de un dispositivo. Los nombres, iconos y resultados son ejemplos; la disponibilidad depende del catálogo cargado.

[Descargar Kivo 0.9.13](https://github.com/Salvador9797/kivo-store/releases/tag/v0.9.13)

## Compilar

Requisitos: JDK 17, Android SDK 35 y acceso a Google Maven/Maven Central. Gradle 8.9 y Android Gradle Plugin 8.7.3 están fijados en el proyecto.

```sh
chmod +x gradlew
./gradlew :app:assembleRelease
```

La compilación Gradle genera un APK sin firmar. La clave privada de desarrollo no forma parte de este repositorio. El APK publicado fue compilado mediante `tools/build.py` con las herramientas del SDK y ECJ; el script documenta ese procedimiento. No se afirma que el resultado de ambos procedimientos sea idéntico bit a bit.

Pruebas de interfaz:

```sh
npm install
npm test
```

Consulta `VALIDATION.md` para los límites de la validación. No se ha completado una prueba integral en un teléfono físico de esta versión desde el entorno de desarrollo.

## Privacidad y servicios

El inventario de apps se consulta localmente. GitHub se usa para catálogo, descargas y comunidad. El inicio de sesión solicita `public_repo`; los comentarios son públicos. El token se protege mediante Android Keystore. La traducción opcional envía la descripción a MyMemory después del consentimiento y está sujeta a sus límites. No se incluyen claves privadas ni secretos OAuth en el código.

## F-Droid

La inclusión en el catálogo oficial requiere revisión independiente. Este repositorio no implica aceptación ni disponibilidad en F-Droid. Se preparan metadatos upstream y una solicitud que documenta las verificaciones pendientes.

## Licencias y créditos

Copyright (C) 2026 Salvador9797 y colaboradores de Kivo Store.

El código original de Kivo y su icono se distribuyen bajo **GPL-3.0-or-later**, sin garantía. Consulta `LICENSE`. Los catálogos, iconos de otras apps y Lucide conservan sus licencias y titulares; consulta `THIRD_PARTY_NOTICES.md`. Los nombres y marcas de terceros se usan para identificar sus aplicaciones y no implican afiliación.

## Comunidad

[Reportar un error](https://github.com/Salvador9797/kivo-store/issues) · [Comentarios de aplicaciones](https://github.com/Salvador9797/kivo-comunidad)
