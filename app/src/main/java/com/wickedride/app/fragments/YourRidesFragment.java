package com.wickedride.app.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;
import com.wickedride.app.adapters.YourRidesAdapter;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.all_bike_models.BikeInfoResultModel;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class YourRidesFragment extends BaseFragment {

    public static final String TAG = "YOUR_RIDES_FRAGMENT";
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.tv_pickup_date)
    TextView mPickUpDate;

    @InjectView(R.id.tv_drop_date)
    TextView mDropDate;

    @InjectView(R.id.calender_container)
    LinearLayout calendarLayout;

    @InjectView(R.id.progressLayout)
    RelativeLayout mProgressLayout;

    @InjectView(R.id.error_msg)
    TextView errorMsg;

    private boolean pickUp = true;
    private int pickUpDate;
    private String mStartDate = null, mEndDate = null, mStartTime = null, mDropTime = null;
    private Dialog mDialog;
    public String category;

    int startTime = 9;/*Morning 9 am*/
    int endTime = 22;/*Night 9pm*/

    private ArrayList<String> slots;
    private boolean isPickUp,isDrop;
    private boolean isPickUpSelected;
    private boolean isWithDate;
    private TextView selectedDateMonthYear;
    private boolean userSelectedSameDay;

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.your_rides);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_your_rides, container, false);
        category = getArguments().getString(Constants.CATEGORY);
        ButterKnife.inject(this, mView);
        errorMsg.setVisibility(View.GONE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(Constants.ON_RESULT_CHANGED));
        if(category != null && !category.isEmpty()) {
            fetchAllBikes(category);
        }
        if(category.equals(Constants.BIKATION)){
            calendarLayout.setVisibility(View.GONE);
            ((BaseActivity) getActivity()).setActionBarTitle(R.string.bikation);
        }else{
            ((BaseActivity) getActivity()).setActionBarTitle(R.string.your_rides);
        }
        WickedRideManager.getInstance(getActivity()).setPickUpDate("");
        WickedRideManager.getInstance(getActivity()).setPickupTime("");
        WickedRideManager.getInstance(getActivity()).setDropDate("");
        WickedRideManager.getInstance(getActivity()).setDropTime("");
        WickedRideManager.getInstance(getActivity()).setBookingPrice("");
        WickedRideManager.getInstance(getActivity()).setNoOfDaysHrs("");
        if(WickedRideManager.getInstance(getActivity()).getBrandsSaved() !=null) {
            WickedRideManager.getInstance(getActivity()).getBrandsSaved().clear();
        }
        WickedRideManager.getInstance(getActivity()).setSelectedArea("0");
        WickedRideManager.getInstance(getActivity()).setAreaId("");

        return mView;
    }



    @OnClick({R.id.tv_pickup_date, R.id.tv_drop_date, R.id.pick_up_holder, R.id.drop_holder})
    public void onClickListener(View view) {
        if(view.getId() == R.id.tv_drop_date || view.getId() == R.id.drop_holder) {
            if(isPickUpSelected) {
                showDatePickerDialog(view);
            }else{
                showToast("Please select Pick up date and time.");
            }
        }else{
            showDatePickerDialog(view);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String fragmentToRefresh = intent.getStringExtra(Constants.ON_RESULT_CHANGED);
            if (Constants.YOUR_RIDES_FRAGMENT.equals(fragmentToRefresh)) {
                if (intent.hasExtra(Constants.FILTER)) {
                    String areaId = intent.getStringExtra(Constants.AREA_ID);
                    String makeId = intent.getStringExtra(Constants.MAKE_ID);
                    String startDate = intent.getStringExtra(Constants.START_DATE);
                    String dropDate = intent.getStringExtra(Constants.DROP_DATE);
                    String startTime = intent.getStringExtra(Constants.START_TIME);
                    String dropTime = intent.getStringExtra(Constants.DROP_TIME);
                    if(startDate != null && dropDate != null && startTime != null && dropTime != null){
                        isWithDate = true;
                        mPickUpDate.setText(startDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(startDate.split("-")[1])))+" "+startDate.split("-")[0]+"\n"+startTime);
                        mDropDate.setText(dropDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(dropDate.split("-")[1])))+" "+dropDate.split("-")[0]+"\n"+dropTime);
                    }
                    if (areaId!=null && makeId!=null) {
                        fetchAllBikes(areaId, makeId, startDate, dropDate, startTime, dropTime);
                    }
                } else {
                    isWithDate = false;
                    fetchAllBikes(category);
                }
            }
        }
    };

    private void fetchAllBikes(String category) {
        mProgressLayout.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.GONE);

        if(category.equals(Constants.BIKATION)){
            String apiMethod = APIMethods.BIKATIONS;
            placeRequest(apiMethod, BikeInfoResultModel.class);
        }else {
            String apiMethod = APIMethods.GET_ALL_CITIES + "/" + SessionManager.getUserCityID(getActivity()) + "/" + APIMethods.GET_ALL_BIKES;
            placeRequest(apiMethod, BikeInfoResultModel.class);
        }
    }

    private void fetchAllBikes(String areaId, String makeId, String startDate, String dropDate, String startTime, String dropTime) {
        mProgressLayout.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.GONE);

        String apiMethod = APIMethods.GET_ALL_CITIES + "/" + SessionManager.getUserCityID(getActivity()) + "/" + APIMethods.GET_ALL_BIKES;
        HashMap<String, String> params = new HashMap<>();
        if (makeId != null)
            params.put(Constants.MAKE_IDS, makeId);
        if (areaId != null)
            params.put(Constants.AREA_ID, areaId);
        if (startDate != null)
            params.put(Constants.START_DATE, startDate);
        if (dropDate != null)
            params.put(Constants.DROP_DATE, dropDate);
        if (startTime != null)
            params.put(Constants.START_TIME, startTime);
        if (dropTime != null)
            params.put(Constants.DROP_TIME, dropTime);
        placeRequest(apiMethod, BikeInfoResultModel.class, params);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        mProgressLayout.setVisibility(View.GONE);
        errorMsg.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        Log.d("YourRidesFragment", "ApiMethod::" + apiMethod);
        if (apiMethod.contains(APIMethods.GET_ALL_BIKES) || apiMethod.contains(APIMethods.BIKATIONS)) {
            BikeInfoResultModel bikeInfoResult = (BikeInfoResultModel) response;
            if(bikeInfoResult.getResult().getData().size() >0) {
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setLayoutManager(llm);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setItemAnimator(new LandingAnimator());
                YourRidesAdapter adapter = new YourRidesAdapter(getActivity(), bikeInfoResult.getResult().getData(), category, isWithDate);
//          SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
                ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(adapter);
                mRecyclerView.setAdapter(scaleAdapter);
                adapter.notifyDataSetChanged();
                scaleAdapter.notifyDataSetChanged();
            }else{
                mRecyclerView.setVisibility(View.GONE);
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText("No bikes available.");
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        mProgressLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        super.onErrorResponse(error, apiMethod);
        if(error instanceof com.android.volley.NoConnectionError){
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText("No internet connection.");
        }
        Log.d("YourRidesFragment", "OnError::" + error);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }


    /*
     *    @ Nihar : Old Code Snippet
     */
    //=========================================================================================//
//    private void showDatePickerDialog(View v) {
//        /*({R.id.tv_pickup_date, R.id.tv_drop_date, R.id.pick_up_holder, R.id.drop_holder})*/
//        final int id = v.getId();
//        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {
//
//            showToast("Select Pickup date and time");
//            isPickUp = true;
//            isDrop = false;
//
//        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
//            showToast("Select Drop date and time");
//            isPickUp = false;
//            isDrop = true;
//        }
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        if (!pickUp) {
//
//            c.set(Calendar.DAY_OF_MONTH, pickUpDate + 0);
//        }
//
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePickerDialog datePickerDialog, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {
//                            mStartDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                            mPickUpDate.setText(dayOfMonth +" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            selectedDateMonthYear.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            WickedRideManager.getInstance(getActivity()).setPickUpDate(dayOfMonth +" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            pickUp = false;
//                            pickUpDate = dayOfMonth;
//
//                        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
//                            mEndDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                            if(mStartDate.equals(mEndDate)){
//                                userSelectedSameDay = true;
//                            }
//                            mDropDate.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            selectedDateMonthYear.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            WickedRideManager.getInstance(getActivity()).setDropDate(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//
//                        }
////                        showTimePicker(id);
//                        showTimeSlots();
//                    }
//                }, mYear, mMonth, mDay);
//
//        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
//        datePickerDialog.setMinDate(c);
//
//        datePickerDialog.setYearRange(mYear, mYear + 1);
//        datePickerDialog.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
//        datePickerDialog.setCancelable(false);
////      datePickerDialog.setStyle(R.style.datePickerTheme, R.style.AppTheme);
//
//        // Initialize Time slot selectiong dialog
//        mDialog = new Dialog(getActivity());
//        mDialog.setCancelable(false);
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.dialog_time_slots);
//        selectedDateMonthYear = (TextView) mDialog.findViewById(R.id.month);
//
//    }
//
//    private void showTimeSlots() {
//
//        slots = filteredTimeSlots();
//
//        TextView textView = (TextView) mDialog.findViewById(R.id.action_title);
//        if (isDrop) {
//            textView.setText("DROP TIME SLOT");
//        } else{
//            textView.setText("PICK TIME SLOT");
//        }
//        Button done = (Button) mDialog.findViewById(R.id.done);
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isDrop) {
//                    mDialog.dismiss();
//                    isWithDate = true;
//                    if(mDropTime == null){
//                        mDropTime = slots.get(0);
//                    }
//                    mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
//
////                    mDropDate.setText(mEndDate+ '\n'+mDropTime);
//                    WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
//                    fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
//                }else{
//                    mDialog.dismiss();
//                    mStartTime = slots.get(0);
//                    mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
//
////                    mPickUpDate.setText(mStartDate+ '\n'+mStartTime);
//                    WickedRideManager.getInstance(getActivity()).setPickupTime(mStartTime);
//
//                    isPickUpSelected = true;
//                    showDatePickerDialog(mView.findViewById(R.id.tv_drop_date));
//                }
//            }
//        });
//
//        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radio_group_time_slots);
//        for (int i = 0; i < slots.size(); i++) {
//            RadioButton rb = new RadioButton(getActivity());
//            RadioGroup.LayoutParams radioParams = (new RadioGroup.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//
//
//            int expectedDpFont = (int) Util.convertDpToPixel(17, getActivity());
//            int expectedSpFont = (int) Util.pixelsToSp(getActivity(), expectedDpFont);
//            rb.setTextSize(expectedSpFont);
//
//            int dpMargin = 9;
//            int pixelMargin = (int) Util.convertDpToPixel(dpMargin, getActivity());
//            radioParams.setMargins(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
//            rb.setPadding(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
//            rb.setLayoutParams(radioParams);
//
//            rb.setTextColor(getResources().getColor(R.color.black));
//            rb.setButtonDrawable(R.drawable.check_circle);
//            rb.setText(slots.get(i));
//            rb.setId(i);
//            if (i == 0) rb.setChecked(true);
//            radioGroup.addView(rb);
//        }
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if(isPickUp){
//                    mStartTime = slots.get(checkedId);
//                    mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]) + 1))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
//
////                    mPickUpDate.setText(mStartDate+ '\n'+mStartTime);
////                    mDialog.dismiss();
//                }else{
//                    mDropTime = slots.get(checkedId);
//                    mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])+1))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
////                    mDropDate.setText(mEndDate + '\n' + mDropTime);
//                    WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
//
////                    mDialog.dismiss();
//                }
//                Log.d("",""+slots.get(checkedId));
//            }
//        });
//
//
//       /* LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(llm);
//        recyclerView.setHasFixedSize(true);
//        TimeSlotsAdapter adapter = new TimeSlotsAdapter(getActivity(), slots);
//        recyclerView.setAdapter(adapter);*/
//        mDialog.show();
//    }
//
//    /*private void showTimePicker(final int id) {
//        final Calendar now = Calendar.getInstance();
//        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
//                        Log.d("TimePicker", "Dialog onTimeSet");
//                        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {
//                            Log.d("PickYourRide", "Start Time::" + i + ":" + i1);
//                            mStartTime = "" + i + ":" + i1;
//                        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
//                            Log.d("PickYourRide", "Drop Time::" + i + ":" + i1);
//                            mDropTime = "" + i + ":" + i1;
//                            fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
//                        }
//                    }
//                },
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                false
//        );
//        tpd.setAccentColor(Color.parseColor("#000000"));
//        tpd.setStartTime(now.get(Calendar.HOUR_OF_DAY), 0);
//        tpd.show(getActivity().getFragmentManager(), TIMEPICKER_TAG);
//    }*/
//
//    /*1.Login for Pickup and Drop Down Dates
//    *
//    * Intially check if drop down date is already provided
//    *   else pop user to select pickup
//    *
//    * For PickUp Drop down hardcode or force start date and time in correct format as per current time
//    *   */
//
//    private ArrayList<String> getTimeSlots() {
//        return getTimeSlots(startTime, endTime);
//    }
//
//    private ArrayList<String> getTimeSlots(int from, int to) {
//        /* Allocating one hour slots from morning 10:00:00 to night 21:00:00 */
//        String slots = ":00:00";
//        ArrayList<String> timeSlots = new ArrayList<>();
//        for (int i = from; i <= to; i++) {
//            String startTime = i + slots;
////            String endTime = (i + 1) + slots;
//            String timeFormat = startTime;
//            timeSlots.add(timeFormat);
//        }
//        return timeSlots;
//
//    }
//
//    private ArrayList<String> filteredTimeSlots() {
//
//        Calendar now = Calendar.getInstance();
//        int currentHour = 0;
//        if(userSelectedSameDay){
//            currentHour = Integer.parseInt(mStartTime.split(":")[0]);
//            currentHour += 1;
//        }else{
//            // Check if selected start date is not same current date to display current hour from 10am or from current next hour.
//            if(!mStartDate.split("-")[2].equals(""+now.get(Calendar.DAY_OF_MONTH)) ||  (mEndDate !=null && !mEndDate.split("-")[2].equals(""+now.get(Calendar.DAY_OF_MONTH)))) {
//                currentHour = startTime;
//            }else {
//                currentHour = now.get(Calendar.HOUR_OF_DAY) + 1;
//            }
//        }
//        if (currentHour <= startTime || currentHour >= endTime)
//            return getTimeSlots();
//        else {
//            return getTimeSlots(currentHour, endTime);
//        }
//
//    }
    //=========================================================================================//

    private void showDatePickerDialog(View v) {
        /*({R.id.tv_pickup_date, R.id.tv_drop_date, R.id.pick_up_holder, R.id.drop_holder})*/
        final int id = v.getId();
        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {

            showToast("Select Pickup date and time");
            isPickUp = true;
            isDrop = false;

        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
            showToast("Select Drop date and time");
            isPickUp = false;
            isDrop = true;
        }

        final Calendar c = Calendar.getInstance();
        // Need to debug and correct
        if(!isPickUp)
        {
            // Setting the selected pick up date as current date as drop
            if(mStartDate!= null && mStartTime!= null)
            {
                int y = Integer.parseInt(mStartDate.split("-")[0]);
                int m = Integer.parseInt(mStartDate.split("-")[1]) - 1;
                int d = Integer.parseInt(mStartDate.split("-")[2]);
                c.set(y, m , d);

                if(Integer.parseInt(mStartTime.split(":")[0]) < endTime)
                    c.set(y, m , d);
                else
                    c.set(y, m , d + 1);
            }
            else
                showToast("Select Pick up Date and Time");

        }else {
            if(c.get(Calendar.HOUR_OF_DAY) < endTime)
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
            else
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+ 1);
        }

        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (id == R.id.tv_pickup_date || id == R.id.pick_up_holder) {
                            mStartDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                            mPickUpDate.setText(dayOfMonth + " " + (Util.getMonthInWords(monthOfYear + 1))+" "+year);
                            selectedDateMonthYear.setText(dayOfMonth + " " + (Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            WickedRideManager.getInstance(getActivity()).setPickUpDate(dayOfMonth + " " + (Util.getMonthInWords(monthOfYear + 1)) + " " + year);
                        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
                            mEndDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            if(mStartDate.equals(mEndDate))
                                userSelectedSameDay = true;
                            else
                                userSelectedSameDay = false;

//                            mDropDate.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
                            selectedDateMonthYear.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            WickedRideManager.getInstance(getActivity()).setDropDate(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear+1))+" "+year);

                        }

                        showTimeSlots();
                    }



                },c.get(Calendar.YEAR) , c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
        datePickerDialog.setMinDate(c);
        datePickerDialog.autoDismiss(true);
        datePickerDialog.setYearRange(c.get(Calendar.YEAR), c.get(Calendar.YEAR) + 1);
        datePickerDialog.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
       // datePickerDialog.setCancelable(false);

        mDialog = new Dialog(this.getActivity());
      //  mDialog.setCancelable(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_time_slots);
        selectedDateMonthYear = (TextView) mDialog.findViewById(R.id.month);

    }

    private void showTimeSlots() {

        slots = filteredTimeSlots();

        TextView textView = (TextView) mDialog.findViewById(R.id.action_title);
        if (isDrop) {
            textView.setText("DROP TIME SLOT");
        } else{
            textView.setText("PICK TIME SLOT");
        }
        Button done = (Button) mDialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrop) {
                    mDialog.dismiss();

//                    WickedRideManager.getInstance(getActivity()).setPickupTime(Util.removeSeconds(mStartTime));
//                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0]);
//                    WickedRideManager.getInstance(getActivity()).setDropTime(Util.removeSeconds(mDropTime));
//                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1]))) + " " + mEndDate.split("-")[0]);

                    mPickUpDate.setText(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0] + '\n' + mStartTime);
                    WickedRideManager.getInstance(getActivity()).setPickupTime(Util.removeSeconds(mStartTime));
                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate);
                    mDropDate.setText(mEndDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1]))) + " " + mEndDate.split("-")[0] + '\n' +mDropTime);
                    WickedRideManager.getInstance(getActivity()).setDropTime(Util.removeSeconds(mDropTime));
                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate);
                    isWithDate = true;
                    fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
                }else{
                    mDialog.dismiss();
                    //   mStartTime = slots.get(0);
                    isPickUpSelected = true;
                    showDatePickerDialog(mView.findViewById(R.id.tv_drop_date));
                }
            }
        });

        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radio_group_time_slots);
        for (int i = 0; i < slots.size(); i++) {
            RadioButton rb = new RadioButton(getActivity());
            RadioGroup.LayoutParams radioParams = (new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            int expectedDpFont = (int) Util.convertDpToPixel(17, getActivity());
            int expectedSpFont = (int) Util.pixelsToSp(getActivity(), expectedDpFont);
            rb.setTextSize(expectedSpFont);

            int dpMargin = 9;
            int pixelMargin = (int) Util.convertDpToPixel(dpMargin, getActivity());
            radioParams.setMargins(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
            rb.setPadding(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
            rb.setLayoutParams(radioParams);

            rb.setTextColor(getResources().getColor(R.color.black));
            rb.setButtonDrawable(R.drawable.check_circle);
            rb.setText(slots.get(i));
            rb.setId(i);
//            if (i == 0) rb.setChecked(true);
            if(!isDrop)
                mStartTime = slots.get(0);
            else
                mDropTime = slots.get(0);
            //mDropTime = slots.get(slots.size() - 1);

            radioGroup.addView(rb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(isPickUp){
                    mStartTime = slots.get(checkedId);
                    // mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
                }else{
                    mDropTime = slots.get(checkedId);
                    // mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
                    // WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
                }

                Log.d("",""+slots.get(checkedId));

                if(isDrop) {
                    mDialog.dismiss();

//                    WickedRideManager.getInstance(getActivity()).setPickupTime(Util.removeSeconds(mStartTime));
//                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0]);
//                    WickedRideManager.getInstance(getActivity()).setDropTime(Util.removeSeconds(mDropTime));
//                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1]))) + " " + mEndDate.split("-")[0]);

                    mPickUpDate.setText(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0] + '\n' + mStartTime);
                    WickedRideManager.getInstance(getActivity()).setPickupTime(mStartTime);
                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate);
                    mDropDate.setText(mEndDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1]))) + " " + mEndDate.split("-")[0] + '\n' + mDropTime);
                    WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate);
                    isWithDate = true;
                    fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
                }else{
                    mDialog.dismiss();
                    //   mStartTime = slots.get(0);
                    isPickUpSelected = true;
                    showDatePickerDialog(mView.findViewById(R.id.tv_drop_date));
                }

            }
        });

        mDialog.show();
    }


    private ArrayList<String> filteredTimeSlots() {

        ArrayList <String> tempSlots ;

        Calendar now = Calendar.getInstance();

        if (!isDrop)
        {
            if(!(Integer.parseInt(mStartDate.split("-")[2]) == now.get(Calendar.DAY_OF_MONTH)))
            {
                tempSlots = getAllTimeSlots();
            }
            else
            {
                if(now.get(Calendar.HOUR_OF_DAY) < startTime)
                    tempSlots = getAllTimeSlots();
                else
                    tempSlots = getAvailableTimeSlots(now.get(Calendar.HOUR_OF_DAY) + 1 , endTime);
            }
        }
        else {
            if (userSelectedSameDay) {
                tempSlots = getAvailableTimeSlots(Integer.parseInt(mStartTime.split(":")[0]) + 1, endTime);
            } else {
                tempSlots = getAllTimeSlots();
            }
        }
        return tempSlots;
    }

    private ArrayList<String> getAllTimeSlots() {
        return getTimeSlots(startTime, endTime);
    }

    private ArrayList<String> getAvailableTimeSlots(int from , int to) {
        return getTimeSlots(from, to);
    }

    private ArrayList<String> getTimeSlots(int from, int to) {
        /* Allocating one hour slots from morning 9:00:00 to night 22:00:00 */
        String slots = ":00";
        ArrayList<String> timeSlots = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            String startTime = i + slots;
            String timeFormat = startTime;
            timeSlots.add(timeFormat);
        }
        return timeSlots;

    }
}
