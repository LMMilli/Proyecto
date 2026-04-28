package com.gymprogress.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal y punto de entrada de la aplicación GymProgress API.
 * <p>
 * Esta clase inicializa el framework Spring Boot, escanea los componentes,
 * configura el servidor embebido (Tomcat por defecto) y levanta todo el
 * contexto de la aplicación.
 * </p>
 */
@SpringBootApplication
/* * @SpringBootApplication es una "super-anotación" que combina tres funciones clave:
 * 1. @Configuration: Indica que esta clase puede definir beans para el contexto.
 * 2. @EnableAutoConfiguration: Le dice a Spring que configure automáticamente las dependencias
 * que encuentre (ej. si ve el driver de base de datos en el pom.xml, configura el DataSource).
 * 3. @ComponentScan: Escanea todos los paquetes y subpaquetes (com.gymprogress.api.*) en busca
 * de @Service, @Repository, @Controller y @Component para registrarlos.
 */
public class ApiApplication {

	/**
	 * Método main: El punto de arranque estándar de cualquier aplicación Java.
	 * * @param args Argumentos de línea de comandos que se pueden pasar al iniciar la app.
	 */
	public static void main(String[] args) {
		/*
		 * Inicia el motor de Spring Boot, lanzando el proceso de inicialización,
		 * carga de propiedades (application.properties/yml) y puesta en marcha del servidor.
		 */
		SpringApplication.run(ApiApplication.class, args);
	}

}