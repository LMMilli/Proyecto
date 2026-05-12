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

class CatalogoRutinasActivity : AppCompatActivity() {
    private var listaRutinas: List<Rutina> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo_rutinas)

        val lvRutinas = findViewById<ListView>(R.id.lvCatalogoRutinas)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarRutinas)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyStateRutinas)

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        //1. Mostramos la barra de carga antes de pedri los datos al servidor
        progressBar.visibility = View.VISIBLE
        lvRutinas.visibility = View.GONE
        tvEmptyState.visibility = View.GONE

        //Descargar las rutinas del servidor
        apiService.obtenerTodasLasRutinas().enqueue(object : Callback<List<Rutina>>{
            override fun onResponse(call: Call<List<Rutina>>, response: Response<List<Rutina>>){
                //2. Ocultamos la barra de carga al recibir la respuesta
                progressBar.visibility = View.GONE

                if(response.isSuccessful && response.body() != null){
                    listaRutinas = response.body()!!

                    if(listaRutinas.isEmpty()){
                        //Si la lista vien vacía, mostramos el mensjae
                        tvEmptyState.visibility = View.VISIBLE
                    }else {
                        //Si hay rutinas, mostramos la lista
                        lvRutinas.visibility = View.VISIBLE

                        //Transforma la lista de objetos Ruitna en una lista de textos
                        val textRutinas = listaRutinas.map { rutina ->
                            val numEjercicios = rutina.ejercicio?.size ?: 0
                            "${rutina.nombre} ($numEjercicios ejercicios)"
                        }

                        //Se ponen en la ListView
                        val adapter = ArrayAdapter(
                            this@CatalogoRutinasActivity,
                            android.R.layout.simple_list_item_1,
                            textRutinas
                        )
                        lvRutinas.adapter = adapter
                    }
                }else{
                    Toast.makeText(this@CatalogoRutinasActivity, "Error al cargar las rutinas",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Rutina>>, t: Throwable){
                //Si falla el internt hay que ocultar la barra de carga
                progressBar.visibility = View.GONE
                Toast.makeText(this@CatalogoRutinasActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })

        //Accion al pueslar la rutina de la lista
        lvRutinas.setOnItemClickListener { _, _, position, _ ->
            val rutinaSeleccionada = listaRutinas[position]
            val intent = Intent(this, EntrenamientoActivoActivity::class.java)

            val idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
            intent.putExtra("ID_USUARIO", idUsuario)
            intent.putExtra("ID_RUTINA", rutinaSeleccionada.id)

            val idsLista = rutinaSeleccionada.ejercicio?.mapNotNull { it.id } ?: emptyList()
            val idsTexto = idsLista.joinToString(",")

            intent.putExtra("IDS_EJERCICIOS_STRING", idsTexto)

            startActivity(intent)

        }
    }
}