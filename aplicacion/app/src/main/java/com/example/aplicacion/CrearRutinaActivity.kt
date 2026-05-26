package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Rutina
import com.example.aplicacion.model.RutinaRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad para la creación de nuevas rutinas de entrenamiento.
 * Permite filtrar ejercicios disponibles según el tipo de equipamiento y asignar varios ejercicios a una rutina.
 */
class CrearRutinaActivity : AppCompatActivity() {

    private var listaEjerciciosOriginal: List<Ejercicio> = emptyList()
    private var listaEjerciciosFiltrada: List<Ejercicio> = emptyList()

    private lateinit var lvEjercicios: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        val etNombre = findViewById<TextInputEditText>(R.id.etNombreRutina)
        lvEjercicios = findViewById(R.id.lvEjercicios)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarRutina)
        val spinner = findViewById<Spinner>(R.id.spDificultad)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Configuración del Spinner para seleccionar el tipo de rutina
        val opcionesTipo = listOf("Completo", "Sin equipamiento", "Peso libre", "Máquinas")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipo)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Carga inicial de ejercicios desde la API
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
            override fun onResponse(call: Call<List<Ejercicio>>, response: Response<List<Ejercicio>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaEjerciciosOriginal = response.body()!!
                    filtrarEjerciciosPorTipo(0) // Inicialización del filtrado en la primera posición
                }
            }

            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable) {
                Toast.makeText(this@CrearRutinaActivity, "Error al cargar los ejercicios", Toast.LENGTH_SHORT).show()
            }
        })

        // Listener para actualizar la lista dinámicamente según el tipo seleccionado
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (listaEjerciciosOriginal.isNotEmpty()) {
                    filtrarEjerciciosPorTipo(position)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnGuardar.setOnClickListener {
            val nombreRutina = etNombre.text.toString().trim()
            if (nombreRutina.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipoRutinaSeleccionado = spinner.selectedItem.toString()

            // Recolección de IDs de los ejercicios seleccionados mediante el estado de la ListView
            val ejerciciosSeleccionadosIds = mutableListOf<Long>()
            val posicionesMarcadas = lvEjercicios.checkedItemPositions

            for (i in 0 until lvEjercicios.count) {
                if (posicionesMarcadas.get(i)) {
                    val idEjercicio = listaEjerciciosFiltrada[i].id
                    if (idEjercicio != null) {
                        ejerciciosSeleccionadosIds.add(idEjercicio)
                    }
                }
            }

            if (ejerciciosSeleccionadosIds.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un ejercicio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construcción del DTO y envío a la API
            val request = RutinaRequest(nombreRutina, ejerciciosSeleccionadosIds, tipoRutinaSeleccionado)
            apiService.crearRutina(request).enqueue(object : Callback<Rutina> {
                override fun onResponse(call: Call<Rutina>, response: Response<Rutina>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearRutinaActivity, "Rutina creada con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@CrearRutinaActivity, "Error al crear la rutina", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Rutina>, t: Throwable) {
                    Toast.makeText(this@CrearRutinaActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    /**
     * Filtra la lista de ejercicios según la categoría seleccionada en el Spinner
     * y actualiza el adaptador de la ListView.
     */
    private fun filtrarEjerciciosPorTipo(posicionSpinner: Int) {
        listaEjerciciosFiltrada = when (posicionSpinner) {
            0 -> listaEjerciciosOriginal // "Completo"
            1 -> listaEjerciciosOriginal.filter { it.equipamiento?.any { e -> e.id == 1L } == true } // "Sin equipamiento"
            2 -> listaEjerciciosOriginal.filter { it.equipamiento?.any { e -> e.id == 2L || e.id == 3L } == true } // "Peso libre"
            3 -> listaEjerciciosOriginal.filter { it.equipamiento?.any { e -> e.id == 4L } == true } // "Máquinas"
            else -> listaEjerciciosOriginal
        }

        val nombresEjercicios = listaEjerciciosFiltrada.map { it.nombre }
        val adapter = ArrayAdapter(
            this@CrearRutinaActivity,
            android.R.layout.simple_list_item_multiple_choice,
            nombresEjercicios
        )
        lvEjercicios.adapter = adapter
    }
}