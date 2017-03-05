package com.wickedride.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.inkoniq.parallaxviewpager.CanScrollVerticallyDelegate;
import com.inkoniq.parallaxviewpager.OnScrollChangedListener;
import com.inkoniq.parallaxviewpager.ScrollableLayout;
import com.wickedride.app.R;
import com.wickedride.app.adapters.DetailsPagerAdapter;
import com.wickedride.app.fragments.BaseFragment;
import com.wickedride.app.fragments.GeneralFragment;
import com.wickedride.app.fragments.MoreInfoFragment;
import com.wickedride.app.fragments.ReviewFragment;
import com.wickedride.app.interfaces.ConfigurationFragmentCallbacks;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.models.BikationDetails;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.RippleTextView;
import com.wickedride.app.views.WRProgressView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class BikationDetailsActivity extends ActionBarActivity implements ConfigurationFragmentCallbacks,
        ViewPager.OnPageChangeListener, ServerCallback {

    private static final String ARG_LAST_SCROLL_Y = "arg.LastScrollY";

    @InjectView(R.id.scrollable_layout)
    ScrollableLayout mScrollableLayout;

    @InjectView(R.id.details_image)
    ImageView mDetailsImage;

    @InjectView(R.id.title_holder)
    LinearLayout mTitleHolder;

    @InjectView(R.id.back_button)
    ImageView mBackButton;

    @InjectView(R.id.details_pager)
    ViewPager mDetailsPager;

    @InjectView(R.id.general)
    RippleTextView mGeneral;

    @InjectView(R.id.more_info)
    RippleTextView mMoreInfo;

    @InjectView(R.id.review)
    RippleTextView mReview;
    @InjectView(R.id.biker_rating)
    RatingBar mBikerRating;
    @InjectView(R.id.bikation_club)
    TextView mBikationClub;


    private DetailsPagerAdapter detailsPagerAdapter;
    private String bikationID,bikationTitle;
    private BikationDetails bikationDetails;

    @InjectView(R.id.bikation_name)
    TextView bikationName;
    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikation_details);
        ButterKnife.inject(this);
        mScrollableLayout.setDraggableView(mTitleHolder);
        bikationID = getIntent().getStringExtra(Constants.BIKATION_ID);
        bikationTitle = getIntent().getStringExtra(Constants.BIKATION_TITLE);
        bikationName.setText(bikationTitle);
        placeDetailsRequest();
    }

    private void placeDetailsRequest(){
        mProgress.setVisibility(View.VISIBLE);
        RequestManager.getInstance(this).placeRequest(APIMethods.BIKATIONS+"/"+bikationID, BikationDetails.class, this, null, false);

    }

    @OnClick({R.id.back_button, R.id.general, R.id.more_info, R.id.review})
    public void onClickListener(View view) {
        unSelectAll();
        switch (view.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.general:
                mGeneral.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_black_horizontal);
                mDetailsPager.setCurrentItem(0);
                break;
            case R.id.more_info:
                mMoreInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.moreinfo_black_horizontal);
                mDetailsPager.setCurrentItem(1);
                break;
            case R.id.review:
                mReview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.review_black_horizontal);
                mDetailsPager.setCurrentItem(2);
                break;

        }
    }

    public void showBikationReview() {
        mBikerRating.setVisibility(View.VISIBLE);
        mBikationClub.setVisibility(View.VISIBLE);
    }

    public void hideBikationReview() {
//        mBikerRating.setVisibility(View.GONE);
//        mBikationClub.setVisibility(View.GONE);
    }

    private void unSelectAll() {
        mGeneral.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mMoreInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mReview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void loadDetailsFragments(Bundle savedInstanceState) {
        mGeneral.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_black_horizontal);
        detailsPagerAdapter = new DetailsPagerAdapter(getSupportFragmentManager(), getResources(), getFragments());
        mDetailsPager.setAdapter(detailsPagerAdapter);
        mDetailsPager.setOnPageChangeListener(this);
        mScrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                return detailsPagerAdapter.canScrollVertically(mDetailsPager.getCurrentItem(), direction);
            }
        });

        mScrollableLayout.setOnScrollChangedListener(new OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int y, int oldY, int maxY) {

                final float tabsTranslationY;
                if (y < maxY) {
                    tabsTranslationY = .0F;
                } else {
                    tabsTranslationY = y - maxY;
                }

                mTitleHolder.setTranslationY(tabsTranslationY);

                mDetailsImage.setTranslationY(y / 2);
            }
        });

        if (savedInstanceState != null) {
            final int y = savedInstanceState.getInt(ARG_LAST_SCROLL_Y);
            mScrollableLayout.post(new Runnable() {
                @Override
                public void run() {
                    mScrollableLayout.scrollTo(0, y);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_LAST_SCROLL_Y, mScrollableLayout.getScrollY());
        super.onSaveInstanceState(outState);
    }

    private List<BaseFragment> getFragments() {

        final FragmentManager manager = getSupportFragmentManager();
        final List<BaseFragment> list = new ArrayList<>();

        GeneralFragment generalFragment = (GeneralFragment) manager.findFragmentByTag(GeneralFragment.TAG);
        if (generalFragment == null) {
            generalFragment = GeneralFragment.newInstance();
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BIKATION, new Gson().toJson(bikationDetails));
        bundle.putString(Constants.BIKATION_TITLE,bikationTitle);
        generalFragment.setArguments(bundle);

        MoreInfoFragment moreInfoFragment = (MoreInfoFragment) manager.findFragmentByTag(MoreInfoFragment.TAG);
        if (moreInfoFragment == null) {
            moreInfoFragment = MoreInfoFragment.newInstance();
        }

        moreInfoFragment.setArguments(bundle);

        ReviewFragment reviewFragment = (ReviewFragment) manager.findFragmentByTag(ReviewFragment.TAG);
        if (reviewFragment == null) {
            reviewFragment = ReviewFragment.newInstance();
        }

        reviewFragment.setArguments(bundle);
        Collections.addAll(list, generalFragment, moreInfoFragment, reviewFragment);
        return list;
    }

    @Override
    public void onFrictionChanged(float friction) {
        mScrollableLayout.setFriction(friction);
    }

    @Override
    public void openDialog(float friction) {

    }

    @Override
    public void openActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                unSelectAll();
                mGeneral.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_black_horizontal);
                hideBikationReview();
                break;
            case 1:
                unSelectAll();
                mMoreInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_black_horizontal);
                hideBikationReview();
                break;
            case 2:
                unSelectAll();
                mReview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.general_black_horizontal);
                showBikationReview();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setViewPager(int position) {
        mDetailsPager.setCurrentItem(position);
    }

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        mProgress.setVisibility(View.GONE);
        bikationDetails = (BikationDetails) response;
        Picasso.with(this).load(bikationDetails.getResult().getData().getConductor().getImage().getFull())
                .placeholder(R.drawable.place_holder).fit().centerCrop().into(mDetailsImage);
        mBikerRating.setRating(Float.parseFloat(bikationDetails.getResult().getData().getConductor().getAvgRating()));
        mBikationClub.setText(bikationDetails.getResult().getData().getConductor().getName());
        loadDetailsFragments(new Bundle());
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        mProgress.setVisibility(View.GONE);
    }
}