package com.example.petdaycarekotandfire

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val iniciar = findViewById<Button>(R.id.buttonIniciar)

        iniciar.setOnClickListener {
            var i = Intent(applicationContext,Login::class.java)
            startActivity(i)
        }

    }

}