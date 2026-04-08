package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Entrenamiento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistorialEntrenamientosActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var lvHistorial: ListView
    private var idUsuario: Long = -1L

    private var listaEntrenamientos: List<Entrenamiento> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_entrenamientos)

        lvHistorial = findViewById(R.id.lvHistorialEntrenaientos)
        val btnNuevo = findViewById<Button>(R.id.btnNuveoEntrenamiento)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        lvHistorial.setOnItemClickListener { _, _, position, _ ->

            //Seleccionadomos el entrenamiento
            val entrenamientoSelecionado = listaEntrenamientos[position]

            val intent = Intent(this, DetalleEntrenamientoActivity::class.java)
            intent.putExtra("ID_ENTRENAMIENTO", entrenamientoSelecionado.id)
            startActivity(intent)
        }

        btnNuevo.setOnClickListener {
            val intent = Intent(this, EntrenamientoActivoActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            intent.putExtra("MODO_LIBRE", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarHistorial()
    }

    private fun cargarHistorial(){
        if(idUsuario == -1L) return

        apiService.obtenerHistorialEntrenamientos(idUsuario).enqueue(object : Callback<List<Entrenamiento>>{
            override fun onResponse(call: Call<List<Entrenamiento>>, response: Response<List<Entrenamiento>>){
                if(response.isSuccessful && response.body() !=null){
                    listaEntrenamientos = response.body()!!
                    val nombres = listaEntrenamientos.map {"${it.fecha ?: ""}"}

                    lvHistorial.adapter = ArrayAdapter(
                        this@HistorialEntrenamientosActivity, android.R.layout.simple_list_item_1, nombres
                    )
                }
            }
            override fun onFailure(call: Call<List<Entrenamiento>>, t: Throwable){
                Toast.makeText(this@HistorialEntrenamientosActivity, "Error al cargar el historial",
                    Toast.LENGTH_LONG).show()
            }
        })
    }
}