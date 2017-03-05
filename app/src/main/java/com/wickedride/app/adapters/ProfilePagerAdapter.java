/*
package com.wickedride.app.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.wickedride.app.fragments.CurrentBookingFragment;
import com.wickedride.app.fragments.EventsFragment;
import com.wickedride.app.models.Bookings;
import com.wickedride.app.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;

*/
/**
 * Created by Nitish Kulkarni on 2/8/15.
 *//*

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    Activity mContext;
    private ArrayList<Bookings> bookingList;
    Fragment[] fragments = {new CurrentBookingFragment(), new EventsFragment()};

    FragmentManager mFragmentManager;

    public ProfilePagerAdapter(Activity context, FragmentManager fm,ArrayList<Bookings> bookingsArrayList) {
        super(fm);
        mFragmentManager = fm;
        this.mContext = context;
        this.bookingList=bookingsArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BOOKING_LIST, (Serializable) bookingList);
        CurrentBookingFragment bookingFragment = (CurrentBookingFragment) CurrentBookingFragment.newInstance(getActivity());
        //bookingFragment.setOnPageClickedListener(CurrentBookingFragment.this);
        bookingFragment.setArguments(bundle);

        return bookingFragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
*/
