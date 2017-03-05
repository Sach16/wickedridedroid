package com.wickedride.app.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseNoActionBarActivity;
import com.wickedride.app.adapters.AccessoriesAdapter;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.all_bike_models.BikeInfoResultModel;
import com.wickedride.app.models.all_bike_models.Datum;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.RippleButton;
import com.wickedride.app.views.WRProgressView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class AccessoriesFragment extends BaseFragment implements ServerCallback{

    public static final String TAG = "ACCESSORIES_FRAGMENT";

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.reserve)
    RippleButton mReserve;

    @InjectView(R.id.bike_name_small)
    TextView bikeNameSmall;

    @InjectView(R.id.days_small)
    TextView daysSmall;

    @InjectView(R.id.total_price)
    TextView totalPrice;

    @InjectView(R.id.progress)
    WRProgressView progressView;


    private ArrayList<Datum> mAccessories;
    private String bikeName;
    private String bikeImageUrl;
    private Bundle accessoryData;
    private String startDate, endDate;
    private String locationSelected;

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.accessories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_accessories, container, false);
        ButterKnife.inject(this, mView);
        ((BaseNoActionBarActivity) getActivity()).setBlackBackButton();
        initializeData();


        fetchAllAccessories();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(Constants.ON_ACCESSORIES_SELECTED));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        bikeName = getArguments().getString(Constants.BIKE_NAME);
        bikeNameSmall.setText(bikeName);
        bikeImageUrl = getArguments().getString(Constants.BIKE_IMAGE_URL);
        startDate = getArguments().getString(Constants.START_DATE);
        endDate = getArguments().getString(Constants.DROP_DATE);
        locationSelected = getArguments().getString(Constants.LOCATION);
        if(getArguments().getInt(Constants.TOTAL_PRICE) >=0){
            totalPrice.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(getArguments().getInt(Constants.TOTAL_PRICE)) + "/-");
        }

        if(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs() != null && WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().length() >0) {
            daysSmall.setText(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().split(",")[1] + " hours");
        }
        accessoryData = new Bundle();


        return mView;
    }

    private void fetchAllAccessories() {
        progressView.setVisibility(View.VISIBLE);
        placeRequest(APIMethods.GET_ALL_ACCESSORIES, BikeInfoResultModel.class, null, false);
    }

    @OnClick({R.id.reserve})
    public void onClickListener(View view) {
        if (view.getId() == R.id.reserve)
        {
            accessoryData.putString(Constants.BIKE_IMAGE_URL,bikeImageUrl);
            accessoryData.putString(Constants.BIKE_NAME, bikeName);
            accessoryData.putString(Constants.PRICE, totalPrice.getText().toString().replace("Rs.","").replace("/-","").replace(",",""));
            accessoryData.putString(Constants.START_DATE, startDate);
            accessoryData.putString(Constants.DROP_DATE, endDate);
            accessoryData.putString(Constants.LOCATION,locationSelected);
            ((BaseNoActionBarActivity) getActivity()).loadSummaryFragment(accessoryData);
        }
    }

    private void initializeData() {
        mAccessories = new ArrayList<Datum>();

//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
//        mAccessories.add(new Accessories(R.drawable.hd_night_rod, "Night ROd 2", "Rs.3,000/-", "Worlds Morst versatile accessories", false));
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        progressView.setVisibility(View.GONE);

        super.onAPIResponse(response, apiMethod);
        BikeInfoResultModel accessoriesData = (BikeInfoResultModel)response;
        mAccessories = (ArrayList<Datum>) accessoriesData.getResult().getData();
        if(mAccessories.size() ==0) {
            //Make recylcerview visible once api is sending data.
            mRecyclerView.setVisibility(View.GONE);
            ((LinearLayout) mView.findViewById(R.id.accessoriesInfoLayout)).setVisibility(View.VISIBLE);
        }else{

        }
        AccessoriesAdapter adapter = new AccessoriesAdapter(getActivity(), mAccessories);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        progressView.setVisibility(View.GONE);

        super.onErrorResponse(error, apiMethod);
    }

    private int totalAmt;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if(intent.getAction().equals(Constants.ON_ACCESSORIES_SELECTED)) {
                String itemsName = bikeName +intent.getStringExtra(Constants.ACCESSORY_NAME);
                String accessoriesImageUrls = intent.getStringExtra(Constants.ACCESSORY_IMAGES);
                String accessoriesPrices = intent.getStringExtra(Constants.ACCESSORY_PRICE_LIST);
                String accessoriesDescs = intent.getStringExtra(Constants.ACCESSORIES_DESCRIPTION);
                accessoryData.putString(Constants.ACCESSORY_IMAGES, accessoriesImageUrls);
                accessoryData.putString(Constants.ACCESSORY_PRICE, accessoriesPrices);
                accessoryData.putString(Constants.ACCESSORIES_DESCRIPTION, accessoriesDescs);
                accessoryData.putString(Constants.ACCESSORIES_IDS, intent.getStringExtra(Constants.ACCESSORIES_IDS));
                bikeNameSmall.setText(itemsName);
                int accessoryPrice = intent.getIntExtra(Constants.ACCESSORY_PRICE, 0);
                int bikeBookingPrice = (WickedRideManager.getInstance(getActivity()).getBookingPrice().isEmpty()) ? 0 : Integer.parseInt(WickedRideManager.getInstance(getActivity()).getBookingPrice());
                int noOfBookedHours =  Integer.parseInt(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().split(",")[1]);
                totalAmt = (accessoryPrice * noOfBookedHours) + bikeBookingPrice;
                totalPrice.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(totalAmt) + "/-");
                WickedRideManager.getInstance(getActivity()).setBookingPrice("" + totalAmt);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mMessageReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
            mMessageReceiver = null;
        }
    }
}
