package com.wickedride.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wickedride.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */

//to enable bikation extend from BaseActivity

public class BikationActivity extends Activity {

    @InjectView(R.id.back_button)
    ImageView mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikation);
        ButterKnife.inject(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //to enable bikation uncomment below and comment setContentView and ButterKnife
//       loadYourRides(Constants.BIKATION);
    }

    @OnClick(R.id.back_button)
    public void onClickListener(View view) {
        onBackPressed();
    }
}
