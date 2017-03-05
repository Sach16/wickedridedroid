package com.wickedride.app.activities;

import android.os.Bundle;

import com.wickedride.app.utils.Constants;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class YourRidesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
       if(data != null && data.getString(Constants.CATEGORY) != null){
           loadYourRides(data.getString(Constants.CATEGORY));
       }else {
           loadYourRides(Constants.RENT_RIDE);
       }
    }
}
