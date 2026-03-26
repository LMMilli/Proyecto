package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val bntEjercicios = findViewById<Button>(R.id.btnAdminEjercicios)
        val btnRutinas = findViewById<Button>(R.id.btnAdminRutinas)
        val btnVolver = findViewById<Button>(R.id.btnVolverHome)

        bntEjercicios.setOnClickListener {
            val intent = android.content.Intent(this, CrearEjercicioActivity::class.java)
            startActivity(intent)
        }

        btnRutinas.setOnClickListener {
            Toast.makeText(this, "Proxamente: Crear una rutina",
                Toast.LENGTH_LONG).show()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}