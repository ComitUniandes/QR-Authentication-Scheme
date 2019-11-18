package com.ajparedes.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ajparedes.model.User;

public interface IUserRepository extends JpaRepository<User, String>{
	
	//@Query("SELECT u FROM user WHERE u.username = :username")
	//public Optional<User> findByUsername(@Param("username") String username);
	
	
}
