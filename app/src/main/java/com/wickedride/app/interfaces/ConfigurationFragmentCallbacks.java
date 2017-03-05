package com.wickedride.app.interfaces;

import android.content.Intent;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public interface ConfigurationFragmentCallbacks {
    void onFrictionChanged(float friction);

    void openDialog(float friction);

    void openActivity(Intent intent);
}
