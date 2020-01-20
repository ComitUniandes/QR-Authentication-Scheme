package com.ajparedes.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajparedes.model.User;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * IUserRepository:
 * Interfaz que representa la conexión con la tabla 'user' de la base de datos.
 */
public interface IUserRepository extends JpaRepository<User, String>{
	
}
