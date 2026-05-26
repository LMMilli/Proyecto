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
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacion.api.ApiClient
import com.example.aplicacion.api.ApiService
import com.example.aplicacion.model.Objetivo
import com.example.aplicacion.model.ObjetivoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad para la creación de nuevos objetivos personales (ej. peso, grasa, fuerza).
 * Incluye un selector de fecha y el envío de los datos a la API para su persistencia.
 */
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

        // 1. Configuración del Spinner para seleccionar el tipo de objetivo
        val opciones = arrayOf("Peso Corporal", "Porcentaje Grasa", "Fuerza (RM)", "Otro")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        // 2. Configuración del DatePickerDialog para seleccionar la fecha límite
        btnFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                // Formateo de fecha ISO (YYYY-MM-DD) para compatibilidad con la API
                val mesFormateado = String.format("%02d", month + 1)
                val diaFormateado = String.format("%02d", day)
                fechaFinal = "$year-$mesFormateado-$diaFormateado"
                tvFecha.text = "Seleccionada: $fechaFinal"
            }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 3. Lógica para guardar el objetivo en la base de datos
        btnGuardar.setOnClickListener {
            val tipo = spinner.selectedItem.toString()
            val valorTexto = etValor.text.toString()
            val valorNumerico = valorTexto.toDoubleOrNull()

            if (valorNumerico == null || fechaFinal.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ObjetivoRequest(idUsuario, tipo, valorNumerico, fechaFinal)

            apiService.crearObjetivo(request).enqueue(object : Callback<Objetivo> {
                override fun onResponse(call: Call<Objetivo>, response: Response<Objetivo>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@NuevoObjetivoActivity, "¡Objetivo fijado con éxito!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val codigoError = response.code()
                        Toast.makeText(this@NuevoObjetivoActivity, "Error al guardar ($codigoError)", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Objetivo>, t: Throwable) {
                    Toast.makeText(this@NuevoObjetivoActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}