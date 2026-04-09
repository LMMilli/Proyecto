package com.example.aplicacion

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
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

        var lvRutinas = findViewById<ListView>(R.id.lvCatalogoRutinas)
        var apiService = ApiClient.retrofit.create(ApiService::class.java)

        //Descargar las rutinas del servidor
        apiService.obtenerTodasLasRutinas().enqueue(object : Callback<List<Rutina>>{
            override fun onResponse(call: Call<List<Rutina>>, response: Response<List<Rutina>>){
                if(response.isSuccessful && response.body() !=null){
                    listaRutinas = response.body()!!

                    //Trasformar la lista de objetos Rutina en una lista de textos
                    //Ejemplo "Torso/Pierna (5 ejercicios)
                    val textRutinas = listaRutinas.map { rutina ->
                        val numEjercicios = rutina.ejercicio?.size ?: 0
                        "${rutina.nombre} ($numEjercicios ejercicios)"
                    }

                    //Se ponen en el ListView
                    val adapter = ArrayAdapter(
                        this@CatalogoRutinasActivity,
                        android.R.layout.simple_list_item_1,
                        textRutinas
                    )
                    lvRutinas.adapter = adapter
                }else{
                    Toast.makeText(this@CatalogoRutinasActivity, "Error al cargar las rutinas",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Rutina>>, t: Throwable){
                Toast.makeText(this@CatalogoRutinasActivity, "Error de red",
                    Toast.LENGTH_SHORT).show()
            }
        })

        lvRutinas.setOnItemClickListener { _, _, position, _ ->
            val rutinaSeleccionada = listaRutinas[position]
            val intent = android.content.Intent(this, EntrenamientoActivoActivity::class.java)

            val idUsuario = getIntent().getLongExtra("ID_USUARIO", -1L)
            intent.putExtra("ID_USUARIO", idUsuario)
            intent.putExtra("ID_RUTINA", rutinaSeleccionada.id)

            // EL TRUCO INFALIBLE: Lo convertimos a un texto separado por comas "1,5,8"
            val idsLista = rutinaSeleccionada.ejercicio?.mapNotNull { it.id } ?: emptyList()
            val idsTexto = idsLista.joinToString(",")

            intent.putExtra("IDS_EJERCICIOS_STRING", idsTexto) // Lo mandamos como String

            startActivity(intent)
        }
    }
}