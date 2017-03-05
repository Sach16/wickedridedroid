package com.wickedride.app.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.ReviewActivity;
import com.wickedride.app.models.Attended;
import com.wickedride.app.models.Bikation;
import com.wickedride.app.models.Upcoming;
import com.wickedride.app.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class EventsFragment extends BaseFragment {
    @InjectView(R.id.title)
    TextView mTitle;
@InjectView(R.id.ride_name)
        TextView mRideName;
    @InjectView(R.id.ride_group)
    TextView mRideGroup;
    @InjectView(R.id.confirm_status)
    TextView mStatus;
    @InjectView(R.id.pick_up_date)
    TextView mDate;
    @InjectView(R.id.pick_up_location)
    TextView mLocation;
    @InjectView(R.id.amount)
    TextView mAmount;



    boolean isAttended;
    Attended mAttended;
    Upcoming mUpcoming;
    Bikation bikationObj;
   /* public static android.support.v4.app.Fragment newInstance(Context context,boolean atten) {
        Bundle b = new Bundle();
        b.putBoolean(Constants.IS_ATTENDED,atten);
        return android.support.v4.app.Fragment.instantiate(context, EventsFragment.class.getName(), b);
    }*/

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
        mView=inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.inject(this, mView);
        isAttended = getArguments().getBoolean(Constants.IS_ATTENDED);
        initView();
        return mView;
    }


    private void initView() {
        bikationObj = new Gson().fromJson(getArguments().getString(Constants.BIKATION), Bikation.class);
        updateAttendView(bikationObj);
        }


    private void updateAttendView(final Bikation bikation) {
        mRideName.setText(bikation.getTitle()+"("+bikation.getDistance()+")");
        mRideGroup.setText(bikation.getConductor().getName());
        mDate.setText(bikation.getStartDate()+"("+bikation.getDuration()+")");
        mLocation.setText(bikation.getStartCity());
        mAmount.setText(""+bikation.getPrice()+"/-");
        if (isAttended){
            mStatus.setText("ATTENDED");
            mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(getActivity(), ReviewActivity.class);
                    i.putExtra(Constants.BIKE_ID,bikation.getId());
                    startActivity(i);
                }
            });
        }
        else{
            mStatus.setText("CONFIRMED");
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
