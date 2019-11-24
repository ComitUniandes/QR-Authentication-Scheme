package com.ajparedes.service;

import com.ajparedes.model.User;

public interface IUserService{

	public abstract User getUser(String username);
	public abstract boolean login(User u, String pass);
	public abstract boolean isAuthorized(String username);
}
