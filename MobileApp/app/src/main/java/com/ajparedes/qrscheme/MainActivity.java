package com.ajparedes.qrscheme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_RESULT = 1;
    private static final int CREDENTIALS_RESULT = 2;

    private RequestQueue requestQueue;

    private String username;
    private String device;
    private Button btn_token;
    private ImageView qr_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hawk.init(getApplicationContext()).build();
        boolean isLogged = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("logged", false);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String username = data.getStringExtra("username");
                String device = data.getStringExtra("device");
                Hawk.put("username", username);
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("logged", true).commit();
                btn_token.setEnabled(true);
                this.username = username;
                this.device = device;
            }
        }
        if (requestCode == CREDENTIALS_RESULT){
            Log.d("MainActivity","credetinal result code: "+ resultCode);
            if(resultCode == RESULT_OK){
                btn_token.setEnabled(true);
            }
            else{
                Toast.makeText(getApplicationContext(), "Please unlock your device to confirm your identity", Toast.LENGTH_LONG);
                finish();
            }
        }
    }

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

    public void checkIdentity() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Password required", "please enter your pattern to receive your token");
        if (credentialsIntent != null) {
            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
        }
    }

    public int getDimension(){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int dimension = size.x >= size.y ? size.y : size.x;
        return dimension;
    }
}
