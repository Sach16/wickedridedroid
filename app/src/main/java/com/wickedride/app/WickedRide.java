package com.wickedride.app;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Inkoniq-Admin on 17-Aug-15.
 */
public class WickedRide extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WickedRide", "Application Started");
        FacebookSdk.sdkInitialize(getApplicationContext());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Avenir-Book.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
