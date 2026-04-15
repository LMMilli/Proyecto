package com.gymprogress.api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    //La calve debe ser larga y secreta.
    //Demomento esta para pruebas
    private static final String SECRET = "GymProgressSuperSecretKeyParaTokens2024MuyLargaYSegura!!";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //El token va a durar 24 horas
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Genera un token JWT baso den el email del usuario
     */
    public String generarToken(String email){
        return Jwts.builder()
                .setSubject(email) //El dueño del token
                .setIssuedAt(new Date()) //Fecha de creacion
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256) //Algoritmo de encriptacion
                .compact(); //Construye el string final
    }

    /**
     * Extrae el email que guardmos dentro del token
     */
    public String extraerEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Comprueba si el token tiene el formato correcot y si no ha caducado
     */
    public boolean validarToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
