package com.example.aplicacion

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnEntrenamientos = findViewById<Button>(R.id.btnMisEntrenamientos)
        val btnRutinas = findViewById<Button>(R.id.btnCatalogoRutinas)
        val btnMedidas = findViewById<Button>(R.id.btnMisMedidas)
        val btnAdmin = findViewById<Button>(R.id.btnPalenAdmin)

        //Abrimos intent para sacor los datos del usuario
        val nombre = intent.getStringExtra("NOMBRE_USUARIO") ?: "Atleta"

        val idUsuario = intent.getLongExtra("ID_USUARIO", -1L)

        val email = intent.getStringExtra("EMAIL_USUARIO") ?: ""
        val emailAdim = "millimermar@gmail.com"


        //Personalizamos la pantalla segun el usuario
        tvBienvenida.text = "Hola, $nombre!"

        if(email == emailAdim){
            btnAdmin.visibility = android.view.View.VISIBLE //Asi aparece el boton de Admin
        }


        //Funciones de botones
        btnEntrenamientos.setOnClickListener {
            Toast.makeText(this, "Proximament: Historial de entrenamientos",
                Toast.LENGTH_SHORT).show()
        }

        btnRutinas.setOnClickListener {
            Toast.makeText(this, "Proximament: Ver Rutinas",
                Toast.LENGTH_SHORT).show()
        }

        btnMedidas.setOnClickListener {
            Toast.makeText(this, "Proximament: Mi peos y % grasa",
                Toast.LENGTH_SHORT).show()
        }

        btnAdmin.setOnClickListener {
            Toast.makeText(this, "Proximanete: Panel de Administraccion",
                Toast.LENGTH_SHORT).show()

        }

    }
}