package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.AuthResponse
import com.example.aplicacion.model.LoginRequest
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

/**
 * Actividad encargada de la autenticación del usuario.
 * Gestiona la validación de tokens existentes, el inicio de sesión y la navegación al dashboard.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(MyApp.appContext)

        // Verificación automática de sesión: si el token es válido, saltamos directamente al Home
        if (tokenManager.getToken() != null) {
            if (tokenManager.isTokenExpired()) {
                tokenManager.clearToken()
                tokenManager.clearAll()
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
        }

        setContentView(R.layout.activity_login)

        // Inicialización de vistas
        val etEmail = findViewById<TextInputEditText>(R.id.etEmailLogin)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvIrARegistro)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Acción de inicio de sesión
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // UI: Estado de carga
            btnLogin.isEnabled = false
            btnLogin.text = ""
            progressBar.visibility = View.VISIBLE

            val loginRequest = LoginRequest(email, password)

            apiService.login(loginRequest).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    // UI: Restaurar estado tras la respuesta
                    btnLogin.isEnabled = true
                    btnLogin.text = "ENTRAR"
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        val authResponse = response.body()!!

                        // Almacenamiento persistente de datos del usuario
                        tokenManager.saveToken(authResponse.token)
                        tokenManager.saveUserDAta(
                            id = authResponse.usuario.id,
                            nombre = authResponse.usuario.nombre,
                            email = authResponse.usuario.email
                        )
                        tokenManager.saveLoginTime()

                        Toast.makeText(this@LoginActivity, "Bienvenido ${authResponse.usuario.nombre}", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas (${response.code()})", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    btnLogin.isEnabled = true
                    btnLogin.text = "ENTRAR"
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Error de conexión: Verifica tu internet", Toast.LENGTH_LONG).show()
                }
            })
        }

        // Navegación al registro
        tvRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}