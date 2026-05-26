package com.example.aplicacion

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.EjercicioEntrenamiento
import com.example.aplicacion.model.Entrenamiento
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad encargada de visualizar el informe detallado de un entrenamiento realizado.
 * Genera dinámicamente tarjetas (Cards) por cada ejercicio con sus respectivas series.
 */
class DetalleEntrenamientoActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var tvTitulo: TextView
    private lateinit var tvFecha: TextView
    private lateinit var contenedorSeries: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_entrenamiento)

        tvTitulo = findViewById(R.id.tvTituloDetalle)
        tvFecha = findViewById(R.id.tvFechaDetalle)
        contenedorSeries = findViewById(R.id.llContenedorSeriesDetalle)
        progressBar = findViewById(R.id.progressBarDetalles)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        val idEntrenamiento = intent.getLongExtra("ID_ENTRENAMIENTO", -1L)

        if (idEntrenamiento != -1L) {
            cargarDetalles(idEntrenamiento)
        } else {
            Toast.makeText(this, "Error al identificar el entrenamiento", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Realiza la petición a la API para obtener los datos del entrenamiento específico.
     */
    private fun cargarDetalles(idEntrenamiento: Long) {
        progressBar.visibility = View.VISIBLE

        apiService.obtenerDetallesEntrenamiento(idEntrenamiento).enqueue(object : Callback<Entrenamiento> {
            override fun onResponse(call: Call<Entrenamiento>, response: Response<Entrenamiento>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val entrenamiento = response.body()!!

                    tvTitulo.text = "Entrenamiento:"
                    tvFecha.text = entrenamiento.fecha?.split("T")?.get(0) ?: "Sin fecha"

                    val bloquesRealizados = entrenamiento.ejerciciosEntrenamiento ?: emptyList()

                    if (bloquesRealizados.isEmpty()) {
                        tvTitulo.text = "Entrenamiento Vacío"
                        Toast.makeText(this@DetalleEntrenamientoActivity, "No hay series registradas", Toast.LENGTH_SHORT).show()
                        return
                    }

                    pintarInforme(bloquesRealizados)
                } else {
                    tvTitulo.text = "Error del servidor"
                    Toast.makeText(this@DetalleEntrenamientoActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Entrenamiento>, t: Throwable) {
                progressBar.visibility = View.GONE
                tvTitulo.text = "Fallo de conexión"
                Toast.makeText(this@DetalleEntrenamientoActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Genera dinámicamente la interfaz para mostrar los ejercicios y sus series.
     * Utiliza MaterialCardView para una presentación limpia y adaptable.
     */
    private fun pintarInforme(bloques: List<EjercicioEntrenamiento>) {
        contenedorSeries.removeAllViews()

        val colorSuperficie = ContextCompat.getColor(this, R.color.surfaceColor)
        val colorPrincipal = ContextCompat.getColor(this, R.color.primaryColor)
        val colorTextoPrincipal = ContextCompat.getColor(this, R.color.textColorPrimary)

        for (bloque in bloques) {
            val nombreEjercicio = bloque.nombreEjercicio ?: "Ejercicio Desconocido"
            val equipamiento = bloque.nombreEquipamiento?.let { " ($it)" } ?: ""

            // 1. Configuración de la tarjeta contenedor
            val cardView = MaterialCardView(this).apply {
                radius = dpToPx(12).toFloat()
                cardElevation = dpToPx(4).toFloat()
                setCardBackgroundColor(colorSuperficie)

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 0, dpToPx(16))
                layoutParams = params
            }

            val cardLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
            }

            // 2. Creación del título del ejercicio
            val tituloView = TextView(this).apply {
                text = "$nombreEjercicio$equipamiento"
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(colorPrincipal)
                setPadding(0, 0, 0, dpToPx(12))
            }
            cardLayout.addView(tituloView)

            // 3. Creación dinámica de cada serie dentro del ejercicio
            val listaDeSeries = bloque.series ?: emptyList()
            listaDeSeries.forEachIndexed { index, serie ->
                val tipoSerie = serie.tipo ?: "Normal"
                val detalleSerieView = TextView(this).apply {
                    text = "Set ${index + 1} [$tipoSerie]: ${serie.repeticiones} reps x ${serie.peso}kg (RPE: ${serie.rpe})"
                    textSize = 15f
                    setTextColor(colorTextoPrincipal)
                    setPadding(0, dpToPx(4), 0, dpToPx(4))
                }
                cardLayout.addView(detalleSerieView)
            }

            cardView.addView(cardLayout)
            contenedorSeries.addView(cardView)
        }
    }

    /**
     * Función auxiliar para convertir unidades dp a píxeles, garantizando la misma apariencia
     * independientemente de la densidad de pantalla del dispositivo.
     */
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}