package com.ajparedes.model;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * UserLogin:
 * Clase que representa la esctructura del objeto recibido en la petición de inicio de sesión.
 */
public class UserLogin {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	private String username;
	private String password;
	private String idDevice;
		
    //------------------------------------------------------
    // MÉTODOS
	//------------------------------------------------------
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}
}
