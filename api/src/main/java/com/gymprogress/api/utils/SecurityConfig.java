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

/**
 * Configuración principal de seguridad de la API.
 * <p>
 * Define las reglas de acceso, la gestión de sesiones y la integración
 * del filtro JWT en la cadena de filtros de Spring Security.
 * </p>
 */
@Configuration
@EnableWebSecurity // Habilita el soporte de seguridad web de Spring
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Define la cadena de filtros de seguridad (Security Filter Chain).
     * <p>
     * Aquí se decide qué peticiones requieren autenticación y cuáles son libres.
     * </p>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. Desactivamos CSRF (Cross-Site Request Forgery).
        // No es necesario en APIs REST que usan tokens JWT, ya que no se usan cookies de sesión.
        http.csrf(csrf -> csrf.disable())

                // 2. Establecemos la política de creación de sesiones como STATELESS (Sin estado).
                // Esto le indica a Spring que no cree una sesión HTTP en el servidor;
                // cada petición debe ser validada individualmente con el token.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Definimos las reglas de autorización (quién puede entrar a dónde)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas: Registro y Login deben ser accesibles para todos.
                        .requestMatchers(HttpMethod.POST, "/api/usuarios", "/api/usuarios/login").permitAll()

                        // Catálogos públicos: Permitimos que cualquiera vea ejercicios, rutinas y equipos.
                        .requestMatchers(HttpMethod.GET, "/api/ejercicios", "/api/rutinas", "/api/equipamientos").permitAll()

                        // Rutas privadas: Cualquier otra petición (entrenamientos, medidas, objetivos)
                        // requiere que el usuario esté autenticado.
                        .anyRequest().authenticated()
                )

                // 4. Inyectamos nuestro filtro JWT.
                // Le decimos a Spring: "Antes de que ejecutes tu filtro de usuario/password estándar,
                // ejecuta mi filtro JwtFilter para buscar el token en la cabecera".
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}