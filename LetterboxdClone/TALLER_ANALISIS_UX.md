# Guía del Taller: "Native UI Re-Engineering & UX Analysis"

**Proyecto:** Letterboxd Clone (High Fidelity)
**Tecnología:** React Native 0.86+ (Hermes Engine)
**Estudiante:** Facultad de Ingeniería de Sistemas

---

## 1. Fase A: Selección y Análisis (Entregable 1)

### Reserva de App
- **Aplicación Seleccionada:** Letterboxd (Red social para cinéfilos).

### Definición de Mercado
- **Público Objetivo:** Jóvenes y adultos (18-45 años) con alto interés en el séptimo arte.
- **Intereses:** Crítica de cine, registro de visualizaciones (logging), coleccionismo digital y comunidad.
- **Nivel Socioeconómico:** Medio-Alto (usuarios con acceso a plataformas de streaming y cine).

### Psicología del Color
La marca utiliza una paleta de colores "Dark Mode" por defecto para emular la experiencia de una sala de cine oscura:
- **Background Principal (#14181C):** Aporta elegancia y reduce la fatiga visual. Simboliza la oscuridad del cine.
- **Verde Acento (#00E054):** Usado para "Logging" y acciones positivas. Transmite frescura y vitalidad.
- **Naranja Acento (#FF8000):** Usado para alertas de Spoilers y membresías Pro. Genera contraste y urgencia.
- **Azul Acento (#40BCF4):** Usado para links y títulos de películas, transmitiendo confianza y serenidad.

### Auditoría de Componentes (Listas)
1. **Grilla de Películas (Films):** Layout de 3 columnas para visualización masiva de posters.
2. **Feed de Reseñas (Reviews):** Lista vertical con avatares, estrellas de calificación y bloques de texto.
3. **Sección de Artículos (Journal):** Lista vertical de tarjetas grandes con imágenes cinematográficas panorámicas.

---

## 2. Fase B: Desarrollo Técnico (Entregable 2)

### Estructura de Datos
Se han creado modelos limpios (`mockFilms`, `mockReviews`, `mockJournals`) con datos realistas e imágenes de alta resolución vía Unsplash para simular el entorno productivo.

### Implementación de Listas (Performance 60 FPS)
Se utilizó el componente `<FlatList>` con las siguientes optimizaciones de memoria:
- `removeClippedSubviews={true}`: Libera recursos de elementos fuera de pantalla.
- `initialNumToRender={9}`: Carga inicial rápida del Viewport.
- `windowSize={5}`: Control estricto de la RAM para evitar "lag" en el scrolling.
- `key={numColumns}`: Solución técnica para el cambio dinámico de layout (Portrait/Landscape).

### Estilización
- Replicación exacta de fuentes "Sans-Serif" del sistema.
- Bordes redondeados de 4px a 12px según el componente.
- Paddings consistentes de 16px para el contenido textual.

---

## 3. Fase C: Crítica y Propuesta de Mejora (Entregable Final)

### Análisis Crítico
**Falla identificada:** En la aplicación original, los bloques de advertencia de spoilers son estáticos y a menudo ocupan un espacio excesivo o se confunden con el fondo, rompiendo el flujo de lectura de las reseñas.

### Propuesta de Mejora (Implementada)
**Micro-interacción de Spoiler:** 
Se implementó un componente de reseña inteligente. Si una reseña contiene spoilers:
1. Muestra un contenedor con **borde punteado naranja** y tipografía de advertencia.
2. Al presionar (**Pressable**), se ejecuta una transición de estado local que revela el texto de forma instantánea.
3. Esto mejora la retroalimentación visual y permite que el usuario decida activamente qué contenido consumir sin ensuciar la interfaz.

### Retroalimentación Táctil
Todos los elementos interactivos utilizan la propiedad `transform: [{ scale: 0.98 }]` al ser presionados, simulando una respuesta física nativa de "hundimiento" que mejora la percepción de calidad de la app.

---
