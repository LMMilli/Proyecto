package com.example.aplicacion

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.EjercicioEntrenamiento
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

    override fun onCreate(saveInstanceState: Bundle?){
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_detalle_entrenamiento)

        tvTitulo = findViewById(R.id.tvTituloDetalle)
        tvFecha = findViewById(R.id.tvFechaDetalle)
        contenedorSeries = findViewById(R.id.llContenedorSeriesDetalle)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        val idEntrenamiento = intent.getLongExtra("ID_ENTRENAMIENTO", -1L)

        if(idEntrenamiento != -1L){
            cargarDetalles(idEntrenamiento)
        }
    }

    private fun cargarDetalles(idEntrenamiento: Long){
        apiService.obtenerDetallesEntrenamiento(idEntrenamiento).enqueue(object : Callback<Entrenamiento>{
            override fun onResponse(call: Call<Entrenamiento>, response: Response<Entrenamiento>){
                if(response.isSuccessful && response.body() !=null){
                    val entrenamiento = response.body()!!

                    tvTitulo.text = "Entrenamiento Dia:"
                    tvFecha.text = entrenamiento.fecha?.split("T")?.get(0) ?: "Sin fecha"

                    //Recogemos los bloques
                    val bloquesRealizados = entrenamiento.ejerciciosEntrenamiento ?: emptyList()

                    if(bloquesRealizados.isEmpty()){
                        tvTitulo.text = "Entrenamiento Vacio"
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
                tvTitulo.text = "Fallo de conexion"
                Toast.makeText(this@DetalleEntrenamientoActivity, "Fallo: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pintarInforme(bloques: List<EjercicioEntrenamiento>){
        //Recorremos cada bloque
        for(bloque in bloques){
            //El nombre viene desde el servidor
            val nombreEjercicio = bloque.ejercicio?.nombre ?: "Ejercicio Desconocido"

            //Añadios el titulo del ejercicio
            val tituloView = TextView(this).apply {
                text = nombreEjercicio
                textSize = 20f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(android.graphics.Color.parseColor("#2196F3"))
                setPadding(0, 32, 0, 8)
            }
            contenedorSeries.addView(tituloView)

            //Recorremos las series dentro de este bloque y las imprimimos
            val listaDeSeries = bloque.series ?: emptyList()

            listaDeSeries.forEachIndexed { index, serie ->
                val detalleSerieView = TextView(this).apply {
                    text = "Set ${index +1}: ${serie.repeticiones} reps x ${serie.peso}kg (RPE: ${serie.rpe})"
                    textSize = 16f
                    setPadding(0, 8, 0, 8)
                }
                contenedorSeries.addView(detalleSerieView)
            }
        }
    }

}