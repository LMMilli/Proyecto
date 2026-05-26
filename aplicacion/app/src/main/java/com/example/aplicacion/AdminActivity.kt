package com.example.aplicacion

import android.os.Bundle
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity

/**
 * Actividad encargada de la gestión administrativa.
 * Proporciona acceso a las funciones de creación de ejercicios y rutinas,
 * además de permitir el retorno a la pantalla principal.
 */
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Inicialización de componentes de la UI
        val bntEjercicios = findViewById<MaterialButton>(R.id.btnAdminEjercicios)
        val btnRutinas = findViewById<MaterialButton>(R.id.btnAdminRutinas)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolverHome)

        // Navegación a la pantalla de creación de ejercicios
        bntEjercicios.setOnClickListener {
            val intent = android.content.Intent(this, CrearEjercicioActivity::class.java)
            startActivity(intent)
        }

        // Navegación a la pantalla de creación de rutinas
        btnRutinas.setOnClickListener {
            val intent = android.content.Intent(this, CrearRutinaActivity::class.java)
            startActivity(intent)
        }

        // Cierra la actividad actual para regresar a la anterior en la pila de navegación
        btnVolver.setOnClickListener {
            finish()
        }
    }
}