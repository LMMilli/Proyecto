package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
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
import com.example.aplicacion.model.Equipamiento
import com.example.aplicacion.model.SerieRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad principal que gestiona la sesión de un entrenamiento en vivo.
 * Permite añadir ejercicios de forma dinámica, registar seresi (reps, peso, RPE),
 * gestionar tiempos de descanso y calcular el 1RM. Al finalizar, recoplia todos
 * los datos introducidos en las vistas dinámicas y los envía a la API.
 */
class EntrenamientoActivoActivity : AppCompatActivity() {
    //Variabes de red y UI principal
    private lateinit var apiService: ApiService
    private lateinit var contenedorEjercicios: LinearLayout

    //Datos del usuario y de la rutina actual
    private var idUsuario: Long = -1L
    private var idRutinaAsignada: Long? = null
    private var idsEjercicioRutina: List<Long>? = null

    //Catálogos cargados desde el servidor
    private var listaEjerciciosDisponibles: List<Ejercicio> = emptyList()
    private var listaEquipamiento: List<Equipamiento> = emptyList()

    /**
     * Data clas auxiliar para vincular la vista dinámica (tarjeta) que se infla
     * en pantalla con el ID del ejercicio correspondiente. Esto es crucial a la
     * hora de guardar el entrenamiento para saber de qué ejercicio son las series.
     */
    data class TarjetaEjercicio(val vistaTarjeta: View, val ejercicioId: Long)

    private val tarjetasEnPantalla = mutableListOf<TarjetaEjercicio>()

    //Variables de control de teimpo general
    private lateinit var cronometro: Chronometer

    //Variables de la capa de descanso (Overlay)
    private lateinit var flOverlayDescanso: View
    private lateinit var btnTerminarDescnaso: Button
    private lateinit var choronoDescanso: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento_activo)

        //Inicialización de vistas de tiempo de descanso
        flOverlayDescanso = findViewById(R.id.flOverlayDescanso)
        choronoDescanso = findViewById(R.id.chronoDescanso)
        btnTerminarDescnaso = findViewById(R.id.btnTerminarDescanso)

        //Inicialización e inicio del cronómetro global del entrenamiento
        cronometro = findViewById(R.id.cronometroEntrenamiento)
        cronometro.base = android.os.SystemClock.elapsedRealtime()
        cronometro.start()

        //Inicialización del servicio API
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Recepcióln de datos desde la actividad anterior a través del Intent
        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        val idRutina = intent.getLongExtra("ID_RUTINA", -1L)

        //Carga de catálogos necesarios para los Spinners y menús
        cargarEquipamientos()

        //Configuración del botón para ocultar y detner el cronómetro de descnaso
        btnTerminarDescnaso.setOnClickListener {
            choronoDescanso.stop()
            flOverlayDescanso.visibility = View.GONE
        }

        //Si nos ha pasado rutina preparamos la actividad para ella
        if (idRutina != -1L) {
            idRutinaAsignada = idRutina
            findViewById<TextView>(R.id.tvNombreEntrenamiento).text = "Entrenando Rutina"

            //Recogemos y parseamos los IDs de los ejercicios de esta rutina
            val textoIds = intent.getStringExtra("IDS_EJERCICIOS_STRING")
            if (!textoIds.isNullOrEmpty()) {
                idsEjercicioRutina = textoIds.split(",").mapNotNull { it.toLongOrNull() }
            }
        }

        //Contenedor pincipal donde se añadiran las tarjetas de ejercicios dinámicamente
        contenedorEjercicios = findViewById(R.id.llContendorEjercicios)

        //Descar los ejercios del servidor. Si hay una rutian, inyectará las tarjetas automáticamente
        cargarEjerciciosDelServidor()

        //Configuracio del bóton para añadir un ejercicio libremente mediante un AlterDialog
        findViewById<Button>(R.id.btnAgregarEjercicioEntrenamiento).setOnClickListener {
            if (listaEjerciciosDisponibles.isEmpty()) {
                Toast.makeText(
                    this,
                    "Aún cargando o no hay ejerciocs en la BD...",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            mostrarBuscadorDeEjercicios()
        }

        //Botón final para recopliar los datos y enviarlos a la base de datos
        findViewById<Button>(R.id.btnFinalizarEntrenamiento).setOnClickListener {
            guardarEntrenamiento()
        }
    }

    /**
     * Descarga la lista completa de ejercicois desde la API
     * Si la actividad se inición con una rutina específica (`idsEjercicioRutina`),
     * busca esos ejercicios en la lista descargar y genera sus tarjetas automaticamente
     */
    private fun cargarEjerciciosDelServidor() {
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
            override fun onResponse(
                call: Call<List<Ejercicio>>,
                response: Response<List<Ejercicio>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    listaEjerciciosDisponibles = response.body()!!

                    Toast.makeText(
                        this@EntrenamientoActivoActivity,
                        "Cargados ${listaEjerciciosDisponibles.size} ejercicios",
                        Toast.LENGTH_SHORT
                    ).show()

                    val idsParaInyectar = idsEjercicioRutina

                    //Si hay IDs de rutina, generamos las tarjetas de forma automática
                    if (idsParaInyectar != null) {
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
                        Toast.makeText(
                            this@EntrenamientoActivoActivity,
                            "Existo: Se han inyectado $tarjetasCreadas tarjetas",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@EntrenamientoActivoActivity, "Modo Entrenamiento Libre",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable) {
                Toast.makeText(
                    this@EntrenamientoActivoActivity, "Error de conexion ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * Muestar un cuadro de diálgo con la lista de ejercicios disponibles.
     * Al seleccionar uno, se crea e inserta su tarjeta dinámica en la vista
     */
    private fun mostrarBuscadorDeEjercicios() {
        val nombres = listaEjerciciosDisponibles.map { it.nombre }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Añadir Ejercicios")
            .setItems(nombres) { _, posicion ->
                crearTarjetaEjercicio(listaEjerciciosDisponibles[posicion])
            }
            .show()
    }

    /**
     * Genera una tarjeta de ejercicio inflado el layout XML de forma dinámica,
     * configura sus escuchadores (como el botón de añadir serie), rellena los Spinners
     * y la añade al contenedor principal.
     */
    private fun crearTarjetaEjercicio(ejercicio: Ejercicio) {
        if (ejercicio.id == null) return

        //1. Inflamos el diseño de la tarjeta base
        val vistaTarjeta = layoutInflater.inflate(R.layout.item_ejercicio_activo, null)

        //PROTECCIÓN 1: Asignamos el nombre del ejercico de forma segura
        val tvNombre = vistaTarjeta.findViewById<TextView>(R.id.tvNombreEjercicioItem)
        tvNombre?.text = ejercicio.nombre

        val contenedorDeSeries =
            vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)
        val btnAnadirSerie = vistaTarjeta.findViewById<Button>(R.id.btnAgregarSerieItem)
        val spinnerEquip = vistaTarjeta.findViewById<Spinner>(R.id.spinnerEquipamiento)

        //Preparamos los datos del Spinner de equipamiento (añadiendo una opcion por defecto=
        val listaParaSpinner = mutableListOf<Equipamiento>()
        listaParaSpinner.add(Equipamiento(-1L, "Seleccionar equipamiento..."))
        listaParaSpinner.addAll(listaEquipamiento)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaParaSpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEquip.adapter = adapter

        spinnerEquip.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long){
                val equipamientoSeleccionado = parent?.getItemAtPosition(position) as? Equipamiento

                val esPesoCorporal = equipamientoSeleccionado?.nombre?.equals("Peso Corporal", ignoreCase = true) == true

                if(contenedorDeSeries !=null){
                    //Recorremos las series creadas para actualizar su estado
                    for(i in 0 until contenedorDeSeries.childCount){
                        val filaSerie = contenedorDeSeries.getChildAt(i)
                        val etPeso = filaSerie.findViewById<EditText>(R.id.etPesoSerie)

                        if(esPesoCorporal){
                            etPeso.isEnabled = false
                            etPeso.setText("0")
                        }else{
                            etPeso.isEnabled = true
                            if(etPeso.text.toString()== "0") etPeso.setText("")
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }



        // PROTECCÓN 2: Si el contenedor de series existe, configuramos todo su interior
        if (contenedorDeSeries != null) {
            //2. POr defecto, añadimos UNA fila de serie vacía para que el usuario pueda empezar a escribir
            agregarFilaDeSerie(contenedorDeSeries, spinnerEquip)

            //3. Configuración del botón "Añadir Serie" interno de este ejercicio específico
            btnAnadirSerie?.setOnClickListener {
                agregarFilaDeSerie(contenedorDeSeries,spinnerEquip)
                iniciarCronometroDescanso() //Al añaidir una serie se inica el descanso
            }

            //4. Añaidmos la tarjeta inflada al contenedor padre de la Activity
            contenedorEjercicios.addView(vistaTarjeta)

            //5. Guardamos la referencia de la vista y el ID en la lista para poder leerla al guardar
            tarjetasEnPantalla.add(TarjetaEjercicio(vistaTarjeta, ejercicio.id))

        } else {
            Toast.makeText(
                this,
                "Error: No se encontró el contenedor de series en el XML",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Inlfa el layout de una serie (Inputs de Repeticiones, Peso Y RPE)
     * y lo inyecta dentro del contedor de series de una Tarjeta de Ejercicio concreta
     */
    private fun agregarFilaDeSerie(contenedorPadre: LinearLayout, spinnerEquip: Spinner) {
        val vistaFilaSerie = layoutInflater.inflate(R.layout.item_serie_activa, null)

        val etPeso = vistaFilaSerie.findViewById<EditText>(R.id.etPesoSerie)

        //Comprobas qué hay seleccionado actualemente en el Spinner
        val equipamientoSeleccionado = spinnerEquip.selectedItem as? Equipamiento
        val esPesoCorporal = equipamientoSeleccionado?.nombre?.equals("Peso Corporal", ignoreCase = true) == true

        if (esPesoCorporal){
            etPeso.isEnabled = false
            etPeso.setText("0")
        }else{
            etPeso.isEnabled = true
        }

        contenedorPadre.addView(vistaFilaSerie)
    }

    /**
     * Función principal de guardado.
     * Recorre todas las tarjetas infladas en pantalla, extrae los valores (peso, reps, notas)
     * escritos por el usuario, construye el objeto DTO `EntrenamientoRequest` y lo envía a la API
     */
    private fun guardarEntrenamiento() {
        //Lista donde guardaremos los bloques de ejercicios listos para enviar
        val todosLosBloquesFianles = mutableListOf<EjercicioEntrenamientoRequest>()
        var ordenActual = 1

        //1.Recorremos cada tarjeta de ejercicio que hay guardad en nuestra lista de control
        for (tarjeta in tarjetasEnPantalla) {

            //Encontramos las vistas dontre de la tarjeta actual
            val etNotas =
                tarjeta.vistaTarjeta.findViewById<com.google.android.material.textfield.TextInputEditText>(
                    R.id.etNotasEjercicio
                )
            val spinnerEquip = tarjeta.vistaTarjeta.findViewById<Spinner>(R.id.spinnerEquipamiento)
            val contenedorDeSeries =
                tarjeta.vistaTarjeta.findViewById<LinearLayout>(R.id.llContenedorSeriesDeEsteEjercicio)

            //Extraemos las notas (si estan vacies pasamos un null)
            val notasTexto = etNotas.text.toString().trim()
            val notasFinales = if (notasTexto.isNotEmpty()) notasTexto else null

            //Extraemos el ID del equipameinto seleccionado (si es válido)
            val equipamientoSeleccionado = spinnerEquip.selectedItem as? Equipamiento
            val equipamientoIdFinal = equipamientoSeleccionado?.id

            //Lista temporal para las series específicas de esta tarjeta
            val seriesDeEstaTarjeta = mutableListOf<SerieRequest>()

            //2. Dentro de esta tarjeta, itermoas sobre cada fila de serie generada
            for (i in 0 until contenedorDeSeries.childCount) {
                val filaSerie = contenedorDeSeries.getChildAt(i)

                val etReps = filaSerie.findViewById<EditText>(R.id.etRepsSerie)
                val etPeso = filaSerie.findViewById<EditText>(R.id.etPesoSerie)
                val etRpe = filaSerie.findViewById<EditText>(R.id.etRpeSerie)

                val reps = etReps.text.toString().toIntOrNull()
                val peso = etPeso.text.toString().toDoubleOrNull()
                val rpe = etRpe.text.toString().toIntOrNull()

                //Si al menos hay reps o peso, se considera una serie vállida a registar
                if (reps != null || peso != null) {
                    val nuevaSerie = SerieRequest(
                        repeticiones = reps ?: 0,
                        peso = peso ?: 0.0,
                        rpe = rpe ?: 0,
                        tipo = "Efectiva" // Opcional /Harcodea temporalmente
                    )
                    seriesDeEstaTarjeta.add(nuevaSerie)
                }
            }
            //3. Si el usuario rellenó algua serie válida en esta tarjeta, creamos el bloque del ejercicio
            if (seriesDeEstaTarjeta.isNotEmpty()) {
                val nuevoBloque = EjercicioEntrenamientoRequest(
                    ejercicioId = tarjeta.ejercicioId,
                    equipamientoId = equipamientoIdFinal,
                    orden = ordenActual,
                    notas = notasFinales,
                    series = seriesDeEstaTarjeta
                )
                todosLosBloquesFianles.add(nuevoBloque)
                ordenActual++
            }
        }

        //Validación para evitar guardar entrenamientos sin información
        if (todosLosBloquesFianles.isEmpty()) {
            Toast.makeText(this, "Entrenoa algo", Toast.LENGTH_SHORT).show()
            return
        }

        //Paramos el tiempo general y calculamos la duración total del entreno en minutos
        cronometro.stop()
        val tiempoTranscurridoMilis = android.os.SystemClock.elapsedRealtime() - cronometro.base
        val minutosDuracion = (tiempoTranscurridoMilis / 60000).toInt()

        //4. Creamos el objeto JSON final (Payload) para enviar a nuestra base de datos
        val request = EntrenamientoRequest(
            usuarioId = idUsuario,
            rutinaId = idRutinaAsignada,
            duracionMinutos = minutosDuracion,
            ejercicios = todosLosBloquesFianles
        )

        //5. Enviamos la peticón POST al servidor con Retrofit
        apiService.guardarEntrenamiento(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EntrenamientoActivoActivity, "Entrenamiento guardado",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@EntrenamientoActivoActivity, "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@EntrenamientoActivoActivity,
                    "Error de conexion",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * Muestra e indica el temporizador de descanso entre series.
     * Cambia el coler del texto de rojo a verdo cuando se suepran los 3 minutos
     */
    private fun iniciarCronometroDescanso() {
        flOverlayDescanso.visibility = View.VISIBLE

        //Reinicamos el cronómetro y lo arrancamos
        choronoDescanso.base = android.os.SystemClock.elapsedRealtime()
        choronoDescanso.start()

        //Listener que se ejecuta cada segudno
        choronoDescanso.setOnChronometerTickListener { chronometer ->
            val tiempoDescanso = android.os.SystemClock.elapsedRealtime() - chronometer.base

            if (tiempoDescanso >= 180000) {
                chronometer.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            } else {
                chronometer.setTextColor(android.graphics.Color.parseColor("#F44336"))
            }
        }
    }

    /**
     * LLmada a la API que cargo todos los equipamisntos (Mancuernas, Barra, etc.)
     * para rellenar los adaptadores de los Spinners generados dinámicamente.
     */
    private fun cargarEquipamientos() {
        apiService.obtenerTodosEquipamientos().enqueue(object : Callback<List<Equipamiento>> {
            override fun onResponse(
                call: Call<List<Equipamiento>>,
                response: Response<List<Equipamiento>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    listaEquipamiento = response.body()!!
                } else {
                    Toast.makeText(
                        this@EntrenamientoActivoActivity,
                        "No hay equipamientos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Equipamiento>>, t: Throwable) {
                Toast.makeText(
                    this@EntrenamientoActivoActivity, "Error al cargar los equipamientos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}