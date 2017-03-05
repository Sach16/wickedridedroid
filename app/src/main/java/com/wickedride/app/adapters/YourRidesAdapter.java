package com.wickedride.app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wickedride.app.R;
import com.wickedride.app.activities.BikationDetailsActivity;
import com.wickedride.app.activities.BikeDetailsActivity;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.all_bike_models.Datum;
import com.wickedride.app.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class YourRidesAdapter extends RecyclerView.Adapter<YourRidesAdapter.YourRidesViewHolder> {
    private final Activity mContext;
    private final boolean isWithDates;
    private String mCategory;
    private List<Datum> mBikeInfo;

    public YourRidesAdapter(Activity context, List<Datum> yourRides, String category, boolean isWithDates) {
        this.mBikeInfo = yourRides;
        this.mContext = context;
        this.mCategory = category;
        this.isWithDates = isWithDates;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public YourRidesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_your_rides, parent, false);
        YourRidesViewHolder viewHolder = new YourRidesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(YourRidesViewHolder holder, int position) {

        if (mBikeInfo.get(position).getImage() != null){
            Picasso.with(mContext).load(mBikeInfo.get(position).getImage().getFull())
                    .placeholder(R.drawable.place_holder).into(holder.bikeImage);
        }
        holder.bikeImage.setTag("BIKE_IMAGE" + position);
//        holder.bikeLogo.setImageResource(mBikeInfo.get(position).getLogo());
        if(isWithDates){
            holder.bikeDetailsWithOutDate.setVisibility(View.GONE);
            holder.bikeDetailsWithDate.setVisibility(View.VISIBLE);
            holder.bikeName.setText(mBikeInfo.get(position).getName());
            if(mBikeInfo.get(position).getAvailableLocations().get(0).getPriceDetails().getFinalPrice()!=null) {
                holder.bikePrice.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(mBikeInfo.get(position).getAvailableLocations().get(0).getPriceDetails().getFinalPrice()) + "/-");
            }
            holder.perDay.setText("Rs." +NumberFormat.getNumberInstance(Locale.US).format(mBikeInfo.get(position).getAvailableLocations().get(0).getPriceDetails().getEffectivePrice()) + "/hour");
            if(mBikeInfo.get(position).getAvailableLocations().size() == 1) {
                holder.bikeLocation.setText(mBikeInfo.get(position).getAvailableLocations().get(0).getArea());
                if(mBikeInfo.get(position).getAvailableLocations().get(0).getBikeAvailabilityStatus().getBikeId().equals("none")){
                    holder.soldOutOverlay.setVisibility(View.VISIBLE);
                    mBikeInfo.get(position).setSoldOut(true);
                    holder.soldOutText.setVisibility(View.VISIBLE);

                }else{
                    holder.soldOutOverlay.setVisibility(View.GONE);
                    mBikeInfo.get(position).setSoldOut(false);
                    holder.soldOutText.setVisibility(View.GONE);

                }
            }else{
               int placeCount = (mBikeInfo.get(position).getAvailableLocations().size()-1);
                holder.bikeLocation.setText(mBikeInfo.get(position).getAvailableLocations().get(0).getArea()+" & "+ placeCount+ ((placeCount == 1) ?  " place ": " places"));
                for(int i=0;i<mBikeInfo.get(position).getAvailableLocations().size();i++){
                    if(!mBikeInfo.get(position).getAvailableLocations().get(i).getBikeAvailabilityStatus().getBikeId().equals("none")){
                        holder.soldOutOverlay.setVisibility(View.GONE);
                        mBikeInfo.get(position).setSoldOut(false);
                        holder.soldOutText.setVisibility(View.GONE);
                        break;
                    }else {
                        holder.soldOutOverlay.setVisibility(View.VISIBLE);
                        holder.soldOutText.setVisibility(View.VISIBLE);
                        mBikeInfo.get(position).setSoldOut(true);
                    }
                }
            }
        }else {
            holder.bikeDetailsWithOutDate.setVisibility(View.VISIBLE);
            holder.bikeDetailsWithDate.setVisibility(View.GONE);
            if(mBikeInfo.get(position).getName() != null) {
                holder.bikeNameWo.setText(mBikeInfo.get(position).getName());
            }
            if(mBikeInfo.get(position).getTitle() != null){
                holder.bikeNameWo.setText(mBikeInfo.get(position).getTitle());
            }
            if(mBikeInfo.get(position).getConductor() != null && mBikeInfo.get(position).getConductor().getName() != null){
                holder.bikationConductorName.setVisibility(View.VISIBLE);
                holder.bikationConductorName.setText(mBikeInfo.get(position).getConductor().getName()+"");
            }
            if(mBikeInfo.get(position).getAvailableLocations() != null && mBikeInfo.get(position).getAvailableLocations().size() >0) {
                holder.bikePriceWo.setText(/*"Rs. " + */mBikeInfo.get(position).getAvailableLocations().get(0).getPrice().get(1));
                holder.perDayWo.setText(/*"Rs. " + */mBikeInfo.get(position).getAvailableLocations().get(0).getPrice().get(0));
                if(mBikeInfo.get(position).getAvailableLocations().size() == 1) {
                    holder.bikeLocationWo.setText(mBikeInfo.get(position).getAvailableLocations().get(0).getArea());
                }else{
                    int placeCount = (mBikeInfo.get(position).getAvailableLocations().size()-1);
                    holder.bikeLocationWo.setText(mBikeInfo.get(position).getAvailableLocations().get(0).getArea()+" & "+ placeCount +((placeCount == 1) ?  " place " : " places "));
                }
            }else{
                holder.bikePriceWo.setVisibility(View.GONE);
                holder.perDayWo.setVisibility(View.GONE);
                holder.bikeLocationWo.setVisibility(View.GONE);
                holder.bikationTripDistance.setVisibility(View.VISIBLE);
                holder.bikationStartDate.setVisibility(View.VISIBLE);
                holder.bikationStartDate.setText(mBikeInfo.get(position).getStartDate() + " (" + mBikeInfo.get(position).getDuration() + ")");
                holder.bikationTripDistance.setText(mBikeInfo.get(position).getDistance()+"");

            }
        }
    }

    @Override
    public int getItemCount() {
        return mBikeInfo.size();
    }

    class YourRidesViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.bikes_holder)
        CardView cardView;
        @InjectView(R.id.bike_image)
        ImageView bikeImage;
        @InjectView(R.id.bike_logo)
        ImageView bikeLogo;
        @InjectView(R.id.bike_name)
        TextView bikeName;
        @InjectView(R.id.bike_price)
        TextView bikePrice;
        @InjectView(R.id.per_day)
        TextView perDay;
        @InjectView(R.id.bike_location)
        TextView bikeLocation;
        @InjectView(R.id.bike_details_with_date)
        RelativeLayout bikeDetailsWithDate;
        @InjectView(R.id.bike_details_wo_date)
        RelativeLayout bikeDetailsWithOutDate;
        @InjectView(R.id.bike_name_wo)
        TextView bikeNameWo;
        @InjectView(R.id.bike_price_wo)
        TextView bikePriceWo;
        @InjectView(R.id.per_day_wo)
        TextView perDayWo;
        @InjectView(R.id.bike_location_wo)
        TextView bikeLocationWo;
        @InjectView(R.id.bikation_conductor_name)
        TextView bikationConductorName;
        @InjectView(R.id.bikation_trip_distance)
        TextView bikationTripDistance;
        @InjectView(R.id.bikation_startdate_days)
        TextView bikationStartDate;
        @InjectView(R.id.image_soldout_overlay)
        RelativeLayout soldOutOverlay;
        @InjectView(R.id.soldout_text)
        TextView soldOutText;

        public YourRidesViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.bikes_holder)
        public void onClickListener(View view) {
            int itemPosition = getAdapterPosition();
            if(mBikeInfo.get(itemPosition).isSoldOut()){
                return;
            }
            Intent openDetails = null;
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.bike_holder);

            if (Constants.RENT_RIDE.equals(mCategory)) {
                openDetails = new Intent(mContext, BikeDetailsActivity.class);
                openDetails.putExtra(Constants.BIKE_ID, mBikeInfo.get(itemPosition).getId());
                openDetails.putExtra(Constants.BIKE_NAME, mBikeInfo.get(itemPosition).getName());

                if(mBikeInfo.get(itemPosition).getAvailableLocations().get(0).getPriceDetails() != null) {
                    WickedRideManager.getInstance(mContext).setBookingPrice(mBikeInfo.get(itemPosition).getAvailableLocations().get(0).getPriceDetails().getFinalPrice() + "");
                    WickedRideManager.getInstance(mContext).setNoOfDaysHrs(mBikeInfo.get(itemPosition).getAvailableLocations().get(0).getPriceDetails().getNoOfDays() + "," + mBikeInfo.get(itemPosition).getAvailableLocations().get(0).getPriceDetails().getNoOfHours());
                }else{
                    WickedRideManager.getInstance(mContext).setBookingPrice("");
                    WickedRideManager.getInstance(mContext).setNoOfDaysHrs("");
                }
            } else {
                openDetails = new Intent(mContext, BikationDetailsActivity.class);
                openDetails.putExtra(Constants.BIKATION_ID, mBikeInfo.get(itemPosition).getId());
                openDetails.putExtra(Constants.BIKATION_TITLE, mBikeInfo.get(itemPosition).getTitle());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Get the transition name from the string
//                String transitionName = mContext.getString(R.string.transition_name);
//
//                // Define the view that the animation will start from
//                ImageView viewStart = (ImageView) relativeLayout.findViewById(R.id.bike_image);
//
//                ActivityOptionsCompat options =
//
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
//                                viewStart,   // Starting view
//                                transitionName    // The String
//                        );
//                //Start the Intent
//                ActivityCompat.startActivity(mContext, openDetails, options.toBundle());
                mContext.startActivity(openDetails);

            } else {
                mContext.startActivity(openDetails);
            }
        }
    }
}
