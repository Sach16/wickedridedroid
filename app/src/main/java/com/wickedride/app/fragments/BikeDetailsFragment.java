//package com.wickedride.app.fragments;
//
//
//import android.app.Fragment;
//import android.content.res.Resources;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.wickedride.app.R;
//import com.wickedride.app.activities.BaseNoActionBarActivity;
//import com.wickedride.app.views.CustomSpinner;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class BikeDetailsFragment extends BaseFragment {
//
//    @InjectView(R.id.image_holder)
//    RelativeLayout mBikeHolder;
//
//    @InjectView(R.id.bike_image)
//    ImageView mBikeImage;
//
//    @InjectView(R.id.bike_logo)
//    ImageView mBikeLogo;
//
//    @InjectView(R.id.bike_name)
//    TextView mBikeName;
//
//    @InjectView(R.id.bike_location)
//    CustomSpinner mBikeLocation;
//
//    @InjectView(R.id.bike_description)
//    TextView mBikeDescription;
//
//    @InjectView(R.id.show_more)
//    TextView mShowMore;
//
//    @InjectView(R.id.rental)
//    TextView mRental;
//
//    @InjectView(R.id.bike_price)
//    TextView mBikePrice;
//
//    @InjectView(R.id.book_now)
//    TextView mBookNow;
//
//    @InjectView(R.id.price_text)
//    LinearLayout mPriceText;
//
//    @InjectView(R.id.left_arrow)
//    ImageView mLeftArrow;
//
//    @InjectView(R.id.right_arrow)
//    ImageView mRightArrow;
//
//
//    @Override
//    public String getSelfTag() {
//        return null;
//    }
//
//    @Override
//    public CharSequence getTitle(Resources r) {
//        return null;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        mView = inflater.inflate(R.layout.activity_bike_details, container, false);
//        ButterKnife.inject(this, mView);
//        ((BaseNoActionBarActivity) getActivity()).setWhiteBackButton();
//        return mView;
//    }
//
//    @OnClick({R.id.book_now, R.id.left_arrow, R.id.right_arrow})
//    public void onClickListener(View view) {
//        switch (view.getId()) {
//            case R.id.book_now:
//                ((BaseNoActionBarActivity) getActivity()).loadBikeReserveFragment();
//                break;
//            case R.id.left_arrow:
//                showNextPrice();
//                break;
//            case R.id.right_arrow:
//                showPreviousPrice();
//                break;
//        }
//    }
//
//    private void showPreviousPrice() {
//        Animation rightAnim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.detail_slide_out_from_right);
//        mRental.startAnimation(rightAnim);
//        mBikePrice.startAnimation(rightAnim);
//        rightAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Animation leftAnim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.detail_slide_in_from_left);
//                mRental.startAnimation(leftAnim);
//                mBikePrice.startAnimation(leftAnim);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//       /* mPriceText.setVisibility(View.VISIBLE);
//        anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.slide_in_from_right);
//        mPriceText.startAnimation(anim);*/
//    }
//
//    private void showNextPrice() {
//        Animation leftAanim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.detail_slide_out_from_left);
//        mRental.startAnimation(leftAanim);
//        mBikePrice.startAnimation(leftAanim);
//        leftAanim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Animation rightAnim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.detail_slide_in_from_right);
//                mBikePrice.startAnimation(rightAnim);
//                mRental.startAnimation(rightAnim);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//
//    @Override
//    public boolean canScrollVertically(int direction) {
//        return false;
//    }
//}
