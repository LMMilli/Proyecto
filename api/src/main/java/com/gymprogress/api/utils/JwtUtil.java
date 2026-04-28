package com.gymprogress.api.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilidad para la gestión de JSON Web Tokens (JWT).
 * <p>
 * Esta clase centraliza la lógica de creación, lectura y validación de tokens.
 * Utiliza el algoritmo HS256 para firmar los tokens, garantizando que la
 * información no sea alterada por terceros.
 * </p>
 */
@Component
public class JwtUtil {

    /**
     * Clave secreta utilizada para firmar y verificar los tokens.
     * <p>
     * Nota: En producción, esta clave nunca debería estar "hardcodeada" en el código.
     * Lo ideal es leerla desde una variable de entorno o un archivo de configuración externo.
     * </p>
     */
    private static final String SECRET = "GymProgressSuperSecretKeyParaTokens2024MuyLargaYSegura!!";

    // Genera una clave segura compatible con HMAC-SHA a partir del String secreto.
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Tiempo de expiración del token (24 horas expresadas en milisegundos).
     */
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Genera un token JWT basado en el email del usuario.
     * * @param email El correo del usuario que servirá como identificador (Subject).
     * @return Una cadena JWT compacta y firmada.
     */
    public String generarToken(String email){
        return Jwts.builder()
                .setSubject(email) // El "quién" es el dueño del token
                .setIssuedAt(new Date()) // Marca de tiempo de cuándo se creó
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Fecha de caducidad
                .signWith(key, SignatureAlgorithm.HS256) // Firma digital para evitar manipulaciones
                .compact(); // Ensambla todas las partes en el String final
    }

    /**
     * Extrae el email (Subject) que guardamos dentro de los "claims" del token.
     * * @param token El JWT enviado por el cliente.
     * @return El email contenido en el token.
     */
    public String extraerEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key) // Usamos la misma clave con la que se firmó
                .build()
                .parseClaimsJws(token) // Verifica la firma y parsea el contenido
                .getBody()
                .getSubject();
    }

    /**
     * Comprueba si el token es estructuralmente válido, si la firma coincide
     * y si no ha superado su fecha de expiración.
     * * @param token El JWT a validar.
     * @return true si es válido y seguro de usar; false si ha sido manipulado o caducó.
     */
    public boolean validarToken(String token){
        try {
            // Si el parseo tiene éxito, el token es legítimo
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Captura cualquier error: firma inválida, token caducado, formato erróneo, etc.
            return false;
        }
    }
}