package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.DetalleEntrenamientoRequest
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.EntrenamientoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntrenamientoActivoActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var contenedorEjercicios: LinearLayout
    private var idUsuario: Long = -1L

    //Lista para guardar los ejercicios de la base de datos
    private var listaEjerciciosDisponibles: List<Ejercicio> = emptyList()

    //Lista para guardar las tarjetas
    private val tarjetasEnPantalla = mutableListOf<TarjetaEjercicio>()

    //Clase auxiliar para emparejar la tarjeta con su id de ejercicio
    data class TarjetaEjercicio(
        val vista: View,
        val ejercicioId: Long
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenamiento_activo)

        val btnAgregar = findViewById<Button>(R.id.btnAgregarEjercicioEntrenamiento)
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizarEntrenamiento)
        contenedorEjercicios = findViewById(R.id.llContendorEjercicios)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        //1. Al entrar descargarmos el catalogo de ejercicios para elegirlos
        cargarEjerciciosDelServidor()

        //2. Funcion para añadir ejercicios
        btnAgregar.setOnClickListener {
            if (listaEjerciciosDisponibles.isEmpty()){
                Toast.makeText(this, "No hay ejercicios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mostrarBuscadorDeEjercicios()

        }

        //3. Fucion para buscar ejercicios
        btnFinalizar.setOnClickListener {
            guardarEntrenamiento()
        }
    }

    private fun cargarEjerciciosDelServidor(){
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>>{
            override fun onResponse(call: Call<List<Ejercicio>>, response: Response<List<Ejercicio>>){
                if(response.isSuccessful && response.body() !=null){
                    listaEjerciciosDisponibles = response.body()!!
                }
            }
            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable){
                Toast.makeText(this@EntrenamientoActivoActivity, "Error de red",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarBuscadorDeEjercicios(){
        //Se sacan solos los nombre para mostrarlos en el menu
        val nombreEjercicios = listaEjerciciosDisponibles.map { it.nombre }.toTypedArray()

        //Creamos una ventana emergente nativa de Androdi (AlterDialog)
        AlertDialog.Builder(this)
            .setTitle("Elige un ejercicio")
            .setItems(nombreEjercicios){ dialog, posiconElegida ->
                //Cuando el usuario toca uno sacamos el ejercicio que es
                val ejercicioElegido = listaEjerciciosDisponibles[posiconElegida]
                // se crea su tarjeta en la pantalla
                crearTarjetaEnPantalla(ejercicioElegido)
            }.show()

    }

    private fun crearTarjetaEnPantalla(ejercicio: Ejercicio){
        //Convertimes el archvo XML en una View de Kotlin
        val vistaTarjeta = layoutInflater.inflate(R.layout.item_ejercicio_activo, null)

        //Ponemos en lombre en el titulo de la tarjeta
        val tvNombre = vistaTarjeta.findViewById<TextView>(R.id.tvNombreEjercicioItem)
        tvNombre.text = ejercicio.nombre

        //Ponemos la tarjeta en la pantalla
        contenedorEjercicios.addView(vistaTarjeta)

        //Guarda la referencia para leer los datos que escriba el usuario
        if(ejercicio.id != null){
            tarjetasEnPantalla.add(TarjetaEjercicio(vistaTarjeta, ejercicio.id))
        }
    }

    private fun guardarEntrenamiento(){
        if(tarjetasEnPantalla.isEmpty()){
            Toast.makeText(this, "No has entrenado na",
                Toast.LENGTH_SHORT).show()
            return
        }

        val detallesLista = mutableListOf<DetalleEntrenamientoRequest>()

        //Recorremos las tarjetitas de la pantalla

        for (tarjeta in tarjetasEnPantalla){
            val vista = tarjeta.vista

            //Sacamos los items de la tarjeta
            val etSeries = vista.findViewById<EditText>(R.id.etSeriesItem)
            val etReps = vista.findViewById<EditText>(R.id.etRepsItem)
            val etPeso = vista.findViewById<EditText>(R.id.etPesoItem)

            //Sacamos las datos de los items (si estan vacios ponemos 0)
            val series = etSeries.text.toString().toIntOrNull() ?: 0
            val reps = etSeries.text.toString().toIntOrNull() ?: 0
            val peso = etSeries.text.toString().toDoubleOrNull() ?: 0.0

            //Creamos el detalle y lo añadimos a la lista
            val detalles = DetalleEntrenamientoRequest(tarjeta.ejercicioId, series, reps, peso)
            detallesLista.add(detalles)

            //Creamos el paquete completo
            val request = EntrenamientoRequest(idUsuario, "Entrenamiento Libre", detallesLista)

            //Lo enviamos al springboot
            apiService.guardarEntrenamiento(request).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>){
                    if(response.isSuccessful){
                        Toast.makeText(this@EntrenamientoActivoActivity, "Entrenamiento Guardado",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this@EntrenamientoActivoActivity, "Error al guardar el Entrenamiento",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EntrenamientoActivoActivity, "Error de red",
                        Toast.LENGTH_SHORT).show()
                }
            })

        }
    }

}