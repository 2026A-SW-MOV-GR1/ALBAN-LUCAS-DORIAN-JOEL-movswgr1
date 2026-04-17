# 📚 Documentación: Arquitectura y Evolución del Desarrollo Móvil

> **Facultad de Ingeniería de Sistemas - Escuela Politécnica Nacional**
> Recopilación de fuentes oficiales, documentación técnica y blogs de ingeniería utilizados para el análisis arquitectónico.

---

## 📋 Datos

- **Grupo:** 1
- **Aplicativos Analizados:** Tinder y Waze
- **Integrantes:**
  - Dorian Albán
  - Erick Alpusig
  - Eduardo Arcos

---

## 🏗️ Parte 1: Evolución Arquitectónica (Fuentes Generales)

Esta sección contiene la documentación oficial que respalda el funcionamiento subyacente de cada paradigma de desarrollo móvil.

### 1. Desarrollo Nativo (iOS / Android)
Documentación oficial sobre los patrones de diseño y arquitectura recomendados por los propios creadores de los sistemas operativos.
* 🤖 **Android:** [Core Architecture Guidelines - Android Developers](https://developer.android.com/topic/architecture)
* 🍏 **iOS:** [Swift and SwiftUI Essentials - Apple Developer](https://developer.apple.com/swiftui/get-started/)
* 🛠️ **Patrones:** [Model-View-ViewModel (MVVM) en Swift](https://developer.apple.com/documentation/swiftui/managing-model-data-in-your-app)

### 2. La Era del Puente (React Native)
Información técnica sobre cómo los frameworks basados en JavaScript se comunican con los componentes nativos y cómo están resolviendo sus cuellos de botella.
* ⚛️ **React Native:** [The New Architecture (Fabric & TurboModules)](https://reactnative.dev/architecture/overview)

### 3. Motor de Renderizado Propio (Flutter)
Detalles profundos sobre el funcionamiento de motores gráficos (Skia/Impeller) para dibujar interfaces sin usar las vistas nativas del sistema.
* 🦋 **Flutter:** [Architectural Overview & Rendering Pipeline](https://docs.flutter.dev/resources/architectural-overview)

### 4. Nuevos Paradigmas (Foldables & AR)
Guías de adaptación arquitectónica para lidiar con cambios de estado dinámicos (pantallas plegables) y renderizado de baja latencia (Realidad Aumentada).
* 📱 **Foldables:** [Guía de Diseño para Dispositivos Plegables](https://developer.android.com/guide/topics/ui/foldables)
* 🏗️ **AR/XR:** [Conceptos Fundamentales de ARCore](https://developers.google.com/ar/develop/fundamentals)

---

## 🔬 Parte 2: Casos de Estudio (Ingeniería Inversa)

Fuentes directas de los equipos de ingeniería de las aplicaciones seleccionadas para analizar sus decisiones de stack tecnológico y escalabilidad.

### 🔥 Aplicativo 1: Tinder
*Enfoque en desarrollo Nativo (Swift/Kotlin) para optimizar la respuesta táctil (Swipe) y el manejo fluido de imágenes en memoria.*
* 📝 **Blog de Ingeniería:** [Tinder Engineering (Official Tech Portal)](https://enclaveinformatico.com/ingenieria-social-estafa-fraudes-en-tinder-apps-de-citas/)
* ⚙️ **Artículo:** [Tinder se apoya de la IA](https://www.eleconomista.com.mx/tecnologia/tinder-apoya-ia-crear-conexiones-profundas-personalizadas-20260312-803980.html)
* 📂 **Framework Propio:** [Nodes: Tinder's Mobile Architecture Framework](https://github.com/Tinder/Nodes)

### 🚗 Aplicativo 2: Waze
*Enfoque en motor propietario (C++) para renderizado de mapas pesados y microservicios para sockets de tráfico en tiempo real.*
* ☁️ **Infraestructura:** [Waze & Google Cloud: Escalabilidad en Tiempo Real](https://cloud.google.com/blog/products/containers-kubernetes/infrastructure-as-code-at-waze-using-config-connector)
* 🛠️ **Stack Tecnológico:** [Waze Tech Stack & Tools (StackShare)](https://stackshare.io/waze/waze)
* 🗺️ **API Geográfica:** [Waze Deep Links e Interoperabilidad](https://developers.google.com/waze/deeplinks)

---
*Documentación generada para la materia de Aplicaciones Móviles.*
