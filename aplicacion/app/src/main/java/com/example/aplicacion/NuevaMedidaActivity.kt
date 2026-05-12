package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.MedidaRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NuevaMedidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_medida)

        //Enlazamos los componentes
        val etPeso = findViewById<TextInputEditText>(R.id.etNuevoPeso)
        val etGrasa = findViewById<TextInputEditText>(R.id.etGrasaNueva)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarMediaForm)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarNuevaMedida)

        val idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnGuardar.setOnClickListener {
            val pesoTexto = etPeso.text.toString().trim()
            val grasaTexto = etGrasa.text.toString().trim()

            if(pesoTexto.isEmpty() || grasaTexto.isEmpty()){
                Toast.makeText(this, "Rellena ambos campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (idUsuario == -1L){
                Toast.makeText(this, "Error: NO se encontró al usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Cambiamos el estado de la UI a "Guardando..."
            btnGuardar.isEnabled = false
            btnGuardar.text = ""
            progressBar.visibility = View.VISIBLE

            val peso = pesoTexto.toDoubleOrNull() ?: 0.0
            val grasa = grasaTexto.toDoubleOrNull() ?: 0.0

            val request = MedidaRequest(idUsuario, peso, grasa)

            apiService.registarMedida(request).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>){
                    //Restauramos la UI
                    btnGuardar.isEnabled = true
                    btnGuardar.text = "GUARDAR"
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful){
                        Toast.makeText(this@NuevaMedidaActivity, "¡Progreso guardado!", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this@NuevaMedidaActivity, "Error al guardar el progreso",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable){
                    //Restauramos la UI
                    btnGuardar.isEnabled = true
                    btnGuardar.text = "GUARDAR"
                    progressBar.visibility = View.GONE

                    Toast.makeText(this@NuevaMedidaActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}