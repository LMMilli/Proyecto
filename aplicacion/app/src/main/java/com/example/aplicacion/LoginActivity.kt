package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.AuthResponse
import com.example.aplicacion.model.LoginRequest
import com.example.aplicacion.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Instanciamos el Token Manager
        val tokenManager = TokenManager(MyApp.appContext)

        //Comprobamos is hay un token guardado
        if(tokenManager.getToken() != null){
            if(tokenManager.isTokenExpired()){
                tokenManager.clearAll()
            }else{
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
        }

        setContentView(R.layout.activity_login)

        //Enlazamos la variables
        val etEmail = findViewById<TextInputEditText>(R.id.etEmailLogin)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvIrARegistro)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarLogin)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Cambios el estado de UI a "Cargando"
            btnLogin.isEnabled = false
            btnLogin.text = ""
            progressBar.visibility = View.VISIBLE

            val loginRequest = LoginRequest(email, password)

            apiService.login(loginRequest).enqueue(object : Callback<AuthResponse>{
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>){
                    //Restauramos la UI
                    btnLogin.isEnabled = true
                    btnLogin.text = "ENTRAR"
                    progressBar.visibility = View.GONE

                    if(response.isSuccessful && response.body() !=null){
                        val authResponse = response.body()!!

                        tokenManager.saveToken(authResponse.token)
                        val usuarioLogueado = authResponse.usuario

                        tokenManager.saveUserDAta(
                            id = usuarioLogueado.id,
                            nombre = usuarioLogueado.nombre,
                            email = usuarioLogueado.email
                        )
                        tokenManager.saveLoginTime()

                        Toast.makeText(this@LoginActivity, "Bienvenido ${usuarioLogueado.nombre}",
                            Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val errorBody = response.errorBody()?.string()
                        val httpCode = response.code()

                        android.util.Log.e("LOGIN_DEBUG", "Código HTTP: $httpCode | Mensaje: $errorBody")
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas ($httpCode)", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable){
                    //Restauramos la UI
                    btnLogin.isEnabled = true
                    btnLogin.text = "ENTRAR"
                    progressBar.visibility = View.GONE

                    Toast.makeText(this@LoginActivity, "Error de conexión: Verifica tu internet", Toast.LENGTH_LONG).show()
                }
            })
        }

        tvRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}

