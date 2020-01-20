package com.ajparedes.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajparedes.data.IDeviceRepository;
import com.ajparedes.model.Device;
import com.ajparedes.model.ResponseMessage;
import com.ajparedes.service.IDeviceService;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * RestDeviceController:
 * Clase que expone los servicios REST asociados a la entidad 'device'.
 */
@RestController
@RequestMapping("/devices")
public class RestDeviceController {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	
	@Autowired
	private IDeviceRepository repo;
	
	@Autowired
	private IDeviceService service;
	
	//------------------------------------------------------
    // SERVICIOS REST
	//------------------------------------------------------

	/**
	 * Mëtodo GET para listar los dispositivos registrados en la base de datos.
	 * Este método no es utilizado por la aplicación cliente, creado con fines de pruebas.
	 * @return lista con los dispositivos enconrados en base de datos.
	 */
	@GetMapping("/list")
	public List<Device> getAll(){
		return repo.findAll();
	}
	
	/**
	 * Método POST encargado de desasociar una cuenta de usuario con un dispositivo móvil
	 * previamente registrado.
	 * @param dev Dispositivo a desvincular.
	 * @return Mensaje y estado de respuesta a la solicitud recibida.
	 */
	@PostMapping("/unlink")
	public ResponseEntity<ResponseMessage> unlinkDevice(@RequestBody Device dev){
		
		ResponseMessage message = new ResponseMessage("");
		HttpStatus status = HttpStatus.NO_CONTENT;
		Device d = service.isRegistered(dev.getId());
		if(d!=null && !d.getUsername().equals(dev.getUsername())) {
			message.setResponse("You are not authorized to execute this action");
			status = HttpStatus.FORBIDDEN;
		}
		else if (d ==null){
			message.setResponse("Device is not registered");
			status = HttpStatus.BAD_REQUEST;
		}else {
			repo.delete(dev);
			message.setResponse("Device successfuly unlinked");
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(message, status);
	}
		
}
