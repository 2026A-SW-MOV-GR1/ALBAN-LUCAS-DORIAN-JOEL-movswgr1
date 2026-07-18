package com.proyectointents

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import com.facebook.react.bridge.*
import java.io.ByteArrayOutputStream

class CameraModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    private var mPickerPromise: Promise? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "CameraModule"
    }

    @ReactMethod
    fun takePhoto(promise: Promise) {
        val currentActivity = currentActivity

        if (currentActivity == null) {
            promise.reject("E_ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist")
            return
        }

        mPickerPromise = promise

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(currentActivity.packageManager) != null) {
            currentActivity.startActivityForResult(cameraIntent, 1)
        } else {
            promise.reject("E_CAMERA_NOT_AVAILABLE", "Camera not available")
        }
    }

    override fun onActivityResult(activity: Activity?, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (mPickerPromise != null) {
                if (resultCode == Activity.RESULT_OK) {
                    val extras = data?.extras
                    val imageBitmap = extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
                        mPickerPromise?.resolve(encodedString)
                    } else {
                        mPickerPromise?.reject("E_IMAGE_CAPTURE_FAILED", "Failed to capture image")
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mPickerPromise?.reject("E_PICKER_CANCELLED", "Image capture cancelled")
                }
                mPickerPromise = null
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        // No-op
    }
}
