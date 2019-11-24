package com.ajparedes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajparedes.data.IUserRepository;
import com.ajparedes.model.User;

@Service
public class UserService implements IUserService{

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private IUserRepository repo;
	@Override
	public boolean login(User u, String pass) {
		// verificar las credenciales y el permiso de acceso
		if(u !=null){
			return ((encoder.matches(pass, u.getPassword()))&& u.isAccess());
		}
		return false;
	}
	@Override
	public boolean isAuthorized(String username) {
		User u = getUser(username);
		if(u!=null) {
			return u.isAccess();
		}
		return false;
	}
	@Override
	public User getUser(String username) {
		Optional<User> found = repo.findById(username);
		User u = found.isPresent() ? found.get() : null;
		return u;
	}

}
