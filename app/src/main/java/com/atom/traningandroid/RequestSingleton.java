package com.atom.traningandroid;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestSingleton {
    private static RequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    public static synchronized RequestSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestSingleton(context);
        }
        return instance;
    }

    private RequestSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        //retry policy

        getRequestQueue().add(req);
    }
}
