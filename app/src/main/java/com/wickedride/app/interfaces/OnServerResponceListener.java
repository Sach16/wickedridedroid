package com.wickedride.app.interfaces;

import org.json.JSONObject;

/**
 * Created by darko on 3.2.15.
 */


public interface OnServerResponceListener {
    public void onServerResponse(JSONObject response, int method);
}