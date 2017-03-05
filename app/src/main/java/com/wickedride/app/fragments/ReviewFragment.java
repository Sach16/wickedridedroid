package com.wickedride.app.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.activities.BikationConfirmActivity;
import com.wickedride.app.adapters.BikationReviewAdapter;
import com.wickedride.app.models.BikationDetails;
import com.wickedride.app.models.Review;
import com.wickedride.app.utils.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class ReviewFragment extends BaseFragment {

    public static final String TAG = "REVIEW_FRAGMENT";

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.book_now)
    Button bookNow;
    BikationDetails bikationDetailsObj;

    private ArrayList<Review> mBikationReviews;
    private String bikationTitle;

    public static ReviewFragment newInstance() {
        final Bundle bundle = new Bundle();
        final ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.review);
    }

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.inject(this, mView);
        initializeData();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        bikationTitle = getArguments().getString(Constants.BIKATION_TITLE);

        BikationReviewAdapter adapter = new BikationReviewAdapter(getActivity(), mBikationReviews);
        mRecyclerView.setAdapter(adapter);
        return mView;
    }

    @OnClick({R.id.book_now})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.book_now:
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


    private void initializeData() {
        String strBikationDetails = getArguments().getString(Constants.BIKATION);
        bikationDetailsObj = new Gson().fromJson(strBikationDetails, BikationDetails.class);
        mBikationReviews = (ArrayList<Review>) bikationDetailsObj.getResult().getData().getReviews();

//        mBikationReviews = new ArrayList<>();
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 3, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 4, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 3, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 4, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 3, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 4, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 3, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
//        mBikationReviews.add(new BikationReview(R.drawable.hd_night_rod, "Best group, all thanks to Wicked Ride", 4, "By: John Syndal, ", "15/08/2015", "Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine \\n Iron 883 has a 4 valve engine that generates 50 bhp of excellent power and 69NM of torque which enhances the ride experience of this Harley machine"));
    }


    @Override
    public boolean canScrollVertically(int direction) {
        return mRecyclerView != null && mRecyclerView.canScrollVertically(direction);
    }
}
