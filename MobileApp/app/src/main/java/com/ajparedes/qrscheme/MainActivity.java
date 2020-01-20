package com.ajparedes.qrscheme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.hawk.Hawk;
import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ---------------------------------------------------------------------------------------
 * QRAuth
 * Aplicación cliente de esquema te autenticación mediante generación de códigos QR
 * Por Andrea Paredes
 * Versión 1.0 - Enero 2020
 * ---------------------------------------------------------------------------------------
 *
 * MainActivity:
 * Actividad encargada de manejar los servicios de solicitud de generación de tokens y
 * cierre de sesión/desvinculación de dispositivo de la cuenta de usuario.
 */
public class MainActivity extends AppCompatActivity {
    //------------------------------------------------------
    // CONSTANTES
    //------------------------------------------------------
    private static final int LOGIN_RESULT = 1;
    private static final int CREDENTIALS_RESULT = 2;

    //------------------------------------------------------
    // ATRIBUTOS
    //------------------------------------------------------
    private RequestQueue requestQueue;
    private int mStatusCode;
    private String username;
    private String device;
    private Button btn_token;
    private Button btn_unlink;
    private ImageView qr_image;

    //------------------------------------------------------
    // MÉTODOS
    //------------------------------------------------------

    /**
     * onCreate se encarga de:
     * inicializar la vista e la actividad.
     * Obtener la instancia de la cola de peticiones.
     * Verificar si ya se ha iniciado sesión en el dispositivo, en caso afirmativo ctiva las
     * opciones de cierre de sesión y generación de tokens. En caso contrario se inicia la
     * actividad LoginActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hawk.init(getApplicationContext()).build();
        boolean isLogged = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("logged", false);

        if(!isDeviceSecure()){
            closeApp();
        }
        if(!isLogged){
            Intent i = new Intent( MainActivity.this, LoginActivity.class);
            startActivityForResult(i, 1);
        }
        else {
            checkIdentity();
            username = Hawk.get("username");
            device = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        requestQueue = APIClientSingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_token);
        qr_image = findViewById(R.id.imageViewQR);
        btn_token = findViewById(R.id.buttonToken);
        btn_token.setEnabled(false);
        btn_token.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                generateToken(username, device);
            }
        });
        btn_unlink = findViewById(R.id.unlink);
        btn_unlink.setEnabled(false);
        btn_unlink.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        unlinkDevice();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to sign out and close the app?");
                alertDialog.show();
            }
        });
    }

    /**
     * Realiza el manejo de los resultados obtenidos por la finalización de actividades.
     * En caso de proceso de login exitoso se guarda de manera cifrada el nombre de usuario en
     * medoria del dispositivo y se habilitan los demás servicios.
     * En caso de desbloqueo del dispositvo exitoso se habilitan los servios de la aplicación, en
     * caso contrario se solicita de nuevo el desbloqueo de este.
     *
     * @param requestCode código de la actividad ejectuda
     * @param resultCode código de resultado de la actividad
     * @param data datos enviados por la actividad que finaliza
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String username = data.getStringExtra("username");
                String device = data.getStringExtra("device");
                Hawk.put("username", username);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("logged", true).commit();
                btn_token.setEnabled(true);
                btn_unlink.setEnabled(true);
                this.username = username;
                this.device = device;
            }
        }
        if (requestCode == CREDENTIALS_RESULT){
            if(resultCode == RESULT_OK){
                btn_token.setEnabled(true);
                btn_unlink.setEnabled(true);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please unlock your device to confirm your identity", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Método de creación y envío de solicitud de generación de token
     *
     * @param username nombre de usuario de quien solicita el token
     * @param device identificador del dipositivo
     */
    public void generateToken(final String username, final String device){
        final String URL_GEN_TOKEN = getString(R.string.base_url) + getString(R.string.gen_token_url);
        Log.d("MainActivity", "token user: "+username);
        // recive username, password vacia, idDevice
        JSONObject js = new JSONObject();
        try {
            js.put("username", username);
            js.put("password", "");
            js.put("idDevice", device);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_GEN_TOKEN, js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            long id = response.getLong("id");
                            String tokenValue =  response.getString("tokenValue");
                            String expDate = response.getString("expDate");
                            boolean notUsed = response.getBoolean("active");

                            Log.d("MainActivity", "get token: "+tokenValue);

                            Token t = new Token(id, username, device, tokenValue, expDate, notUsed);
                            Bitmap qr = t.generateQR(getDimension());
                            if (qr !=null)
                                qr_image.setImageBitmap(qr);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "token volley error: "+error.getMessage());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    /**
     * Método para solicitar al usuario realice el desbloqueo del dispositivo para verificar su
     * propiedad sobre este.
     */
    public void checkIdentity() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Password required", "please enter your pattern to receive your token");
        if (credentialsIntent != null) {
            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
        }
    }

    /**
     * Metodo para verificar que el dispositivo cuente con un mecanismo de bloqueo habilitado.
     *
     * @return true en caso afirmativo, false en caso contrario.
     */
    public boolean isDeviceSecure(){
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        return  keyguardManager.isDeviceSecure();
    }

    /**
     * Método para obtener las dimensiones de la pantalla del dispositivo
     * @return la menor dimensión de la pantalla
     */
    public int getDimension(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int dimension = size.x >= size.y ? size.y : size.x;
        return dimension;
    }

    /**
     * Método para crear y enviar la solicitud de desvinculación entre cuenta de usuario y el
     * dispositivo. Si la desvinculación es exitosa la sesión del usuario es cerrada.
     */
    public void unlinkDevice (){
        final String URL_UNLINK = getString(R.string.base_url) + getString(R.string.gen_unlink_url);
        Log.d("MainActivity", "unlink device: "+device);
        JSONObject js = new JSONObject();
        try {
            js.put("username", username);
            js.put("id", device);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_UNLINK, js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (mStatusCode == HttpURLConnection.HTTP_OK){
                               getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("logged", false).commit();
                               Hawk.put("username", "");
                               finish();
                            }
                            Toast.makeText(getApplicationContext(), response.getString("response"), Toast.LENGTH_LONG).show();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "token volley error: "+error.getMessage());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        requestQueue.add(request);
    }

    /**
     * Método para cerrar la aplicación y notificar al usuario de esto.
     */
    public void closeApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity();
                System.exit(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Your device is not secure. Please set PIN, pattern or password to lock your device.");
        alertDialog.show();
    }
}
