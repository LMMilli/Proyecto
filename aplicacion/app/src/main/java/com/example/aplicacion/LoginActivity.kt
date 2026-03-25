package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.LoginRequest
import com.example.aplicacion.model.Usuario
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            apiService.longin(loginRequest).enqueue(object : Callback<Usuario>{
                //Si el servidor responde tanto un OK como un error
                override fun onResponse(call : Call<Usuario>, response: Response<Usuario>){
                    if(response.isSuccessful){
                        //Funciona el Login
                        val usuarioLogueado = response.body()
                        Toast.makeText(this@LoginActivity, "Bienvendio ${usuarioLogueado?.nombre}!",
                            Toast.LENGTH_LONG).show()

                        //Vamos a la HomeActivity
                        intent = android.content.Intent(this@LoginActivity, HomeActivity::class.java)

                        //Le añadimos los datos del usuario
                        intent.putExtra("NOMBRE_USUARIO", usuarioLogueado?.nombre)
                        intent.putExtra("ID_USUARIO", usuarioLogueado?.id)
                        intent.putExtra("EMAIL_USUARIO", usuarioLogueado?.email)
                        startActivity(intent)
                        finish()//Se cirra la el login para que el usuario si le da para atras no vuelva al login iuu

                    }else{
                        //No funciona
                        Toast.makeText(this@LoginActivity, "Email o contraseña incorrecto",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable){
                    Toast.makeText(this@LoginActivity, "Error de conexión",
                        Toast.LENGTH_LONG).show()
                }
            })

        }

        //Fucion no tines cuenta
        tvRegistro.setOnClickListener {
            //Pantalla de registro
            val intent = android.content.Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }



    }
}

