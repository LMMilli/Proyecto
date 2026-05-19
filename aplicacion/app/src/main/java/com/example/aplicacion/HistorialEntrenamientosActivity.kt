package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Entrenamiento
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class HistorialEntrenamientosActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var lvHistorial : ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private var idUsuario: Long = -1L

    private var listaEntrenamientos: List<Entrenamiento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_entrenamientos)

        //Enlazamos la varibales
        lvHistorial = findViewById(R.id.lvHistorialEntrenamientos)
        progressBar = findViewById(R.id.progressBarHistorial)
        tvEmptyState = findViewById(R.id.tvEmptyStateHistorial)
        val btnNuevo = findViewById<MaterialButton>(R.id.btnNuevoEntrenamiento)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        lvHistorial.setOnItemClickListener { _, _, position, _ ->
            // Seleccionamos el entrenamiento
            val entrenamientoSeleccionado = listaEntrenamientos[position]

            val intent = Intent(this, DetalleEntrenamientoActivity::class.java)
            intent.putExtra("ID_ENTRENAMIENTO", entrenamientoSeleccionado.id)
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

        //1. Mostar barra de progres, ocultar lista y estado vacio
        progressBar.visibility = View.VISIBLE
        lvHistorial.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        apiService.obtenerHistorialEntrenamientos(idUsuario).enqueue(object : Callback<List<Entrenamiento>>{
            override fun onResponse(call: Call<List<Entrenamiento>>, response: Response<List<Entrenamiento>>){
                //2. Ocultar progres al recibr respuesta
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body() != null){
                    listaEntrenamientos = response.body()!!

                    if(listaEntrenamientos.isEmpty()){
                        //Si no ha entrenado nunca
                        tvEmptyState.visibility = View.VISIBLE
                    }else{
                        //Si hay historial
                        lvHistorial.visibility = View.VISIBLE

                        val idioma = Locale.forLanguageTag("es-ES")
                        //Forma to para la fecha
                        val formateador = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm 'h'", idioma)
                        //Formater el texto de la lista para que quede bonito
                        val nombres = listaEntrenamientos.map { entrenamiento ->
                            val fechaFormateada = try {
                                entrenamiento.fecha?.let { fechaIso ->
                                    LocalDate.parse(fechaIso).format(formateador)

                                }?: "Sin fecha"
                            }catch (e: Exception){
                                "Fecha inválida"
                            }
                            "Entrenamiento: $fechaFormateada"
                        }

                        lvHistorial.adapter = ArrayAdapter(
                            this@HistorialEntrenamientosActivity,
                            android.R.layout.simple_list_item_1,
                            nombres
                        )
                    }
                }else {
                    Toast.makeText(this@HistorialEntrenamientosActivity, "Error al cargar los datos",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Entrenamiento>>, t: Throwable){
                progressBar.visibility = View.GONE
                Toast.makeText(this@HistorialEntrenamientosActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}