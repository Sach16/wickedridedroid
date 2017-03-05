package com.wickedride.app.activities;

import android.os.Bundle;

import com.wickedride.app.utils.Constants;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProfileFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.IS_PROFILE_CHANGED){
            loadProfileFragment();
        }
    }
}
