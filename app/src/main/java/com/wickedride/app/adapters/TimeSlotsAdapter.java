/*
package com.wickedride.app.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;
import com.wickedride.app.models.TimeSlot;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

*
 * Created by Nitish Kulkarni on 2/8/15.


public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotsViewHolder> {
    private final Activity mActivity;
    private ArrayList<TimeSlot> mSlots;
    private int mPreviousPosition = -1;

    public TimeSlotsAdapter(Activity activity, ArrayList<TimeSlot> slots) {
        this.mSlots = slots;
        this.mActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TimeSlotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_time_slot, parent, false);
        TimeSlotsViewHolder viewHolder = new TimeSlotsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TimeSlotsViewHolder holder, int position) {
        holder.slotTime.setText(mSlots.get(position).getTimeSlot());
        holder.slotCheck.setTag("CheckBox" + position);
        final int itemPosition = position;
        holder.slotCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                mSlots.get(itemPosition).setChecked(isChecked);
                holder.slotCheck.setChecked(mSlots.get(itemPosition).isChecked());
                ((BaseActivity) mActivity).showToast(mSlots.get(itemPosition)
                        .getTimeSlot() + " Selected");
                if (mPreviousPosition != -1) {
                    mSlots.get(mPreviousPosition).setChecked(false);
                }
            }
        });

        mPreviousPosition = itemPosition;
    }


    @Override
    public int getItemCount() {
        return mSlots.size();
    }

    class TimeSlotsViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.slot_time)
        TextView slotTime;
        @InjectView(R.id.slot_check)
        CheckBox slotCheck;

        public TimeSlotsViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
*/
