# Taller: "Native UI Re-Engineering & UX Analysis"

**Facultad de Ingeniería de Sistemas**  
**Proyecto:** Letterboxd Clone (High Fidelity)  
**Tecnología:** React Native 0.86+ (Hermes Engine)

---

## 1. Fase A: Selección y Análisis

### Reserva de App
- **Aplicación Seleccionada:** Letterboxd.

### Definición de Mercado
- **Público Objetivo:** Usuarios de 18 a 45 años apasionados por el cine y el consumo de contenidos en streaming.
- **Intereses:** Seguimiento de películas, redacción de reseñas, creación de listas personalizadas y socialización cinéfila.
- **Nivel Socioeconómico:** Medio-Alto, con acceso frecuente a dispositivos móviles y servicios de entretenimiento digital.

### Psicología del Color
- **Fondo Principal (#14181C):** Emula la oscuridad de una sala de cine, proporcionando elegancia y permitiendo que los posters de las películas (coloridos) resalten. Reduce la fatiga visual.
- **Verde Acento (#00E054):** Utilizado para el botón de "log" y acciones principales. Transmite energía, crecimiento y es el color distintivo de la marca.
- **Naranja Acento (#FF8000):** Se usa para destacar estados "Pro" o advertencias (como spoilers), captando la atención de forma inmediata por su alta visibilidad.
- **Azul Acento (#40BCF4):** Color de calma y confianza, utilizado para hipervínculos y títulos de películas.

### Auditoría de Componentes (Listas a clonar)
1. **Grilla de Posters (Films):** Implementada con `FlatList` en modo `numColumns={3}`.
2. **Lista de Reseñas (Reviews):** Estructura vertical con avatares circulares y estrellas de calificación.
3. **Sección de Artículos (Journal):** Tarjetas de ancho completo con imágenes panorámicas y tipografía audaz.

---

## 2. Fase B: Desarrollo Técnico

### Estructura de Datos
Se han definido modelos de datos realistas (`mockFilms`, `mockReviews`, `mockJournals`) que contienen identificadores únicos, URLs de imágenes de alta resolución y metadatos específicos (rating, spoilers, etc.).

### Implementación de Listas y Performance (60 FPS)
Se ha utilizado el componente `FlatList` nativo de React Native con las siguientes optimizaciones de recursos:
- **`removeClippedSubviews={true}`**: Mejora el uso de memoria al no renderizar elementos fuera de la pantalla.
- **`initialNumToRender={9}`**: Asegura que el área visible se llene instantáneamente.
- **`windowSize={5}`**: Limita el área de renderizado preventivo para mantener la fluidez del scroll.
- **`memo`**: Todos los items de las listas están memorizados para evitar re-renderizados innecesarios.

### Estilización
Se replicaron estrictamente los paddings (16px), márgenes y bordes redondeados (4px para posters, 12px para tarjetas de Journal) de la aplicación original para lograr una alta fidelidad visual.

---

## 3. Fase C: Crítica y Propuesta de Mejora

### Análisis Crítico
**Falla identificada:** En la aplicación original, las advertencias de spoilers son bloques de texto estáticos que a veces se pierden visualmente en reseñas largas o interrumpen bruscamente la estética del feed sin una invitación clara a la interacción.

### Propuesta de Mejora (Implementada)
Se ha re-diseñado la gestión de spoilers mediante una **micro-interacción reactiva**:
- **Componente Spoiler:** El texto del spoiler está oculto tras un contenedor con borde naranja intermitente (estilo dashed) que indica claramente que es una zona interactiva.
- **Retroalimentación:** Al tocarlo (`Pressable`), el estado local `isRevealed` cambia de forma fluida para mostrar el texto real, dando al usuario el control total sobre la información que desea ver.
- **Animación:** Se incluyó un efecto de escala (`transform: [{ scale: 0.98 }]`) al presionar para mejorar la sensación de interactividad nativa.

---

## 4. Criterios de Evaluación Cumplidos
- **Fidelidad Visual:** Interfaz idéntica a la original con tipografía y paleta de colores exacta.
- **Eficiencia:** Scroll suave de 60 FPS garantizado mediante optimización de ciclos de renderizado.
- **Código Limpio:** Estructura modularizada siguiendo principios SOLID.
