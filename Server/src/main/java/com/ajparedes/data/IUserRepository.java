package com.ajparedes.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajparedes.model.User;

public interface IUserRepository extends JpaRepository<User, String>{
	
}
