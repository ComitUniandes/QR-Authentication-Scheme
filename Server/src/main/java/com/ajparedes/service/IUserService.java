package com.ajparedes.service;

import com.ajparedes.model.User;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * IUserService:
 * Interfaz que declara los servicios necesarios para el manejo de los usuarios del sistema.
 */
public interface IUserService{

	/**
	 * Método para solicitar el usuario con nombre de usuario dado.
	 * @param username nombre de usuario
	 * @return el usuario solicitado o null en caso de no encontrarlo
	 */
	public abstract User getUser(String username);

	/**
	 * Método para verificar las credenciales y autorización del usuario.
	 * @param u nombre de usuario
	 * @param pass contraseña del usuario
	 * @return true en caso de autenticación exitosa, false en caso contrario
	 */
	public abstract boolean login(User u, String pass);

	/**
	 * Método para verificar si un usuario tiene permiso de acceso a la instalación protegida.
	 * @param username nombre de usuario
	 * @return true en caso de estár autorizado, false en caso contraio
	 */
	public abstract boolean isAuthorized(String username);
}
