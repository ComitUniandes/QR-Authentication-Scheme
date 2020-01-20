package com.ajparedes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajparedes.data.IUserRepository;
import com.ajparedes.model.User;
/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * UserService:
 * Clase que implementa los servicios necesarios para el manejo de los usuarios del sistema.
 */

@Service
public class UserService implements IUserService{

	//---------------------------------------------------------------------------------------
	// ATRIBUTOS
	//---------------------------------------------------------------------------------------
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private IUserRepository repo;

	//---------------------------------------------------------------------------------------
	// MÉTODOS
	//---------------------------------------------------------------------------------------

	/**
	 * Método para verificar las credenciales y autorización del usuario.
	 * @param u nombre de usuario
	 * @param pass contraseña del usuario
	 * @return true en caso de autenticación exitosa, false en caso contrario
	 */
	@Override
	public boolean login(User u, String pass) {
		// verificar las credenciales y el permiso de acceso
		if(u !=null){
			return ((encoder.matches(pass, u.getPassword()))&& u.isAccess());
		}
		return false;
	}

	/**
	 * Método para verificar si un usuario tiene permiso de acceso a la instalación protegida.
	 * @param username nombre de usuario
	 * @return true en caso de estár autorizado, false en caso contraio
	 */
	@Override
	public boolean isAuthorized(String username) {
		User u = getUser(username);
		if(u!=null) {
			return u.isAccess();
		}
		return false;
	}
	
	/**
	 * Método para solicitar el usuario con nombre de usuario dado.
	 * @param username nombre de usuario
	 * @return el usuario solicitado o null en caso de no encontrarlo
	 */
	@Override
	public User getUser(String username) {
		Optional<User> found = repo.findById(username);
		User u = found.isPresent() ? found.get() : null;
		return u;
	}

}
