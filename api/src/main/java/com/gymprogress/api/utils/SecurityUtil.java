package com.gymprogress.api.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {

    public static String cifrarPassword(String passwordNormal) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwordNormal.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        }catch (Exception e){
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }
}
