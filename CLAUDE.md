# CLAUDE.md: Proyecto de Programación Móvil (UCB)

Kotlin Multiplatform + Compose Multiplatform. Módulos: `androidApp` y `shared`.
Paquete base: `org.donnico.projectv1`.
Todo el código de la app va en `shared/src/commonMain/kotlin/org/donnico/projectv1/`.

Soy estudiante y debo poder explicar cada archivo en un examen.
Prioriza código simple y predecible. No hagas código "ingenioso".

## Referencia obligatoria

Antes de escribir un feature nuevo, mira los features que ya existen en el proyecto y copia su
estructura, sus nombres y su estilo. Si existe un feature de ejemplo del docente (por ejemplo
`userinformation`), ese es el modelo principal. Si no hay ninguno, sigue la estructura de este archivo.
Si algo no está en los ejemplos, pregúntame antes de inventarlo.

## Arquitectura: Clean Architecture, una carpeta por feature

```
org/donnico/projectv1/<feature>/
├── domain/
│   ├── model/        data class con lo que usa la app
│   ├── repository/   interfaz (el contrato)
│   └── usecase/      una acción de negocio, función suspend invoke()
├── data/
│   ├── dto/          copia exacta del JSON de la API
│   ├── mapper/       extensión Dto.toModel()
│   ├── service/      cliente Ktor (<Nombre>Client)
│   ├── datasource/   clase que envuelve al cliente
│   └── repository/   <Nombre>RepositoryImpl
└── presentation/
    ├── viewmodel/    <Nombre>State y <Nombre>ViewModel (y <Nombre>Event si el usuario actúa)
    └── screen/       <Nombre>Screen (Composable)
```

Reglas de capas:
- `domain` no depende de nadie: sin Ktor, sin JSON, sin Android, sin Compose.
- `presentation` y `data` dependen de `domain`, nunca entre ellas.
- El repositorio es una interfaz en `domain` y su implementación está en `data`.

## Convenciones de código

- Las funciones que llaman a la red o al repositorio son `suspend` y devuelven `Result<T>`.
- El cliente Ktor atrapa excepciones con try/catch y devuelve `Result.success(...)` o `Result.failure(e)`.
- DTO: `@Serializable`; `@SerialName("...")` cuando el nombre del JSON no sirve en Kotlin
  (snake_case, guiones o MAYÚSCULAS); **valores por defecto en los campos que pueden faltar**
  (`emptyList()`, `""`, `null`).
- `Json { ignoreUnknownKeys = true }` siempre.
- El mapper convierte DTO en Model y es donde se formatea lo que viene "crudo" (fechas, listas, URLs).
- ViewModel: `private val _state = MutableStateFlow(XState())` y `val state = _state.asStateFlow()`.
  Carga datos en `init`, o con `emitEvent(XEvent)` y un `sealed interface XEvent` si el usuario hace algo.
- State: `data class` con los datos, `loading: Boolean` y `error: String?`.
- Screen: `@Composable fun XScreen(viewModel: XViewModel = koinViewModel())`,
  `viewModel.state.collectAsState()` y un `when` que cubra cargando, error y datos.
- Nombres de clases y variables en inglés. Textos que ve el usuario en español.
- Cada archivo tiene su `package` igual a su carpeta. Un archivo nuevo sin `package` correcto es un error.

## Inyección de dependencias (Koin): obligatorio registrar todo

Cada clase nueva se registra en `di/`. Si falta un registro, la app compila pero se cierra al abrir.

```kotlin
// DataModule
single { XClient() }
single { XRemoteDataSource(get()) }
single<XRepository> { XRepositoryImpl(get()) }

// DomainModule
singleOf(::GetXUseCase)

// PresentationModule
viewModelOf(::XViewModel)
```

En `DataModule`, `DomainModule` y `PresentationModule` solo **agrega** líneas e imports. No borres ni reordenes lo existente.

## Dependencias y build

- Las librerías se agregan solo en `gradle/libs.versions.toml` y `shared/build.gradle.kts`.
- **Pregúntame antes de agregar una librería.**
- **Nunca cambies versiones** de Kotlin, AGP, Gradle, Compose, Koin ni Ktor, y no uses el asistente de actualización de AGP.
  Una versión más nueva de una librería puede exigir un AGP más nuevo y romper el proyecto
  (ejemplo real: Coil 3.6.3 falló; Coil 3.3.0 compiló).
- Ktor: `core`, `content-negotiation` y `serialization-kotlinx-json` en `commonMain`; `okhttp` en `androidMain`; `darwin` en `iosMain`.
- El permiso `INTERNET` debe estar en `androidApp/src/main/AndroidManifest.xml`.
- El proyecto usa detekt (carpeta `config/`). No ignores sus reglas.
- Estoy en Windows con PowerShell. Para compilar: `.\gradlew.bat :androidApp:assembleDebug`.
  Los targets de iOS no se compilan en Windows: ignora el aviso `iosSimulatorArm64Test is disabled`.
- Yo ejecuto la app desde Android Studio. No lances emuladores ni comandos `adb` salvo que te lo pida.

## Lo que NO debes hacer

- No toques features que no te pedí.
- No renombres, muevas ni borres archivos existentes.
- No reformatees ni "mejores" código que no pedí cambiar.
- No agregues comentarios de relleno ni funcionalidades que no están en el pedido o en el diseño.
- No hagas `git commit` ni `git push` salvo que te lo pida.
- No subas claves secretas. Si una API pide key, déjala en una constante dentro del cliente y avísame
  que el repo es público.

## Autoría

El autor de este proyecto es el estudiante. Su usuario de GitHub es `BerniUCb`.
Esto aplica a todo lo que escribas o generes:
- No agregues coautores ni trailers de autoría de ningún tipo, salvo que te lo pida.
- Nunca agregues `Co-Authored-By`, líneas tipo "Generated with Claude Code" ni `Claude-Session`
  en mensajes de commit o de pull request.
- No pongas encabezados de autor (`@author`, "Creado por...") ni menciones de Claude o de IA
  en comentarios, README, nombres de archivos ni nombres de ramas.
- No cambies `user.name` ni `user.email` de git ni ninguna configuración de git.
- Los commits los hago yo desde Android Studio. Tú no los haces salvo que te lo pida.

## Cómo trabajar cada pedido

1. **Plan primero.** Antes de escribir código, dame la lista de archivos por capa (con su ruta completa)
   y las líneas de Koin que vas a agregar. Espera mi OK.
2. **Orden:** domain, luego data, luego presentation, luego `di/` y al final `App.kt`.
3. **Compila** con el comando de arriba. Si falla, arregla solo lo que tú rompiste.
4. **Reporte final en español sencillo:**
   - archivos creados y modificados (ruta completa),
   - líneas de Koin agregadas,
   - el recorrido de los datos en una línea por capa, para que yo pueda explicarlo.

## Diseños de Figma (capturas)

Las capturas están en `docs/ui/`. Implementa una pantalla a la vez.
- Usa Material3 y `MaterialTheme`. Pon colores fijos solo si ningún color del tema coincide.
- Usa `dp` y `Modifier`. No agregues elementos que no estén en la captura.
- Si algo del diseño es ambiguo (estados, textos, navegación), hazme la pregunta antes de decidir.
- Cargar imágenes desde internet requiere una librería (Coil). Pregúntame antes de agregarla.

## Descripción de la app (completar)

- Nombre y objetivo de la app:
- Pantallas y navegación:
- API o fuente de datos:
