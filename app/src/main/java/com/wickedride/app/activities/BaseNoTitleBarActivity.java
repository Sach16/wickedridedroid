package com.wickedride.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.WickedRide;
import com.wickedride.app.dialog.IQProgressDialog;
import com.wickedride.app.dialog.IQTextMessageDialog;
import com.wickedride.app.fragments.BaseFragment;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.PageManager;
import com.wickedride.app.models.ResponseError;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.Constants;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BaseNoTitleBarActivity extends FragmentActivity implements OnClickListener, ServerCallback {

    protected ListFragment mFrag;
    protected PageManager pageManager = new PageManager(this,
            Constants.MAX_PAGE_BUFFER);
    protected Toast mToast;
    protected String LOG_TAG = "WickedRide : ";
    protected WickedRide appObj;
    private IQProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appObj = (WickedRide) getApplication();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance(this).activityStop(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.actionbarsherlock.app.SherlockFragmentActivity#onConfigurationChanged
     * (android.content.res.Configuration)
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (pageManager != null) {
            pageManager.onResume();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pageManager != null) {
            if (pageManager.hasFragments()) {
                pageManager.getMostRecent().onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!pageManager.onBackPressed()) {
            super.onBackPressed();
        }

    }


    /*
     * Method called to return back to the Base Screen of the activity
     */
    protected void showRootScreenInActivity() {

    }

    /**
     * Method called to log the user out
     */
    public void logoutUser() {
//		Intent intent = new Intent(this, LoginOptionsActivity.class);
//		intent.putExtra(IntentConstants.LOGOUT_USER, true);
//		intent.putExtra(IntentConstants.SESSION_TYPE, SessionManager.getUserSessionType(this));
//		SessionManager.clearUserSession(this);
//		UserProfile.getinstance().clearAllData();
//		//appObj.unregisterTheDevice();		
//		startActivity(intent);		
//		finish();
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

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

    }

    /*
     * Method to show the Toast Message
     *
     * @param message String value of the message to be shown in the toast
     */
    public void showToast(String message) {
        try {
            mToast.setText(message);
            mToast.show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Toast Error");
        }
        ;
    }

    // Server Calls
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params) {
        RequestManager.getInstance(getApplicationContext()).placeRequest(methodName, clazz, this, params, false);
    }

    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params, boolean isPost) {
        RequestManager.getInstance(getApplicationContext()).placeRequest(methodName, clazz, this, params, isPost);
    }

    public void placeRequest(String methodName, Class clazz) {
        RequestManager.getInstance(getApplicationContext()).placeRequest(methodName, clazz, this, null, false);
    }


    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        hideProgressDialog();
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        hideProgressDialog();
        if (error != null && error.getClass().equals(ResponseError.class)) {
            //Get the error message and show it to the user
            IQTextMessageDialog dialog = new IQTextMessageDialog();
            dialog.init(((ResponseError) error).getErrorMessage());
            dialog.show(getSupportFragmentManager(), "");
        }

        if(error instanceof NoConnectionError){
            showToast("No Internet Connection.");
        }
    }


    /**
     * @author Bala K J
     */
    public Object getTagFromPreviousState() {
        return pageManager.getTagFromPreviousState();
    }

    @Override
    public void complete(int code) {
        // TODO Auto-generated method stub

    }

    /**
     * Method to show the progress dialog
     */
    protected void showProgressDialog() {
        hideProgressDialog();
        mDialog = new IQProgressDialog();
        mDialog.init(this, getString(R.string.loader_message));
    }

    /**
     * Method to show the progress dialog
     *
     * @param stringid
     */
    protected void showProgressDialog(int stringid) {
        hideProgressDialog();
        mDialog = new IQProgressDialog();
        mDialog.init(this, getString(stringid));
    }

    protected void hideProgressDialog() {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
