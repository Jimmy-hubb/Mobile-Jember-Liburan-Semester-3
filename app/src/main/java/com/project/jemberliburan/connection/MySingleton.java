package com.project.jemberliburan.connection;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class untuk mengelola RequestQueue Volley.
 */
public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Mendapatkan instance MySingleton.
     * @param context Context aplikasi
     * @return Instance MySingleton
     */
    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    /**
     * Mendapatkan RequestQueue.
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // Gunakan getApplicationContext() untuk mencegah memory leaks
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Menambahkan request ke RequestQueue.
     * @param req Request yang akan ditambahkan
     * @param <T> Tipe data respons
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}