package com.ajparedes.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tokens")
public class Token {
	
	@Id
	private int id;
	private String idUser;
	private String idDevice;
	private String tokenValue;
	private Date date;
	//active garantiza que si la fecha de expiracion no ha terminado solo se use una vez el token
	private boolean active;
	
	public Token() {
		// date establece fecha de expiración 15 minutos a partir de la creación del token
		long time = new Date().getTime()+ (60 * 1000 * 15);
		date = new Date(time);
		active = true;
	}
	
	public int getId() {
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
	public Date getDate() {
		return date;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
