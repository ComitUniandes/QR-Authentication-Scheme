package com.ajparedes.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ajparedes.model.Device;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * IDeviceRepository:
 * Interfaz que representa la conexión con la tabla 'device' de la base de datos.
 */
public interface IDeviceRepository extends JpaRepository<Device, String> {

	/**
	 * Query utilizada para permitir la busqueda de dispositivos de acuerdo al nombre del 
	 * usuario asociado a este.
	 */
	//@Query("SELECT d FROM device WHERE d.username = :username")
	//public Optional<Device> findByUsername(@Param("username") String username);
}
