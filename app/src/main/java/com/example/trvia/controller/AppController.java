package com.example.trvia.controller;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    private static AppController instance;
    private RequestQueue requestQueue;
    

    public RequestQueue getRequestQueue() {
        if(requestQueue == null)
            return Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }

    public static AppController getInstance(){
        return instance;
    }

    public <T> void addRequest(Request<T> request){
        getRequestQueue().add(request);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
