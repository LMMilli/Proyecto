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

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🚪 Alguien intenta entrar a: " + request.getRequestURI());
        System.out.println("Tipo de petición: " + request.getMethod());


        // Extreamos la cabezar "Authorization" de la peticion
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        //Comprobamos si la cabezera existe y empezar poer "Bearer"
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extraerEmail(token);
            }catch (Exception e){
                System.out.println("Error al extraer el token: " + e.getMessage());
            }
        }

        //Si tenemos un email y el usuario aun no esta autenticado en este hilo:
        if(email != null && SecurityContextHolder.getContext().getAuthentication()== null){
            //Validamos que el token sea legitimo y no este cadcuado
            if(jwtUtil.validarToken(token)){
                //Le decimos a Spring security que este usuario tiene permiso
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Dejamos que la peticon siga su cruso
        filterChain.doFilter(request, response);

    }
}
