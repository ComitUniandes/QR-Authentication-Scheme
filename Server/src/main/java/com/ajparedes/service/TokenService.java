package com.ajparedes.service;

import java.security.SecureRandom;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajparedes.data.ITokenRepository;
import com.ajparedes.model.Token;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * TokenService:
 * Clase que implementa los servicios necesarios para el manejo de los tokens
 */
@Service
public class TokenService implements ITokenService {

	//---------------------------------------------------------------------------------------
	// ATRIBUTOS
	//---------------------------------------------------------------------------------------
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
	
	//---------------------------------------------------------------------------------------
	// MÉTODOS
	//---------------------------------------------------------------------------------------
	
	/**
	 * Método para crear un nuevo token de acceso.
	 * @param user nombre de usuario que crea el token
	 * @param device identificador del dispositivo utilizado por el usuario
	 * @return el nuevo token creado o null en caso de que la verificación de datos de usuario falle.
	 */
	@Override
	public Token createToken(String user, String device) {
		
		if (deviceService.verifyDevice(device, user) && userService.isAuthorized(user)){
			
			Token token = new Token();
			token.setIdDevice(device);
			token.setIdUser(user);
			token.setTokenValue(generateTokenValue());
			
			repo.save(token);
			return getToken(token.getId());
		}
		return null;
	}
	
	/**
	 * Método para verificar la autenticidad y validez de un token.
	 * @param token token a verificar
	 * @return true en caso de que la validación sea exitosa
	 * @throws Exception en caso de que el token no sea autentico, haya sido utilizado previamente
	 * o haya expirado.
	 */
	@Override
	public boolean validate(Token token) throws Exception {
		Token t = getToken(token.getId());
		if(t != null && t.isActive()) {
			System.out.println(t.getExpDate());
			Date expDate = new Date(t.getExpDate());
			if(expDate.after(new Date())&& compare(t, token)) {
				t.setActive(false);
				repo.save(t);
				return true;
			}
			else throw new Exception ("Expired Token");
		}
		else throw new Exception("The token is invalid or was alredy used");
	}

	/**
	 * Método para solicitar el token con id dado.
	 * @param id identificador del token a solicitar
	 * @return el token solicitado o null en caso de no encontrarlo
	 */
	@Override
	public Token getToken(long id) {
		Optional<Token> t = repo.findById(id);
		return t.isPresent() ? t.get() : null;
	}
	
	/**
	 * Método para compara los valores de dos tokens.
	 * @param t1 token a comparar
	 * @param t2 segundo token a comparar
	 * @return true en caso de que sean iguales los tokens
	 * @throws Exception en caso de que los tokens no posean los mismos valores
	 */
	public boolean compare (Token t1, Token t2) throws Exception{
		boolean a = t1.getId() == t2.getId();
		boolean b = t1.getIdDevice().equals(t2.getIdDevice());
		boolean c = t1.getIdUser().equals(t2.getIdUser());
		boolean d = t1.getExpDate() == t2.getExpDate();
		boolean e = t1.getTokenValue().equals(t2.getTokenValue());
		if (a && b && c && d && e)
			return b;
		else
			throw new Exception("Invalid Token");
	}

	/**
	 * Método para generar un valor aleatorio seguro que será utilizado como parte del token
	 * @return String de 32 caracteres con el valor aleatorio
	 */
	public String generateTokenValue() {
	    byte[] randomBytes = new byte[24];
	    random.nextBytes(randomBytes);
	    return base64.encodeToString(randomBytes);
	}
}
