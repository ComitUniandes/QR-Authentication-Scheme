package com.ajparedes.qrscheme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

public class MainActivity extends AppCompatActivity {
    private static final int LOGIN_RESULT = 1;
    private static final int CREDENTIALS_RESULT = 2;

    private String username;
    private String device;
    private Button btn_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hawk.init(getApplicationContext()).build();
        boolean isLogged = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("logged", false);

        if(!isLogged){
            Intent i = new Intent( MainActivity.this, LoginActivity.class);
            startActivityForResult(i, 1);
        }
        else
            checkIdentity();

        setContentView(R.layout.activity_token);
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

    public void generateToken(String username, String device){

    }

    public void checkIdentity() {
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        Intent credentialsIntent = keyguardManager.createConfirmDeviceCredentialIntent("Password required", "please enter your pattern to receive your token");
        if (credentialsIntent != null) {
            startActivityForResult(credentialsIntent, CREDENTIALS_RESULT);
        }
    }
}
