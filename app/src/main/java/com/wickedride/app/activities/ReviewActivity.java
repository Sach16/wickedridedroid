package com.wickedride.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.models.SignInResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Madhumita on 19-01-2016.
 */
public class ReviewActivity extends BaseDefaultActionActivity {
    @InjectView(R.id.review_msg)
    EditText mReviewMsg;
    @InjectView(R.id.review_title)
    EditText mReviewTitle;
    @InjectView(R.id.biker_rating)
    RatingBar mRating;
    String ratingValue;
    String bikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
         bikeId=getIntent().getExtras().getString(Constants.BIKE_ID);
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i("RatingValue", String.valueOf(rating));
                ratingValue = String.valueOf(rating);
            }
        });

    }
    @OnClick(R.id.close)
    public void closeScreen(){
        onBackPressed();
    }
    @OnClick(R.id.send_review)
    public void sendReview(){
        String review_title= mReviewTitle.getText().toString();
        String review_msg= mReviewMsg.getText().toString();
        if (!review_title.isEmpty() && !review_msg.isEmpty() && !ratingValue.isEmpty())
        if(Util.isNetworkOnline(this)){
            HashMap<String, String> params = new HashMap<>();
            params.put(Constants.RATING,ratingValue );
            params.put(Constants.REVIEW_TITLE,review_title );
            params.put(Constants.REVIEW_TEXT, review_msg);
            placeRequest(APIMethods.BIKATIONS+"/"+bikeId+APIMethods.REVIEWS, SignInResult.class, params, true);
        }
        else{
            showToast("No Internet Connection");
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        showToast("Some Error Occured. Please submit your review again.");
    }
}

