package com.wickedride.app.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.ProfileActivity;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.Bookings;
import com.wickedride.app.models.ProfileDetails;
import com.wickedride.app.models.Subscription;
import com.wickedride.app.models.bike_details_model.Data;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class ProfileFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @InjectView(R.id.profile_pic)
    ImageView mProfilePic;

    @InjectView(R.id.bikes)
    TextView mBikes;

    @InjectView(R.id.events)
    TextView mEvents;

   /* @InjectView(R.id.profile_pager)
    ViewPager mProfilePager;*/
    //private ProfilePagerAdapter mAdapter;

    @InjectView(R.id.rides_available)
    TextView mRidesAvailable;

    @InjectView(R.id.rides_expiry)
    TextView mRideExpiry;
    boolean event;
    private BookingEventFragment bookingEventFragment;
    Data mResultData;


    @Override
    public String getSelfTag() {
        return null;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, mView);
        getProfileDetails();
       // mAdapter = new ProfilePagerAdapter(getActivity(), getActivity().getSupportFragmentManager());
       // mProfilePager.setAdapter(mAdapter);
      //  mProfilePager.setOnPageChangeListener(this);
        mEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_transparent_horizontal);
        mBikes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bikes_black_horizontal);
        if(!SessionManager.getUserImageUrl(getActivity()).isEmpty()) {
            Picasso.with(getActivity()).load(SessionManager.getUserImageUrl(getActivity())).placeholder(R.drawable.user).into(mProfilePic);
        }
        return mView;
    }

    private void getProfileDetails() {
        JSONObject jsonObj =  new JSONObject();
        placeRequest(APIMethods.PROFILE_DETAILS, ProfileDetails.class, false, jsonObj);
    }

    //R.id.bikes,  Add click to bikes once bikation events are enabled
    @OnClick({R.id.events})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.bikes:
                event=false;
                setBikesSelected();
                loadFragment(mResultData,event);
               // mProfilePager.setCurrentItem(0);
                break;
            case R.id.events:
                setEventsSelected();
                event=true;
                loadFragment(mResultData,event);
              //  mProfilePager.setCurrentItem(2);
                break;
        }
    }

    private void setEventsSelected() {
        mBikes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_transparent_horizontal);
        mEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.review_black_horizontal);
    }

    private void setBikesSelected() {
        mEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_transparent_horizontal);
        mBikes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bikes_black_horizontal);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setBikesSelected();
        } else if (position == 1) {
            setEventsSelected();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onBackPressed() {
        ((ProfileActivity) getActivity()).finish();
        return super.onBackPressed();
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        ProfileDetails profileDetails = (ProfileDetails) response;
        if(profileDetails.getResult() != null && profileDetails.getResult().getData() != null){
           // mRidesAvailable.setText(profileDetails.getResult().getData().getSubscription().getRidesAvailable()+"/"+profileDetails.getResult().getData().getSubscription().getRidesTotal()+" Rides available");
            //mRideExpiry.setText("Subscription Expires on "+profileDetails.getResult().getData().getSubscription().getSubscriptionExpiryDate());
            Log.i("bookings", profileDetails.getResult().getData().getBookings().size() + "");
            mResultData=profileDetails.getResult().getData();
            loadFragment(mResultData,event);
            //loadBookingFragment(profileDetails.getResult().getData().getBookings(),profileDetails.getResult().getData().getSubscription());
        }
    }

    private void loadBookingFragment(List<Bookings> bookings, Subscription subscription) {

    }

    private void loadFragment(Data resultData,boolean isEvent) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PROFILE_DATA, new Gson().toJson(resultData));
        bundle.putBoolean(Constants.IS_EVENT,isEvent);
       // magazineGridFragment = new HomeGridFragment();
        //magazineGridFragment.setArguments(bundle);

        bookingEventFragment = new BookingEventFragment();
        bookingEventFragment.setArguments(bundle);
        //magazineListFragment.setOnThemeChangeListener(this);
        changeFragment(bookingEventFragment);
    }

        private void changeFragment(Fragment fragment) {
            //if(magazineList!=null && !magazineList.isEmpty()) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.contentPanel_bikation, fragment)
                        .commit();
          //  }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!SessionManager.getUserImageUrl(getActivity()).isEmpty()) {
            Picasso.with(getActivity()).load(SessionManager.getUserImageUrl(getActivity())).placeholder(R.drawable.user).into(mProfilePic);
        }

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_profile, menu);
    }*/
}
