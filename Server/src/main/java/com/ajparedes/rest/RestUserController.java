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

@RestController
@RequestMapping("/users")
public class RestUserController {

	@Autowired
	private IUserRepository repo;
	
	@Autowired
	private IUserService service;
	
	@Autowired
	private IDeviceService devService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//no usado por la app movil
	@GetMapping
	public List<User> getAll(){
		return repo.findAll();
	}
	
	//no usado por la app movil
	@PostMapping
	public void addUser(@RequestBody User per){
		String pass = per.getPassword();
		per.setPassword(encoder.encode(pass));
		repo.save(per);
	}
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
