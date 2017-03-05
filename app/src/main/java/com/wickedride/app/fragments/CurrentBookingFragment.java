package com.wickedride.app.fragments;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.models.Bookings;
import com.wickedride.app.utils.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class CurrentBookingFragment extends BaseFragment {
    @InjectView(R.id.text_bike_details)
    TextView mBikeDetails;
    @InjectView(R.id.pick_up_location)
    TextView mPickUpLocation;
    @InjectView(R.id.pick_up_time)
    TextView mPickUpTime;
    @InjectView(R.id.pick_up_address)
    TextView mPickUpAddress;
    @InjectView(R.id.amount)
    TextView mAmount;
    private ArrayList<Bookings> bookingList;
    Bookings booking;

    public static android.support.v4.app.Fragment newInstance(Context context) {
        Bundle b = new Bundle();
        return android.support.v4.app.Fragment.instantiate(context, CurrentBookingFragment.class.getName(), b);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_current_booking, container, false);
        ButterKnife.inject(this, mView);
        initView();
        return mView;

    }

    private void initView() {
        //bookingList = (ArrayList<Bookings>) getArguments().getSerializable(Constants.BOOKING_LIST);
         booking= new Gson().fromJson(getArguments().getString(Constants.BOOKING), Bookings.class);
        mBikeDetails.setText(booking.getItems().getBike().getModel().getName() + "+" + booking.getItems().getAccessories().get(0).getAccessoriesName() + "+" + booking.getItems().getAccessories().get(1).getAccessoriesName());
        mPickUpTime.setText(booking.getPickup_date() + " - " + booking.getPickup_time());
        mPickUpLocation.setText(booking.getPickup_area().getArea());
        mPickUpAddress.setText(booking.getPickup_area().getAddress());
        if (booking.getPrice()!=null) {
            mAmount.setText("Rs. " + booking.getPrice() + "/-");
        }
        else{
            mAmount.setText("Rs. 0" + "/-");
        }

    }

    /*@OnClick({R.id.next_arrow})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.next_arrow:
                changeFragment();
                break;

        }
    }*/

    private void changeFragment() {

    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
