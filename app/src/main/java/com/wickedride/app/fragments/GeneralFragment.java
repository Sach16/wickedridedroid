package com.wickedride.app.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.BikationConfirmActivity;
import com.wickedride.app.models.BikationDetails;
import com.wickedride.app.models.bike_details_model.Data;
import com.wickedride.app.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class GeneralFragment extends BaseFragment {

    public static final String TAG = "GENERAL_FRAGMENT";

    @InjectView(R.id.scrollview)
    ScrollView mScrollView;
    @InjectView(R.id.price_value)
    TextView priceValue;
    @InjectView(R.id.distance_value)
    TextView distanceValue;
    @InjectView(R.id.date_value)
    TextView dateValue;
    @InjectView(R.id.description_value)
    TextView descriptionValue;
    @InjectView(R.id.joined_value)
    TextView joinedValue;
    @InjectView(R.id.meeting_value)
    TextView meetingValue;
    @InjectView(R.id.recommended_value)
    TextView recommendedValue;
    @InjectView(R.id.book_now)
    Button bookNowBtn;

    private Data bikationData;
    private String bikationTitle;

    public static GeneralFragment newInstance() {
        final Bundle bundle = new Bundle();
        final GeneralFragment fragment = new GeneralFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        ButterKnife.inject(this, view);
        String bikationDetails = getArguments().getString(Constants.BIKATION);
        bikationTitle = getArguments().getString(Constants.BIKATION_TITLE);
        BikationDetails bikationDetailsObj = new Gson().fromJson(bikationDetails, BikationDetails.class);
        bikationData = bikationDetailsObj.getResult().getData();
        priceValue.setText(bikationData.getPrice()+"");
        distanceValue.setText(bikationData.getDistance());
        dateValue.setText(bikationData.getStartDate()+" ("+bikationData.getDuration()+")");
        descriptionValue.setText(bikationData.getDescription());
        joinedValue.setText(bikationData.getAvailableSlots() +"/"+ bikationData.getTotalSlots());
        meetingValue.setText(bikationData.getMeetingPoint());
        String recommendedBikes = "";
        for(int i=0;i<bikationData.getRecommendedBikes().size();i++) {
            recommendedBikes += bikationData.getRecommendedBikes().get(i)+",";
        }
        recommendedValue.setText(recommendedBikes.substring(0, recommendedBikes.length()-1));

        return view;
    }

    @OnClick({R.id.book_now})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.book_now:
                Intent intent = new Intent(getActivity(), BikationConfirmActivity.class);
                intent.putExtra(Constants.BIKATION_TITLE, bikationData.getName());
                intent.putExtra(Constants.START_DATE, bikationData.getStartDate()+" ("+bikationData.getDuration()+")");
                intent.putExtra(Constants.PRICE, bikationData.getPrice());
                intent.putExtra(Constants.DAYS, bikationData.getDuration());
                intent.putExtra(Constants.BIKATION_TITLE,bikationTitle);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return mScrollView != null && mScrollView.canScrollVertically(direction);
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.general);
    }

    @Override
    public String getSelfTag() {
        return TAG;
    }
}
