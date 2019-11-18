package com.ajparedes.service;

import com.ajparedes.model.User;

public interface IUserService{

	public abstract boolean login(User u, String pass);
}
