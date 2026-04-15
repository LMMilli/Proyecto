package com.gymprogress.api.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        //Desactivasmo CSRF que no es necesario en APIs REST
        http.csrf(csrf -> csrf.disable())

                //Le decmos a Spring que la API es Stateless basicamente que no tiene memoria de sesiones en el navegador
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //Definimos quien entra y quien no
                .authorizeHttpRequests(auth -> auth
                        //Rutas publicas
                        .requestMatchers(HttpMethod.POST,"/api/usuarios", "/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/ejercicios", "/api/rutinas", "/api/equipamientos").permitAll() //Los catalogos son publicos tambien

                        //Rutas privadas
                        .anyRequest().authenticated()
                )

                //Añadimos el filtro jwt antes de el filtro normal de Srping
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
