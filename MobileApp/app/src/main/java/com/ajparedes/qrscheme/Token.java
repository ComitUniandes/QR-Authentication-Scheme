package com.ajparedes.qrscheme;

import android.graphics.Bitmap;
import android.util.Log;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Token {

    private long id;
    private String idUser;
    private String idDevice;
    private String tokenValue;
    private String expDate;
    private boolean notUsed;

    public Token(long id, String idUser, String idDevice, String tokenValue, String expDate, boolean notUsed) {
        this.id = id;
        this.idUser = idUser;
        this.idDevice = idDevice;
        this.tokenValue = tokenValue;
        this.expDate = expDate;
        this.notUsed = notUsed;
    }

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
