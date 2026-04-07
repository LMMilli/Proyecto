package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.MedidaRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NuevaMedidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_medida)

        val etPeso = findViewById<EditText>(R.id.etNuevoPeso)
        val etGrasa = findViewById<EditText>(R.id.etGrasaNueva)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarMedidaForm)

        val idUsuario = intent.getLongExtra("ID_USUARIO", -1L)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnGuardar.setOnClickListener {
            val pesoTexto = etPeso.text.toString().trim()
            val grasaTexto = etGrasa.text.toString().trim()

            if(pesoTexto.isEmpty() || grasaTexto.isEmpty()){
                Toast.makeText(this, "Rellena ambos campos",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(idUsuario == -1L){
                Toast.makeText(this, "Error: No se encontro el usuario",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val peso = pesoTexto.toDoubleOrNull() ?: 0.0
            val grasa = grasaTexto.toDoubleOrNull() ?: 0.0

            val request = MedidaRequest(idUsuario, peso, grasa)

            apiService.registarMedida(request).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>){
                    if (response.isSuccessful){
                        Toast.makeText(this@NuevaMedidaActivity, "¡Progreso guardado!",
                            Toast.LENGTH_LONG).show()
                        finish()
                    }else{
                        Toast.makeText(this@NuevaMedidaActivity, "Error al guardar el progres",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable){
                    Toast.makeText(this@NuevaMedidaActivity, "Error de conexion",
                        Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}