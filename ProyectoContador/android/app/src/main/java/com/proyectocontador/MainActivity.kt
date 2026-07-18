package com.proyectocontador

import android.os.Bundle
import android.util.Log
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

class MainActivity : ReactActivity() {

    private val TAG = "CICLO_VIDA_NATIVO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: La Activity ha sido creada. [savedInstanceState: ${savedInstanceState != null}]")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: La Activity está a punto de hacerse visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: La Activity se ha vuelto interactiva (foco).")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: La Activity está perdiendo el foco (ej. multiventana o transición).")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: La Activity ya no es visible para el usuario.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: La Activity se está reiniciando tras haber estado detenida.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: La Activity va a ser destruida. El sistema libera recursos.")
    }

    /**
     * Returns the name of the main component registered from JavaScript. This is used to schedule
     * rendering of the component.
     */
    override fun getMainComponentName(): String = "ProyectoContador"

    /**
     * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
     * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
     */
    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
