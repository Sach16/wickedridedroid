package com.wickedride.app.activities;

import android.os.Bundle;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class ReserveActivity extends BaseNoActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBikeReserveFragment(getIntent().getExtras());
    }
}
