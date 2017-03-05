package com.wickedride.app.networking;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class WRRequestQueue {

    private static WRRequestQueue mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private WRRequestQueue(Context context) {
        this.mContext = context;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized WRRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WRRequestQueue(context);
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
    }

}
