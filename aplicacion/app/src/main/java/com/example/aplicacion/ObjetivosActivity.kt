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
import com.example.aplicacion.model.Objetivo
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ObjetivosActivity: AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var lvObjetivos: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private var idUsuario: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objetivos)

        lvObjetivos = findViewById(R.id.lvObjetivos)
        progressBar = findViewById(R.id.progressBarObjetivos)
        tvEmptyState = findViewById(R.id.tvEmptyStateObjetivos)
        val btnNuevo = findViewById<MaterialButton>(R.id.btnIrNuevoObjetivo)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

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

    private fun cargarObjetivos(){
        if(idUsuario == -1L) return

        progressBar.visibility = View.VISIBLE
        lvObjetivos.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        apiService.obtenerObjetivos(idUsuario).enqueue(object : Callback<List<Objetivo>>{
            override fun onResponse(call: Call<List<Objetivo>>, response: Response<List<Objetivo>>){
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body() !=null){
                    val lista = response.body()!!

                    if (lista.isEmpty()){
                        tvEmptyState.visibility = View.VISIBLE
                    }else{
                        lvObjetivos.visibility = View.VISIBLE

                        //Mapeamos los daots para mostrarlos en la lista
                        val textObjetivos = lista.map {
                            "${it.tipo}: ${it.valorObjetivo}\nLimite: ${it.fechaLimite}"
                        }

                        lvObjetivos.adapter = ArrayAdapter(
                            this@ObjetivosActivity,
                            android.R.layout.simple_list_item_1,
                            textObjetivos
                        )
                    }
                }else{
                    Toast.makeText(this@ObjetivosActivity, "Error al cargar metas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Objetivo>>, t: Throwable){
                progressBar.visibility = View.GONE
                Toast.makeText(this@ObjetivosActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}