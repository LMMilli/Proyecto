package com.example.aplicacion

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Entrenamiento
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleEntrenamientoActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var tvTitulo: TextView
    private lateinit var tvFecha: TextView
    private lateinit var contenedorSeries: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_entrenamiento)

        tvTitulo = findViewById(R.id.tvTituloDetalle)
        tvFecha = findViewById(R.id.tvFechaDetalle)
        contenedorSeries = findViewById(R.id.llContenedorSeriesDetalle)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Recogemos el ID del entrenamiento que nos mandan desde el historial
        val idEntrenamiento = intent.getLongExtra("ID_ENTRENAMIENTO", -1L)

        if(idEntrenamiento != -1L){
            cargarDetalles(idEntrenamiento)
        }
    }

    private fun cargarDetalles(idEntrenamiento: Long) {
        apiService.obtenerDetallesEntrenamiento(idEntrenamiento).enqueue(object : Callback<Entrenamiento> {
            override fun onResponse(call: Call<Entrenamiento>, response: Response<Entrenamiento>) {
                if (response.isSuccessful && response.body() != null) {
                    val entrenamiento = response.body()!!

                    tvTitulo.text = "Entrenamiento Dia:"
                    tvFecha.text = entrenamiento.fecha ?: "Sin fecha"

                    val seriesRealizadas = entrenamiento.series ?: emptyList()

                    if (seriesRealizadas.isEmpty()) {
                        tvTitulo.text = "Entrenamiento Vacío"
                        Toast.makeText(this@DetalleEntrenamientoActivity, "No hay series registradas", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // 2. Pedimos los ejercicios
                    apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
                        override fun onResponse(call: Call<List<Ejercicio>>, responseEjercicios: Response<List<Ejercicio>>) {
                            if (responseEjercicios.isSuccessful && responseEjercicios.body() != null) {
                                val catalogoEjercicios = responseEjercicios.body()!!
                                val seriesAgrupadas = seriesRealizadas.groupBy { it.ejercicioId }
                                pintarInforme(seriesAgrupadas, catalogoEjercicios)
                            } else {
                                tvTitulo.text = "Error al cargar ejercicios"
                            }
                        }
                        override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable) {
                            tvTitulo.text = "Error de red (Ejercicios)"
                        }
                    })
                } else {
                    // ¡EL CHIVATO 1!
                    tvTitulo.text = "Error del servidor"
                    Toast.makeText(this@DetalleEntrenamientoActivity, "Código de error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Entrenamiento>, t: Throwable) {
                // ¡EL CHIVATO 2!
                tvTitulo.text = "Fallo de conexión"
                Toast.makeText(this@DetalleEntrenamientoActivity, "Fallo: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun pintarInforme(seriesAgrupadas: Map<Long, List<com.example.aplicacion.model.Serie>>, catalogo: List<Ejercicio>){

        //Recorremos cada grupo de series
        for ((idEjercicio, listaDeSeries) in seriesAgrupadas){

            //Buscamos el nombre del ejerccio en el catalogo
            val nombreEjercicio = catalogo.find { it.id == idEjercicio }?.nombre

            //1. Añadirmos el Título del Ejercicio en azul
            val tituloView = TextView(this).apply {
                text = nombreEjercicio
                textSize = 20f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(android.graphics.Color.parseColor("#2196F3"))
                setPadding(0, 32, 0, 8)
            }
            contenedorSeries.addView(tituloView)

            //2. Recorremos las serias de este este ejerccio y las imprimimos
            listaDeSeries.forEachIndexed { index, serie ->
                val detalleSerieView = TextView(this).apply {
                    //Texto: "Set 1: 10 reps x 60.0kg (RPE:9)
                    text = "Set ${index +1}: ${serie.repeticiones} reps x ${serie.repeticiones}kg (RPE: ${serie.rpe})"
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                contenedorSeries.addView(detalleSerieView)
            }
        }
    }
}