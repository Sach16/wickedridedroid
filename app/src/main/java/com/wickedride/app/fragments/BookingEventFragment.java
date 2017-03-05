package com.wickedride.app.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.models.Attended;
import com.wickedride.app.models.Bikations;
import com.wickedride.app.models.Bookings;
import com.wickedride.app.models.Subscription;
import com.wickedride.app.models.Upcoming;
import com.wickedride.app.models.bike_details_model.Data;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Madhumita on 13-01-2016.
 */
public class BookingEventFragment extends BaseFragment {
    @InjectView(R.id.next_arrow)
    ImageView nextArrw;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.prev_arrow)
    ImageView prevArrw;
    @InjectView(R.id.viewPager_booking)
    ViewPager mViewPager;
    @InjectView(R.id.viewPager_booking_attend)
    ViewPager mAttendedPager;
    @InjectView(R.id.subscription_Lyt)
    RelativeLayout mSubLyt;
    @InjectView(R.id.empty_Lyt)
    LinearLayout empty_Lyt;
@InjectView(R.id.text_subscription)
    TextView mSubRides;
    @InjectView(R.id.text_exp_sub)
    TextView mExpSubRide;
    @InjectView(R.id.page_indicator)
    CirclePageIndicator pageIndicator;
    @InjectView(R.id.pager_Lyt)
    RelativeLayout mPagerLyt;
    private BookingPagerAdapter mAdapter;
    private Bikations mBikation;
    private Subscription mSubsciption;
    private List<Bookings> bookingList =new ArrayList<Bookings>();
    List<Attended> mAttendedList;
    List<Upcoming> mUpcomingList;
    private Data profileData;
    boolean isEvent;
    boolean isAttended;
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
        mView=inflater.inflate(R.layout.fragment_bookig_events, container, false);
        ButterKnife.inject(this, mView);
        initView();
        return mView;

    }
    @OnClick({R.id.next_arrow, R.id.prev_arrow})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.next_arrow:
                if (!isEvent) {
                    showSubScribe(mSubsciption);
                }
                else{
                    showBikation(mBikation,true);
                }
                break;
            case R.id.prev_arrow:
                if (!isEvent) {
                    showBooking(bookingList);
                }
                else{
                    showBikation(mBikation,false);
                }
                break;
        }
    }

    private void showSubScribe(Subscription mSubObj) {
        if (mPagerLyt.getVisibility()==View.VISIBLE || empty_Lyt.getVisibility()==View.VISIBLE){
            mPagerLyt.setVisibility(View.GONE);
            empty_Lyt.setVisibility(View.GONE);
            mSubLyt.setVisibility(View.VISIBLE);
            mTitle.setText("Subscription");
            nextArrw.setVisibility(View.GONE);
            prevArrw.setVisibility(View.VISIBLE);
            mSubRides.setText(mSubObj.getRidesAvailable() + "/" + mSubObj.getRidesTotal() + " rides");
            mExpSubRide.setText("Your subscription expires on" + mSubObj.getSubscriptionExpiryDate());
        }
    }

    private void initView() {
        profileData= new Gson().fromJson(getArguments().getString(Constants.PROFILE_DATA), Data.class);
        isEvent=getArguments().getBoolean(Constants.IS_EVENT);
        bookingList = profileData.getBookings();
        mSubsciption=profileData.getSubscription();
        mBikation=profileData.getBikations();
        mAttendedList= mBikation.getAttended();
        mUpcomingList=mBikation.getUpcoming();
        if (isEvent){
            showBikation(mBikation, isAttended);
        }
        else{
            showBooking(bookingList);
        }
    }

    private void showBikation(Bikations bikationObj, boolean isAttended) {
        mPagerLyt.setVisibility(View.VISIBLE);
        BikationPagerAdapter mBikationAdapter = new BikationPagerAdapter(getActivity(), getChildFragmentManager(),bikationObj,isAttended);
        if (isAttended){
            mTitle.setText("Attended Event");
            nextArrw.setVisibility(View.GONE);
            prevArrw.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mAttendedPager.setVisibility(View.VISIBLE);
            mAttendedPager.setAdapter(mBikationAdapter);
            pageIndicator.setViewPager(mAttendedPager);
            mAttendedPager.setCurrentItem(0);

        }
        else{
            mTitle.setText("Upcoming Event");
            nextArrw.setVisibility(View.VISIBLE);
            prevArrw.setVisibility(View.GONE);
            mBikationAdapter.notifyDataSetChanged();
            mViewPager.setVisibility(View.VISIBLE);
            mAttendedPager.setVisibility(View.GONE);
            mViewPager.setAdapter(mBikationAdapter);
            pageIndicator.setViewPager(mViewPager);
            mViewPager.setCurrentItem(0);
        }

           //
    }

    private void showBooking(List<Bookings> bookingList) {
        if (mSubLyt.getVisibility()== View.VISIBLE) {
            mSubLyt.setVisibility(View.GONE);
            mTitle.setText("Current Booking");
            nextArrw.setVisibility(View.VISIBLE);
            prevArrw.setVisibility(View.GONE);
        }
            if (bookingList.size() > 0) {
                mPagerLyt.setVisibility(View.VISIBLE);

                if (bookingList.size() > 0) {
                    Log.i("bookinglistsz", bookingList.size() + bookingList.get(0).getBooking_id());
                }
                mAdapter = new BookingPagerAdapter(getActivity(), getChildFragmentManager(), bookingList);
                mViewPager.setAdapter(mAdapter);
                // mViewPager.setOnPageChangeListener(mPageChangeListener);
                //pageIndicator.setOnPageChangeListener(mPageChangeListener);
                pageIndicator.setViewPager(mViewPager);
                // Set current item to the middle page so we can fling to both
                // directions left and right
                mViewPager.setCurrentItem(0);
            } else {
            empty_Lyt.setVisibility(View.VISIBLE);
            }

    }


    private void changeFragment() {

    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
    public class BookingPagerAdapter extends FragmentPagerAdapter {
        Activity mContext;
        private List<Bookings> bookingList;
        Fragment[] fragments = {new CurrentBookingFragment(), new EventsFragment()};

        FragmentManager mFragmentManager;

        public BookingPagerAdapter(Activity context, FragmentManager fm,List<Bookings> bookingsArrayList) {
            super(fm);
            mFragmentManager = fm;
            this.mContext = context;
            this.bookingList=bookingsArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            // make the first pager bigger than others
            Bundle bundle = new Bundle();
           // bundle.putSerializable(Constants.BOOKING_LIST, (Serializable) bookingList);
            bundle.putString(Constants.BOOKING, new Gson().toJson(bookingList.get(position)));
            CurrentBookingFragment bookingFragment = (CurrentBookingFragment) CurrentBookingFragment.newInstance(getActivity());
            //bookingFragment.setOnPageClickedListener(CurrentBookingFragment.this);
            bookingFragment.setArguments(bundle);

            return bookingFragment;
        }

        @Override
        public int getCount() {
            return bookingList.size();
        }

    }
    public class BikationPagerAdapter extends FragmentPagerAdapter {
        Activity mContext;
        private List<Attended> attendList;
        private List<Upcoming> upcomingList;
        Bikations mBikationObj;
        boolean isAttended;

        FragmentManager mFragmentManager;
        public BikationPagerAdapter(Activity context, FragmentManager fm,Bikations bikationObj,boolean isAttend) {
            super(fm);
            mFragmentManager = fm;
            this.mContext = context;
            this.mBikationObj=bikationObj;
            this.isAttended=isAttend;
            if (isAttended){
                attendList=mBikationObj.getAttended();
            } else {
                upcomingList = mBikationObj.getUpcoming();
            }

        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
                if (isAttended){
               // attendList=mBikationObj.getAttended();
                bundle.putString(Constants.BIKATION, new Gson().toJson(attendList.get(position).getBikation()));
                    bundle.putBoolean(Constants.IS_ATTENDED, true);
            } else{
                //upcomingList=mBikationObj.getUpcoming();
                bundle.putString(Constants.BIKATION, new Gson().toJson(upcomingList.get(position).getBikation()));
                    bundle.putBoolean(Constants.IS_ATTENDED, false);
            }


           // bundle.putBoolean(Constants.IS_ATTENDED,isAttended);
            // bundle.putSerializable(Constants.BOOKING_LIST, (Serializable) bookingList);

             //eventsFragment = (EventsFragment) EventsFragment.newInstance(getActivity(),isAttended);
          final EventsFragment  eventsFragment= new EventsFragment();
            //bookingFragment.setOnPageClickedListener(CurrentBookingFragment.this);
            eventsFragment.setArguments(bundle);

            return eventsFragment;
        }
        @Override
        public int getItemPosition(Object object) {
            //don't return POSITION_NONE, avoid fragment recreation.
            return POSITION_NONE;
        }
       /* public int getItemPosition(Object object) {
            return POSITION_NONE;
        }*/
        @Override
        public int getCount() {
            if (isAttended){
                return attendList.size();
            }
            else {
                return upcomingList.size();
            }
        }
    }
}
