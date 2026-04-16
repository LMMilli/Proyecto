package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnEntrenamientos = findViewById<Button>(R.id.btnMisEntrenamientos)
        val btnRutinas = findViewById<Button>(R.id.btnCatalogoRutinas)
        val btnMedidas = findViewById<Button>(R.id.btnMisMedidas)
        val btnAdmin = findViewById<Button>(R.id.btnPalenAdmin)
        val tokenManager = TokenManager(MyApp.appContext)
        val btnLogout = findViewById<Button>(R.id.btnLogout)


        //Abrimos intent para sacor los datos del usuario
        val nombre = tokenManager.getUserName()
        val idUsuario = tokenManager.getUserId()
        val email = tokenManager.getUserEmail()
        val emailAdim = "millimermar@gmail.com"


        //Personalizamos la pantalla segun el usuario
        tvBienvenida.text = "Hola, $nombre!"

        if(email == emailAdim){
            btnAdmin.visibility = android.view.View.VISIBLE //Asi aparece el boton de Admin
        }


        //Funciones de botones
        btnEntrenamientos.setOnClickListener {
            val intent = android.content.Intent(this, HistorialEntrenamientosActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnRutinas.setOnClickListener {
            val intent = android.content.Intent(this, CatalogoRutinasActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnMedidas.setOnClickListener {
            val intent = android.content.Intent(this, MedidasActivity::class.java)
            intent.putExtra("ID_USUARIO", idUsuario)
            startActivity(intent)
        }

        btnAdmin.setOnClickListener {
            val intent = android.content.Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            //Vaciamos la caja fuerte
            tokenManager.clearAll()

            val intent = Intent(this@HomeActivity, LoginActivity::class.java)

            //Limpiemos la pila de actividades
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }

    }
}