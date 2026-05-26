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
import com.example.aplicacion.model.Entrenamiento
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Actividad que muestra el historial de entrenamientos del usuario.
 * Recarga automáticamente los datos al volver a la pantalla (onResume).
 */
class HistorialEntrenamientosActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var lvHistorial: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private var idUsuario: Long = -1L

    private var listaEntrenamientos: List<Entrenamiento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial_entrenamientos)

        lvHistorial = findViewById(R.id.lvHistorialEntrenamientos)
        progressBar = findViewById(R.id.progressBarHistorial)
        tvEmptyState = findViewById(R.id.tvEmptyStateHistorial)
        val btnNuevo = findViewById<MaterialButton>(R.id.btnNuevoEntrenamiento)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Navegación al detalle del entrenamiento seleccionado
        lvHistorial.setOnItemClickListener { _, _, position, _ ->
            val entrenamientoSeleccionado = listaEntrenamientos[position]
            val intent = Intent(this, DetalleEntrenamientoActivity::class.java)
            intent.putExtra("ID_ENTRENAMIENTO", entrenamientoSeleccionado.id)
            startActivity(intent)
        }

        // Navegación para iniciar una nueva sesión
        btnNuevo.setOnClickListener {
            val intent = Intent(this, EntrenamientoActivoActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            intent.putExtra("MODO_LIBRE", true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarHistorial()
    }

    /**
     * Obtiene y formatea la lista de entrenamientos pasados desde el servidor.
     */
    private fun cargarHistorial() {
        if (idUsuario == -1L) return

        progressBar.visibility = View.VISIBLE
        lvHistorial.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        apiService.obtenerHistorialEntrenamientos(idUsuario).enqueue(object : Callback<List<Entrenamiento>> {
            override fun onResponse(call: Call<List<Entrenamiento>>, response: Response<List<Entrenamiento>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    listaEntrenamientos = response.body()!!

                    if (listaEntrenamientos.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                    } else {
                        lvHistorial.visibility = View.VISIBLE

                        // Formateo de fechas para el catálogo visual
                        val idioma = Locale.forLanguageTag("es-ES")
                        val formateador = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm 'h'", idioma)

                        val nombres = listaEntrenamientos.map { entrenamiento ->
                            val fechaFormateada = try {
                                entrenamiento.fecha?.let { fechaIso ->
                                    java.time.LocalDateTime.parse(fechaIso).format(formateador)
                                } ?: "Sin fecha"
                            } catch (e: Exception) {
                                "Fecha inválida"
                            }
                            "Entrenamiento: $fechaFormateada"
                        }

                        lvHistorial.adapter = ArrayAdapter(
                            this@HistorialEntrenamientosActivity,
                            android.R.layout.simple_list_item_1,
                            nombres
                        )
                    }
                } else {
                    Toast.makeText(this@HistorialEntrenamientosActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Entrenamiento>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@HistorialEntrenamientosActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}