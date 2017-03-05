package com.wickedride.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.GetResultData;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.RippleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class SplashActivity extends Activity implements MediaPlayer.OnPreparedListener, ServerCallback {

    @InjectView(R.id.splash_video)
    VideoView mVideoView;

    @InjectView(R.id.btn_bikation)
    RippleButton mBtnBikation;

    @InjectView(R.id.btn_rent)
    RippleButton mBtnRent;

    private Vibrator vibe;
    private MediaPlayer mp;
    private boolean isBikationClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(this, R.raw.motorcycle_start_wickedride);

//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                if(isBikationClicked){
//                    openBikationActivity();
//                }else{
//                    openRideActivity();
//                }
//            }
//        });

        if(SessionManager.getAboutUrl(this) == null && SessionManager.getReviewUrl(this) == null && SessionManager.getContactUrl(this) == null && SessionManager.getTariffUrl(this) == null && SessionManager.getFaqUrl(this) == null) {
            RequestManager.getInstance(this).placeRequest(APIMethods.GET_URLS, GetResultData.class, this, null, false);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String deviceName=getDeviceName();
        int buildVersion=android.os.Build.VERSION.SDK_INT;
        if (deviceName.equalsIgnoreCase("MicromaxA210") && buildVersion==17) {
            Toast.makeText(getApplicationContext(),"Device could not play the video.",Toast.LENGTH_SHORT).show();
        }
        else{
            String uri = "android.resource://" + getPackageName() + "/" + R.raw.video1;
            Uri videoUri = Uri.parse(uri);
            mVideoView.setVideoURI(videoUri);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            mVideoView.setLayoutParams(params);
            mVideoView.requestFocus();
            mVideoView.seekTo(0);
            mVideoView.start();
            mVideoView.setOnPreparedListener(this);
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @OnClick({R.id.btn_bikation, R.id.btn_rent})
    public void onClickListener(View view) {

        switch (view.getId()) {
            case R.id.btn_bikation:
//                mp.start();
                isBikationClicked = true;
                vibe.vibrate(100);
                openBikationActivity();
                break;
            case R.id.btn_rent:
//                mp.start();
                vibe.vibrate(100);
                isBikationClicked = false;
                openRideActivity();
                break;
        }
    }

    @OnLongClick(R.id.btn_rent)
    public boolean onItemLongClickListener(View view) {
        openSignInActivity();
        return true;
    }

    private void openSignInActivity() {
        Intent openSignInActivity = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(openSignInActivity);
    }

    private void openRideActivity() {
        if (SessionManager.getUserCity(getApplicationContext()) != null) {
            Intent openRideActivity = new Intent(SplashActivity.this, YourRidesActivity.class);
            openRideActivity.putExtra(Constants.CATEGORY, Constants.RENT_RIDE);
            startActivity(openRideActivity);
        } else {
            Intent openCityActivity = new Intent(SplashActivity.this, CityActivity.class);
            startActivity(openCityActivity);
        }

    }

    private void openBikationActivity() {
        //To enable bikation invoke YourRidesActivity.class
        Intent openBikationActivity = new Intent(SplashActivity.this, YourRidesActivity.class);
        openBikationActivity.putExtra(Constants.CATEGORY, Constants.BIKATION);
        startActivity(openBikationActivity);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        GetResultData result = (GetResultData) response;
        if(result != null){
            SessionManager.setAboutUrl(this, result.getResult().getData().getAbout());
            SessionManager.setReviewUrl(this, result.getResult().getData().getReview());
            SessionManager.setContactUrl(this, result.getResult().getData().getContact());
            SessionManager.setTariffUrl(this, result.getResult().getData().getTariff());
            SessionManager.setFaq(this, result.getResult().getData().getFaq());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + "" + model;
        }
    }
}
