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
 * User:
 * Clase que representa la esctructura de la tabla 'user' de la base de datos.
 */
@Entity
@Table(name = "user")
public class User {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	@Id
	private String username;
	private String name;
	private String password;
	private boolean access;
		
    //------------------------------------------------------
    // MÉTODOS
	//------------------------------------------------------
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public boolean isAccess() {
		return access;
	}
	public void setAccess(boolean access) {
		this.access = access;
	}

	

}
