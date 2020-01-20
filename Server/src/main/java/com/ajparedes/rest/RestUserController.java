package com.ajparedes.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajparedes.data.IUserRepository;
import com.ajparedes.model.ResponseMessage;
import com.ajparedes.model.User;
import com.ajparedes.model.UserLogin;
import com.ajparedes.service.IDeviceService;
import com.ajparedes.service.IUserService;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * RestUserController:
 * Clase que expone los servicios REST asociados a la entidad 'user'.
 */
@RestController
@RequestMapping("/users")
public class RestUserController {
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	
	@Autowired
	private IUserRepository repo;
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private IDeviceService devService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//------------------------------------------------------
    // SERVICIOS REST
	//------------------------------------------------------

	/**
	 * Mëtodo GET para listar los usuarios registrados en la base de datos.
	 * Este método no es utilizado por la aplicación cliente, creado con fines de pruebas.
	 * @return lista con los usuarios enconrados en base de datos.
	 */
	@GetMapping
	public List<User> getAll(){
		return repo.findAll();
	}
	/**
	 * Mëtodo POST para resgistrar un usuario nuevo en la base de datos.
	 * Este método no es utilizado por la aplicación cliente, creado con fines de pruebas.
	 * @param per nuevo usuario a agregar.
	 */
	@PostMapping
	public void addUser(@RequestBody User per){
		String pass = per.getPassword();
		per.setPassword(encoder.encode(pass));
		repo.save(per);
	}
	/**
	 * Método POST encargado de recibir las peticiones de inicio de sesión. En caso de login
	 * exitoso el dispositivo es regsitrado y asociado a la cuenta del usuario.
	 * @param ulogin Información del usuario y dispositivo en el que se pretende iniciar sesión.
	 * @return Respuesta con mensaje y estado de la socilitud.
	 */
	@PostMapping("/login")
	public ResponseEntity<ResponseMessage> login(@RequestBody UserLogin ulogin){
		User u = service.getUser(ulogin.getUsername());
		
		boolean login = service.login(u, ulogin.getPassword());
		String status = "User Not Authorized Or Incorrect";
		if(login) {
			status = "Login Successful";
			try{
			devService.checkDevice(ulogin.getIdDevice(), ulogin.getUsername());
			}catch (Exception e){
				status = e.getMessage();
			}
		}
		return new ResponseEntity<ResponseMessage>(new ResponseMessage(status), HttpStatus.OK);
	}
}
