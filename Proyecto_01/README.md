# Proyecto Práctico - Red y Persistencia Segura

## Introducción
Este proyecto constituye una solución técnica avanzada desarrollada en Android Nativo (Kotlin/Compose), diseñada para demostrar la integración de servicios web asíncronos y la gestión multidimensional de la persistencia de datos bajo criterios de seguridad industrial.

---

## Módulo 1 - Conectividad REST (Retrofit & JSONPlaceholder)
La arquitectura de red se implementó utilizando **Retrofit 2**, interactuando con la API de pruebas *JSONPlaceholder*. El sistema soporta operaciones transaccionales mediante los verbos HTTP **GET** (consulta de recursos) y **PUT** (actualización simulada).

**Puntos Clave de Implementación:**
- **Asincronía y UX:** Se gestionó el ciclo de vida de las peticiones mediante `viewModelScope`. Para garantizar una experiencia de usuario (UX) coherente, se implementó una variable de estado `isLoadingNetwork` que bloquea reactivamente los componentes de interacción (botones de envío) durante la latencia de red, evitando condiciones de carrera o peticiones redundantes.
- **Persistencia Simulada:** Dado que JSONPlaceholder no persiste cambios de forma real, se integró un `localPostsCache` en el `MainViewModel`. Esto permite que una actualización (PUT) exitosa se vea reflejada inmediatamente en consultas posteriores (GET) dentro de la misma sesión, simulando un entorno de producción real.

---

## Módulo 2 - Criterio Tecnológico de Persistencia
Se han integrado tres paradigmas de almacenamiento distintos para cubrir diversos escenarios operativos:

| Mecanismo | Nivel de Seguridad | Naturaleza de Acceso | Propósito Arquitectónico |
| :--- | :--- | :--- | :--- |
| **SharedPreferences** | Bajo (Texto Plano) | Síncrono / XML Directo | Gestión de flags de interfaz y preferencias de usuario no sensibles que requieren acceso inmediato. |
| **Jetpack DataStore** | Medio (Sin cifrado) | Reactivo (Kotlin Flow) | Sustituto moderno de SP que evita bloqueos del hilo principal (UI Thread) mediante operaciones asíncronas seguras. |
| **EncryptedSharedPreferences** | Máximo (Cifrado) | AES-256 SIV & GCM | Almacenamiento de secretos, tokens de identidad y fichas confidenciales mediante cifrado por hardware (Android Keystore). |

### Diferenciación Estructural:
A diferencia de **SharedPreferences**, que opera sobre un archivo XML accesible y realiza operaciones de lectura/escritura que pueden comprometer el rendimiento del hilo principal, **DataStore** utiliza una infraestructura basada en corrutinas para garantizar la integridad de los datos y la fluidez de la interfaz, eliminando el riesgo de errores `ANR` (App Not Responding).

---

## Comportamiento Operativo de Secretos
La gestión de secretos se rige bajo el principio de **conocimiento previo de llave**. El flujo lógico es el siguiente:
1. **Guardar:** Se asocia un valor a una clave identificadora. En el caso de `EncryptedSharedPreferences`, los datos se cifran inmediatamente usando el esquema `AES256_GCM` para valores y `AES256_SIV` para llaves.
2. **Recuperar:** El sistema exige la llave exacta para realizar el desempaquetado de la información. Si la llave no existe o el compartimento es erróneo, se retorna una respuesta nula gestionada mediante tipos seguros de Kotlin, evitando excepciones en tiempo de ejecución.

---

## Gestión de Estado y Hilos
La aplicación utiliza **State Hoisting** y **Unidirectional Data Flow (UDF)** para mantener la consistencia visual:
- **Corrutinas:** Todas las operaciones de E/S (Red y Disco) se ejecutan fuera del hilo principal para asegurar una UI de 60 FPS.
- **Reactividad:** El uso de `mutableStateOf` en el `MainViewModel` permite que la interfaz de Compose se recomponga instantáneamente ante cambios en la disponibilidad de datos o el estado de los secretos.
- **Seguridad en Hilos:** Al utilizar DataStore y Retrofit (con suspend functions), se garantiza que no existan bloqueos mutuos ni corrupción de memoria durante el acceso concurrente a la persistencia.
