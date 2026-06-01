package com.example.moviles2026aswgr1

import android.content.res.Configuration
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        applyResourcesToUi()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyResourcesToUi()
    }

    private fun applyResourcesToUi() {
        val root = findViewById<ConstraintLayout>(R.id.main)
        val messageTextView = findViewById<TextView>(R.id.messageTextView)

        root.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_background_color))
        messageTextView.text = getString(R.string.custom_message)
        messageTextView.setTextColor(ContextCompat.getColor(this, R.color.custom_text_color))
    }
}