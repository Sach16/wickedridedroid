package com.wickedride.app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wickedride.app.R;
import com.wickedride.app.models.all_bike_models.Datum;
import com.wickedride.app.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class AccessoriesAdapter extends RecyclerView.Adapter<AccessoriesAdapter.AccessoriesViewHolder> {
    private final Activity mActivity;
    private ArrayList<Datum> mAccessories;
    private String selectedItems = "";

    public AccessoriesAdapter(Activity activity, ArrayList<Datum> accessories) {
        this.mAccessories = accessories;
        this.mActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AccessoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_accessories, parent, false);
        AccessoriesViewHolder viewHolder = new AccessoriesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AccessoriesViewHolder holder, int position) {
        if (mAccessories.get(position).getImage().getFull() != null){
            Picasso.with(mActivity).load(mAccessories.get(position).getImage().getFull())
                    .placeholder(R.drawable.place_holder).fit().centerCrop().into(holder.accessoriesImage);
        }
        holder.accessoriesName.setText(mAccessories.get(position).getName());
        holder.accessoriesDescription.setText(mAccessories.get(position).getDescription());
        holder.accessoriesPrice.setText(mAccessories.get(position).getRentalAmount());
        holder.accessoriesChecked.setTag("CheckBox" + position);
        final int itemPosition = position;
        holder.accessoriesChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String imageUrls = "";
                String prices = "";
                String itemNames = "";
                String accessoriesIds =  "";
                if(!mAccessories.get(itemPosition).isChecked()) {
                    mAccessories.get(itemPosition).setChecked(true);
                    if(selectedItems.isEmpty()) {
                        selectedItems = mAccessories.get(itemPosition).getDescription();
                        imageUrls = mAccessories.get(itemPosition).getImage().getFull()+",";
                        prices = mAccessories.get(itemPosition).getRentalAmount()+",";
                        itemNames = mAccessories.get(itemPosition).getDescription()+",";
                        accessoriesIds = mAccessories.get(itemPosition).getId()+",";
                    }else{
                        int count = 0;
                        for(int i= 0;i<mAccessories.size();i++){
                            if(mAccessories.get(i).isChecked()) {
                                count += 1;
                                imageUrls += mAccessories.get(i).getImage().getFull()+",";
                                prices += mAccessories.get(i).getRentalAmount()+",";
                                itemNames += mAccessories.get(i).getDescription()+",";
                                accessoriesIds += mAccessories.get(i).getId()+",";
                            }
                        }
                        selectedItems = count+" more items";
                    }
                    Intent intent = new Intent(Constants.ON_ACCESSORIES_SELECTED);
                    intent.putExtra(Constants.ACCESSORY_PRICE, mAccessories.get(itemPosition).getAmount());
                    intent.putExtra(Constants.ACCESSORY_NAME, "+"+selectedItems);
                    intent.putExtra(Constants.ACCESSORY_IMAGES, imageUrls);
                    intent.putExtra(Constants.ACCESSORY_PRICE_LIST, prices);
                    intent.putExtra(Constants.ACCESSORIES_DESCRIPTION, itemNames);
                    intent.putExtra(Constants.ACCESSORIES_IDS, accessoriesIds);
                    LocalBroadcastManager.getInstance(mActivity.getApplicationContext()).sendBroadcast(intent);
                }else{
                    mAccessories.get(itemPosition).setChecked(false);
                    int count = 0;
                    for(int i = 0; i < mAccessories.size(); i++) {
                        if(mAccessories.get(i).isChecked()) {
                            count += 1;
                        }
                    }
                    if(count == 1){
                        for(int i = 0; i < mAccessories.size(); i++) {
                            if (mAccessories.get(i).isChecked()) {
                                selectedItems = "+" + mAccessories.get(i).getDescription();
                                imageUrls = mAccessories.get(i).getImage().getFull()+",";
                                prices = mAccessories.get(i).getRentalAmount()+",";
                                itemNames = mAccessories.get(i).getDescription()+",";
                                accessoriesIds = mAccessories.get(i).getId()+",";

                            }
                        }
                    }else if(count >1) {
                        if (selectedItems.equals(mAccessories.get(itemPosition).getDescription())) {
                            selectedItems = selectedItems.replace("+" + mAccessories.get(itemPosition).getDescription(), "");
                            imageUrls = imageUrls.replace(mAccessories.get(itemPosition).getImage().getFull()+",", "");
                            prices = prices.replace(mAccessories.get(itemPosition).getRentalAmount()+",","");
                            itemNames = itemNames.replace(mAccessories.get(itemPosition).getDescription()+",","");
                            accessoriesIds = accessoriesIds.replace(mAccessories.get(itemPosition).getId()+",","");
                        }
                    }else if(count == 0){
                        selectedItems = "";
                    }
                    Intent intent = new Intent(Constants.ON_ACCESSORIES_SELECTED);
                    intent.putExtra(Constants.ACCESSORY_PRICE, (-mAccessories.get(itemPosition).getAmount()));
                    intent.putExtra(Constants.ACCESSORY_NAME, selectedItems);
                    intent.putExtra(Constants.ACCESSORIES_DESCRIPTION, itemNames);
                    intent.putExtra(Constants.ACCESSORY_IMAGES, imageUrls);
                    intent.putExtra(Constants.ACCESSORY_PRICE_LIST, prices);
                    intent.putExtra(Constants.ACCESSORIES_IDS, accessoriesIds);
                    LocalBroadcastManager.getInstance(mActivity.getApplicationContext()).sendBroadcast(intent);
                }
            }
        });

        holder.accessoriesChecked.setChecked(mAccessories.get(position).isChecked());
    }


    @Override
    public int getItemCount() {
        return mAccessories.size();
    }

    class AccessoriesViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.accessories_holder)
        CardView cardView;
        @InjectView(R.id.accessories_image)
        ImageView accessoriesImage;
        @InjectView(R.id.accessories_name)
        TextView accessoriesName;
        @InjectView(R.id.accessories_description)
        TextView accessoriesDescription;
        @InjectView(R.id.accessories_price)
        TextView accessoriesPrice;
        @InjectView(R.id.accessories_checked)
        CheckBox accessoriesChecked;

        public AccessoriesViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
