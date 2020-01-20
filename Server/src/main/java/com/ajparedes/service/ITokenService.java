package com.ajparedes.service;

import com.ajparedes.model.Token;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * ITokenService:
 * Interfaz que declara los servicios necesarios para el manejo de los tokens
 */
public interface ITokenService {
	
	/**
	 * Método para solicitar el token con id dado.
	 * @param id identificador del token a solicitar
	 * @return el token solicitado o null en caso de no encontrarlo
	 */
	public abstract Token getToken(long id);

	/**
	 * Método para crear un nuevo token de acceso.
	 * @param user nombre de usuario que crea el token
	 * @param device identificador del dispositivo utilizado por el usuario
	 * @return el nuevo token creado o null en caso de que la verificación de datos de usuario falle.
	 */
	public abstract Token createToken(String user, String device);

	/**
	 * Método para verificar la autenticidad y validez de un token.
	 * @param token token a verificar
	 * @return true en caso de que la validación sea exitosa
	 * @throws Exception en caso de que el token no sea autentico, haya sido utilizado previamente
	 * o haya expirado.
	 */
	public abstract boolean validate(Token token) throws Exception;

}
