package com.ajparedes.service;

import com.ajparedes.model.Token;

public interface ITokenService {
	
	public abstract Token getToken(int id);
	public abstract Token createToken(String user, String device);
	public abstract boolean validate(Token token) throws Exception;

}
