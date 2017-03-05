package com.wickedride.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.fragments.AccessoriesFragment;
import com.wickedride.app.fragments.BaseFragment;
import com.wickedride.app.fragments.ChangePasswordFragment;
import com.wickedride.app.fragments.EditProfileFragment;
import com.wickedride.app.fragments.WebFragment;
import com.wickedride.app.fragments.HappyRidingFragment;
import com.wickedride.app.fragments.ReserveFragment;
import com.wickedride.app.fragments.SummaryFragment;
import com.wickedride.app.fragments.TermsAndConditionFragment;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.PageManager;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.Constants;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BaseNoActionBarActivity extends ActionBarActivity implements ServerCallback{

    protected PageManager pageManager = new PageManager(this,
            Constants.MAX_PAGE_BUFFER);
    @InjectView(R.id.back_button)
    ImageView mBackButton;
    private Toast mToast;
    public final int CROP_IMAGE = 10005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_actionbar);
        ButterKnife.inject(this);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    public void setBlackBackButton() {
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setImageResource(R.drawable.arrow_black_left);
    }

    public void setWhiteBackButton() {
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setImageResource(R.drawable.arrow_white_left);
    }

    public void hideBackButton() {
        mBackButton.setVisibility(View.GONE);
    }

    public void showBackButton() {
        mBackButton.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back_button})
    public void OnClickListener(View view) {
        if (view.getId() == R.id.back_button) {
            onBackPressed();
        }
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

    public void loadBikeReserveFragment(Bundle data) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        ReserveFragment reserveFragment = new ReserveFragment();
        reserveFragment.setArguments(data);
        ft.add(R.id.frame_container, reserveFragment, "reserveFragment");
        ft.addToBackStack("reserveFragment");
        ft.commit();
    }

    public void loadAccessoriesFragment(Bundle data) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        AccessoriesFragment accessoriesFragment = new AccessoriesFragment();
        accessoriesFragment.setArguments(data);
        //ft.add(R.id.frame_container, accessoriesFragment, "accessoriesFragment");
        ft.replace(R.id.frame_container, accessoriesFragment, "accessoriesFragment");
        ft.addToBackStack("accessoriesFragment");
        ft.commit();
    }

    public void loadSummaryFragment(Bundle data) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        SummaryFragment summaryFragment = new SummaryFragment();
        summaryFragment.setArguments(data);
       // ft.add(R.id.frame_container, summaryFragment, "summaryFragment");
        ft.replace(R.id.frame_container, summaryFragment, "summaryFragment");
        ft.addToBackStack("summaryFragment");
        ft.commit();
    }


    public void loadTermsAndConditionFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        TermsAndConditionFragment termsAndConditionFragment = new TermsAndConditionFragment();
        ft.add(R.id.frame_container, termsAndConditionFragment, "termsAndConditionFragment");
        ft.addToBackStack("termsAndConditionFragment");
        ft.commit();
    }

    public void loadHappyRidingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        HappyRidingFragment happyRidingFragment = new HappyRidingFragment();
        ft.add(R.id.frame_container, happyRidingFragment, "happyRidingFragment");
//        ft.addToBackStack("happyRidingFragment");
        ft.commit();
    }

    public void loadEditProfileFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        ft.add(R.id.frame_container, editProfileFragment, "editProfileFragment");
        //ft.addToBackStack("editProfileFragment");
        ft.commit();
    }

    public void loadWebFragment(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        WebFragment webFragment = new WebFragment();
        ft.add(R.id.frame_container, webFragment, "webFragment");
        ft.addToBackStack("webFragment");
        Bundle bundle = new Bundle();
            bundle.putString(Constants.WEB_URL, url);
        webFragment.setArguments(bundle);
        ft.commit();
    }

    public void loadChangePasswordFregment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        ft.replace(R.id.frame_container, changePasswordFragment, "changePasswordFragment");
       // ft.addToBackStack("webFragment");
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


}
