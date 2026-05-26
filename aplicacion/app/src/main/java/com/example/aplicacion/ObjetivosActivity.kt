package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Objetivo
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que gestiona la visualización y actualización de objetivos personales.
 * Permite filtrar por estado (completados/pendientes) y actualizar el progreso en la BD.
 */
class ObjetivosActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var lvObjetivos: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private lateinit var switchVerCompletados: SwitchMaterial

    private var idUsuario: Long = -1L
    private var listaCompleta: List<Objetivo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objetivos)

        lvObjetivos = findViewById(R.id.lvObjetivos)
        progressBar = findViewById(R.id.progressBarObjetivos)
        tvEmptyState = findViewById(R.id.tvEmptyStateObjetivos)
        val btnNuevo = findViewById<MaterialButton>(R.id.btnIrNuevoObjetivo)
        switchVerCompletados = findViewById(R.id.switchVerCompletados)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Listener para alternar la visualización de objetivos
        switchVerCompletados.setOnCheckedChangeListener { _, _ ->
            filtrarYMostrarObjetivos()
        }

        btnNuevo.setOnClickListener {
            val intent = Intent(this, NuevoObjetivoActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarObjetivos()
    }

    /**
     * Obtiene la lista completa de objetivos del usuario desde la API.
     */
    private fun cargarObjetivos() {
        if (idUsuario == -1L) return

        progressBar.visibility = View.VISIBLE
        lvObjetivos.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        apiService.obtenerObjetivos(idUsuario).enqueue(object : Callback<List<Objetivo>> {
            override fun onResponse(call: Call<List<Objetivo>>, response: Response<List<Objetivo>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    listaCompleta = response.body()!!
                    if (listaCompleta.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                        switchVerCompletados.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        switchVerCompletados.visibility = View.VISIBLE
                        filtrarYMostrarObjetivos()
                    }
                } else {
                    Toast.makeText(this@ObjetivosActivity, "Error al cargar metas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Objetivo>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ObjetivosActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Aplica el filtro seleccionado en el switch y refresca la lista usando el adaptador personalizado.
     */
    private fun filtrarYMostrarObjetivos() {
        val mostrarCompletados = switchVerCompletados.isChecked
        val listaFiltrada = if (mostrarCompletados) listaCompleta else listaCompleta.filter { !it.completado }

        if (listaFiltrada.isEmpty() && !mostrarCompletados && listaCompleta.isNotEmpty()) {
            tvEmptyState.text = "¡Todos los objetivos están completados!"
            tvEmptyState.visibility = View.VISIBLE
            lvObjetivos.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            lvObjetivos.visibility = View.VISIBLE

            // Callback para manejar el clic en el CheckBox del adaptador
            lvObjetivos.adapter = ObjetivoAdapter(this, listaFiltrada) { objetivo, isChecked ->
                if (objetivo.id != null) {
                    apiService.actualizarEstadoObjetivo(objetivo.id, isChecked).enqueue(object : Callback<Objetivo> {
                        override fun onResponse(call: Call<Objetivo>, response: Response<Objetivo>) {
                            if (response.isSuccessful) {
                                // Sincronización de la lista local tras la actualización en BD
                                val objetivoActualizado = response.body()
                                if (objetivoActualizado != null) {
                                    listaCompleta = listaCompleta.map { if (it.id == objetivoActualizado.id) objetivoActualizado else it }
                                    filtrarYMostrarObjetivos()
                                }
                            }
                        }
                        override fun onFailure(call: Call<Objetivo>, t: Throwable) {
                            Toast.makeText(this@ObjetivosActivity, "Error de red", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}