//package com.wickedride.app.fragments;
//
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.wickedride.app.R;
//import com.wickedride.app.activities.BaseActivity;
//import com.wickedride.app.interfaces.OnSpinnerEventsListener;
//import com.wickedride.app.views.CustomSpinner;
//import com.wickedride.app.views.HeaderGridView;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
//import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by Nitish Kulkarni on 02-Sep-15.
// */
//public class PickYourRideFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
//        AbsListView.OnScrollListener, OnSpinnerEventsListener {
//
//    String[] spinnerValues = {"Bangalore", "Bangalore, Indiranager",
//            "Bangalore, Jayanagar", "Pune, Maharashtra", "Jaipur", "Udaipur"};
//    ArrayList<Integer> mImages = new ArrayList<>();
//    @InjectView(R.id.gridView)
//    HeaderGridView mGridView;
//
//    @InjectView(R.id.search)
//    Button mSearch;
//
//    private View mPickRideHeader;
//    private HeaderViewHolder headerViewHolder;
//
//    @Override
//    public boolean canScrollVertically(int direction) {
//        return false;
//    }
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
//        View view = inflater.inflate(R.layout.activity_pick_your_ride, container, false);
//        ButterKnife.inject(this, view);
//        ((BaseActivity) getActivity()).hideActionBarImage();
//        initHeader();
//        setDate();
//        return view;
//    }
//
//    @OnClick({R.id.search})
//    public void onClickListener(View view){
//        ((BaseActivity)getActivity()).unLoadPickYourRide();
//    }
//
//    private void initHeader() {
//        headerViewHolder = new HeaderViewHolder();
//        mPickRideHeader = ((LayoutInflater) this.getActivity()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
//                R.layout.header_pick_ride, null, false);
//        ButterKnife.inject(headerViewHolder, mPickRideHeader);
//        mGridView.addHeaderView(mPickRideHeader);
//
//        ArrayAdapter<CharSequence> spinnerCitiesArrayAdapter = new ArrayAdapter<CharSequence>(getActivity(),
//                R.layout.simple_spinner_item, spinnerValues);
//        headerViewHolder.spinnerCities.setAdapter(spinnerCitiesArrayAdapter);
//        headerViewHolder.spinnerCities.setOnItemSelectedListener(this);
//        headerViewHolder.spinnerCities.setSpinnerEventsListener(this);
//
//        mPickRideHeader.setVisibility(View.VISIBLE);
//        mImages.add(R.drawable.harley_logo);
//        mImages.add(R.drawable.re_logo);
//        mImages.add(R.drawable.triumph_logo);
//        mImages.add(R.drawable.kawasaki_logo);
//        mImages.add(R.drawable.indian_logo);
//        mImages.add(R.drawable.ducati_logo);
////        PickRideAdapter pickRideAdapter = new PickRideAdapter(getActivity(), mImages);
////        mGridView.setAdapter(pickRideAdapter);
//        mGridView.setOnScrollListener(this);
//    }
//
//    private void setDate() {
//        try {
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            String today = dateFormat.format(c.getTime());
//            c.setTime(dateFormat.parse(today));
//            c.add(Calendar.DATE, 2);
//            String tomorrow = dateFormat.format(c.getTime());
//            headerViewHolder.mPickUpDate.setText(today);
//            headerViewHolder.mDropDate.setText(tomorrow);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showDatePickerDialog(final int id) {
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                new OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePickerDialog datePickerDialog, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {
//                            headerViewHolder.mPickUpDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
//                            headerViewHolder.mDropDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                        }
////                        showTimePicker();
//                    }
//                }, mYear, mMonth, mDay);
//
//        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
//        datePickerDialog.setMinDate(c);
//        datePickerDialog.setYearRange(mYear, mYear + 1);
//        datePickerDialog.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
////        datePickerDialog.setStyle(R.style.datePickerTheme, R.style.AppTheme);
//    }
//
////    private void showTimePicker() {
////        final Calendar now = Calendar.getInstance();
////        TimePickerDialog tpd = TimePickerDialog.newInstance(
////                new TimePickerDialog.OnTimeSetListener() {
////                    @Override
////                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
////                        Log.d("TimePicker", "Dialog onTimeSet");
////                    }
////                },
////                now.get(Calendar.HOUR_OF_DAY),
////                now.get(Calendar.MINUTE),
////                false
////        );
////        tpd.setAccentColor(Color.parseColor("#000000"));
////        tpd.setStartTime(now.get(Calendar.HOUR_OF_DAY), 0);
////        tpd.show(getActivity().getFragmentManager(), TIMEPICKER_TAG);
////    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if (parent.getSelectedView() != null) {
//            ((TextView) parent.getSelectedView()).setPadding(0, 0, 0, 0);
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//    }
//
//    @Override
//    public void onSpinnerOpened() {
//        headerViewHolder.spinnerCities.setBackgroundResource(R.drawable.spinner_exp_bg_black);
//    }
//
//    @Override
//    public void onSpinnerClosed() {
//        headerViewHolder.spinnerCities.setBackgroundResource(R.drawable.spinner_bg_black);
//    }
//
//    public class HeaderViewHolder {
//
//        @InjectView(R.id.tv_pickup_date)
//        TextView mPickUpDate;
//
//        @InjectView(R.id.tv_drop_date)
//        TextView mDropDate;
//
//        @InjectView(R.id.spinner_cities)
//        CustomSpinner spinnerCities;
//
//        @OnClick({R.id.tv_pickup_date, R.id.tv_drop_date, R.id.pick_up_holder, R.id.drop_holder})
//        public void onClickListener(View view) {
//            showDatePickerDialog(view.getId());
//        }
//    }
//}
