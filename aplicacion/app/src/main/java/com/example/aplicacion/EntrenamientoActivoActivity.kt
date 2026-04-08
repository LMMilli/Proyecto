package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.DetalleEntrenamientoRequest
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.EntrenamientoRequest
import com.example.aplicacion.model.SerieRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntrenamientoActivoActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var contendorEjercicios : LinearLayout
    private var idUsuario: Long = -1L
    private var listaEjerciciosDisponibles: List<Ejercicio> = emptyList()

    //Guardamos la tarjeta del ejerccio, y su ID en la base de datos
    data class TarjetaEjercicio(val vistaTarjeta: View, val ejercicioId: Long)
    private val tarjetasEnPantalla = mutableListOf<TarjetaEjercicio>()

    //Calcualar la duración del entrenamiento
    private val tiempoInicio = System.currentTimeMillis()

    private var idRutinaAsignada: Long? =null
    private var idsEjercicioRutina: LongArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento_activo)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        val idRutina = intent.getLongExtra("ID_RUTINA", -1L)
        if(idRutina != -1L){
            idRutinaAsignada = idRutina
            idsEjercicioRutina = intent.getLongArrayExtra("IDS_EJERCICIOS_RUTINA")
            findViewById<TextView>(R.id.tvNombreEntrenamiento).text = "Entrenando Rutina"
        }
        apiService = ApiClient.retrofit.create(ApiService::class.java)
        contendorEjercicios = findViewById(R.id.llContendorEjercicios)

        cargarEjerciciosDelServidor()

        findViewById<Button>(R.id.btnAgregarEjercicioEntrenamiento).setOnClickListener {
            if(listaEjerciciosDisponibles.isEmpty()) return@setOnClickListener
            mostrarBuscadorDeEjercicios()
        }

        findViewById<Button>(R.id.btnFinalizarEntrenamiento).setOnClickListener {
            guardarEntrenamiento()
        }
    }

    private fun cargarEjerciciosDelServidor(){
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>>{
            override fun onResponse(call: Call<List<Ejercicio>>, response: Response<List<Ejercicio>>){
                if(response.isSuccessful && response.body() != null) {
                    listaEjerciciosDisponibles = response.body()!!

                    //Si han pasado una id de la rutina, inyectamos las tarjetas de los ejercicios directamente
                    for(idBuscado in idsEjercicioRutina!!){
                        //Buscamos el ejercicio en el catalogo
                        val ejercicioEncontrado = listaEjerciciosDisponibles.find { it.id == idBuscado }
                        //Si existe, cremoas su tarje visula automaticamente
                        if(ejercicioEncontrado != null){
                            crearTarjetaEjercicio(ejercicioEncontrado)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable){}
        })
    }

    private fun mostrarBuscadorDeEjercicios(){
        val nombres = listaEjerciciosDisponibles.map { it.nombre }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Añadir Ejercicios")
            .setItems(nombres){ _, posicion ->
                crearTarjetaEjercicio(listaEjerciciosDisponibles[posicion])
            }.show()
    }

    private fun crearTarjetaEjercicio(ejercicio: Ejercicio){
        if (ejercicio.id == null) return

        //1. Inflamos la tarjeta del EJERCICIO
        val vistaTarjeta = layoutInflater.inflate(R.layout.item_ejercicio_activo, null)
        vistaTarjeta.findViewById<TextView>(R.id.tvNombreEjercicioItem).text = ejercicio.nombre

        val contenedorDeSeries = vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)
        val btnAnadirSerie = vistaTarjeta.findViewById<Button>(R.id.btnAgregarSerieItem)

        // 2. Por defecto, le inyectamos UNA fila de serie para que pueda empezar a escribir
        agregarFilaDeSerie(contenedorDeSeries)

        //3. Configurar el boton "Añadir Serie" interno de este ejercicio
        btnAnadirSerie.setOnClickListener {
            agregarFilaDeSerie(contenedorDeSeries)
        }

        //4. Pegamos la tarjeta completa en la pantalla principal y la guardamos
        contendorEjercicios.addView((vistaTarjeta))
        tarjetasEnPantalla.add(TarjetaEjercicio(vistaTarjeta, ejercicio.id))
    }

    //Esta funcion inyecta la fila pequea de reps/kg/rpe dentro de una Ejercicio
    private fun agregarFilaDeSerie(contenedorPadre: LinearLayout){
        val vistaFilaSerie = layoutInflater.inflate(R.layout.item_serie_activa, null)
        contenedorPadre.addView(vistaFilaSerie)
    }

    private fun guardarEntrenamiento(){
        val todasLasSeriesFinales = mutableListOf<SerieRequest>()

        //1. Recorremos cada TARJETA DE EJERCICIO
        for(tarjeta in tarjetasEnPantalla){
            val contenedorDeSeries = tarjeta.vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)

            //2.Dentro de esa tarjeta, recorremos CADA FILA DE SERIE que haya creado el usuari
            for (i in 0 until contenedorDeSeries.childCount){
                val filaSerie =  contenedorDeSeries.getChildAt(i)//Pillamos la fila visual

                val etReps = filaSerie.findViewById<EditText>(R.id.etRepsSerie)
                val etPeso = filaSerie.findViewById<EditText>(R.id.etPesoSerie)
                val etRpe = filaSerie.findViewById<EditText>(R.id.etRpeSerie)

                val reps = etReps.text.toString().toIntOrNull()
                val peso = etPeso.text.toString().toDoubleOrNull()
                val rpe = etRpe.text.toString().toIntOrNull()

                //Si ha rellenado algo, lo guardamos como UNA serie en la base de datos
                if(reps != null || peso != null){
                    val nuevaSerie = SerieRequest(
                        ejercicioId = tarjeta.ejercicioId,
                        repeticiones = reps ?: 0,
                        peso = peso ?: 0.0,
                        rpe = rpe ?: 0
                    )
                    todasLasSeriesFinales.add(nuevaSerie)
                }
            }
        }
        if(todasLasSeriesFinales.isEmpty()){
            Toast.makeText(this, "Entrena algo CABRON",
                Toast.LENGTH_SHORT).show()
        }

        //Calculamos la duraccion aproximada en minutos
        val minutosDuracion = ((System.currentTimeMillis() - tiempoInicio) / 60000).toInt()

        //Creamos el objeto final
        val request = EntrenamientoRequest(
            usuarioId = idUsuario,
            rutinaId = idRutinaAsignada,
            duracionMinutos = minutosDuracion,
            series = todasLasSeriesFinales
        )

        apiService.guardarEntrenamiento(request).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>){
                if(response.isSuccessful){
                    Toast.makeText(this@EntrenamientoActivoActivity, "Entrenamiento Guardado",
                        Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@EntrenamientoActivoActivity, "Error en la base de datos",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EntrenamientoActivoActivity, "Error de conexion",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}