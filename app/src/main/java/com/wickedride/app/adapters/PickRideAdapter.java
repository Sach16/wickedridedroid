package com.wickedride.app.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wickedride.app.R;
import com.wickedride.app.models.all_bike_models.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class PickRideAdapter extends BaseAdapter {

    private List<Datum> mBrands;
    private Activity mContext;
    private ViewHolder viewHolder;

    public PickRideAdapter(Activity context, List<Datum> result) {
        this.mContext = context;
        this.mBrands = result;
    }


    @Override
    public int getCount() {
        return mBrands.size();
    }

    @Override
    public Datum getItem(int position) {
        return mBrands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.adapter_pick_ride, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Log.d("PickRideAdapter", "Image URL::" + "http:" + mBrands.get(position).getLogo().getFull());
        Picasso.with(mContext).load(mBrands.get(position).getLogo().getFull())
                .placeholder(R.drawable.place_holder).into(viewHolder.brandImage);
        if(mBrands.get(position).isChecked()){
            viewHolder.checkImage.setVisibility(View.VISIBLE);
            viewHolder.checkImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.agree));
        }else{
            viewHolder.checkImage.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    class ViewHolder {
        @InjectView(R.id.brand_image)
        ImageView brandImage;
        @InjectView(R.id.brand_checkimage)
        ImageView checkImage;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
   public List<Datum> getBrandList(){
        return mBrands;
    }
}
