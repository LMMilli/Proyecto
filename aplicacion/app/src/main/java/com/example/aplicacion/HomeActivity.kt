package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/**
 * Actividad principal (Dashboard) que actúa como punto de navegación central.
 * Gestiona el control de acceso administrativo y la sesión del usuario.
 */
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicialización de componentes de la UI
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnEntrenamientos = findViewById<MaterialButton>(R.id.btnMisEntrenamientos)
        val btnRutinas = findViewById<MaterialButton>(R.id.btnCatalogoRutinas)
        val btnMedidas = findViewById<MaterialButton>(R.id.btnMisMedidas)
        val btnObjetivos = findViewById<MaterialButton>(R.id.btnMisObjetivos)
        val btnAdmin = findViewById<MaterialButton>(R.id.btnPanelAdmin)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        val tokenManager = TokenManager(MyApp.appContext)

        // Recuperación de datos del usuario logueado
        val nombre = tokenManager.getUserName()
        val idUsuario = tokenManager.getUserId()
        val email = tokenManager.getUserEmail()
        val emailAdmin = "millimermar@gmail.com"

        // Personalización del saludo
        tvBienvenida.text = "Hola, $nombre!"

        // Control de acceso: mostrar botón de administración solo al administrador
        if (email == emailAdmin) {
            btnAdmin.visibility = View.VISIBLE
        }

        // --- Configuración de Navegación ---

        btnEntrenamientos.setOnClickListener {
            val intent = Intent(this, HistorialEntrenamientosActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnRutinas.setOnClickListener {
            val intent = Intent(this, CatalogoRutinasActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnMedidas.setOnClickListener {
            val intent = Intent(this, MedidasActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnObjetivos.setOnClickListener {
            val intent = Intent(this, ObjetivosActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnAdmin.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        // Gestión de sesión: cierre seguro y limpieza de la pila de navegación
        btnLogout.setOnClickListener {
            tokenManager.clearAll() // Borrado de credenciales

            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            // Asegura que el usuario no pueda volver atrás al dashboard tras cerrar sesión
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}