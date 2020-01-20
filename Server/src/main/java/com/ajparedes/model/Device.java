package com.ajparedes.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * Device:
 * Clase que representa la esctructura de la tabla 'device' de la base de datos.
 */
@Entity
@Table(name = "device")
public class Device {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	@Id
	private String id;
	private String username;
	
    //------------------------------------------------------
    // MÉTODOS
	//------------------------------------------------------

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
