# GestorCitasSANNA — Especificación Técnica

## 1. Descripción
**GestorCitasSANNA** es una aplicación móvil nativa desarrollada para Android que optimiza la gestión integral de citas médicas, la administración de recursos clínicos (sedes, especialidades, médicos, horarios y seguros) y el control de costos operativos dentro de una red de salud. 

El proyecto resuelve la necesidad de agilizar la programación de citas para pacientes y centralizar la administración operativa para el personal autorizado, garantizando una experiencia fluida, segura y con persistencia local robusta bajo un enfoque *offline-first*.

## 2. Funcionalidades
La aplicación cuenta con las siguientes funcionalidades implementadas y operativas:

- **Módulo de Autenticación y Onboarding:**
  - Pantalla de bienvenida (*Welcome*) interactiva en el primer inicio de la aplicación.
  - Registro de nuevos usuarios con validación estricta de DNI, correo electrónico, teléfono móvil y contraseña.
  - Inicio de sesión seguro con soporte para roles diferenciados (`ADMIN` y `PACIENTE`).
- **Portal del Paciente:**
  - Panel principal (*Home Paciente*) con visualización de citas médicas programadas y su historial.
  - Flujo completo de reserva de citas seleccionando sede, especialidad, médico, fecha, horario disponible y aseguradora/convenio.
  - Visualización detallada de la cita con opciones de gestión.
  - Pantalla de perfil de usuario.
- **Módulo de Administración:**
  - Panel de control administrativo (*Home Admin*) con acceso directo a módulos de gestión.
  - Gestión de Sedes clínicas.
  - Gestión de Especialidades médicas.
  - Gestión de Médicos y su asociación con sedes y especialidades.
  - Gestión de Horarios Médicos.
  - Gestión global de Citas.
  - Gestión de Seguros y Coberturas de salud.
  - Gestión de Costos y Gastos Operativos.

## 3. Stack Tecnológico
El proyecto utiliza herramientas modernas del ecosistema Android y Jetpack:

| Tecnología | Versión | Propósito en el Proyecto |
| :--- | :--- | :--- |
| **Kotlin** | 2.2.10 | Lenguaje de programación principal para toda la lógica de la app. |
| **Jetpack Compose** | BOM 2026.02.01 | Toolkit declarativo moderno para la construcción de interfaces de usuario nativas. |
| **Material 3** | Última (BOM) | Sistema de diseño oficial para componentes visuales, colores y tipografía. |
| **Navigation Compose** | 2.8.5 | Enrutamiento y navegación declarativa entre pantallas con paso de argumentos. |
| **Room Database** | 2.8.5 | Base de datos relacional local basada en SQLite con soporte *offline-first*. |
| **KSP (Google Symbol Processing)** | 2.3.14 | Procesador de anotaciones para la compilación eficiente de Room. |
| **DataStore Preferences** | 1.1.1 | Almacenamiento clave-valor persistente para gestionar la sesión activa y estado de inicio. |
| **Kotlin Coroutines & Flow** | 2.8.7 (Lifecycle) | Gestión de operaciones asíncronas de I/O y flujos reactivos de datos. |
| **AndroidX Lifecycle & ViewModel** | 2.8.7 | Retención de estados de UI seguros frente a cambios de configuración y ciclo de vida. |

## 4. Arquitectura y Organización
El proyecto sigue rigurosamente los principios de **Architecture Components** de Android y el patrón **MVVM (Model-View-ViewModel)** con una clara separación por capas:

- **Capa Data (`data/`):**
  - Contiene las entidades relacionales de Room (`data/local/entity/`), los objetos de acceso a datos (`data/local/dao/`), la configuración de la base de datos y carga de datos iniciales (`data/local/database/`) y los repositorios (`data/repository/`) que centralizan las fuentes de datos.
- **Capa Domain (`domain/`):**
  - Aloja la lógica de negocio transversal, como las validaciones de formato y los mecanismos de seguridad criptográfica para el cifrado de contraseñas (`domain/validation/ValidationUtils.kt`).
- **Capa Presentation (`presentation/`):**
  - **`components/`:** Elementos visuales reutilizables.
  - **`navigation/`:** Grafo de navegación central (`SannaNavGraph.kt`).
  - **`screens/`:** Pantallas organizadas por dominios funcionales (`admin/`, `auth/`, `bienvenida/`, `citas/`, `home/`, `perfil/`).
  - **`viewmodel/`:** ViewModels que administran el estado reactivo (`StateFlow`) y se comunican con los repositorios.
- **Capa UI Theme (`ui/theme/`):**
  - Definición de paleta de colores (`Color.kt`), tipografías (`Type.kt`) y tema Material 3 (`Theme.kt`).
- **Utilidades (`utils/`):**
  - Gestor de sesiones basado en DataStore (`SessionManager.kt`) y utilidades de notificación (`NotificationHelper.kt`).

## 5. Estructura del Proyecto
```text
com.charahua.gestorcitassanna/
├── MainActivity.kt
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   ├── AseguradoraDao.kt
│   │   │   ├── CitaDao.kt
│   │   │   ├── CostDao.kt
│   │   │   ├── MedicoDao.kt
│   │   │   ├── PacienteDao.kt
│   │   │   ├── SedeDao.kt
│   │   │   ├── TarifaDao.kt
│   │   │   └── UsuarioDao.kt
│   │   ├── database/
│   │   │   └── SannaDatabase.kt
│   │   └── entity/
│   │       ├── AseguradoraEntity.kt
│   │       ├── CitaEntity.kt
│   │       ├── ConvenioCoberturaEntity.kt
│   │       ├── CostoSalarialDirectoEntity.kt
│   │       ├── EspecialidadEntity.kt
│   │       ├── GastoOperativoEntity.kt
│   │       ├── HistorialReprogramacionEntity.kt
│   │       ├── HorarioMedicoEntity.kt
│   │       ├── InsumoEntity.kt
│   │       ├── MaterialEntity.kt
│   │       ├── MedicoEntity.kt
│   │       ├── MedicoEspecialidadEntity.kt
│   │       ├── MedicoSedeEntity.kt
│   │       ├── PacienteEntity.kt
│   │       ├── SedeEntity.kt
│   │       ├── SedeEspecialidadEntity.kt
│   │       ├── SueldoAdministrativoEntity.kt
│   │       ├── TarifaEntity.kt
│   │       ├── TiempoProductivoEntity.kt
│   │       └── UsuarioEntity.kt
│   └── repository/
│       ├── AdminRepository.kt
│       ├── AuthRepository.kt
│       ├── CitaRepository.kt
│       └── CostosRepository.kt
├── domain/
│   └── validation/
│       └── ValidationUtils.kt
├── presentation/
│   ├── components/
│   │   └── SannaComponents.kt
│   ├── navigation/
│   │   └── SannaNavGraph.kt
│   ├── screens/
│   │   ├── admin/
│   │   │   ├── GestionCitasScreen.kt
│   │   │   ├── GestionCostosScreen.kt
│   │   │   ├── GestionEspecialidadesScreen.kt
│   │   │   ├── GestionHorariosScreen.kt
│   │   │   ├── GestionMedicosScreen.kt
│   │   │   ├── GestionSedesScreen.kt
│   │   │   ├── GestionSegurosScreen.kt
│   │   │   └── HomeAdminScreen.kt
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── bienvenida/
│   │   │   └── WelcomeScreen.kt
│   │   ├── citas/
│   │   │   ├── DetalleCitaScreen.kt
│   │   │   ├── MisCitasScreen.kt
│   │   │   └── ReservarCitaScreen.kt
│   │   ├── home/
│   │   │   └── HomePacienteScreen.kt
│   │   └── perfil/
│   │       └── PerfilScreen.kt
│   └── viewmodel/
│       ├── AdminViewModel.kt
│       ├── AuthViewModel.kt
│       ├── CitasViewModel.kt
│       ├── CostosViewModel.kt
│       └── PacienteViewModel.kt
├── ui/
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── utils/
    ├── NotificationHelper.kt
    └── SessionManager.kt
```

## 6. Persistencia de Datos
La aplicación implementa **Room Database** (`SannaDatabase`), estructurada con **20 entidades relacionales** que modelan el dominio clínico y administrativo:

- **Entidades Principales:**
  - `UsuarioEntity` y `PacienteEntity`: Gestión de credenciales, roles y datos personales de pacientes.
  - `SedeEntity`, `EspecialidadEntity` y `SedeEspecialidadEntity`: Estructura física y especialidades médicas disponibles.
  - `MedicoEntity`, `MedicoSedeEntity`, `MedicoEspecialidadEntity` y `HorarioMedicoEntity`: Personal médico, asignación de sedes/especialidades y horarios de atención.
  - `CitaEntity` y `HistorialReprogramacionEntity`: Registro y seguimiento de citas médicas.
  - `AseguradoraEntity`, `ConvenioCoberturaEntity` y `TarifaEntity`: Gestión de seguros, convenios y tarifarios.
  - `InsumoEntity`, `MaterialEntity`, `TiempoProductivoEntity`, `CostoSalarialDirectoEntity`, `GastoOperativoEntity` y `SueldoAdministrativoEntity`: Modelado de costos operativos y financieros.
- **DAOs y Repositorios:** Cada entidad cuenta con su respectivo DAO optimizado con consultas SQL nativas y flujos reactivos, expuestos a los ViewModels mediante repositorios dedicados.
- **Seed Database:** La base de datos inicializa registros por defecto en su creación mediante un callback de Room (`RoomDatabase.Callback`).

## 7. Navegación y Pantallas
La navegación se centraliza en `SannaNavGraph.kt` utilizando **Jetpack Navigation Compose**, determinando la ruta inicial según el estado de sesión y rol del usuario:

- **`welcome`**: Pantalla de bienvenida en el primer inicio.
- **`login` & `register`**: Autenticación y registro de nuevos usuarios.
- **`home_paciente/{userId}/{pacienteId}`**: Panel principal del paciente con listado de citas.
- **`reservar_cita/{pacienteId}`**: Asistente de reserva de citas médicas.
- **`detalle_cita/{citaId}`**: Vista detallada de una cita específica.
- **`home_admin`**: Panel de control general para administradores.
- **Pantallas de Gestión Admin**: `gestion_sedes`, `gestion_especialidades`, `gestion_medicos`, `gestion_horarios`, `gestion_citas`, `gestion_seguros` y `gestion_costos`.

## 8. Validaciones y Buenas Prácticas
- **Validaciones de Entrada (`ValidationUtils`):**
  - Verificación de formato de correo electrónico mediante patrones estándar.
  - Validación de DNI peruano (exactamente 8 dígitos numéricos).
  - Validación de teléfono móvil (9 dígitos, iniciando en 9).
  - Validación de longitud mínima de contraseña (6 caracteres).
- **Seguridad Cifrada:** Hashing de contraseñas utilizando el algoritmo `PBKDF2WithHmacSHA1` con un *salt* aleatorio de 16 bytes (`SecureRandom`).
- **Gestión de Estado Reactiva:** Uso intensivo de Corrutinas y `StateFlow` para garantizar una interfaz fluida y libre de bloqueos en el hilo principal.
- **Interfaz Adaptativa:** Soporte completo para pantallas *Edge-to-Edge* mediante `enableEdgeToEdge()`.

## 9. Instalación y Ejecución
Para clonar, compilar y ejecutar el proyecto en su entorno de desarrollo:

### Requisitos Previos
- **Android Studio** (versión Jellyfish o superior).
- **JDK 11** instalado y configurado.
- Android SDK con **`compileSdk = 37`** y **`minSdk = 30`** (Android 11 o superior).

### Pasos de Ejecución
1. Clonar el repositorio o descargar el proyecto en su equipo.
2. Abrir **Android Studio** y seleccionar **Open an Existing Project**, indicando la carpeta raíz `GestorCitasSANNA`.
3. Esperar a que Gradle sincronice automáticamente las dependencias (o hacer clic en *Sync Project with Gradle Files*).
4. Conectar un dispositivo físico con depuración USB activada o iniciar un emulador con API 30+.
5. Hacer clic en el botón **Run** (`Shift + F10`) en la barra superior de Android Studio.

## 10. Estado del Proyecto
- **Implementado:**
  - Arquitectura MVVM completa con 20 entidades Room y repositorios.
  - Sistema de autenticación con DataStore y cifrado de claves.
  - Portal del Paciente con reserva de citas y visualización de historial.
  - Panel de Administración completo con sedes, médicos, horarios, especialidades, seguros y costos.
  - Navegografía robusta con Compose Navigation y paso seguro de argumentos.
- **Pendientes / Evolución Futura:**
  - Integración opcional con servicios backend en la nube (API REST / Firebase).
  - Pasarela de pagos en línea para copagos y tarifas médicas.
  - Notificaciones push locales avanzadas (AlarmManager / WorkManager).

## 11. Autor y Propósito
- **Autor:** Axcel Charahua.
- **Propósito:** Proyecto de portafolio técnico orientado al desarrollo avanzado de aplicaciones móviles nativas en Android.
