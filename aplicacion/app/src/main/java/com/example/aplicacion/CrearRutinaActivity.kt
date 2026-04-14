package com.example.aplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Ejercicio
import com.example.aplicacion.model.Rutina
import com.example.aplicacion.model.RutinaRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearRutinaActivity : AppCompatActivity() {
    private var listaEjerciciosOriginal : List<Ejercicio> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        val etNombre = findViewById<EditText>(R.id.etNombreRutina)
        val lvEjercicios = findViewById<ListView>(R.id.lvEjercicios)
        val btnGuarda = findViewById<Button>(R.id.btnGuardarRutina)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Obtener los ejercicios disponibles
        apiService.obtenerEjercicios().enqueue(object : Callback<List<Ejercicio>> {
            override fun onResponse(call: Call<List<Ejercicio>>, response: Response<List<Ejercicio>>){
                if(response.isSuccessful && response.body() != null){
                    listaEjerciciosOriginal=response.body()!!
                    //Extraemos los nombre para mostrarlos en la lista
                    val nombresEjercicios = listaEjerciciosOriginal.map { it.nombre }

                    //LLenamos la lista visula con los nombre y casillas

                    val adapter = ArrayAdapter(this@CrearRutinaActivity, android.R.layout.simple_list_item_multiple_choice, nombresEjercicios)

                    lvEjercicios.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Ejercicio>>, t: Throwable){
                Toast.makeText(this@CrearRutinaActivity, "Error al cargar los ejercicios",
                    Toast.LENGTH_SHORT).show()
            }
        })

        btnGuarda.setOnClickListener {
            val nombreRutina = etNombre.text.toString().trim()

            if (nombreRutina.isEmpty()){
                Toast.makeText(this, "Pon un nombre",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Recojer el id de los ejercicios selecionados para las rutinas
            val ejerciciosSelecionadosIds = mutableListOf<Long>()
            val posicionesMarcadas = lvEjercicios.checkedItemPositions

            for(i in 0 until lvEjercicios.count){
                if(posicionesMarcadas.get(i)){
                    val idEjercicio = listaEjerciciosOriginal[i].id
                    if(idEjercicio != null){
                        ejerciciosSelecionadosIds.add(idEjercicio)
                    }
                }
            }

            if (ejerciciosSelecionadosIds.isEmpty()){
                Toast.makeText(this, "Seleciona al menos un ejercicio",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Creamos el DTO y lo enviamos
            val request = RutinaRequest(nombreRutina, ejerciciosSelecionadosIds)
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
}