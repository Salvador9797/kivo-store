# Comunidad de Kivo

Plantilla para un repositorio público dedicado a las opiniones de Kivo. No es el repositorio de un desarrollador de apps. Repositorio ya creado y conectado en la compilación 0.9.1: https://github.com/Salvador9797/kivo-comunidad . Esta carpeta queda como referencia de formato.

## Activación por el propietario

1. Crear un repositorio público dedicado, por ejemplo `kivo-comunidad`, con Issues habilitados y con acceso de lectura/comentarios para la comunidad.
2. Copiar `.kivo-community.json` a la raíz del repositorio, junto con estas instrucciones. El marcador evita configurar por error un proyecto ajeno como espacio de reseñas.
3. En Tu espacio → Comunidad de Kivo → Configurar repositorio, introducir `propietario/kivo-comunidad`. Kivo verifica que sea público, activo, con Issues y marcador válido.
4. Antes de distribuir la app a todos, fijar ese mismo identificador en `app/src/main/assets/community.json` y recompilar. La configuración local es para pruebas del propietario; los usuarios no deberían elegir repositorios diferentes.
5. Registrar la aplicación GitHub y activar Device Flow para dar acceso por navegador. Kivo Store ya está registrada y su Client ID público está incorporado; falta la prueba autorizada desde el teléfono. El token personal existente sirve para pruebas; publicar necesita acceso de escritura en Issues de este repositorio, no solo lectura de catálogo. No todos los tipos de token pueden comentar en repositorios públicos ajenos: hay que validar el flujo real con una segunda cuenta. No incluir tokens en el código ni compartirlos por chat.
6. Probar lectura pública, primera reseña, edición y lectura desde una segunda cuenta/dispositivo. Estas pruebas aún no se han realizado en vivo.

## Funcionamiento

Un issue identifica el paquete Android. Los comentarios contienen reseñas estructuradas (calificación de 1 a 5 y texto). La identidad y fecha proceden de GitHub; el autor no se obtiene del cuerpo del comentario. Para la media se toma la última reseña válida de cada cuenta por paquete. El cliente permite actualizar el comentario propio. Las estrellas del repositorio de la app son un dato distinto de estas calificaciones.

Kivo pide confirmar expresamente antes de enviar una reseña pública. No se publica al abrir la ficha, al cambiar estrellas en el formulario ni al instalar. Se exige localmente que la app esté instalada y que haya un APK comprobado de esa fuente en Kivo. Este control local no es un comprobante remoto de instalación y puede eludirse usando GitHub directamente: no mostrar un sello de instalación verificada por servidor.

Si varias primeras reseñas crean hilos simultáneos, se buscan y agregan hilos del mismo paquete cuando vuelven a consultarse; puede haber demoras por la indexación/caché de GitHub. Esta versión lee hasta cinco hilos y diez páginas de 100 comentarios por hilo; muestra promedio parcial si se alcanza el límite. El formulario de edición busca el comentario propio en el hilo principal hasta 1,000 comentarios. Un hilo demasiado grande necesita una mejora de paginación antes de seguir publicando desde esta versión.

## Moderación

El propietario debe revisar spam/abusos y usar las herramientas de moderación, bloqueo, eliminación o cierre de GitHub cuando corresponda. Una cuenta puede modificarse o eliminar su opinión desde GitHub. Se recomienda no incluir datos personales en comentarios. No hay auditoría automática de todas las reseñas, verificación de instalaciones en servidor ni sistema antimanipulación de calificaciones.

Se publicaron únicamente el marcador y las instrucciones de la comunidad. No se han publicado reseñas reales durante la implementación. Se mantienen los límites de API de GitHub; esta solución no ofrece capacidad ilimitada.
