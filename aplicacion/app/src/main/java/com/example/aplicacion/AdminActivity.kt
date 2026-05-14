package com.example.aplicacion

import android.os.Bundle
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val bntEjercicios = findViewById<MaterialButton>(R.id.btnAdminEjercicios)
        val btnRutinas = findViewById<MaterialButton>(R.id.btnAdminRutinas)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolverHome)

        bntEjercicios.setOnClickListener {
            val intent = android.content.Intent(this, CrearEjercicioActivity::class.java)
            startActivity(intent)
        }

        btnRutinas.setOnClickListener {
            val intent = android.content.Intent(this, CrearRutinaActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}