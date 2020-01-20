package com.ajparedes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajparedes.data.IDeviceRepository;
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

@Service
public class DeviceService implements IDeviceService {

	//---------------------------------------------------------------------------------------
	// ATRIBUTOS
	//---------------------------------------------------------------------------------------
	@Autowired
	private IDeviceRepository repo;
	
	//---------------------------------------------------------------------------------------
	// MÉTODOS
	//---------------------------------------------------------------------------------------

	/**
	 * Si es posible asociar el dispositivo en la cuenta del usuario, este es registrado.
	 * @param idDevice identificador del dispositivo móvil
	 * @param username nombre de usuario
	 * @throws Exception Lanza excepción en caso de que el dispositivo ya este registrado
	 */
	@Override
	public void checkDevice(String idDevice, String username) throws Exception {
		//  verifica si isRegistered y si no lo esta addDevice
		Device dev = isRegistered(idDevice);
		if (dev == null) {
			Device d = new Device();
			d.setId(idDevice);
			d.setUsername(username);
			addDevice(d);
		}
		else if (!dev.getUsername().equals(username))
			throw new Exception("The user logged dosn't match with the user associated to this device.\nTo continue with this user, first log in with the linked account and unlink the device.");
	}

	/**
	 * Método que verifica si un dispositivo ya se encuentra registrado en el sistema.
	 * @param idDevice identificador del disposiivo.
	 * @return el dispositivo si este es encontrado, null en caso contrario.
	 */
	@Override
	public Device isRegistered(String idDevice){
		Optional<Device> dev = repo.findById(idDevice);
		return (dev.isPresent()) ? dev.get() : null;
	}
	
	/**
	 * Método para registrar un dispositivo en la bse de datos.
	 * @param dev dispositivo a registrar
	 */
	public void addDevice(Device dev) {
		repo.save(dev);
	}

	/**
	 * Método para verificar que el id del dispositivo corresponda a la cuenta del usuario
	 * que lo desea utilizar.
	 * @param device identificador del dispositivo móvil
	 * @param user nombre de usuario
	 * @return true en caso de que el dispositivo no corresponda a la cuenta del usuario,
	 * false en caso contrario.
	 */
	@Override
	public boolean verifyDevice(String device, String user) {
		// verifica si el dispositivo existe y coincide con el usuario
		Device d = isRegistered(device);
		if(d !=null){
			return d.getUsername().equals(user);
		}
		return false;
	}

}
