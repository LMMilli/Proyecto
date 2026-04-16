package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.AuthResponse
import com.example.aplicacion.model.LoginRequest
import com.example.aplicacion.model.Usuario
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Istaciamos el Token Manager
        val tokenManager = TokenManager(MyApp.appContext)

        //Comprobamos si hay un token guardado
        if (tokenManager.getToken() != null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        //Enlazmos las variables con los item de la pantalla
        val etEmail = findViewById<EditText>(R.id.etEmailLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvIrARegistro)

        //Iniciamos Retrofit
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Fucion del boton entrar
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            //Validacion para que los campos no este vacioes
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Crear el LogineRequest con los datos
            val loginRequest = LoginRequest(email, password)

            //Eviamos la peticon POST al servidor en segundo plano
            apiService.login(loginRequest).enqueue(object : Callback<AuthResponse>{
                //Si el servidor responde tanto un OK como un error
                override fun onResponse(call : Call<AuthResponse>, response: Response<AuthResponse>){
                    if(response.isSuccessful && response.body() != null){
                        //Funciona el Login
                        val authResponse = response.body()!!

                        //Guardamos el token en la caja fuerte
                        val tokenManager = TokenManager(MyApp.appContext)
                        tokenManager.saveToken(authResponse.token)

                        //Extraemos el usuario
                        val usuarioLogueado = authResponse.usuario

                        tokenManager.saveUserDAta(
                            id = usuarioLogueado.id,
                            nombre = usuarioLogueado.nombre,
                            email = usuarioLogueado.email
                        )

                        Toast.makeText(this@LoginActivity, "Bienvenido ${usuarioLogueado.nombre}!",
                            Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)

                        startActivity(intent)
                        finish()

                    }else{
                        //No funciona
                        Toast.makeText(this@LoginActivity, "Email o contraseña incorrecto",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<AuthResponse>, t: Throwable){
                    Toast.makeText(this@LoginActivity, "Error de conexión",
                        Toast.LENGTH_LONG).show()
                }
            })

        }

        //Fucion no tines cuenta
        tvRegistro.setOnClickListener {
            //Pantalla de registro
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }



    }
}

