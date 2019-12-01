package com.ajparedes.qrscheme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button btn_login;
    private ProgressBar progressBar;
    private TextView failed;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.loading);
        btn_login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        failed = findViewById(R.id.textFailed);

        requestQueue = APIClientSingleton.getInstance(this).getRequestQueue();

        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){

        try{
            beforeRequest();
            requestLogin();

        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }

    }

    public void beforeRequest(){
        progressBar.setVisibility(View.VISIBLE);
        btn_login.setEnabled(false);
        failed.setVisibility(View.INVISIBLE);
    }

    public void afterRequest(String message){
        Log.d("LoginActivity","response login1: probando si der verdad lo hace despues");
        progressBar.setVisibility(View.INVISIBLE);
        btn_login.setEnabled(true);
        failed.setText(message);
        failed.setVisibility(View.VISIBLE);
    }

    public void requestLogin(){
        final String username = this.username.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String device = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        final String URL_LOGIN = getString(R.string.base_url) + getString(R.string.login_url);

        JSONObject js = new JSONObject();
        try {
            js.put("username", username);
            js.put("password", password);
            js.put("idDevice", device);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_LOGIN, js,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String responseMessage = response.getString("response");
                            Log.d("AuthClient", "response string: "+responseMessage);
                            if(responseMessage.equals("Login Successful")){

                                Toast.makeText(getApplicationContext(), responseMessage, Toast.LENGTH_SHORT);
                                //TODO cerrar actividad y mandar a la main
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("username",username);
                                returnIntent.putExtra("device", device);
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            afterRequest(responseMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("AuthClient", "volley error: "+error);
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
}
