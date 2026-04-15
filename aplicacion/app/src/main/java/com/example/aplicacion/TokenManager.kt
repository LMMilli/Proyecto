package com.example.aplicacion

import android.content.Context
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {
    //Creamos una llave maestr super segura del propi sistema Android
    private val masterKey = MasterKey.Builder(context)

}
