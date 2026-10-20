# Puriy Ayacucho

**Puriy** significa "caminar" o "viajar" en quechua. Puriy Ayacucho es una aplicación Android de turismo para la ciudad de Ayacucho (Perú). Reúne en un solo lugar los atractivos de la ciudad, una guía que arma rutas según el medio de transporte, el calendario de festividades y una experiencia de realidad aumentada en sitios arqueológicos.

## Funcionalidades principales

- **Explorar lugares por categorías**: templos, museos, restaurantes, cafés, bares, naturaleza, hoteles, alojamientos tipo Airbnb y recreación. Cada lugar tiene su ficha con galería, descripción, ubicación en el mapa y comodidades, y se puede filtrar y guardar en favoritos.
- **Guía personalizada con rutas y transporte**: elige un origen (o tu ubicación actual) y un destino, y la app arma la ruta a pie, en combi o en taxi, con las líneas trazadas sobre las calles. Durante el viaje, un personaje sigue tu posición GPS en tiempo real, avisa si te sales de la ruta y la recalcula.
- **Eventos y festividades**: calendario de las fiestas de Ayacucho, eventos próximos, compra de boletos con código QR y reservas de hospedaje.
- **Realidad aumentada**: reconstrucciones 3D de sitios como Wari, Vilcashuamán y el Obelisco de Quinua sobre la cámara, con ARCore y narración por voz.
- **Varios idiomas**: español, inglés, portugués y quechua, con cambio de idioma dentro de la app.
- **Cuenta de usuario**: inicio de sesión con correo, Google o Facebook, perfil con foto y guía de bienvenida.

## Tecnologías

- **Kotlin** con vistas en **XML** y View Binding
- **Material Components** para Android
- **Room** para la base de datos local (favoritos, reservas, boletos y recorridos)
- **Glide** para la carga de imágenes
- **Google Maps SDK** y **Fused Location Provider** para mapas y ubicación
- **ARCore** con SceneView y **CameraX** para la realidad aumentada
- **Firebase Authentication**, Google Sign-In y Facebook Login
- **Media3 (ExoPlayer)** para los videos, **ZXing** para los códigos QR y Material CalendarView

## Cómo ejecutarlo

### Requisitos

- Android Studio reciente (compatible con Android Gradle Plugin 8.13) y JDK 17
- Un dispositivo o emulador con Android 7.0 (API 24) o superior. La realidad aumentada necesita un celular compatible con ARCore.

### Pasos

1. Clona el repositorio:

   ```bash
   git clone https://github.com/curahuamoralesanymiluska/PuriyAyacucho.git
   ```

2. Abre la carpeta en Android Studio y espera a que Gradle sincronice.

3. Agrega tu API key de Google Maps en el archivo `local.properties` de la raíz del proyecto (Android Studio lo crea al abrir el proyecto; este archivo no se sube a Git):

   ```properties
   MAPS_API_KEY=tu_clave_de_google_maps

   # Opcional, para el inicio de sesión con Facebook
   FACEBOOK_APP_ID=tu_app_id
   FACEBOOK_CLIENT_TOKEN=tu_client_token
   ```

   La clave se obtiene en [Google Cloud Console](https://console.cloud.google.com/) activando "Maps SDK for Android".

4. Descarga el archivo `google-services.json` de tu proyecto de [Firebase](https://console.firebase.google.com/) (con Authentication habilitado) y colócalo en la carpeta `app/`.

5. Ejecuta la app desde Android Studio o compílala por consola:

   ```bash
   ./gradlew assembleDebug
   ```

## Estructura del proyecto

```
app/src/main/java/com/example/puriyayacucho/
├── adapters/     Adaptadores de las listas (RecyclerView)
├── bienvenida/   Guía de bienvenida al iniciar sesión
├── boletos/      Compra de boletos y pagos
├── data/         Datos de los lugares, entidades y DAOs de Room
├── guia/         Planificador de rutas, transporte y viaje en vivo
├── perfil/       Foto de perfil
├── ra/           Realidad aumentada
├── ui/           Vistas personalizadas (bordado, manta y retablo ayacuchano)
└── *Activity.kt / *Fragment.kt   Pantallas de la app
```

## Capturas

| Inicio | Explorar | Detalle del lugar |
|:---:|:---:|:---:|
| <img src="docs/capturas/inicio.png" width="220"> | <img src="docs/capturas/explorar.png" width="220"> | <img src="docs/capturas/detalle.png" width="220"> |

| Guía y rutas | Viaje en vivo | Realidad aumentada |
|:---:|:---:|:---:|
| <img src="docs/capturas/guia.png" width="220"> | <img src="docs/capturas/viaje.png" width="220"> | <img src="docs/capturas/ra.png" width="220"> |

> Para agregar las capturas, guarda las imágenes en `docs/capturas/` con los nombres de la tabla.

## Autora

Any Miluska Curahua Morales
