package com.ajparedes.qrscheme;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 *
 * APIClientSingleton:
 * Clase para la creación y manejo de la cola de peticiones HTTP como singleton
 */
public class APIClientSingleton {
    //------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
    private static APIClientSingleton clientInstance;
    private RequestQueue requestQueue;
    private Context appContext;

    //------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------

    /**
     * Método constructor de la clase.
     * @param context
     */
    private APIClientSingleton (Context context){
        this. appContext = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new HurlStack(null, getSocketFactory()));
    }

    /**
     * Método para la obtención de instancia del singleton contenedor de la cola de peticiones
     * @param context
     * @return instancia de la cola de peticiones
     */
    public static synchronized APIClientSingleton getInstance(Context context){
        if(clientInstance==null){
            clientInstance = new APIClientSingleton(context);
        }
        return clientInstance;
    }

    /**
     * Método que retorna la cola de peticiones
     * @return valor de requestQueue
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Método que configura el contexto de la cola de peticiones para que acepte la conexión y
     * el certificado digital del servidor en el host especificado
     * @return objeto de tipo SSLSocketFactory con la configuración deseada
     */
    private SSLSocketFactory getSocketFactory() {

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = appContext.getResources().openRawResource(R.raw.ajparedes);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo(appContext.getString(R.string.host))==0;

                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            SSLContext context = null;
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            SSLSocketFactory sf = context.getSocketFactory();

            return sf;

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return  null;
    }
}