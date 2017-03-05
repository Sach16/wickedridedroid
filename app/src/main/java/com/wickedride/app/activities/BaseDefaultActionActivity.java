package com.wickedride.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.wickedride.app.fragments.BaseFragment;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.PageManager;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.Constants;

import java.io.File;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BaseDefaultActionActivity extends ActionBarActivity implements ServerCallback{

    protected PageManager pageManager = new PageManager(this,
            Constants.MAX_PAGE_BUFFER);
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

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

    @Override
    public void onBackPressed() {
        if (!pageManager.onBackPressed())
            super.onBackPressed();
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    public void setTagToPreviousState(Object tag) {
        pageManager.setTagToPreviousState(tag);
    }

    public Object getTagFromPreviousState() {
        return pageManager.getTagFromPreviousState();
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
    public void placeMultiPartRequest(String methodName, Class clazz, HashMap<String, String> params, File file, String fileKey) {
        RequestManager.getInstance(this).placeMultiPartRequest(methodName, clazz, this, params, file, fileKey);
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
}
