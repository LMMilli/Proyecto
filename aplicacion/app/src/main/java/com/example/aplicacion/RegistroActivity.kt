package com.example.aplicacion

import android.os.Bundle
import android.util.Patterns // Importante para validar el email
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.RegistroRequest
import com.example.aplicacion.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad encargada del registro de nuevos usuarios en la plataforma.
 * Envía las credenciales al servidor y gestiona la respuesta de éxito o error.
 */
class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicialización de vistas
        val etNombre = findViewById<TextInputEditText>(R.id.etNombreRegistro)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmailRegistro)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarRegistro)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 1. Validación de campos obligatorios vacíos
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Validación de formato de correo electrónico
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Introduce un correo electrónico válido"
                etEmail.requestFocus() // Lleva el cursor directamente a este campo
                return@setOnClickListener
            }

            // 3. Validación de seguridad mínima para la contraseña
            if (password.length < 6) {
                etPassword.error = "La contraseña debe tener al menos 6 caracteres"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // Limpiar errores previos si todo está bien
            etEmail.error = null
            etPassword.error = null

            // UI: Estado de carga
            btnRegistrar.isEnabled = false
            btnRegistrar.text = ""
            progressBar.visibility = View.VISIBLE

            val request = RegistroRequest(nombre, email, password)

            // Petición asíncrona al backend
            apiService.registrarUsuario(request).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    // UI: Restaurar estado
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "REGISTRARSE"
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful) {
                        Toast.makeText(this@RegistroActivity, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                        finish() // Regreso al Login
                    } else {
                        Toast.makeText(this@RegistroActivity, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "REGISTRARSE"
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RegistroActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Navegación de retorno al Login
        tvVolver.setOnClickListener {
            finish()
        }
    }
}