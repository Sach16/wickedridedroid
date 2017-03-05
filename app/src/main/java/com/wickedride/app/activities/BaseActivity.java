package com.wickedride.app.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.facebook.appevents.AppEventsLogger;
import com.wickedride.app.R;
import com.wickedride.app.fragments.BaseFragment;
import com.wickedride.app.fragments.NavigationDrawerFragment;
import com.wickedride.app.fragments.ProfileFragment;
import com.wickedride.app.fragments.WebFragment;
import com.wickedride.app.fragments.YourBikationFragment;
import com.wickedride.app.fragments.YourRidesFragment;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.PageManager;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.Constants;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BaseActivity extends ActionBarActivity implements ServerCallback {

    public static String PACKAGE_NAME;
    protected PageManager pageManager = new PageManager(this,
            Constants.MAX_PAGE_BUFFER);
    @InjectView(R.id.toolbar)
    Toolbar mToolBar;
    @InjectView(R.id.shadow)
    FrameLayout mShadow;
    @InjectView(R.id.action_title)
    TextView mTextActionTitle;
    @InjectView(R.id.action_btn)
    ImageView mActionImage;
    NavigationDrawerFragment mNavigationDrawerFragment;
    private Toast mToast;
    private Typeface typefaceTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        ButterKnife.inject(this);
        typefaceTitle = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Regular.otf");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("");
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolBar);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @OnClick({R.id.action_btn})
    public void OnClickListener(View view) {
        if (view.getId() == R.id.action_btn) {
            String tagName = getResources().getResourceName((Integer) mActionImage.getTag());
            if (tagName.contains("forward_arrow")) {
                unLoadPickYourRide();
                setActionBarImage(R.drawable.settings_icon);
            } else if (tagName.contains("settings_icon")) {
                loadPickYourRide();
            } else if (tagName.contains("pencil")) {
                openEditProfileActivity();
            }
        }
    }

    private void openEditProfileActivity() {
        Intent openEditProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(openEditProfile);
    }

    public void hideActionBarImage() {
        mActionImage.setVisibility(View.INVISIBLE);
    }

    public void showActionBarImage() {
        mActionImage.setVisibility(View.VISIBLE);
    }

    public void hideActionBarTitle() {
        mTextActionTitle.setVisibility(View.GONE);
    }

    public void showActionBarTitle() {
        mToolBar.setVisibility(View.VISIBLE);
        mTextActionTitle.setVisibility(View.VISIBLE);
    }

    public void setActionBarTitle(int action_text) {
        mTextActionTitle.setText(getString(action_text));
        mTextActionTitle.setTypeface(typefaceTitle);
    }

    public void showShadow() {
        mShadow.setVisibility(View.VISIBLE);
    }

    public void hideShadow() {
        mShadow.setVisibility(View.GONE);
    }

    public void setActionBarImage(int action_image) {
        mActionImage.setImageResource(action_image);
        mActionImage.setTag(action_image);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Measure the StatusBar Height
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*
     * Method called to save a tag Object to the previous Fragment in the stack.
	 * This is used to communicate to the previous stack
	 *
	 * @param tag An Object saved to the previous fragment in the stack
	 */
    public void setTagToPreviousState(Object tag) {
        pageManager.setTagToPreviousState(tag);
    }

    /**
     * This method is called when the fragment that we want to add as the next
     * in the stack
     *
     * @param fragment
     */
    public void showNextScreen(BaseFragment fragment) {
        pageManager.pushScreen(fragment);

    }

    /*
     * This method is called when the fragment that we want to add has to be
     * added as the root fragment, thus clearing the stack
     *
     * @param fragment
     */
    public void showAsRootScreen(BaseFragment fragment) {
        pageManager.pushAsRootScreen(fragment);
    }

    /**
     * @return of type
     * @author Hari K J
     * @since Dec 6, 2013
     */
    public Object getTagFromPreviousState() {
        return pageManager.getTagFromPreviousState();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment != null && mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    public String getActivityOnTop() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("BaseActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        return taskInfo.get(0).topActivity.getClassName();
    }

    /**
     * Method to show the Toast message
     *
     * @param stringID
     */
    public void showToast(int stringID) {
        String str = getString(stringID);
        mToast.setText(str);
        mToast.show();
    }

    /**
     * Method to show the Toast message
     *
     * @param str
     */
    public void showToast(String str) {
        mToast.setText(str);
        mToast.show();
    }

    public void loadPickYourRide() {
        Intent openPickyourRIde = new Intent(getApplicationContext(), PickYourRideActivity.class);
        startActivityForResult(openPickyourRIde, Constants.FILTER_REQUEST_CODE);

//        setActionBarTitle(R.string.pick_your_ride);
//        setActionBarImage(R.drawable.forward_arrow);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right);
//        PickYourRideFragment pickYourRideFragment = new PickYourRideFragment();
//        ft.add(R.id.frame_container, pickYourRideFragment, "pickYourRideFragment");
//        ft.commit();
    }

    public void unLoadPickYourRide() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right);
        ft.remove(getSupportFragmentManager().findFragmentByTag("pickYourRideFragment"));
        ft.commit();
    }

    public void loadProfileFragment() {
        mToolBar.setVisibility(View.VISIBLE);
        setActionBarTitle(R.string.profile);
        showActionBarTitle();
        showActionBarImage();

        setActionBarImage(R.drawable.pencil);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        ProfileFragment profileFragment = new ProfileFragment();
        ft.replace(R.id.frame_container, profileFragment, "profileFragment");
        //ft.addToBackStack("profileFragment");
        ft.commit();
    }


    public boolean isFragmentVisible(String fragmentTag) {
        YourRidesFragment yourRidesFragment = (YourRidesFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (yourRidesFragment != null) {
            return yourRidesFragment.isVisible();
        }else {
            if(fragmentTag.equals("yourRidesFragment")){
                loadYourRides(Constants.RENT_RIDE);
            }else{
                loadYourRides(Constants.BIKATION);
            }
            return true;
        }
    }


    public void hideToolBar(){
        mToolBar.setVisibility(View.GONE);
    }

    public void loadYourRides(String category) {
        // remove if part when BIkation is available

        if (Constants.BIKATION.equals(category)){

            mToolBar.setVisibility(View.GONE);
            hideActionBarTitle();
            hideActionBarImage();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            YourBikationFragment yourBikeFragment = new YourBikationFragment();
            ft.replace(R.id.frame_container, yourBikeFragment, "bikationFragmentTest");
            ft.commit();
        } else {
            showActionBarTitle();
            showActionBarImage();
            setActionBarTitle(R.string.your_rides);
            setActionBarImage(R.drawable.settings_icon);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            YourRidesFragment yourRidesFragment = new YourRidesFragment();
            Bundle bundle = new Bundle();
            // uncomment if part when BIkation is available
            /*if (Constants.BIKATION.equals(category)) {
                bundle.putString(Constants.CATEGORY, Constants.BIKATION);
                ft.add(R.id.frame_container, yourRidesFragment, "bikationFragment");
            } else {*/
                bundle.putString(Constants.CATEGORY, Constants.RENT_RIDE);
                ft.replace(R.id.frame_container, yourRidesFragment, "yourRidesFragment");
           // }
            yourRidesFragment.setArguments(bundle);
            ft.commit();
        }
    }

    public void loadWebFragment(String url) {
        showActionBarTitle();
        hideActionBarImage();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        WebFragment webFragment = new WebFragment();
        ft.replace(R.id.frame_container, webFragment, "webFragment");
        ft.addToBackStack("webFragment");
        Bundle bundle = new Bundle();
        bundle.putString(Constants.WEB_URL, url);
        webFragment.setArguments(bundle);
        ft.commit();
    }

    /**
     * Place GET API requet with params
     *
     * @param methodName
     * @param clazz
     * @param params
     */
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params) {
        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, params, false);
    }

    /**
     * Place simple GET API Request
     *
     * @param methodName
     * @param clazz
     */
    public void placeRequest(String methodName, Class clazz) {
        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, null, false);
    }


    /**
     * Place API request with isPOST boolean param
     *
     * @param methodName
     * @param clazz
     * @param params
     * @param isPOST
     */
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params, boolean isPOST) {
        //  if (!methodName.equals(APIMethods.CHECK_USERNAME))

        // showProgressDialog();
        RequestManager.getInstance(this).placeRequest(methodName, clazz, this, params, isPOST);
    }

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        if(error instanceof NoConnectionError){
            showToast("No Internet Connection.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.FILTER_REQUEST_CODE) {
            Intent intent = new Intent(Constants.ON_RESULT_CHANGED);
            // You can also include some extra data.
            intent.putExtra(Constants.ON_RESULT_CHANGED, Constants.YOUR_RIDES_FRAGMENT);
            intent.putExtra(Constants.FILTER, Constants.FILTER);
            intent.putExtra(Constants.AREA_ID, data.getStringExtra(Constants.AREA_ID));
            intent.putExtra(Constants.MAKE_ID, data.getStringExtra(Constants.MAKE_ID));
            intent.putExtra(Constants.START_DATE, data.getStringExtra(Constants.START_DATE));
            intent.putExtra(Constants.DROP_DATE, data.getStringExtra(Constants.DROP_DATE));
            intent.putExtra(Constants.START_TIME, data.getStringExtra(Constants.START_TIME));
            intent.putExtra(Constants.DROP_TIME, data.getStringExtra(Constants.DROP_TIME));
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public void openDrwer() {
        mNavigationDrawerFragment.openDrawer();
    }

    public void closeDrawer() {
        mNavigationDrawerFragment.closeDrawer();
    }

   public boolean isDrawerOpen() {
        if (mNavigationDrawerFragment.isDrawerOpen()){
            return true;
        }
        else{
            return false;
        }

    }

}
