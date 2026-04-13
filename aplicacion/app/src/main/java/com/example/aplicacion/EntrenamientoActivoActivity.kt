package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
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
import com.example.aplicacion.model.EjercicioEntrenamientoRequest
import com.example.aplicacion.model.EntrenamientoRequest
import com.example.aplicacion.model.SerieRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntrenamientoActivoActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var contendorEjercicios: LinearLayout
    private var idUsuario: Long = -1L
    private var listaEjerciciosDisponibles: List<Ejercicio> = emptyList()

    //Guardamos la tarjeta del ejerccio, y su ID en la base de datos
    data class TarjetaEjercicio(val vistaTarjeta: View, val ejercicioId: Long)

    private val tarjetasEnPantalla = mutableListOf<TarjetaEjercicio>()

    private lateinit var cronometro: Chronometer
    private var idRutinaAsignada: Long? = null
    private var idsEjercicioRutina: List<Long>? = null

    private lateinit var llDescanso: LinearLayout

    private lateinit var btnTerminarDescanso: Button
    private lateinit var choronoDescanso: Chronometer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento_activo)

        llDescanso = findViewById(R.id.llDescanso)
        choronoDescanso = findViewById(R.id.chronoDescanso)
        cronometro = findViewById(R.id.cronometroEntrenamiento)
        btnTerminarDescanso = findViewById(R.id.btnTerminarDescanso)
        cronometro.base = android.os.SystemClock.elapsedRealtime()
        cronometro.start()

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        val idRutina = intent.getLongExtra("ID_RUTINA", -1L)


        findViewById<Button>(R.id.btnAbrirCalculadora).setOnClickListener {
            Toast.makeText(
                this, "¡Botón pulsado!",
                Toast.LENGTH_SHORT
            ).show()
            mostrarCalculadoraRM()
        }


        btnTerminarDescanso.setOnClickListener {
            choronoDescanso.stop()
            llDescanso.visibility = View.GONE
        }

        if (idRutina != -1L) {
            idRutinaAsignada = idRutina
            findViewById<TextView>(R.id.tvNombreEntrenamiento).text = "Entrenando Rutina"

            val textoIds = intent.getStringExtra("IDS_EJERCICIOS_STRING")
            if (!textoIds.isNullOrEmpty()) {
                idsEjercicioRutina = textoIds.split(",").mapNotNull { it.toLongOrNull() }
            }
        }
        apiService = ApiClient.retrofit.create(ApiService::class.java)
        contendorEjercicios = findViewById(R.id.llContendorEjercicios)

        cargarEjerciciosDelServidor()

        findViewById<Button>(R.id.btnAgregarEjercicioEntrenamiento).setOnClickListener {
            if (listaEjerciciosDisponibles.isEmpty()){
                Toast.makeText(this, "Aún cargando o no hay ejercicios en la BD...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mostrarBuscadorDeEjercicios()
        }

        findViewById<Button>(R.id.btnFinalizarEntrenamiento).setOnClickListener {
            guardarEntrenamiento()
        }
    }

    private fun cargarEjerciciosDelServidor() {
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
            override fun onResponse(
                call: Call<List<Ejercicio>>,
                response: Response<List<Ejercicio>>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    listaEjerciciosDisponibles = response.body()!!.filterNotNull()

                    Toast.makeText(this@EntrenamientoActivoActivity,
                        "Cargados ${listaEjerciciosDisponibles.size} ejercicios", Toast.LENGTH_SHORT).show()

                    val idsParaInyectar = idsEjercicioRutina

                    if (idsParaInyectar != null) {
                        // CHIVATO 1: ¿Llegó la maleta sana y salva a esta pantalla?
                        Toast.makeText(
                            this@EntrenamientoActivoActivity,
                            "Aduana: Recibidos ${idsParaInyectar.size} IDs",
                            Toast.LENGTH_SHORT
                        ).show()

                        var tarjetasCreadas = 0

                        for (idBuscado in idsParaInyectar) {
                            val ejercicioEncontrado =
                                listaEjerciciosDisponibles.find { it.id == idBuscado }

                            if (ejercicioEncontrado != null) {
                                crearTarjetaEjercicio(ejercicioEncontrado)
                                tarjetasCreadas++
                            }
                        }

                        // CHIVATO 2: ¿Cuántos ejercicios coincidieron con el catálogo?
                        Toast.makeText(
                            this@EntrenamientoActivoActivity,
                            "Éxito: Se han inyectado $tarjetasCreadas tarjetas",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(
                            this@EntrenamientoActivoActivity,
                            "Error: La maleta llegó vacía (null)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable) {
                Toast.makeText(
                    this@EntrenamientoActivoActivity,
                    "🚨 Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun mostrarBuscadorDeEjercicios() {
        val nombres = listaEjerciciosDisponibles.map { it.nombre }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Añadir Ejercicios")
            .setItems(nombres) { _, posicion ->
                crearTarjetaEjercicio(listaEjerciciosDisponibles[posicion])
            }.show()
    }

    private fun crearTarjetaEjercicio(ejercicio: Ejercicio) {
        if (ejercicio.id == null) return

        //1. Inflamos la tarjeta del EJERCICIO
        val vistaTarjeta = layoutInflater.inflate(R.layout.item_ejercicio_activo, null)
        vistaTarjeta.findViewById<TextView>(R.id.tvNombreEjercicioItem).text = ejercicio.nombre

        val contenedorDeSeries =
            vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)
        val btnAnadirSerie = vistaTarjeta.findViewById<Button>(R.id.btnAgregarSerieItem)

        // 2. Por defecto, le inyectamos UNA fila de serie para que pueda empezar a escribir
        agregarFilaDeSerie(contenedorDeSeries)

        //3. Configurar el boton "Añadir Serie" interno de este ejercicio
        btnAnadirSerie.setOnClickListener {
            agregarFilaDeSerie(contenedorDeSeries)
            iniciarCronometroDescanso()
        }

        //4. Pegamos la tarjeta completa en la pantalla principal y la guardamos
        contendorEjercicios.addView((vistaTarjeta))
        tarjetasEnPantalla.add(TarjetaEjercicio(vistaTarjeta, ejercicio.id))
    }

    //Esta funcion inyecta la fila pequea de reps/kg/rpe dentro de una Ejercicio
    private fun agregarFilaDeSerie(contenedorPadre: LinearLayout) {
        val vistaFilaSerie = layoutInflater.inflate(R.layout.item_serie_activa, null)
        contenedorPadre.addView(vistaFilaSerie)
    }

    private fun guardarEntrenamiento() {
        //Creamos una lista de Bloques EjercicioEntrenamientoRequest
        val todosLosBloquesFinales = mutableListOf<EjercicioEntrenamientoRequest>()
        var ordenActual = 1

        //Reccorremos cada tarjeta de ejercicio en la pantalla
        for (tarjeta in tarjetasEnPantalla){
            val contenedorDeSeries = tarjeta.vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)

            //Creamos una lista temporal solo para las series de esta tarjeta
            val seriesDeEstaTarjeta = mutableListOf<SerieRequest>()

            //Dentro de esta tarjeta, recorremos cada fila de serie
            for (i in 0 until contenedorDeSeries.childCount){
                val filaSerie = contenedorDeSeries.getChildAt(i)

                val etReps = filaSerie.findViewById<EditText>(R.id.etRepsSerie)
                val etPeso = filaSerie.findViewById<EditText>(R.id.etPesoSerie)
                val etRpe = filaSerie.findViewById<EditText>(R.id.etRpeSerie)

                val reps = etReps.text.toString().toIntOrNull()
                val peso = etPeso.text.toString().toDoubleOrNull()
                val rpe = etRpe.text.toString().toIntOrNull()

                if(reps !=null || peso !=null){
                    val nuevaSerie = SerieRequest(
                        repeticiones = reps ?: 0,
                        peso = peso ?: 0.0,
                        rpe = rpe ?: 0,
                        tipo = "Efectiva" //Opcional
                    )
                    seriesDeEstaTarjeta.add(nuevaSerie)
                }
            }
            // Si el usuario relleno alugna serie en la tarjeta creamos el bloque
            if(seriesDeEstaTarjeta.isNotEmpty()){
                val nuevoBloque = EjercicioEntrenamientoRequest(
                    ejercicioId = tarjeta.ejercicioId,
                    equipamientoId = null, //Hasta modificar la tarjeta
                    orden = ordenActual,
                    notas = "",
                    series = seriesDeEstaTarjeta
                )
                todosLosBloquesFinales.add(nuevoBloque)
                ordenActual++
            }
        }
        if(todosLosBloquesFinales.isEmpty()){
            Toast.makeText(this, "Entrrena algo CABRON"
                , Toast.LENGTH_SHORT).show()
            return
        }

        cronometro.stop()
        val tiempoTranscurridoMilis = android.os.SystemClock.elapsedRealtime() - cronometro.base
        val minutosDuracion = (tiempoTranscurridoMilis/6000).toInt()

        //5. Creamos el obejti final
        val request = EntrenamientoRequest(
            usuarioId = idUsuario,
            rutinaId = idRutinaAsignada,
            duracionMinutos = minutosDuracion,
            ejercicios = todosLosBloquesFinales
        )

        //Enviamos el entrenamiento a la base de datos

        apiService.guardarEntrenamiento(request).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>){
                if (response.isSuccessful){
                    Toast.makeText(this@EntrenamientoActivoActivity, "Entrenamiento guardado",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@EntrenamientoActivoActivity, "Error: \${response.code()}\""
                        , Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable){
                Toast.makeText(this@EntrenamientoActivoActivity, "Error de conexion",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun iniciarCronometroDescanso() {
        llDescanso.visibility = View.VISIBLE

        //Reinicamos el contrometro y lo arrancamos
        choronoDescanso.base = android.os.SystemClock.elapsedRealtime()
        choronoDescanso.start()

        // Cada segundo comprueba el tiempo
        choronoDescanso.setOnChronometerTickListener { chronometer ->
            val tiempoDescanso = android.os.SystemClock.elapsedRealtime() - chronometer.base


            if (tiempoDescanso >= 180000) {
                chronometer.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            } else {
                chronometer.setTextColor(android.graphics.Color.parseColor("#F44336"))
            }
        }

    }

    private fun mostrarCalculadoraRM() {
        //Inflamos el diseño xml
        val vistaCalculadora = layoutInflater.inflate(R.layout.dialog_calculadora_rm, null)

        val etPeso = vistaCalculadora.findViewById<EditText>(R.id.etPesoCalc)
        val etReps = vistaCalculadora.findViewById<EditText>(R.id.etRepsCalc)
        val btnCalcular = vistaCalculadora.findViewById<Button>(R.id.btnCalcularRM)
        val tvResultado = vistaCalculadora.findViewById<TextView>(R.id.tvResultadoRM)

        //Creamos la ventana emergente
        val dialogo = android.app.AlertDialog.Builder(this)
            .setView(vistaCalculadora)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()

        //Funcion del botoon
        btnCalcular.setOnClickListener {
            val peso = etPeso.text.toString().toDoubleOrNull()
            val reps = etReps.text.toString().toIntOrNull()

            if (peso != null && reps != null && reps > 0) {
                //FORMULA DE EPLEY: Peso * (1 + 0.0333 * Reps)
                val rmCalculado = peso * (1 + 0.0333 * reps)

                //Redondemoas a 1 decimal para que se mas bonti
                val rmRedondeado = String.format("%.1f", rmCalculado)

                //Porcentajes claves
                val hipertrofia = String.format("%.1f", rmCalculado * 0.80)
                val calentamiento = String.format("%.1f", rmCalculado * 0.60)

                tvResultado.text = """
                    🔥 Tu 1RM estimado: $rmRedondeado kg
                    
                    🎯 Zonas Recomendadas:
                    Fuerza (90%): ${String.format("%.1f", rmCalculado * 0.90)} kg
                    Hipertrofia (80%): $hipertrofia kg
                    Velocidad (60%): $calentamiento kg
                    
                    💡 Nota: El cálculo es más preciso en ejercicios 
                    compuestos (Banca, Sentadilla) y en rangos 
                    de 1 a 10 repeticiones.
                """.trimIndent()
            } else {
                Toast.makeText(
                    this, "Introduce datos validos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dialogo.show()
    }
}