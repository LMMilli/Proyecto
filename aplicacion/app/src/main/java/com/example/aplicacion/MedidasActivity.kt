package com.example.aplicacion

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Medida
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MedidasActivity : AppCompatActivity() {
   private lateinit var apiService: ApiService
   private lateinit var lvHistorial: ListView
   private lateinit var graficaPeso: LineChart
   private lateinit var progressBar: ProgressBar
   private lateinit var tvEmptyState: TextView

   private var idUsuario: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medidas)

        lvHistorial = findViewById(R.id.lvHistorialMedidas)
        graficaPeso = findViewById(R.id.graficaPeso)
        progressBar = findViewById(R.id.progressBarMedidas)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        val btnNuevo = findViewById<MaterialButton>(R.id.btnIrNuevaMedida)



        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        btnNuevo.setOnClickListener {
            val intent = Intent(this, NuevaMedidaActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        cargarHistorial()
    }

    private fun cargarHistorial(){
        val idioma = Locale.forLanguageTag("es-ES")
        val formatoEjeX = DateTimeFormatter.ofPattern("d MMM", idioma)

        if(idUsuario == -1L) return

        //1. Mostar progres y ocultar lista/estado vación mientras carga
        progressBar.visibility = View.VISIBLE
        lvHistorial.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
        graficaPeso.clear() //Limpismo la gráfica anterior por si acaso

        apiService.obtenerMedidas(idUsuario).enqueue(object : Callback<List<Medida>>{
            override fun onResponse(call: Call<List<Medida>>, response: Response<List<Medida>>){
                //2. Ocultar la barra de carga al reciber la respuesta
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body() != null){
                    val listaMedidas = response.body()!!

                    //3. Comprobar si hay datos
                    if(listaMedidas.isEmpty()){
                        tvEmptyState.visibility = View.VISIBLE
                    }else{
                        lvHistorial.visibility = View.VISIBLE

                        //Formatemoas el texto para la lista
                        val textoHistorial = listaMedidas.map {
                            "Peso ${it.pesoCorporal} kg | Grasa: ${it.porcentajeGrasa}%"
                        }

                        lvHistorial.adapter = ArrayAdapter(
                            this@MedidasActivity,
                            android.R.layout.simple_list_item_1,
                            textoHistorial
                        )



                        //DIBUJAR LA GRAFIA
                        //Ordenar las meidas por ID o FECHA para que la grafai vaya de izquierda a derecha
                        val medidasOrdenadas = listaMedidas.sortedBy { it.id }

                        //Convertimos cada peso en un punto de la gráfica
                        val puntosGrafica = ArrayList<Entry>()
                        val etiquetasFechas = ArrayList<String>()

                        medidasOrdenadas.forEachIndexed { index, medida ->
                            puntosGrafica.add(Entry(index.toFloat(), medida.pesoCorporal.toFloat()))

                            val fechaString = try{
                                medida.fecha?.let{
                                    LocalDateTime.parse(it).format(formatoEjeX)
                                }?: ""
                            }catch (e: Exception){
                                ""
                            }
                            etiquetasFechas.add(fechaString)
                        }



                        //Creamos la línea y le damos estilos
                        val lineaDatos = LineDataSet(puntosGrafica , "Evolucópn de Peso (kg)")
                        lineaDatos.color = Color.parseColor("#FF5722")
                        lineaDatos.setCircleColor(Color.parseColor("#FF5722"))
                        lineaDatos.lineWidth = 3f
                        lineaDatos.circleRadius = 5f
                        lineaDatos.setDrawFilled(true)
                        lineaDatos.fillColor = Color.parseColor("#FFCCBC")
                        lineaDatos.mode = LineDataSet.Mode.CUBIC_BEZIER
                        lineaDatos.valueTextSize = 10f //Tamaño del texo de los números sobre la gráfica

                        //Empaquetamos los datos y se los damos a la gráfica
                        val datosFinales = LineData(lineaDatos)
                        graficaPeso.data = datosFinales


                        val xAxis = graficaPeso.xAxis
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.setDrawGridLines(false)
                        xAxis.setDrawAxisLine(false)
                        xAxis.granularity = 1f
                        xAxis.isGranularityEnabled = true

                        xAxis.valueFormatter = object : ValueFormatter(){
                            override fun getAxisLabel(value: Float, axis: com.github.mikephil.charting.components.AxisBase): String? {
                                val index = value.toInt()
                                return if (index >= 0 && index < etiquetasFechas.size){
                                    etiquetasFechas[index]
                                }else{
                                    ""
                                }
                            }
                        }

                        //Retoques visuales para que la gráfica quede mas limpia
                        graficaPeso.description.isEnabled = false
                        graficaPeso.axisRight.isEnabled = false
                        graficaPeso.animateX(1200)

                        //Refrescamos la pantalla
                        graficaPeso.invalidate()
                    }
                }else{
                    Toast.makeText(this@MedidasActivity, "Error al cargar el historial", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Medida>>, t: Throwable){
                progressBar.visibility = View.GONE
                Toast.makeText(this@MedidasActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}