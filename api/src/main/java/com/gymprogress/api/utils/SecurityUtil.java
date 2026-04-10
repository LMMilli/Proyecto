package com.gymprogress.api.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {

    //Funcion que se una al regsitar un nuevo usairo
    public static String cifrarPassword(String passwordNormal){
        //gensalt() genera una semilla aleatoria unica para este usuario
        return BCrypt.hashpw(passwordNormal, BCrypt.gensalt());
    }

    //Fucion que se usa en el login para comparar la clava escrita con la de la base de datos
    public static boolean verificarPassword(String passwordNormal, String hashEnBD){
        //BCrypt extare la 'sal' del has guardado para hacer la comparacion
        return BCrypt.checkpw(passwordNormal, hashEnBD);
    }
}
