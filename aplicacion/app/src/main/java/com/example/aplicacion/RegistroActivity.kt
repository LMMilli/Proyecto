package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.RegistroRequest
import com.example.aplicacion.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Enlazmos las variables con los item de la pantalla
        val etNombre = findViewById<EditText>(R.id.etNombreRegistro)
        val etEmail = findViewById<EditText>(R.id.etEmailRegistro)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegistro)
        val btnRegistar = findViewById<Button>(R.id.btnRegistrar)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLogin)

        //Iniciamos Retrofit
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Funcion del boton Registar
        btnRegistar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            //Validacion para que los campos no este vacioes
            if(nombre.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Crear el RegisterRequest con los datos
            val request = RegistroRequest(nombre, email, password)

            //Eviamos la peticon POST al servidor en segundo plano
            apiService.registrarUsuario(request).enqueue(object : Callback<Usuario>{
                //Si el servidor responde on OK o con error
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>){
                    if(response.isSuccessful){
                        Toast.makeText(this@RegistroActivity,
                            "Cuenta creada", Toast.LENGTH_LONG).show()
                        finish() // Para cerrar la pantalla de registro y volver al login directamente
                    }else{
                        Toast.makeText(this@RegistroActivity, "Error al crear la cuenta",
                            Toast.LENGTH_LONG).show()
                    }
                }

                //Si el servidor no responde
                override fun onFailure(call: Call<Usuario>, t: Throwable ) {
                    Toast.makeText(this@RegistroActivity, "Error de conexion",
                        Toast.LENGTH_LONG).show()
                }
            })
        }

        //Accion para el texto de volver
        tvVolver.setOnClickListener {
            finish() //Cierra la pantalla y vuelve al login
        }

    }
}