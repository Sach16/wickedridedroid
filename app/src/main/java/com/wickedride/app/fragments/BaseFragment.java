/**
 * @author Bala K J
 */
package com.wickedride.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.inkoniq.parallaxviewpager.CanScrollVerticallyDelegate;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;
import com.wickedride.app.activities.BaseDefaultActionActivity;
import com.wickedride.app.dialog.IQProgressDialog;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.StateHolder;
import com.wickedride.app.models.FeedParams;
import com.wickedride.app.networking.RequestManager;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * @author Bala K J
 */
public abstract class BaseFragment extends Fragment implements ServerCallback, CanScrollVerticallyDelegate {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    protected String LOG_TAG;
    protected Context fragmentContext;
    protected StateHolder stateHolder;
    protected View mView;

    protected boolean isNextPageLoading = false;
    protected boolean isNextPageLoadable = false;
    protected int PAGECOUNT = 30;
    protected int currentPageCount = 1;
    protected IQProgressDialog mPrgressDialog;
    private View moreDataLayout;
    private Object startPageCount;
    private Toast mToast;
    private View mProgressView;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG += this.getClass().getSimpleName().toString() + " : ";
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

    }

    public abstract String getSelfTag();

    public abstract CharSequence getTitle(Resources r);

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO GA Tracking.
        //this.tracker.set(Fields.SCREEN_NAME, getClass().getSimpleName());
        //this.tracker.send(MapBuilder.createAppView().build());
    }


    /*
     * Method to make the server call
     * @param methodname String value which defines the method to be called in the api
     * @param params Hashmap which contains the params to be passed to the backend
     */
    // Server Calls
    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params) {
        RequestManager.getInstance(getActivity()).placeRequest(methodName, clazz, this, params, false);
    }

    public void placeRequest(String methodName, Class clazz) {
        RequestManager.getInstance(getActivity()).placeRequest(methodName, clazz, this, null, false);
    }

    public void placeRequest(String methodName, Class clazz, HashMap<String, String> params, boolean isPOST) {
        RequestManager.getInstance(getActivity()).placeRequest(methodName, clazz, this, params, isPOST);
    }

    public void placeRequest(String methodName, Class clazz, boolean isPost ,JSONObject jsonObject) {
        RequestManager.getInstance(getActivity()).placeRequest(methodName, clazz, this, isPost,jsonObject);
    }

    public void placeMultiPartRequest(String methodName, Class clazz, HashMap<String, String> params, File file, String fileKey) {
        RequestManager.getInstance(getActivity()).placeMultiPartRequest(methodName, clazz, this, params, file, fileKey);
    }

    public void placeRequest(String methodName, Class clazz, HashMap<String,String> params,boolean isPost,boolean toaddTokenToHeader, boolean isPut) {
        RequestManager.getInstance(getActivity()).placeRequest(methodName, clazz, this, params,isPost, toaddTokenToHeader, isPut);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        hideProgressDialog();
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        hideProgressDialog();
        if(error instanceof NoConnectionError){
            showToast("No Internet Connection.");
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.clipix.clipix.clipixserver.ClipixServer.ClipixServerCallback#complete
     * (int)
     */
    @Override
    public void complete(int code) {
        // TODO Auto-generated method stub

    }


    /**
     * Method to set the User Data for the Params while making the server calls
     *
     * @param params
     */
    public void setUserData(HashMap<String, String> params) {
        params.put(FeedParams.AUTHKEY, SessionManager.getSessionId(getActivity()));
    }

    /**
     * Had to be overridden by the Fragments who wants to save their states
     */
    public StateHolder onSaveViewSettings() {
        StateHolder stateHolder = getStateHolder();
        if (stateHolder == null) {
            stateHolder = new StateHolder(getClass().getName(), getArguments());
            setStateHolder(stateHolder);
        }
        return stateHolder;
    }

    public void showNextScreen(BaseFragment fragment) {
        ((BaseActivity) getActivity()).showNextScreen(fragment);
    }

    public void showDefaultNextScreen(BaseFragment fragment) {
        ((BaseDefaultActionActivity) getActivity()).showNextScreen(fragment);
    }

    /*
     * This method is called when the fragment that we want to add has to be
     * added as the root fragment, thus clearing the stack
     *
     * @param fragment
     */
    public void showAsRootScreen(BaseFragment fragment) {
        ((BaseActivity) getActivity()).showAsRootScreen(fragment);
    }

    public void showAsDefaultRootScreen(BaseFragment fragment) {
        ((BaseDefaultActionActivity) getActivity()).showAsRootScreen(fragment);
    }

    public void freeFromLastParent() {
        if (mView != null) {
            ViewGroup v = ((ViewGroup) mView.getParent());
            if (v != null) {
                v.removeAllViews();
            }
        }

        // commented as it is making the fragment lose its properties
        // setStateHolder(null);
    }

    /**
     * Should return true if the fragment has to animate in trasition while
     * appearing or exiting
     *
     * @return
     */
    public boolean shouldAnimate() {
        return false;
    }

    /**
     * keeps updating the state of the view like scrolling position, button
     * clicked etc
     *
     * @return
     */
    public StateHolder getStateHolder() {
        return stateHolder;
    }

    /*
     * Method to set the StateHolder
     */
    public void setStateHolder(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    /*
     * Method to get the context in the fragment
     *
     * @return Context
     */
    public Context getFragmentContext() {
        return fragmentContext;
    }

    /*
     * Method to set the Context value which will be accessed in the Fragment
     */
    public void setFragmentContext(Context fragmentContext) {
        this.fragmentContext = fragmentContext;
    }

    /*
     * Method called to check if the onBackPressed has been overridden in the
     * fragments
     */
    public boolean onBackPressed() {
        return false;
    }

    public void resetToReload() {
        mView = null;
        getStateHolder().setTag(null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void loadData() {
    }

    public void showNonModalProgress() {
        if (mProgressView != null) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                mProgressView = inflater.inflate(R.layout.layout_progressonly, (ViewGroup) mView);

            }
        }
    }


    private void showLoadingLabel(int duration) {

        moreDataLayout.setVisibility(View.VISIBLE);
        int width = moreDataLayout.getWidth();
        Animation animation = new TranslateAnimation(Animation.ABSOLUTE, width, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        moreDataLayout.setAnimation(animation);
        moreDataLayout.startAnimation(animation);

    }

    protected void hideLoadingLabel(int duration) {
        int width = moreDataLayout.getWidth();
        Animation animation = new TranslateAnimation(Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, width, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        moreDataLayout.setAnimation(animation);
        moreDataLayout.startAnimation(animation);

    }

    /**
     * Method to show the progress dialog
     */
    protected void showProgressDialog() {
        hideProgressDialog();
        mPrgressDialog = new IQProgressDialog();
        mPrgressDialog.init(getActivity(), getString(R.string.loader_message));
    }

    /**
     * Method to show the progress dialog
     *
     * @param stringid
     */
    protected void showProgressDialog(int stringid) {
        hideProgressDialog();
        mPrgressDialog = new IQProgressDialog();
        mPrgressDialog.init(getActivity(), getString(stringid));
    }

    protected void hideProgressDialog() {
        if (mPrgressDialog != null && mPrgressDialog.isShowing())
            mPrgressDialog.dismiss();

        if (mProgressView != null) {
            ((ViewGroup) mView).removeView(mProgressView);
        }
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


    /**
     * Method called when the user login state gets updated
     */
    public void updateUserLoggedInState() {

    }
}
