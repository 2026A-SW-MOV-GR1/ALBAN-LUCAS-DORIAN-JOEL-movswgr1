# Informe de Experimento: La Batalla del Estado
**Asignatura:** Taller de Desarrollo Móvil (Clase 07)
**Enfoque:** React Native Lifecycle & Persistence

## 1. Rutas e Inspección de Código

### A. AndroidManifest.xml
**Ruta:** `android/app/src/main/AndroidManifest.xml`
```xml
<activity
  android:name=".MainActivity"
  android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|screenSize|smallestScreenSize|uiMode"
  android:launchMode="singleTask"
  android:exported="true">
```
*Nota: La propiedad `configChanges` es la clave para entender la persistencia del estado en React Native durante la rotación.*

### B. MainActivity.kt (Logs Nativo)
**Ruta:** `android/app/src/main/java/com/proyectocontador/MainActivity.kt`
```kotlin
// Sobrescritura para trazabilidad total de eventos nativos
override fun onPause() {
    super.onPause()
    Log.d("CICLO_VIDA_NATIVO", "onPause: La Activity está perdiendo el foco.")
}

override fun onDestroy() {
    super.onDestroy()
    Log.d("CICLO_VIDA_NATIVO", "onDestroy: La Activity va a ser destruida.")
}
```

### C. App.tsx (Lógica en JS)
**Ruta:** `App.tsx`
```javascript
// Hook usado para persistencia y monitoreo
const [count, setCount] = useState(0);

useEffect(() => {
  const subscription = AppState.addEventListener('change', (nextAppState) => {
    console.log(`[React Native AppState] Cambio detectado: ${nextAppState}`);
  });
  return () => subscription.remove();
}, []);
```

## 2. Análisis del Experimento de Rotación

**Observación Técnica:** En React Native, al aumentar el contador y rotar el dispositivo, el valor **se mantiene**.

**¿Cómo se soluciona en React Native?**
A diferencia de Android Nativo (donde se suele usar `onSaveInstanceState`), en React Native la solución viene preconfigurada en el `AndroidManifest.xml` mediante `android:configChanges`. Esto evita que el sistema operativo destruya y recree la `Activity`. Como la `Activity` no se destruye, el contexto de JavaScript (donde reside el `useState`) permanece intacto.

**Secuencia de Logs en Rotación:**
1. `ActivityThread: onConfigurationChanged` -> El sistema detecta el cambio de orientación (port a land).
2. `VRI[MainActivity]: updateConfiguration` -> La vista se ajusta a las nuevas dimensiones.
3. **Resultado:** **NO se dispara `onDestroy`**. La Activity se mantiene viva y el estado de JS no se reinicia.

## 3. Secuencia de Logs Capturada (Logcat)

Basado en la inspección real del Logcat durante el experimento:

### Flujo al presionar "Home" (Segundo Plano):
```text
22:01:03.700 D CICLO_VIDA_NATIVO: onPause: La Activity está perdiendo el foco.
22:01:03.701 I ReactNativeJS: [React Native AppState] Cambio detectado: background
22:01:03.749 D CICLO_VIDA_NATIVO: onStop: La Activity ya no es visible para el usuario.
```

### Flujo al regresar a la App:
```text
22:01:08.690 D CICLO_VIDA_NATIVO: onRestart: La Activity se está reiniciando.
22:01:08.692 D CICLO_VIDA_NATIVO: onStart: La Activity está a punto de hacerse visible.
22:01:08.706 D CICLO_VIDA_NATIVO: onResume: La Activity se ha vuelto interactiva (foco).
22:01:08.712 I ReactNativeJS: [React Native AppState] Cambio detectado: active
```

## 4. Funciones y Hooks para la Persistencia

Para lograr que la información no se pierda, se utilizaron:
- **`useState`**: Mantiene el estado del contador en la memoria del motor de JavaScript.
- **`android:configChanges`**: Evita la destrucción de la `Activity` nativa, protegiendo así el hilo de ejecución de JS.
- **`useEffect` + `AppState`**: Permiten interceptar las transiciones de ciclo de vida nativas desde el lado de JavaScript para reaccionar ante el envío a segundo plano sin perder la lógica de negocio.
