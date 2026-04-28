package com.gymprogress.api.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para la gestión segura de contraseñas.
 * <p>
 * Utiliza el algoritmo BCrypt para transformar contraseñas legibles en hashes
 * irreversibles, añadiendo una "sal" (salt) aleatoria para proteger contra
 * ataques de tablas de arcoíris (rainbow tables).
 * </p>
 */
public class SecurityUtil {

    /**
     * Cifra una contraseña utilizando BCrypt.
     * <p>
     * Este método se debe llamar únicamente durante el registro de un nuevo usuario
     * o cuando este decide cambiar su contraseña.
     * </p>
     * * @param passwordNormal La contraseña en texto plano enviada por el usuario.
     * @return Un String que contiene el hash generado (incluyendo la sal).
     */
    public static String cifrarPassword(String passwordNormal){
        // gensalt() genera una semilla aleatoria única para este usuario.
        // Esto garantiza que si dos usuarios tienen la misma clave "1234",
        // sus hashes en la base de datos sean totalmente distintos.
        return BCrypt.hashpw(passwordNormal, BCrypt.gensalt());
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     * <p>
     * Este método se utiliza durante el proceso de Login.
     * </p>
     * * @param passwordNormal La contraseña que el usuario acaba de escribir en el formulario.
     * @param hashEnBD El hash que tenemos guardado en la tabla de usuarios.
     * @return true si la contraseña es correcta; false en caso contrario.
     */
    public static boolean verificarPassword(String passwordNormal, String hashEnBD){
        // BCrypt es inteligente: extrae automáticamente la 'sal' del hash guardado
        // para aplicarla a la contraseña escrita y verificar si el resultado coincide.
        return BCrypt.checkpw(passwordNormal, hashEnBD);
    }
}