package com.ajparedes.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajparedes.model.Token;
import com.ajparedes.model.UserLogin;
import com.ajparedes.service.ITokenService;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * RestTokenController:
 * Clase que expone los servicios REST asociados a la entidad 'tokens'.
 */
@RestController
@RequestMapping("/tokens")
public class RestTokenController {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	
	@Autowired
	private ITokenService service;
	
	//------------------------------------------------------
    // SERVICIOS REST
	//------------------------------------------------------

	/**
	 * Método POST encargado de recibir las solicitudes de generación de token QR
	 * @param ulogin Objeto en el que se recibe la identidad del usuario y dispositivo asociado
	 * @return Respuesta con el token generado en caso de que no haya problemas con la autorización
	 * para crearlo. Respuesta con token nulo en caso contrario.
	 */
	@PostMapping("/generate")
	public ResponseEntity<Token> getNewToken(@RequestBody UserLogin ulogin){
		// el objeto ulogin debe venir sin password
		Token t = service.createToken(ulogin.getUsername(), ulogin.getIdDevice());
		if(t==null) {
			return new ResponseEntity<>(t, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(t, HttpStatus.OK);
	}
	/**
	 * Método POST encargado de recibir las solicitudes de verificación de autenticidad y validez de token QR.
	 * @param token valor del token QR a validar.
	 * @return Respuesta con mensaje y estado de la socilitud.
	 */
	@PostMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestBody Token token){
		try{
			service.validate(token);
			return new ResponseEntity<>("Valid token", HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
}
