package com.example.aplicacion

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.AuthResponse
import com.example.aplicacion.model.Objetivo
import com.example.aplicacion.model.ObjetivoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NuevoObjetivoActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private var idUsuario: Long = -1L
    private var fechaFinal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_objetivo)

        idUsuario = intent.getLongExtra("ID_USUARIO", -1L)
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        val spinner = findViewById<Spinner>(R.id.spinnerTipoObjetivo)
        val etValor = findViewById<EditText>(R.id.etValorObjetivo)
        val btnFecha = findViewById<Button>(R.id.btnSeleccionarFecha)
        val tvFecha = findViewById<TextView>(R.id.tvFechaSeleccionada)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarObjetivo)

        //1. Configurar opciones del Spinner
        val opciones = arrayOf("Peso Corporal", "Porcentaje Grasa", "Fuerza (RM)", "Otro")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        //2. Configurar el Calendario
        btnFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(this, {_, year, month, day ->
                //Formatemnoas para que la API lo entienda
                val mesFormateado = String.format("%02d", month+1)
                val diaFormateado = String.format("%02d", day)
                fechaFinal = "$year-$mesFormateado-$diaFormateado"
                tvFecha.text = "Seleccionada: $fechaFinal"
            }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        //3. Botón Guardar
        btnGuardar.setOnClickListener {
            val tipo = spinner.selectedItem.toString()
            val valorTexto = etValor.text.toString()

            val valorNumerico = valorTexto.toDoubleOrNull()

            if(valorNumerico == null ||fechaFinal.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ObjetivoRequest(idUsuario, tipo, valorNumerico, fechaFinal)

            apiService.crearObjetivo(request).enqueue(object : Callback<Objetivo>{
                override fun onResponse(call: Call<Objetivo>, response: Response<Objetivo>){
                    if(response.isSuccessful){
                        Toast.makeText(this@NuevoObjetivoActivity, "¡Obejtivo fijado!", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        val errorDelServidor = response.errorBody()?.string()
                        val codigoError = response.code()


                        Toast.makeText(this@NuevoObjetivoActivity, "Error al guardar $codigoError: $errorDelServidor", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Objetivo>, t: Throwable){
                    Toast.makeText(this@NuevoObjetivoActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}