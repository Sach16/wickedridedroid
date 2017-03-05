package com.wickedride.app.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.BikationConfirmActivity;
import com.wickedride.app.models.BikationDetails;
import com.wickedride.app.models.Exclusion;
import com.wickedride.app.models.Inclusion;
import com.wickedride.app.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class MoreInfoFragment extends BaseFragment {

    public static final String TAG = "MORE_INFO_FRAGMENT";

    @InjectView(R.id.scrollview)
    ScrollView mScrollView;
    @InjectView(R.id.food_bottle_holder)
    LinearLayout inclusionsLayout;
    @InjectView(R.id.medical_accom_holder)
    LinearLayout exclusionsLayout;
    @InjectView(R.id.ride_rules)
    LinearLayout rulesLayout;
    @InjectView(R.id.register_now)
    Button bookNow;

    private View inclusionView;
    private View exclutionView;
    private View rulesView;
    private BikationDetails bikationDetailsObj;
    private String bikationTitle;

    public static MoreInfoFragment newInstance() {
        final Bundle bundle = new Bundle();
        final MoreInfoFragment fragment = new MoreInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.more_info);
    }

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        ButterKnife.inject(this, view);
        String strBikationDetails = getArguments().getString(Constants.BIKATION);
        bikationTitle = getArguments().getString(Constants.BIKATION_TITLE);
        bikationDetailsObj = new Gson().fromJson(strBikationDetails, BikationDetails.class);
        ArrayList<Exclusion> exclusions = (ArrayList<Exclusion>) bikationDetailsObj.getResult().getData().getExclusions();
        ArrayList<Inclusion> inclusions = (ArrayList<Inclusion>) bikationDetailsObj.getResult().getData().getInclusions();
        ArrayList<String> rulesList = (ArrayList<String>) bikationDetailsObj.getResult().getData().getRideRules();

        for(int i=0;i< inclusions.size();i++){
            inclusionView = inflater.inflate(R.layout.adapter_image_textview, null);
            TextView titleTxtView = (TextView) inclusionView.findViewById(R.id.title);
            ImageView imageView = (ImageView) inclusionView.findViewById(R.id.image);
            titleTxtView.setText(inclusions.get(i).getDescription());
            Picasso.with(getActivity()).load(inclusions.get(i).getIcon().getFull())
                    .placeholder(R.drawable.place_holder).fit().centerCrop().into(imageView);
            inclusionsLayout.addView(inclusionView);
        }

        for(int i=0;i< exclusions.size();i++){
            exclutionView = inflater.inflate(R.layout.adapter_image_textview, null);
            TextView titleTxtView = (TextView) exclutionView.findViewById(R.id.title);
            ImageView imageView = (ImageView) exclutionView.findViewById(R.id.image);
            titleTxtView.setText(exclusions.get(i).getDescription());
            Picasso.with(getActivity()).load(exclusions.get(i).getIcon().getFull())
                    .placeholder(R.drawable.place_holder).fit().centerCrop().into(imageView);
            exclusionsLayout.addView(exclutionView);
        }

        for(int i=0;i< rulesList.size();i++){
            rulesView = inflater.inflate(R.layout.adapter_image_textview, null);
            TextView titleTxtView = (TextView) rulesView.findViewById(R.id.title);
            ImageView imageView = (ImageView) rulesView.findViewById(R.id.image);
            imageView.setVisibility(View.GONE);
            titleTxtView.setText("* " + rulesList.get(i));
            rulesLayout.addView(rulesView);
        }
        return view;
    }

    @OnClick({R.id.register_now})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.register_now:
                Intent intent = new Intent(getActivity(), BikationConfirmActivity.class);
                intent.putExtra(Constants.BIKATION_TITLE, bikationDetailsObj.getResult().getData().getName());
                intent.putExtra(Constants.START_DATE, bikationDetailsObj.getResult().getData().getStartDate()+" ("+bikationDetailsObj.getResult().getData().getDuration()+")");
                intent.putExtra(Constants.PRICE, bikationDetailsObj.getResult().getData().getPrice());
                intent.putExtra(Constants.DAYS, bikationDetailsObj.getResult().getData().getDuration());
                intent.putExtra(Constants.BIKATION_TITLE,bikationTitle);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return mScrollView != null && mScrollView.canScrollVertically(direction);
    }
}
