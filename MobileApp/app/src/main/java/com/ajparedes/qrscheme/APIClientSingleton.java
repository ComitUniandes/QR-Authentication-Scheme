package com.ajparedes.qrscheme;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIClientSingleton {
    private static APIClientSingleton clientInstance;
    private RequestQueue requestQueue;

    private APIClientSingleton (Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized APIClientSingleton getInstance(Context context){
        if(clientInstance==null){
            clientInstance = new APIClientSingleton(context);
        }
        return clientInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
