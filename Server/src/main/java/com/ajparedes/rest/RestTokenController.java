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

@RestController
@RequestMapping("/tokens")
public class RestTokenController {

	@Autowired
	private ITokenService service;
	
	//Obtener nuevo token
	@PostMapping("generate")
	public Token getNewToken(@RequestBody UserLogin ulogin){
		// el objeto ulogin debe venir sin password
		//TODO dar un estado de 403 http si no se logra 
		return service.createToken(ulogin.getUsername(), ulogin.getIdDevice());
	}
	
	@PostMapping("validate")
	public ResponseEntity<Object> validateToken(@RequestBody Token token){
		try{
			service.validate(token);
			return new ResponseEntity<>("Valid token", HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
}