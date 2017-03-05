package com.wickedride.app.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;
import com.wickedride.app.activities.SignInActivity;
import com.wickedride.app.interfaces.OnSpinnerEventsListener;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.City;
import com.wickedride.app.models.CityResult;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.CustomSpinner;
import com.wickedride.app.views.ScrimInsetsFrameLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class NavigationDrawerFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, OnSpinnerEventsListener {

    public static final int RESULT_CODE_SIGN_IN = 10010;
    public static String TAG = "NavigationDrawerFragment";
    View mView;
    @InjectView(R.id.imgAvatar)
    ImageView mImageAvtar;
    @InjectView(R.id.user_info_holder)
    LinearLayout mUserInfoHolder;
    @InjectView(R.id.tv_Username)
    TextView mUserName;
    @InjectView(R.id.tv_UserEmail)
    TextView mUserEmail;
    @InjectView(R.id.tv_rent_bike)
    TextView mRentABike;
    @InjectView(R.id.tv_bikation)
    TextView mBikation;
    @InjectView(R.id.tv_review)
    TextView mReview;
    @InjectView(R.id.tv_tariff)
    TextView mTariff;
    @InjectView(R.id.tv_faq)
    TextView mFAQ;
    @InjectView(R.id.tv_contactus)
    TextView mContactUs;
    @InjectView(R.id.tv_about)
    TextView mAbout;
    @InjectView(R.id.tv_logout)
    TextView mLogout;
    @InjectView(R.id.spinner_cities)
    CustomSpinner mSpinnerCities;
    @InjectView(R.id.separator)
    View mSeparator;
    @InjectView(R.id.nav_bottom_Lyt)
    LinearLayout mNavBottomLyt;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearnedDrawer = true;
    private boolean hasStarted;
    private ArrayList<String> mCityList;
    private ArrayList<City> mCities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_navigation_google, container, false);
        ButterKnife.inject(this, mView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setUpSpinner();
        checkUserLogin();
        return mView;
    }

    private void checkUserLogin() {
        if(SessionManager.isUserSessionValid(getActivity())) {
            Log.d(TAG,"FirstName::" + SessionManager.getUserFirstName(getActivity()));
            Log.d(TAG,"LastName::" + SessionManager.getUserLastName(getActivity()));
            mUserName.setText(SessionManager.getUserFirstName(getActivity()));
            mUserEmail.setVisibility(View.VISIBLE);
            mImageAvtar.setVisibility(View.VISIBLE);
            mLogout.setVisibility(View.VISIBLE);
            if(SessionManager.getUserSessionType(getActivity())==Constants.FB_SESSION){
                Picasso.with(getActivity()).load(getFacebookProfilePicUrl(SessionManager.getUserImageUrl(getActivity()))).placeholder(R.drawable.user).into(mImageAvtar);
            }
            if(!SessionManager.getUserImageUrl(getActivity()).isEmpty()) {
                Picasso.with(getActivity()).load(SessionManager.getUserImageUrl(getActivity())).placeholder(R.drawable.user).into(mImageAvtar);
            }
            mUserEmail.setText(SessionManager.getUserEmail(getActivity()));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mUserInfoHolder.getLayoutParams();
            //int leftMargin = (int) getResources().getDimension(R.dimen.after_login_margin);
            int topMargin = (int) getResources().getDimension(R.dimen.top_margin);
            params.setMargins(0, topMargin, 0, 0);
            mUserInfoHolder.setLayoutParams(params);
        }
        else{
            mLogout.setVisibility(View.GONE);
            mImageAvtar.setVisibility(View.INVISIBLE);

        }
    }

    public static Bitmap getFacebookProfilePicture(String userID) {
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setUpSpinner() {
        if(SessionManager.getAllCities(getActivity()) != null) {
            CityResult cityResult = new Gson().fromJson(SessionManager.getAllCities(getActivity()), CityResult.class);
            mCities = cityResult.getResult().getData();
            mCityList = new ArrayList<>();
            for (int i = 0; i < cityResult.getResult().getData().size(); i++) {
                mCityList.add(cityResult.getResult().getData().get(i).getCity());
            }
            ArrayAdapter<String> spinnerCitiesArrayAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.simple_spinner_item_profile, mCityList);
            mSpinnerCities.setAdapter(spinnerCitiesArrayAdapter);
            mSpinnerCities.setOnItemSelectedListener(this);
            mSpinnerCities.setSpinnerEventsListener(this);
        }
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    @OnClick({R.id.tv_rent_bike, R.id.tv_bikation, R.id.tv_contactus,
            R.id.tv_faq, R.id.tv_review, R.id.tv_about, R.id.tv_tariff, R.id.tv_Username,
            R.id.tv_UserEmail, R.id.imgAvatar,R.id.tv_logout})
    public void OnClickListener(View view) {
        closeDrawer();
        switch (view.getId()) {
            case R.id.tv_rent_bike:
                mRentABike.setSelected(true);
                loadRentABike();
                break;
            case R.id.tv_bikation:
                mBikation.setSelected(true);
                loadBikation();
                break;
            case R.id.tv_tariff:
                mTariff.setSelected(true);
                ((BaseActivity)getActivity()).loadWebFragment(SessionManager.getTariffUrl(getActivity()));
                ((BaseActivity)getActivity()).setActionBarTitle(R.string.tariff);
                break;
            case R.id.tv_contactus:
                ((BaseActivity)getActivity()).loadWebFragment(SessionManager.getContactUrl(getActivity()));
                ((BaseActivity)getActivity()).setActionBarTitle(R.string.contact_us);
                break;
            case R.id.tv_faq:
                loadFAQ();
                break;
//            case R.id.tv_news:
//                loadNews();
//                break;
            case R.id.tv_about:
                loadAbout();
                break;
//            case R.id.tv_settings:
//                loadSettings();
//                break;
            case R.id.tv_review:
                loadReview();
                break;
            case R.id.tv_logout:
                openLogoutPop();
                break;
            case R.id.tv_Username:
            case R.id.tv_UserEmail:
            case R.id.imgAvatar:
                if(SessionManager.isUserSessionValid(getActivity())) {
                    openProfileActivity();
                }else{
                    openLoginScreen();
                }
                break;
        }
    }

    private void openLogoutPop() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Logout");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        SessionManager.clearUserSession(getActivity());

                        getActivity().finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void openLoginScreen() {
        Intent openLoginActivity = new Intent(getActivity(), SignInActivity.class);
        startActivityForResult(openLoginActivity, RESULT_CODE_SIGN_IN);
    }

    private void openProfileActivity() {
        ((BaseActivity)getActivity()).loadProfileFragment();
        /*Intent openProfileActivity = new Intent(getActivity(), ProfileActivity.class);
        startActivity(openProfileActivity);*/
    }

    private void loadRentABike() {
//        if (!((BaseActivity) getActivity()).isFragmentVisible("yourRidesFragment")) {
            ((BaseActivity) getActivity()).loadYourRides(Constants.RENT_RIDE);
//        }
    }

    private void loadBikation() {
        Log.d("NavigationFragment", "Activity::" + ((BaseActivity) getActivity()).getActivityOnTop());
//        if(!((BaseActivity)getActivity()).isFragmentVisible("bikationFragment")) {
            ((BaseActivity) getActivity()).loadYourRides(Constants.BIKATION);
//        }
    }

    private void loadReview() {
        ((BaseActivity)getActivity()).loadWebFragment(SessionManager.getReviewUrl(getActivity()));
        ((BaseActivity)getActivity()).setActionBarTitle(R.string.review);
    }

    private void gallery() {
    }

    private void loadFAQ() {
        ((BaseActivity)getActivity()).loadWebFragment(SessionManager.getFaqUrl(getActivity()));
        ((BaseActivity)getActivity()).setActionBarTitle(R.string.faq);
    }

    private void loadNews() {
    }

    private void loadAbout() {
        ((BaseActivity)getActivity()).loadWebFragment(SessionManager.getAboutUrl(getActivity()));
        ((BaseActivity)getActivity()).setActionBarTitle(R.string.about);
    }

    private void loadSettings() {
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        if (mFragmentContainerView.getParent() instanceof ScrimInsetsFrameLayout) {
            mFragmentContainerView = (View) mFragmentContainerView.getParent();
        }
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.primaryDarkColor));
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hasStarted = false;
                if (!isAdded()) return;
                getActivity().invalidateOptionsMenu();
              //  ((BaseActivity) getActivity()).showActionBarImage();
                hideViews();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) return;
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (!hasStarted) {
                    if (Constants.IS_PROFILE_CHANGED){
                        Picasso.with(getActivity()).load(SessionManager.getUserImageUrl(getActivity())).placeholder(R.drawable.user).into(mImageAvtar);
                      Constants.IS_PROFILE_CHANGED=false;
                    }
                    checkUserLogin();
                    hasStarted = true;
                   // ((BaseActivity) getActivity()).hideActionBarImage();
                    animateViews(mRentABike, 0);
                    animateViews(mBikation, 50);
                    animateViews(mSeparator, 100);
                    animateViews(mReview, 150);
                    animateViews(mTariff, 200);
                    animateViews(mFAQ, 250);
                    animateViews(mAbout, 300);
                    animateViews(mContactUs, 350);
                    if(SessionManager.isUserSessionValid(getActivity())) {
                        animateViews(mLogout, 400);
                    }
                }
            }
        };

        if (!mUserLearnedDrawer)
            mDrawerLayout.openDrawer(mFragmentContainerView);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void hideViews() {
        mRentABike.setVisibility(View.GONE);
        mBikation.setVisibility(View.GONE);
        mSeparator.setVisibility(View.GONE);
        mReview.setVisibility(View.GONE);
        mTariff.setVisibility(View.GONE);
        mFAQ.setVisibility(View.GONE);
        mAbout.setVisibility(View.GONE);
        mContactUs.setVisibility(View.GONE);
    }

    private void animateViews(View view, int duration) {
        view.invalidate();
        view.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.slide_in_from_left);
        anim.setStartOffset(duration);
        view.startAnimation(anim);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    public void hideDrawer() {
        mDrawerLayout.setVisibility(View.GONE);

    }

    public void showDrawer() {
        mDrawerLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Changes the icon of the drawer to back
     */
    public void showBackButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Changes the icon of the drawer to menu
     */
    public void showDrawerButton() {
        if (getActivity() instanceof ActionBarActivity) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mActionBarDrawerToggle.syncState();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedView() != null) {
            ((TextView) parent.getSelectedView()).setPadding(0, 0, 0, 0);
            ((TextView) parent.getSelectedView()).setGravity(Gravity.LEFT);
        }
        City city = mCities.get(mSpinnerCities.getSelectedItemPosition());
        if(SessionManager.getUserCityID(getActivity()) != city.getId()) {
            Intent intent = new Intent(Constants.ON_RESULT_CHANGED);
            intent.putExtra(Constants.ON_RESULT_CHANGED, Constants.YOUR_RIDES_FRAGMENT);
//        intent.putExtra(Constants.IS_CITY_CHANGED, true);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        SessionManager.setUserCity(getActivity(), city.getCity(), city.getId());
        closeDrawer();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSpinnerOpened() {
        mSpinnerCities.setBackgroundResource(R.drawable.spinner_exp_bg_white);
    }

    @Override
    public void onSpinnerClosed() {
        mSpinnerCities.setBackgroundResource(R.drawable.spinner_bg_white);
    }

    public static String getFacebookProfilePicUrl(String userID) {
        return "https://graph.facebook.com/" + userID + "/picture?type=large";
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public String getSelfTag() {
        return null;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_CODE_SIGN_IN==resultCode){
            checkUserLogin();
        }
    }
}
