package com.example.aplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Equipamiento
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad para la creación de nuevos ejercicios en el sistema.
 * Permite definir nombre, grupo muscular, descripción y asociar equipamiento mediante selección múltiple.
 */
class CrearEjercicioActivity : AppCompatActivity() {

    private var listaEquipamiento: List<Equipamiento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ejercicio)

        // Inicialización de componentes de la UI (Material Design)
        val etEjercicio = findViewById<TextInputEditText>(R.id.etNombreEjercicio)
        val etGrupo = findViewById<TextInputEditText>(R.id.etGrupoMuscular)
        val lvEquipamiento = findViewById<ListView>(R.id.lvEquipamiento)
        val etDescripcion = findViewById<TextInputEditText>(R.id.etDescripcion)
        val btnGuardar = findViewById<MaterialButton>(R.id.btnGuardarEjercicios)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Obtención de la lista de equipamiento disponible desde el servidor
        apiService.obtenerTodosEquipamientos().enqueue(object : Callback<List<Equipamiento>> {
            override fun onResponse(call: Call<List<Equipamiento>>, response: Response<List<Equipamiento>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaEquipamiento = response.body()!!
                    val nombreEquipamiento = listaEquipamiento.map { it.nombre }

                    // Adaptador para selección múltiple
                    val adapter = ArrayAdapter(
                        this@CrearEjercicioActivity,
                        android.R.layout.simple_list_item_multiple_choice,
                        nombreEquipamiento
                    )
                    lvEquipamiento.adapter = adapter
                } else {
                    Toast.makeText(this@CrearEjercicioActivity, "No hay equipamiento disponible", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Equipamiento>>, t: Throwable) {
                Toast.makeText(this@CrearEjercicioActivity, "Error de conexión al cargar equipamiento", Toast.LENGTH_SHORT).show()
            }
        })

        // Lógica de guardado al pulsar el botón
        btnGuardar.setOnClickListener {
            val ejercicio = etEjercicio.text.toString().trim()
            val grupo = etGrupo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            // Captura del equipamiento seleccionado por el usuario
            val equipamientoMarcado = mutableListOf<Equipamiento>()
            val posicionesMarcadas = lvEquipamiento.checkedItemPositions

            for (i in 0 until lvEquipamiento.count) {
                if (posicionesMarcadas.get(i)) {
                    equipamientoMarcado.add(listaEquipamiento[i])
                }
            }

            // Validación básica de campos obligatorios
            if (ejercicio.isEmpty() || grupo.isEmpty()) {
                Toast.makeText(this, "El nombre y el grupo muscular son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creación del objeto Ejercicio (ID generado por el backend)
            val nuevoEjercicio = Ejercicio(
                nombre = ejercicio,
                grupoMuscular = grupo,
                descripcion = descripcion,
                equipamiento = equipamientoMarcado
            )

            // Envío del nuevo ejercicio al servidor
            apiService.crearEjercicio(nuevoEjercicio).enqueue(object : Callback<Ejercicio> {
                override fun onResponse(call: Call<Ejercicio>, response: Response<Ejercicio>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CrearEjercicioActivity, "Ejercicio guardado en la BD", Toast.LENGTH_LONG).show()
                        finish() // Retorno al panel anterior
                    } else {
                        Toast.makeText(this@CrearEjercicioActivity, "Error al guardar: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Ejercicio>, t: Throwable) {
                    Toast.makeText(this@CrearEjercicioActivity, "Error de conexión al guardar", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}