package com.wickedride.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.interfaces.OnScrollViewListener;
import com.wickedride.app.interfaces.OnSpinnerEventsListener;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.bike_details_model.AvailableLocation;
import com.wickedride.app.models.bike_details_model.BikeDetailResultModel;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.CustomParallaxScrollView;
import com.wickedride.app.views.CustomSpinner;
import com.wickedride.app.views.WRProgressView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BikeDetailsActivity extends BaseDefaultActionActivity implements AdapterView.OnItemSelectedListener,
        OnSpinnerEventsListener, OnScrollViewListener {

    @InjectView(R.id.image_holder)
    RelativeLayout mBikeHolder;

    @InjectView(R.id.bike_image)
    ImageView mBikeImage;

    @InjectView(R.id.bike_logo)
    ImageView mBikeLogo;

    @InjectView(R.id.bike_name)
    TextView mBikeName;

    @InjectView(R.id.bike_location)
    CustomSpinner mSpinnerCities;

    @InjectView(R.id.bike_description)
    TextView mBikeDescription;

    @InjectView(R.id.show_more)
    TextView mShowMore;

    @InjectView(R.id.rental)
    TextView mRental;

    @InjectView(R.id.effective_price)
    TextView mEffectivePrice;

    @InjectView(R.id.bike_price)
    TextView mBikePrice;

    @InjectView(R.id.book_now)
    TextView mBookNow;

    @InjectView(R.id.price_text)
    LinearLayout mPriceText;

    @InjectView(R.id.left_arrow)
    ImageView mLeftArrow;

    @InjectView(R.id.right_arrow)
    ImageView mRightArrow;

    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;


    @InjectView(R.id.scrollable_layout)
    CustomParallaxScrollView mScrollableLayout;
    public static final int SIGN_IN_FROM_RESERVE_FRG = 10010;
    private BikeDetailResultModel bikeDetailResult;
    private ArrayList<AvailableLocation> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);
        ButterKnife.inject(this);
        String bikeID = getIntent().getStringExtra(Constants.BIKE_ID);
        fetchBikeDetails(bikeID);
        mScrollableLayout.setOnScrollViewListener(this);
    }

    private void fetchBikeDetails(String bikeID) {
        mProgress.setVisibility(View.VISIBLE);
        placeRequest(APIMethods.GET_ALL_BIKES + "/" + bikeID+"?"+ Constants.CITY_ID+"="+SessionManager.getUserCityID(this), BikeDetailResultModel.class);
    }

    private void setUpSpinner(BikeDetailResultModel bikeDetailResult) {
        ArrayList<String> areas = new ArrayList<>();
        locations = (ArrayList<AvailableLocation>) bikeDetailResult.getResult().getData().getAvailableLocations();
        for (int i = 0; i < locations.size(); i++) {
            areas.add(locations.get(i).getArea());
        }
        ArrayAdapter<String> spinnerCitiesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_spinner_item_center, areas);
        mSpinnerCities.setAdapter(spinnerCitiesArrayAdapter);
        mSpinnerCities.setOnItemSelectedListener(this);
        mSpinnerCities.setSpinnerEventsListener(this);
    }

    @OnClick({R.id.book_now, R.id.left_arrow, R.id.right_arrow, R.id.back_button, R.id.show_more, R.id.bike_description})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.book_now:
                if(SessionManager.isUserSessionValid(getApplicationContext())) {
                    openReserveActivity();
                }
                else{
                    openLoginScreen();
                }
                break;
            case R.id.left_arrow:
                showNextPrice();
                break;
            case R.id.right_arrow:
                showPreviousPrice();
                break;

            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.show_more:
            case R.id.bike_description:
               // scrollOnTop();
                break;

            case R.id.show_less:
               // scrollToBottom();
                break;
        }
    }

    private void scrollToBottom() {
    }

    private void scrollOnTop() {

        if (!mShowMore.isSelected()) {
            expandLayout();
        } else {
            collapseLayout();
        }
    }

    private void expandLayout() {
        mScrollableLayout.smoothScrollTo(0, mScrollableLayout.getBottom());
        mShowMore.setSelected(true);
        mBikeDescription.setSelected(true);
    }

    private void collapseLayout() {
        mScrollableLayout.smoothScrollTo(0, 0);
        mShowMore.setSelected(false);
        mBikeDescription.setSelected(false);
    }

    private void openReserveActivity() {
        if(bikeDetailResult != null && bikeDetailResult.getResult().getData()!= null) {
            Intent openReserveActivity = new Intent(BikeDetailsActivity.this, ReserveActivity.class);
            openReserveActivity.putExtra(Constants.BIKE_NAME, bikeDetailResult.getResult().getData().getName());
            openReserveActivity.putExtra(Constants.BIKE_ID, bikeDetailResult.getResult().getData().getId());
            openReserveActivity.putExtra(Constants.BIKE_IMAGE_URL, bikeDetailResult.getResult().getData().getImage().getFull());
            openReserveActivity.putExtra(Constants.LOCATION, mSpinnerCities.getSelectedItem() + "");
            startActivity(openReserveActivity);
        }
    }
    private void openLoginScreen() {
        Intent openLoginActivity = new Intent(getApplicationContext(), SignInActivity.class);
        startActivityForResult(openLoginActivity, SIGN_IN_FROM_RESERVE_FRG);
    }
    private void showPreviousPrice() {
        Animation rightAnim = AnimationUtils.loadAnimation(BikeDetailsActivity.this, R.anim.detail_slide_out_from_right);
        mRental.startAnimation(rightAnim);
        mBikePrice.startAnimation(rightAnim);
        rightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation leftAnim = AnimationUtils.loadAnimation(BikeDetailsActivity.this, R.anim.detail_slide_in_from_left);
                mRental.startAnimation(leftAnim);
                mBikePrice.startAnimation(leftAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
       /* mPriceText.setVisibility(View.VISIBLE);
        anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.slide_in_from_right);
        mPriceText.startAnimation(anim);*/
    }

    private void showNextPrice() {
        Animation leftAanim = AnimationUtils.loadAnimation(BikeDetailsActivity.this, R.anim.detail_slide_out_from_left);
        mRental.startAnimation(leftAanim);
        mBikePrice.startAnimation(leftAanim);
        leftAanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation rightAnim = AnimationUtils.loadAnimation(BikeDetailsActivity.this, R.anim.detail_slide_in_from_right);
                mBikePrice.startAnimation(rightAnim);
                mRental.startAnimation(rightAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedView() != null) {
            ((TextView) parent.getSelectedView()).setPadding(0, 0, 0, 0);
            WickedRideManager.getInstance(this).setAreaId(locations.get(position).getId());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSpinnerOpened() {
        mSpinnerCities.setBackgroundResource(R.drawable.spinner_exp_bg_black);
    }

    @Override
    public void onSpinnerClosed() {
        mSpinnerCities.setBackgroundResource(R.drawable.spinner_bg_black);
    }

    @Override
    public void onScrollChanged(CustomParallaxScrollView v, int x, int y, int oldx, int oldy) {
        if (y > 0) {
            mShowMore.setSelected(true);
        } else {
            mShowMore.setSelected(false);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.d("BikeDetailsActivity", "ApiMethod::" + apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.GET_ALL_BIKES)) {
            bikeDetailResult = (BikeDetailResultModel) response;
            Log.d("BikeDetailsActivity", "BikeName::" + bikeDetailResult.getResult().getData().getName());
            setUpSpinner(bikeDetailResult);
            setLogo(bikeDetailResult.getResult().getData().getLogo().getFull());
            setTitle(bikeDetailResult.getResult().getData().getName());
            setBikeImage(bikeDetailResult.getResult().getData().getImage().getFull());
            setDescription(bikeDetailResult.getResult().getData().getDescription());
            setEffectivePrice("" + bikeDetailResult.getResult().getData().getAvailableLocations().get(0).getPrice().get(0)/*.getEffectivePerHrPrice()*/);
            setRentalPrice("" + bikeDetailResult.getResult().getData().getAvailableLocations().get(0).getPrice().get(1)/*.getRentalAmount()*/);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
    }

    private void setDescription(String description) {
        mBikeDescription.setText(description);
    }

    private void setBikeImage(String imageURL) {
        Picasso.with(getApplicationContext()).load(imageURL).into(mBikeImage);
    }

    private void setLogo(String logoURL) {
        Picasso.with(getApplicationContext()).load(logoURL).into(mBikeLogo);
    }

    private void setTitle(String title) {
        mBikeName.setText("" + title);
    }

    private void setEffectivePrice(String price) {
      //  mEffectivePrice.setText(/*"Effective Price: Rs. " + */price + "/-");
        mEffectivePrice.setText(/*"Effective Price: Rs. " + */price);
    }

    private void setRentalPrice(String price) {
        //mRental.setText(/*"Rental Amount: Rs. " + */price + "/-");
        mRental.setText(/*"Rental Amount: Rs. " + */price);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(SIGN_IN_FROM_RESERVE_FRG==resultCode){
            openReserveActivity();
        }
    }
}