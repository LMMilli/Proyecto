package com.example.aplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Equipamiento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearEjercicioActivity : AppCompatActivity() {
    private var listaEquipamiento : List<Equipamiento> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ejercicio)


        val etEjercicio = findViewById<EditText>(R.id.etNombreEjercicio)
        val etGrupo = findViewById<EditText>(R.id.etGurpoMuscular)
        val lvEquipamiento = findViewById<ListView>(R.id.lvEquipamiento)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEjercicios)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.obtenerTodosEquipamientos().enqueue(object : Callback<List<Equipamiento>>{
            override fun onResponse(call: Call<List<Equipamiento>>, response: Response<List<Equipamiento>>){
                if(response.isSuccessful && response.body() != null){
                    listaEquipamiento = response.body()!!
                    val nombreEquipamiento = listaEquipamiento.map { it.nombre }


                    val adapter = ArrayAdapter(this@CrearEjercicioActivity, android.R.layout.simple_list_item_multiple_choice, nombreEquipamiento)

                    lvEquipamiento.adapter = adapter
                }else{
                    Toast.makeText(this@CrearEjercicioActivity, "No hay equipamiento",
                        Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Equipamiento>>, t: Throwable) {
                Toast.makeText(this@CrearEjercicioActivity, "Error de conexion",
                    Toast.LENGTH_SHORT).show()
            }
        })

        btnGuardar.setOnClickListener {
            val ejercicio = etEjercicio.text.toString().trim()
            val grupo = etGrupo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()


            val equipamientoMarcado = mutableListOf<Equipamiento>()
            val posicionesMarcadas = lvEquipamiento.checkedItemPositions

            for(i in 0 until lvEquipamiento.count){
                if (posicionesMarcadas.get(i)){
                    val auxEquipamiento = listaEquipamiento[i]
                    equipamientoMarcado.add(auxEquipamiento)
                }
            }


            if(ejercicio.isEmpty() || grupo.isEmpty()){
                Toast.makeText(this, "El nombre y el grupo muscular son obligatorios",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Objeto Ejercicio. El ID se deja vacio porue lo genera el servidor
            val nuevoEjercicio = Ejercicio(nombre = ejercicio, grupoMuscular = grupo, descripcion = descripcion, equipamiento = equipamientoMarcado)

            //Enviar al Spring Boot
            apiService.crearEjercicio(nuevoEjercicio).enqueue(object : Callback<Ejercicio>{
                override fun onResponse(call: Call<Ejercicio>, response: Response<Ejercicio>){
                    if (response.isSuccessful){
                        Toast.makeText(this@CrearEjercicioActivity, "Ejercicio guardado en la BD",
                            Toast.LENGTH_LONG).show()
                        finish() //Vuelve al Panel de Control
                    }else{
                        Toast.makeText(this@CrearEjercicioActivity, "Error al guardar: ${response.code()}",
                            Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Ejercicio>, t: Throwable){
                    Toast.makeText(this@CrearEjercicioActivity, "Error de conexion",
                        Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}