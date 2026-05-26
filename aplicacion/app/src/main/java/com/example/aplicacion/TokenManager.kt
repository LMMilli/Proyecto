package com.example.aplicacion

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

/**
 * Gestor de autenticación y datos sensibles del usuario.
 * Utiliza [EncryptedSharedPreferences] para asegurar que el token JWT y los datos
 * del perfil se almacenen cifrados en el dispositivo, cumpliendo con estándares de seguridad.
 */
class TokenManager(context: Context) {

    // Generación de una llave maestra robusta mediante el sistema de seguridad de Android
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Configuración de las preferencias encriptadas
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /** Guarda el token JWT tras un login exitoso. */
    fun saveToken(token: String) {
        sharedPreferences.edit { putString("JWT_TOKEN", token) }
    }

    /** Recupera el token JWT almacenado. */
    fun getToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    /** Elimina el token (Logout). */
    fun clearToken() {
        sharedPreferences.edit { remove("JWT_TOKEN") }
    }

    /** Guarda los datos básicos del perfil de usuario. */
    fun saveUserDAta(id: Long, nombre: String?, email: String) {
        sharedPreferences.edit().apply {
            putLong("USER_ID", id)
            putString("USER_NAME", nombre)
            putString("USER_EMAIL", email)
            apply()
        }
    }

    fun getUserName(): String? = sharedPreferences.getString("USER_NAME", null)
    fun getUserId(): Long = sharedPreferences.getLong("USER_ID", -1L)
    fun getUserEmail(): String? = sharedPreferences.getString("USER_EMAIL", null)

    /** Limpia todo el almacenamiento seguro (Logout completo). */
    fun clearAll() {
        sharedPreferences.edit { clear() }
    }

    /** Registra el timestamp del momento en que se inicia sesión. */
    fun saveLoginTime() {
        sharedPreferences.edit { putLong("LOGIN_TIME", System.currentTimeMillis()) }
    }

    /** * Comprueba si la sesión ha superado las 24 horas de validez.
     * @return true si el token ha expirado o no hay registro de inicio.
     */
    fun isTokenExpired(): Boolean {
        val loginTime = sharedPreferences.getLong("LOGIN_TIME", 0L)
        if (loginTime == 0L) return true

        val currentTime = System.currentTimeMillis()
        val twentyFourHoursInMillis = 24 * 60 * 60 * 1000L

        return (currentTime - loginTime) > twentyFourHoursInMillis
    }
}