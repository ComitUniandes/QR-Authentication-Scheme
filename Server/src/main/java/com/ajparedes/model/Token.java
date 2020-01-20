package com.ajparedes.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * Token:
 * Clase que representa la esctructura de la tabla 'tokens' de la base de datos.
 */
@Entity
@Table(name="tokens")
public class Token {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String idUser;
	private String idDevice;
	private String tokenValue;
	private long expDate;
	//notUsed garantiza que si la fecha de expiracion no ha terminado solo se use una vez el token
	private boolean notUsed;
		
    //------------------------------------------------------
    // MÉTODOS
	//------------------------------------------------------

	/**
	 * Método constructor. Establece el valor de la fecha de expiración como 15 minutos despues 
	 * a partir de la creación del token e inicializa el atributo notUsed como true para indicar 
	 * que el token no ha sido utilizado aún.
	 */
	public Token() {
		
		expDate = new Date().getTime()+ (60 * 1000 * 15);
		notUsed = true;
	}
	
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getIdDevice() {
		return idDevice;
	}
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}
	public String getTokenValue() {
		return tokenValue;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public long getExpDate() {
		return expDate;
	}
	public boolean isActive() {
		return notUsed;
	}
	public void setActive(boolean active) {
		this.notUsed = active;
	}
	
}
