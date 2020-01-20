package com.ajparedes.service;

import com.ajparedes.model.Device;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * IDeviceService:
 * Interfaz que declara los servicios necesarios para el manejo de los dispositivos móviles
 */
public interface IDeviceService {

	/**
	 * Si es posible asociar el dispositivo en la cuenta del usuario, este es registrado.
	 * @param idDevice identificador del dispositivo móvil
	 * @param username nombre de usuario
	 * @throws Exception Lanza excepción en caso de que el dispositivo ya este registrado
	 */
	public abstract void checkDevice(String idDevice, String username) throws Exception;

	/**
	 * Método que verifica si un dispositivo ya se encuentra registrado en el sistema.
	 * @param idDevice identificador del disposiivo.
	 * @return el dispositivo si este es encontrado, null en caso contrario.
	 */
	public abstract Device isRegistered(String idDevice);

	/**
	 * Método para verificar que el id del dispositivo corresponda a la cuenta del usuario
	 * que lo desea utilizar.
	 * @param device identificador del dispositivo móvil
	 * @param user nombre de usuario
	 * @return true en caso de que el dispositivo no corresponda a la cuenta del usuario,
	 * false en caso contrario.
	 */
	public abstract boolean verifyDevice(String device, String user);

}
