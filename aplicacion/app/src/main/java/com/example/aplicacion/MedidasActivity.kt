package com.example.aplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Medida
import com.example.aplicacion.model.MedidaRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedidasActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var lvHistorial: ListView
    private var idUsuario: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medidas)

        lvHistorial = findViewById(R.id.lvHistorialMedidas)
        val btnNuevo = findViewById<Button>(R.id.btnIrNuevaMedida)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnNuevo.setOnClickListener {
            val intent = android.content.Intent(this, NuevaMedidaActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        cargarHistorial()
    }

    private fun cargarHistorial(){
        if (idUsuario == -1L) return

        apiService.obtenerMedidas(idUsuario).enqueue(object : Callback<List<Medida>>{
            override fun onResponse(call: Call<List<Medida>>, response: Response<List<Medida>>){
                if(response.isSuccessful && response.body() != null){
                    val listaMedida = response.body()!!

                    val textoHistorial = listaMedida.map { "Peso: ${it.pesoCorporal}kg | Grasa ${it.porcentajeGrasa}%" }

                    lvHistorial.adapter = ArrayAdapter(
                        this@MedidasActivity, android.R.layout.simple_list_item_1, textoHistorial
                    )
                }
            }
            override fun onFailure(call: Call<List<Medida>>, t: Throwable){
                Toast.makeText(this@MedidasActivity, "Error de red",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}