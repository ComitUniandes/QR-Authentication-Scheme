package com.ajparedes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajparedes.model.User;

@Service
public class UserService implements IUserService{

	@Autowired
	private BCryptPasswordEncoder encoder;
	@Override
	public boolean login(User u, String pass) {
		// verificar las credenciales y el permiso de acceso
		if(u !=null){
			return ((encoder.matches(pass, u.getPassword()))&& u.isAccess());
		}
		return false;
	}

}
