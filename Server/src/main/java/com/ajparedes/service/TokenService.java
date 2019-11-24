package com.ajparedes.service;

import java.security.SecureRandom;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajparedes.data.ITokenRepository;
import com.ajparedes.model.Token;

@Service
public class TokenService implements ITokenService {

	@Autowired
	private ITokenRepository repo;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IDeviceService deviceService;
	
	@Autowired
	private SecureRandom random;
	
	@Autowired
	private Encoder base64;
	
	@Override
	public Token createToken(String user, String device) {
		// TODO Auto-generated method stub
		
		// 1 verificar usuario y dispositivo
		if (deviceService.verifyDevice(device, user) && userService.isAuthorized(user)){
			
			// el id del token lo genera la db
			Token token = new Token();
			token.setIdDevice(device);
			token.setIdUser(user);
			// el value
			token.setTokenValue(generateTokenValue());
			// agregar a db
			repo.save(token);
			return token;
		}
		return null;
	}
	
	// busca el token y verifica que no haya expirado, si es usado marca active en false
	@Override
	public boolean validate(Token token) throws Exception {
		Token t = getToken(token.getId());
		if(t != null && t.isActive()) {
			Date expDate = t.getDate();
			if(expDate.after(new Date())&& compare(t, token)) {
				t.setActive(false);
				repo.save(t);
				return true;
			}
			else throw new Exception ("Expired Token");
		}
		else throw new Exception("The token was alredy used");
	}

	@Override
	public Token getToken(int id) {
		Optional<Token> t = repo.findById(id);
		return t.isPresent() ? t.get() : null;
	}
	
	public boolean compare (Token t1, Token t2) throws Exception{
		boolean b= t1.equals(t2);
		System.out.println("compare:"+b);
		if (b)
			return b;
		else
			throw new Exception("Invalid Token");
	}

	public String generateTokenValue() {
	    byte[] randomBytes = new byte[24];
	    random.nextBytes(randomBytes);
	    // 32 caracteres
	    return base64.encodeToString(randomBytes);
	}
}
