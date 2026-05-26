package com.example.aplicacion

import android.content.Intent
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
import com.example.aplicacion.model.Rutina
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad encargada de visualizar el catálogo de rutinas disponibles.
 * Realiza una petición asíncrona a la API para obtener y listar las rutinas.
 */
class CatalogoRutinasActivity : AppCompatActivity() {

    private var listaRutinas: List<Rutina> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_rutinas)

        val lvRutinas = findViewById<ListView>(R.id.lvCatalogoRutinas)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarRutinas)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyStateRutinas)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Gestión inicial de visibilidad: mostramos carga, ocultamos resultados
        progressBar.visibility = View.VISIBLE
        lvRutinas.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        // Ejecución de la petición asíncrona para obtener las rutinas
        apiService.obtenerTodasLasRutinas().enqueue(object : Callback<List<Rutina>> {

            override fun onResponse(call: Call<List<Rutina>>, response: Response<List<Rutina>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    listaRutinas = response.body()!!

                    if (listaRutinas.isEmpty()) {
                        // Caso: La API devuelve una lista vacía
                        tvEmptyState.visibility = View.VISIBLE
                    } else {
                        // Caso: Se han recibido datos correctamente
                        lvRutinas.visibility = View.VISIBLE

                        // Mapeo de objetos Rutina a strings descriptivos para el adaptador
                        val textRutinas = listaRutinas.map { rutina ->
                            val numEjercicios = rutina.ejercicio?.size ?: 0
                            "${rutina.nombre} ($numEjercicios ejercicios) -- ${rutina.tipo}"
                        }

                        val adapter = ArrayAdapter(
                            this@CatalogoRutinasActivity,
                            android.R.layout.simple_list_item_1,
                            textRutinas
                        )
                        lvRutinas.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@CatalogoRutinasActivity, "Error al cargar las rutinas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Rutina>>, t: Throwable) {
                // Caso: Error de conexión o fallo en la petición
                progressBar.visibility = View.GONE
                Toast.makeText(this@CatalogoRutinasActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })

        // Configuración de navegación al hacer clic en un elemento de la lista
        lvRutinas.setOnItemClickListener { _, _, position, _ ->
            val rutinaSeleccionada = listaRutinas[position]
            val idUsuario = intent.getLongExtra("ID_USUARIO", -1L)

            val intent = Intent(this, EntrenamientoActivoActivity::class.java)

            // Propagación de datos necesarios para la actividad de entrenamiento
            intent.putExtra("ID_USUARIO", idUsuario)
            intent.putExtra("ID_RUTINA", rutinaSeleccionada.id)
            intent.putExtra("TIPO_RUTINA", rutinaSeleccionada.tipo)

            // Conversión de IDs de ejercicios a formato CSV para su transporte
            val idsLista = rutinaSeleccionada.ejercicio?.mapNotNull { it.id } ?: emptyList()
            val idsTexto = idsLista.joinToString(",")
            intent.putExtra("IDS_EJERCICIOS_STRING", idsTexto)

            startActivity(intent)
        }
    }
}