package com.ajparedes.model;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 * ResponseMessage:
 * Clase que representa la esctructura del mensaje de respuesta de las peticiones HTTPS.
 */
public class ResponseMessage {
	
	//------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
	private String response;
	
    //------------------------------------------------------
    // MÉTODOS
	//------------------------------------------------------

	/**
	 * Método constructor.
	 * @param response Mensaje a enviar en la respuesta.
	 */
	public ResponseMessage(String response) {
		this.response = response;
	}
	/**
	 * Devuelve el mensaje de la respuesta.
	 * @return Retorna el valor del atributo response.
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * Permite modificar el mensaje de la espuesta.
	 * @param id nuevo valor del atributo response.
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	
	

}
