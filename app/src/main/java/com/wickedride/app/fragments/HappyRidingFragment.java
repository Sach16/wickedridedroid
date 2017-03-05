package com.wickedride.app.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wickedride.app.R;
import com.wickedride.app.activities.BaseNoActionBarActivity;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class HappyRidingFragment extends BaseFragment {

    public static final String TAG = "HAPPY_ENDING_FRAGMENT";

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.happy_riding);
    }

//    @InjectView(R.id.faq)
//    TextView mFaq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_happy_riding, container, false);
        ButterKnife.inject(this, view);
        WickedRideManager.getInstance(getActivity()).setPickUpDate("");
        WickedRideManager.getInstance(getActivity()).setPickupTime("");
        WickedRideManager.getInstance(getActivity()).setDropDate("");
        WickedRideManager.getInstance(getActivity()).setDropTime("");
        WickedRideManager.getInstance(getActivity()).setBookingPrice("");
        WickedRideManager.getInstance(getActivity()).setNoOfDaysHrs("");
        return view;
    }

    @OnClick({R.id.faq, R.id.close})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.close:
                onBackPressed();
                break;
            case R.id.faq:
                ((BaseNoActionBarActivity) getActivity()).loadWebFragment(SessionManager.getFaqUrl(getActivity()));
                break;
        }
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
