# JosakApp

Este proyecto es una aplicación móvil nativa para Android desarrollada en **Kotlin** utilizando **Jetpack Compose**. El sistema integra de manera lúdica la gestión de hábitos diarios con el cuidado de una mascota virtual (un pingüino). Al cumplir sus tareas en la vida real, el usuario es recompensado con monedas y experiencia, las cuales puede administrar en una tienda integrada para comprar consumibles o adquirir prendas de ropa exclusivas para personalizar a su mascota.

---

##  Requisitos Previos

Para poder compilar y ejecutar el proyecto sin inconvenientes, asegúrate de contar con el siguiente entorno:

* **Android Studio**: Versión Ladybug (2024.2.1), Koala o superior.
* **Java Development Kit (JDK)**: Versión 17 (configurada como Gradle JDK).
* **Configuración del SDK de Android**:
  * `compileSdk`: 34
  * `minSdk`: 26 (Garantiza el soporte nativo de la API de fechas `java.time`).
  * `targetSdk`: 34

---

##  Tecnologías Utilizadas

* **Capa de UI**: Jetpack Compose (Diseño de interfaz 100% declarativo y reactivo).
* **Persistencia Local**: Room Database (Gestión de SQLite relacional mediante DAOs).
* **Conectividad de Red**: Retrofit & OkHttp (Para servicios e integraciones API).
* **Servicios Cloud**: Firebase Client (Autenticación y soporte en la nube).
* **Arquitectura**: MVVM (Model-View-ViewModel) con patrón de repositorio.
* **Procesamiento Asíncrono**: Kotlin Coroutines y flujos reactivos (`StateFlow` / `Flow`).

---

##  Arquitectura General del Sistema

La aplicación está diseñada bajo una arquitectura limpia **MVVM**, dividida de forma estricta en las siguientes capas:

1. **Capa de Vista (UI)**: Ubicada en `ui.view` y `ui.components`. Son funciones `@Composable` que reaccionan pasivamente al estado expuesto por la lógica de negocio.
2. **Capa de Lógica (ViewModel)**: Actúa como puente. Controla el estado del juego (sed, inventario, progreso) y transforma los datos reactivos para la UI.
3. **Capa de Datos (Data & Repository)**: Centraliza las fuentes de datos. Decide si la información se obtiene desde la persistencia local (Room) o desde los servicios remotos (Retrofit/Firebase) mediante la abstracción de repositorios.

---

##  Estructura del Repositorio (Estructura de Carpetas Real)

A continuación se detalla la organización exacta de los paquetes y archivos clave del proyecto:

```text
📂 app/src/main/java/edu/josakapp/proyectoJosakapp
 │
 ├── 📂 converter
 │    └── 📄 base64ToBitmap.kt                 # Utilidad para la conversión de imágenes en Base64
 │
 ├── 📂 data
 │    ├── 📂 datasource
 │    │    ├── 📄 AppDatabase.kt               # Clase Singleton central de la Base de Datos Room
 │    │    └── 📄 RemoteDatasource.kt           # Origen de datos remoto centralizado
 │    │
 │    ├── 📂 di
 │    │    └── 📄 Appmodule.kt                 # Proveedor manual de dependencias del sistema
 │    │
 │    ├── 📂 local
 │    │    ├── 📄 AmigosDao.kt                 # Consultas Room para el sistema de amigos
 │    │    ├── 📄 HabitosDao.kt                # Consultas Room para la gestión de hábitos
 │    │    ├── 📄 LocalDatasource.kt            # Origen de datos local encapsulado
 │    │    └── 📄 UserDao.kt                   # Consultas de persistencia de usuario y pingüino
 │    │
 │    ├── 📂 model
    │    ├── 📄 Accesorios.kt                # Entidad que define el catálogo de ropa, precios e imágenes
    │    ├── 📄 Amigo.kt                     # Modelo de datos para la gestión del sistema de amigos
    │    ├── 📄 Calendar.kt                  # Entidad para el control y registro de fechas en el sistema
    │    ├── 📄 Habito.kt                    # Modelo de persistencia local para los hábitos del usuario
    │    ├── 📄 HabitoMapper.kt              # conversor de datos entre el modelo local y el modelo remoto
    │    ├── 📄 HabitoRemote.kt              # Estructura de datos de hábitos para la sincronización en la nube
    │    ├── 📄 Invitacion.kt                # Entidad para la gestión de solicitudes y alertas de amistad
    │    ├── 📄 Pinguino.kt                  # Datos base de la mascota (vitalidad, nivel, sed, última sesión)
    │    ├── 📄 PinguinoAccesoriosCrossRef.kt# Tabla de cruce relacional (Muchos a Muchos) para la ropa comprada
    │    ├── 📄 PinguinoWithAccesorios.kt    # Relación Room avanzada para consultar el armario activo de la mascota
    │    ├── 📄 Racha.kt                     # Entidad para contabilizar los días seguidos completando tareas
    │    ├── 📄 Suscripcion.kt               # Modelo para el control de alertas o estados premium del usuario
    │    ├── 📄 TiendaItem.kt                # Estructura base para los artículos disponibles en el mercado
    │    ├── 📄 User.kt                      # Perfil principal del usuario (ID, apodo, monedas de oro, experiencia)
    │    ├── 📄 UserMapper.kt                # Mapeador relacional para la transferencia de perfiles de usuario
    │    ├── 📄 UserRanking.kt               # Estructura utilizada para procesar la tabla de líderes globales
    │    ├── 📄 UserRemote.kt                # Entidad espejo del usuario para sincronización con servicios Cloud
    │    ├── 📄 UserWithHabito.kt            # Relación de Room para extraer un usuario con su lista de tareas
    │    ├── 📄 UserWithInvitacion.kt        # Vinculación relacional de un usuario con sus invitaciones pendientes
    │    ├── 📄 UserWithPinguino.kt          # Relación estructural uno-a-uno entre el dueño y su mascota
    │    ├── 📄 UserWithRacha.kt             # Asociación de datos del jugador con sus métricas de consistencia
    │    └── 📄 UserWithSuscripcion.kt       # Relación que vincula al usuario con sus pases o suscripciones activas
 │    │
 │    ├── 📂 network
 │    │    ├── 📄 AuthService.kt               # Lógica de autenticación e inicio de sesión
 │    │    └── 📄 FirebaseClient.kt             # Cliente de conexión con Firebase
 │    │
 │    └── 📂 remote
 │         ├── 📄 HabitoRemoteRepository.kt    # Repositorio de sincronización en la nube de hábitos
 │         ├── 📄 RankingApi.kt                # Interfaz Retrofit para el sistema de clasificación
 │         ├── 📄 RetrofitClient.kt            # Configuración base del cliente HTTP Retrofit
 │         ├── 📄 UserApi.kt                   # Endpoints de Retrofit para datos de usuario
 │         └── 📄 UserRemoteRepository.kt      # Sincronización en la nube del perfil
 │
 ├── 📂 repository
 │    ├── 📄 HabitosRepository.kt              # Mediador definitivo para el flujo de hábitos
 │    ├── 📄 RankingRepository.kt              # Mediador para la tabla de líderes
 │    └── 📄 UserRepository.kt                 # Mediador para los datos globales de usuario
 │
 └── 📂 ui
      ├── 📂 components
      │    ├── 📄 AnyadirHabito.kt             # Componente de diálogo/formulario para la creación y parametrización de nuevos hábitos
      │    ├── 📄 AppScaffold.kt               # Estructura base y contenedor global de la aplicación (Scaffold, barras de navegación, etc.)
      │    ├── 📄 CalendarCard.kt              # Tarjeta interactiva de calendario para visualizar el histórico de cumplimiento diario
      │    ├── 📄 DraggablePenguin.kt          # Renderizado animado e interactivo del pingüino junto a su indicador de estado y barra de sed
      │    ├── 📄 HabitoCard.kt                # Tarjeta visual reactiva para listar, interactuar y marcar hábitos individuales
      │    ├── 📄 HabitoWidget.kt              # Widget de escritorio o acceso rápido optimizado para el control del estado del día
      │    ├── 📄 LoginComponents.kt           # Conjunto de campos de texto y validaciones de entrada reutilizables para el inicio de sesión
      │    ├── 📄 LoginContent.kt              # Estructura del formulario central y disposición visual de la pantalla de autenticación
      │    ├── 📄 PinguinoComponents.kt        # Módulos de UI independientes y optimizados para los paneles interactivos del juego:
      │    │                                     - ItemBebidaUI: Cuadrícula con bordes dinámicos según selección.
      │    │                                     - ActionPanel: Control numérico e interruptores inteligentes para Comprar/Alimentar.
      │    │                                     - MochilaBebidasGrid / TiendaBebidasGrid: Listas optimizadas en 3 columnas.
      │    │                                     - MochilaRopaGrid / TiendaRopaGrid: Gestión del armario dinámico y extracción de nombres de los artículos.
      │    └── 📄 SettingsScaffold.kt          # Plantilla estructural y maquetación de la interfaz para el menú de configuraciones generales
      │
      ├── 📂 navigation
      │    ├── 📄 NavigationHost.kt            # Enrutador y grafo de navegación general (NavHost)
           ├── 📄 NotificationReceiver.kt      # Notificar por el Widget
      │    └── 📄 NavScreens.kt                # Definición de las rutas seguras de pantallas
      │
      ├── 📂 theme
      │    └── 📄 Theme.kt / Color.kt /Type.kt     # Configuración de estilos visuales y paleta de colores
      │
      ├── 📂 view
      │    ├── 📄 AnunciosScreen.kt            # Pantalla para la visualización de avisos, novedades y notificaciones globales del sistema
      │    ├── 📄 BuscarAmigosScreen.kt        # Interfaz de usuario para la búsqueda, filtrado y envío de solicitudes de amistad
      │    ├── 📄 ClasificacionNotificacionesScreen.kt # Panel avanzado para organizar, categorizar y gestionar las alertas del usuario
      │    ├── 📄 CompletarPerfilScreen.kt     # Formulario guiado de registro secundario para la personalización de los datos del usuario
      │    ├── 📄 ForgotPasswordScreen.kt      # Pantalla para la recuperación de contraseñas mediante credenciales seguras
      │    ├── 📄 HabitoScreen.kt              # Panel principal de gestión de tareas (MVP): permite listar, añadir y validar hábitos
      │    ├── 📄 LoginScreen.kt               # Pantalla de acceso que conecta la interfaz con los servicios de validación de usuario
      │    ├── 📄 MainContainerScreen.kt       # Contenedor e integrador principal de navegación (Scaffold de enrutamiento general)
      │    ├── 📄 MoneyScreen.kt               # Vista detallada del estado financiero del usuario (historial de ingresos de monedas)
      │    ├── 📄 NotificacionesScreen.kt      # Centro de notificaciones nativas de la aplicación para recordatorios de hábitos
      │    ├── 📄 PerfilScreen.kt              # Panel de estadísticas personales: muestra el nivel, experiencia total y logros del jugador
      │    ├── 📄 PinguinoScreen.kt            # Pantalla del núcleo del juego (MVP): alimentación, degradación de sed y armario dinámico
      │    ├── 📄 PreferenciasScreen.kt        # Panel de personalización de la experiencia de usuario (temas, sonidos y ajustes visuales)
      │    ├── 📄 PrivacidadScreen.kt          # Vista informativa de los términos legales y políticas de protección de datos locales
      │    ├── 📄 RankingScreen.kt             # Tabla de líderes globales que fomenta la competitividad mostrando el progreso de los usuarios
      │    ├── 📄 RecordatorioScreen.kt        # Configuración personalizada de alarmas y franjas horarias para cumplir con los hábitos
      │    ├── 📄 RegisterScreen.kt            # Interfaz de registro para la creación de nuevas cuentas en el ecosistema de la app
      │    ├── 📄 SettingsScreen.kt            # Menú de configuración global del sistema y desconexión/cierre de sesión
      │    ├── 📄 StatsScreen.kt               # Gráficos estadísticos avanzados sobre el progreso e historial de hábitos del usuario
      │    ├── 📄 SubMenuAmigosScreen.kt       # Menú contextual secundario para interactuar directamente con la lista de amigos
      │    └── 📄 TiendaScreen.kt              # Mercado global integrado para la adquisición e inspección de consumibles y cosméticos
      │
      └── 📂 viewmodel
           ├── 📄 HabitosViewModel.kt          # Gestión transaccional de hábitos: controla la creación, edición y marcas diarias de tareas, actualizando la experiencia (XP)
           ├── 📄 LoginViewModel.kt            # Control del flujo de autenticación y lógica de acceso/seguridad de las cuentas de usuario
           ├── 📄 PinguinoViewModel.kt         # El cerebro de la mascota virtual (MVP): administra el monedero de consumibles (mochila), el catálogo de la tienda de ropa, y procesa el algoritmo offline que degrada la sed un 10% diario calculando el desfase temporal de las sesiones
           ├── 📄 RankingViewModel.kt          # Lógica orientada a procesar y ordenar la tabla de clasificación global de usuarios basada en la XP
           ├── 📄 RegisterViewModel.kt         # Gestión de registros de nuevos perfiles, validando contraseñas y correos antes de la persistencia
           ├── 📄 ThemeViewModel.kt            # Control dinámico del estado del tema visual de la app (Modo Claro / Modo Oscuro)
           └── 📄 UserViewModel.kt             # Gestiona el estado global de la sesión activa del jugador, administrando de forma persistente su monedero de monedas de oro, niveles alcanzados y racha actual de consistencia
```
