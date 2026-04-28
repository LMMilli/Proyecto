package com.gymprogress.api.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro de seguridad que se ejecuta en cada petición HTTP entrante.
 * <p>
 * Hereda de {@link OncePerRequestFilter} para garantizar que el filtrado
 * solo se realice una vez por cada solicitud, evitando procesamientos redundantes.
 * </p>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Clase auxiliar para cifrar, descifrar y validar tokens.

    /**
     * Método principal que intercepta la petición antes de que llegue al controlador.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Logs de depuración (útiles durante el desarrollo para ver quién llama a la API)
        System.out.println("🚪 Alguien intenta entrar a: " + request.getRequestURI());
        System.out.println("Tipo de petición: " + request.getMethod());

        // 1. Extraemos la cabecera "Authorization" de la petición HTTP
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // 2. Comprobamos si la cabecera existe y sigue el formato estándar "Bearer <token>"
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7); // Quitamos la palabra "Bearer " para quedarnos solo con el JWT
            try {
                // Intentamos extraer el email del "Subject" del token
                email = jwtUtil.extraerEmail(token);
            } catch (Exception e) {
                System.out.println("⚠️ Error al extraer el token: " + e.getMessage());
            }
        }

        // 3. Si tenemos un email y el usuario aún no está autenticado en el contexto actual:
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Validamos que el token sea legítimo (firma correcta) y no haya caducado
            if(jwtUtil.validarToken(token)){

                // Creamos un objeto de autenticación para Spring Security.
                // Como es una API Stateless, no usamos password aquí (null), y por ahora pasamos una lista de roles vacía.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                // Establecemos al usuario como autenticado para el resto de la vida de esta petición
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Usuario autenticado: " + email);
            }
        }

        // 4. Importantísimo: Dejamos que la petición siga su curso hacia el siguiente filtro o al controlador
        filterChain.doFilter(request, response);
    }
}