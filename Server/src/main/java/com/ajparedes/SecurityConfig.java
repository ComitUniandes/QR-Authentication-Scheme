package com.ajparedes;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * Clase de configuración de seguridad en la que son creadas las
 * instancias de los mecanismos de codificación de datos y generación
 * de valores aleatorios
 */
@Configuration
public class SecurityConfig {
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecureRandom secureRandom() {
		return  new SecureRandom(); 
	}	
	@Bean
	public Encoder base64Encoder(){
		return Base64.getUrlEncoder();
	}

	
}
