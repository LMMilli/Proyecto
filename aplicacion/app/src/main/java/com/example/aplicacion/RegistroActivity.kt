package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
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
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Enlazamos las variables
        val etNombre = findViewById<TextInputEditText>(R.id.etNombreRegistro)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmailRegistro)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvVolver = findViewById<TextView>(R.id.tvVolverLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarRegistro)

        //Iniciamos Retrofit
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Funcion del boton Registar
        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.toString().trim()
            val password = etPassword.text.toString().trim()

            //Validacion para que los campos no este vacios
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Cambiamos el esta de la UI a "Cargando"
            btnRegistrar.isEnabled = false
            btnRegistrar.text = ""
            progressBar.visibility = View.VISIBLE

            //Crear RegistreRequest con los datos
            val request = RegistroRequest(nombre, email, password)

            //Enviamos la peticion POST al servidor en segundo plano
            apiService.registrarUsuario(request).enqueue(object : Callback<Usuario>{
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>){
                    //Restauramos la UI
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "REGISTRARSE"
                    progressBar.visibility = View.GONE

                    if(response.isSuccessful){
                        Toast.makeText(this@RegistroActivity, "Cuenta creada con exisot", Toast.LENGTH_SHORT).show()
                        finish() //Vuelve al login directamente
                    }else{
                        //Extrare el error si es posible
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("REGISTRO_DEBUG", "Error: $errorBody")
                        Toast.makeText(this@RegistroActivity, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>, t: Throwable){
                    //Restauramos la UI
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "REGISTRARSE"
                    progressBar.visibility = View.GONE

                    Toast.makeText(this@RegistroActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //Accion para el texto de volver
        tvVolver.setOnClickListener {
            finish() //Cierra la pantalla y vuelve al login
        }


    }
}