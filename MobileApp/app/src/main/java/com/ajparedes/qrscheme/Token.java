package com.ajparedes.qrscheme;

import android.graphics.Bitmap;
import android.util.Log;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 *
 * Token:
 * Clase para la generación de imagenes de códigos QR.
 */
public class Token {

    //------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
    private long id;
    private String idUser;
    private String idDevice;
    private String tokenValue;
    private String expDate;
    private boolean notUsed;

    //------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------

    /**
     * Método constructor. Inicializa los atributos con los valores recibidos por parametro.
     * @param id identificador del token
     * @param idUser nombre de usuario de quien genera el token
     * @param idDevice identificador del dispositivo
     * @param tokenValue valor del token
     * @param expDate fehca de expiración del token
     * @param notUsed valor que indica si el token ya ha sido usado
     */
    public Token(long id, String idUser, String idDevice, String tokenValue, String expDate, boolean notUsed) {
        this.id = id;
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.tokenValue = tokenValue;
        this.expDate = expDate;
        this.notUsed = notUsed;
    }

    /**
     * Método que implementa la libreria qrgenearator para la generación de la imagen QR a partir
     * de los datos del token
     * @param dimension tamaño máximo de la imagen
     * @return imagen del código QR en mapa de bits
     */
    public Bitmap generateQR(int dimension){
        String data = id+":"+idUser+":"+idDevice+":"+tokenValue+":"+expDate+":"+notUsed;
        QRGEncoder encoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, dimension);
        try{
            return encoder.encodeAsBitmap();
        }catch (Exception e){
            Log.d("Token","error generate qr: "+ e.getMessage());
            return null;
        }
    }
}
