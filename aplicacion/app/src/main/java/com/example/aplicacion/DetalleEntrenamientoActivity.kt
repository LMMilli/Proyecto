package com.example.aplicacion

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // Importante para leer colores dinámicos
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.EjercicioEntrenamiento
import com.example.aplicacion.model.Entrenamiento
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalleEntrenamientoActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var tvTitulo: TextView
    private lateinit var tvFecha: TextView
    private lateinit var contenedorSeries: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_entrenamiento)

        tvTitulo = findViewById(R.id.tvTituloDetalle)
        tvFecha = findViewById(R.id.tvFechaDetalle)
        contenedorSeries = findViewById(R.id.llContenedorSeriesDetalle)
        progressBar = findViewById(R.id.progressBarDetalles)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        val idEntrenamiento = intent.getLongExtra("ID_ENTRENAMIENTO", -1L)

        if(idEntrenamiento != -1L){
            cargarDetalles(idEntrenamiento)
        }else{
            Toast.makeText(this, "Error al identificar el entrenamiento", Toast.LENGTH_SHORT).show()
        }

    }

    private fun cargarDetalles(idEntrenamiento: Long){
        //Mostramos la barra de carga antes de pedir los datos
        progressBar.visibility = View.VISIBLE

        apiService.obtenerDetallesEntrenamiento(idEntrenamiento).enqueue(object : Callback<Entrenamiento>{
            override fun onResponse(call: Call<Entrenamiento>, response: Response<Entrenamiento>){
                //Ocultamos la barra de carga
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body() != null){
                    val entrenamiento = response.body()!!

                    tvTitulo.text = "Entrenamiento:"
                    tvFecha.text = entrenamiento.fecha?.split("T")?.get(0) ?: "Sin fecha"

                    val bloquesRealizados = entrenamiento.ejerciciosEntrenamiento ?: emptyList()

                    if (bloquesRealizados.isEmpty()){
                        tvTitulo.text = "Entrenamiento Vacío"
                        Toast.makeText(this@DetalleEntrenamientoActivity, "No hay series registradas",
                            Toast.LENGTH_SHORT).show()
                        return
                    }

                    pintarInforme(bloquesRealizados)
                }else{
                    tvTitulo.text = "Error del servidor"
                    Toast.makeText(this@DetalleEntrenamientoActivity, "Error: ${response.code()}",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Entrenamiento>, t: Throwable){
                progressBar.visibility = View.GONE
                tvTitulo.text = "Fallo de conexion"
                Toast.makeText(this@DetalleEntrenamientoActivity, "Fallo${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pintarInforme(bloques: List<EjercicioEntrenamiento>){
        // Limpiamos el contenedor por si acaso
        contenedorSeries.removeAllViews()

        // Cargamos los colores desde colors.xml usando ContextCompat
        val colorSuperficie = ContextCompat.getColor(this, R.color.surfaceColor)
        val colorPrincipal = ContextCompat.getColor(this, R.color.primaryColor)
        val colorTextoPrincipal = ContextCompat.getColor(this, R.color.textColorPrimary)

        // Recorremos cada bloque de ejercicio
        for(bloque in bloques){
            val nombreEjercicio = bloque.nombreEjercicio ?: "Ejercicio Desconocido"
            val equipamiento = bloque.nombreEquipamiento?.let { " ($it)" }?: ""

            // 1. Creamos la tarjeta
            val cardView = MaterialCardView(this).apply {
                radius = dpToPx(12).toFloat()
                cardElevation = dpToPx(4).toFloat()
                setCardBackgroundColor(colorSuperficie) // Fondo adaptable (Blanco o Gris Asfalto)

                // Márgenes de la tarjeta
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0,0,0, dpToPx(16))
                layoutParams = params
            }

            // 2. Creamos un Layout interno para la tarjeta
            val cardLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
            }

            // 3. Añadimos el título del ejercicio
            val tituloView = TextView(this).apply {
                text = "$nombreEjercicio$equipamiento"
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(colorPrincipal) // Color de acento (Azul o Cian)
                setPadding(0, 0, 0, dpToPx(12))
            }
            cardLayout.addView(tituloView)

            // 4. Recorremos las series y las añadimos al Layout interno
            val listaDeSeries = bloque.series ?: emptyList()

            listaDeSeries.forEachIndexed { index, serie ->
                val tipoSerie = serie.tipo ?: "Normal"

                val detalleSerieView = TextView(this).apply {
                    text = "Set ${index + 1} [$tipoSerie]: ${serie.repeticiones} reps x ${serie.peso}kg (RPE: ${serie.rpe})"
                    textSize = 15f
                    setTextColor(colorTextoPrincipal) // Texto adaptable (Negro o Blanco)
                    setPadding(0, dpToPx(4), 0, dpToPx(4))
                }
                cardLayout.addView(detalleSerieView)
            }

            // 5. Ensamblamos todo: El Layout a la Tarjeta, y la Tarjeta al Contenedor principal
            cardView.addView(cardLayout)
            contenedorSeries.addView(cardView)
        }
    }

    // Función auxiliar para convertir "dp" a píxeles exactos de cada pantalla
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}