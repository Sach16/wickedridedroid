package com.wickedride.app.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wickedride.app.R;
import com.wickedride.app.activities.CityActivity;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.City;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private final Activity mActivity;
    private ArrayList<City> mCities;

    public CityAdapter(Activity activity, ArrayList<City> cities) {
        this.mActivity = activity;
        this.mCities = cities;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_city, parent, false);
        CityViewHolder viewHolder = new CityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.cityName.setText(mCities.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.city_holder)
        RelativeLayout cardView;
        @InjectView(R.id.city_name)
        TextView cityName;

        public CityViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.city_holder)
        public void onClickListener(View view) {
            int itemPosition = getAdapterPosition();
            SessionManager.setUserCity(mActivity, mCities.get(itemPosition).getCity(), mCities.get(itemPosition).getId());
            ((CityActivity) mActivity).openRideActivity();
        }
    }
}
