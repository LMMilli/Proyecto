package com.example.aplicacion

import android.os.Bundle
import android.view.View
import android.view.ViewParent
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
// Nuevos imports para Material Design
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearRutinaActivity : AppCompatActivity() {
    private var listaEjerciciosOriginal : List<Ejercicio> = emptyList()
    private var listaEjerciciosFiltrada : List<Ejercicio> = emptyList()

    private lateinit var lvEjercicios: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        // Enlaces a la vista actualizados a los nuevos componentes Material
        val etNombre = findViewById<TextInputEditText>(R.id.etNombreRutina)
        lvEjercicios = findViewById(R.id.lvEjercicios)
        val btnGuarda = findViewById<MaterialButton>(R.id.btnGuardarRutina)
        val spinner = findViewById<Spinner>(R.id.spDificultad)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Configurar Spinner con las opciones
        val opcionesTipo = listOf("Completo", "Sin equipamiento", "Peso libre", "Máquinas")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesTipo)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter



        // Obtener los ejercicios disponibles
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
            override fun onResponse(call: Call<List<Ejercicio>>, response: Response<List<Ejercicio>>){
                if(response.isSuccessful && response.body() != null){
                    listaEjerciciosOriginal = response.body()!!

                    println("DEBUG_API: ${listaEjerciciosOriginal.firstOrNull()}")

                    filtrarEjerciciosPorTipo(0)
                }
            }

            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable){
                Toast.makeText(this@CrearRutinaActivity, "Error al cargar los ejercicios",
                    Toast.LENGTH_SHORT).show()
            }
        })

        //Escuchar los camibos en el Spinner para filtrar la lista
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // solo se filtra si la lista original a cargado
                if(listaEjerciciosOriginal.isNotEmpty()){
                    filtrarEjerciciosPorTipo(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnGuarda.setOnClickListener {
            val nombreRutina = etNombre.text.toString().trim()

            if (nombreRutina.isEmpty()){
                Toast.makeText(this, "Pon un nombre",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Obetner el texto seleccionado en el Spinner
            val tipoRutinaSeleccionado = spinner.selectedItem.toString()

            // Recojer el id de los ejercicios selecionados para las rutinas
            val ejerciciosSelecionadosIds = mutableListOf<Long>()
            val posicionesMarcadas = lvEjercicios.checkedItemPositions

            for(i in 0 until lvEjercicios.count){
                if(posicionesMarcadas.get(i)){
                    val idEjercicio = listaEjerciciosFiltrada[i].id
                    if(idEjercicio != null){
                        ejerciciosSelecionadosIds.add(idEjercicio)
                    }
                }
            }

            if (ejerciciosSelecionadosIds.isEmpty()){
                Toast.makeText(this, "Selecciona al menos un ejercicio",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creamos el DTO y lo enviamos
            val request = RutinaRequest(nombreRutina, ejerciciosSelecionadosIds, tipoRutinaSeleccionado)
            apiService.crearRutina(request).enqueue(object : Callback<Rutina>{
                override fun onResponse(call: Call<Rutina>, response: Response<Rutina>){
                    if(response.isSuccessful){
                        Toast.makeText(this@CrearRutinaActivity, "Rutina Creada",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this@CrearRutinaActivity, "Error al crear la rutina",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Rutina>, t: Throwable){
                    Toast.makeText(this@CrearRutinaActivity, "Error de red",
                        Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //Metodo para filtar y refrescar el ListView
    private fun filtrarEjerciciosPorTipo(posicionSpinner: Int){
        listaEjerciciosFiltrada = when(posicionSpinner){
            //0 ->"Completo": Todos los ejercicios
            0 -> listaEjerciciosOriginal

            //1 -> "Sin equipamiento": ID 1 (Peso Corporal)
            1 -> listaEjerciciosOriginal.filter { ejercicio -> ejercicio.equipamiento?.any { it.id == 1L} == true }

            //2 -> "Peso libre": IDs (Barra) y 3 (Mancuerna)
            2 -> listaEjerciciosOriginal.filter { ejercicio -> ejercicio.equipamiento?.any { it.id == 2L || it.id == 3L} == true }

            //3 -> "Maquinas": ID 4 (Maquina)
            3 -> listaEjerciciosOriginal.filter { ejercicio -> ejercicio.equipamiento?.any {it.id == 4L} == true }

            else -> listaEjerciciosOriginal
        }

        //Actualizamos la vista con los nombre filtrados
        val nombresEjercicios = listaEjerciciosFiltrada.map { it.nombre }
        val adapter = ArrayAdapter(
            this@CrearRutinaActivity,
            android.R.layout.simple_list_item_multiple_choice,
            nombresEjercicios
        )
        lvEjercicios.adapter = adapter
    }
}