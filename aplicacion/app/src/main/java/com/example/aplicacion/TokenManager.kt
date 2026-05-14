package com.example.aplicacion

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class TokenManager(context: Context) {
    //Creamos una llave maestr super segura del propi sistema Android
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    //Creamos las preferecnias encriptadas
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    //Funcion para guardar el token cuando haces login
    fun saveToken(token: String){
        sharedPreferences.edit { putString("JWT_TOKEN", token) }
    }

    //Funcion para leer el token
    fun getToken(): String?{
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    //Funcion para borra el token(Cerrar sesion o caducar)
    fun clearToken(){
        sharedPreferences.edit { remove("JWT_TOKEN") }
    }

    //Funcion para guardar usuario
    fun saveUserDAta(id: Long, nombre: String?, email: String){
        sharedPreferences.edit().apply(){
            putLong("USER_ID", id)
            putString("USER_NAME", nombre)
            putString("USER_EMAIL", email)
            apply()
        }
    }

    fun getUserName(): String? = sharedPreferences.getString("USER_NAME", null)
    fun getUserId(): Long = sharedPreferences.getLong("USER_ID", -1L)
    fun getUserEmail(): String? = sharedPreferences.getString("USER_EMAIL", null)

    fun clearAll(){
        sharedPreferences.edit { clear() }
    }

    //Funcion para guardar la hora exacta en la que se hace el login
    fun saveLoginTime(){
        sharedPreferences.edit { putLong("LOGIN_TIME", System.currentTimeMillis()) }
    }

    //Funciona pra comprobar si han pasado 24 horas
    fun isTokenExpired(): Boolean{
         val loginTime = sharedPreferences.getLong("LOGIN_TIME", 0L)

        if(loginTime == 0L) return true

        val currenTime = System.currentTimeMillis()
        val twentyFourHourInMillis = 24 * 60 * 60 * 1000L //24 horas en milisengundos

        //Duvuelve true si la difrenica de tiempo es mayor a 24 horas
        return (currenTime - loginTime) > twentyFourHourInMillis
    }


}
