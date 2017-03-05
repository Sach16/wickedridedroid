package com.wickedride.app.activities;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class EditProfileActivity extends BaseNoActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBackButton();
           loadEditProfileFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
