package com.wickedride.app.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseNoActionBarActivity;
import com.wickedride.app.activities.DatePickerActivity;
import com.wickedride.app.interfaces.OnSpinnerEventsListener;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.GetResultData;
import com.wickedride.app.models.all_bike_models.BikeInfoResultModel;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.utils.ValidatorUtils;
import com.wickedride.app.views.WRProgressView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class ReserveFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        OnSpinnerEventsListener, ServerCallback {

    public static final String TAG = "RESERVE_FRAGMENT";

    public static final int REQUEST_PIVKUP_DATE = 6462;
    public static final int REQUEST_DROP_DATE = 6362;

    @InjectView(R.id.accessories)
    Button mAccessoriesBtn;

    @InjectView(R.id.tv_pickup_date)
    TextView mPickUpDate;

    @InjectView(R.id.tv_drop_date)
    TextView mDropDate;

//    @InjectView(R.id.spinner_cities)
//    CustomSpinner mSpinnerCities;

    @InjectView(R.id.biker_name)
    TextView mBikerName;

    @InjectView(R.id.biker_email)
    TextView mBikerEmail;

    @InjectView(R.id.biker_phone)
    TextView mBikerPhone;

    @InjectView(R.id.total_price)
    TextView totalPrice;

    @InjectView(R.id.days_small)
    TextView noOfDays;

    @InjectView(R.id.bike_name)
    TextView bikeName;

    @InjectView(R.id.bike_name_small)
    TextView bikeNameSmall;

    @InjectView(R.id.selected_location)
    TextView locationName;

    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;

    private TextView selectedDateMonthYear;

    StringBuilder tempAllDates;

    private String mStartDate = null, mEndDate = null, mStartTime = null, mDropTime = null;
    private Dialog mDialog;


    private ArrayList<String> slots;
    private boolean isDrop;
   // private boolean isPickUpSelected;

    private String bikeId,bikeImageUrl;
    final Calendar c = Calendar.getInstance();

    private BikeInfoResultModel bikeInfoResultModel;
    private Integer totalAmount;

    private boolean isPickUp_Check;

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.reserve);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_reserve, container, false);
        ButterKnife.inject(this, mView);
        ((BaseNoActionBarActivity) getActivity()).setBlackBackButton();
        if(SessionManager.isUserSessionValid(getActivity())){
            mBikerName.setText(SessionManager.getUserFirstName(getActivity()) + " " +SessionManager.getUserLastName(getActivity()));
            mBikerEmail.setText(SessionManager.getUserEmail(getActivity()));
            mBikerPhone.setText(SessionManager.getUserPhone(getActivity()));
            mBikerName.setEnabled(false);
            mBikerEmail.setEnabled(false);
            mBikerPhone.setEnabled(false);
        }

        bikeName.setText(getArguments().getString(Constants.BIKE_NAME) +" TODAY!");
        bikeNameSmall.setText(getArguments().getString(Constants.BIKE_NAME));
        bikeId = getArguments().getString(Constants.BIKE_ID);
        WickedRideManager.getInstance(getActivity()).setBikeId(bikeId);
        bikeImageUrl = getArguments().getString(Constants.BIKE_IMAGE_URL);
        locationName.setText(getArguments().getString(Constants.LOCATION));
        setDate();
       // getAvailableDates();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAvailableDates();
    }

    private void setDate() {


        if(WickedRideManager.getInstance(getActivity()).getBookingPrice() != null && WickedRideManager.getInstance(getActivity()).getBookingPrice().length() >0){
           // totalAmount = Integer.parseInt(WickedRideManager.getInstance(getActivity()).getBookingPrice());
            //totalPrice.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(totalAmount) + "/-");

            if(WickedRideManager.getInstance(getActivity()).getPickupDate() != null && WickedRideManager.getInstance(getActivity()).getPickupDate().length()>0){
                String startDate = WickedRideManager.getInstance(getActivity()).getPickupDate();
                mPickUpDate.setText(startDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(startDate.split("-")[1])))+" "+startDate.split("-")[0]+"\n"+WickedRideManager.getInstance(getActivity()).getPickupTime());
            }
            if(WickedRideManager.getInstance(getActivity()).getDropDate() != null && WickedRideManager.getInstance(getActivity()).getDropDate().length()>0) {
                String dropDate = WickedRideManager.getInstance(getActivity()).getDropDate();
                mDropDate.setText(dropDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(dropDate.split("-")[1])))+" "+dropDate.split("-")[0]+"\n"+WickedRideManager.getInstance(getActivity()).getDropTime());

            }

        }

        if(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs() != null && WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().length() >0){
           // noOfDays.setText(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().split(",")[1] +" hours");
        }

        showDatePickerDialog();
    }

    private void showDatePickerDialog() {

        if(mDialog == null) {
            // Initialize Time slot selection dialog
            mDialog = new Dialog(getActivity());
            // mDialog.setCancelable(false);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.dialog_time_slots);
            selectedDateMonthYear = (TextView) mDialog.findViewById(R.id.month);
        }
    }

    private void getAvailableDates() {
        //http://54.255.177.26/api/from-availability-calendar/?model_id=1&city_id=1&date=19&month=12&year=2015
        mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MODEL_ID, bikeId);
        params.put(Constants.CITY_ID, ""+SessionManager.getUserCityID(getActivity()));
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        params.put(Constants.MONTH, ""+ (mMonth+1));//);+1
        params.put(Constants.YEAR, ""+mYear);
        params.put(Constants.AREA_ID, "" + WickedRideManager.getInstance(getActivity()).getAreaId());
        placeRequest(APIMethods.FROM_AVAILABILITY_CALENDAR, BikeInfoResultModel.class, params, false);
    }

//    private void showTimePicker() {
//        final Calendar now = Calendar.getInstance();
//        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
//
//                    }
//                },
//                now.get(Calendar.HOUR_OF_DAY),
//                now.get(Calendar.MINUTE),
//                false
//        );
//        tpd.setAccentColor(Color.parseColor("#000000"));
//        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                Log.d("TimePicker", "Dialog was cancelled");
//            }
//        });
//        tpd.setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
//        tpd.show(getActivity().getFragmentManager(), TIMEPICKER_TAG);
//    }

    @OnClick({R.id.accessories, R.id.tv_pickup_date, R.id.tv_drop_date, R.id.pick_up_holder, R.id.drop_holder})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.accessories:
                    openAccessoryFragment();
                break;
            case R.id.tv_pickup_date:
            case R.id.tv_drop_date:
            case R.id.pick_up_holder:
            case R.id.drop_holder:
                if(view.getId() == R.id.tv_drop_date || view.getId() == R.id.drop_holder && (mPickUpDate.getText().toString().length() == 0)) {
                    if(isDrop) {
                        Intent datePickerIntent = new Intent(getActivity(), DatePickerActivity.class);
                      //  datePickerIntent.putExtra("BlockedDates" , tempAllDates.toString());
                        datePickerIntent.putExtra("MinDate" , mStartDate);
                        datePickerIntent.putExtra("MinTime", mStartTime);
                        datePickerIntent.putExtra("BikeId" , bikeId);
                        datePickerIntent.putExtra("IsDrop" , true);

                        startActivityForResult(datePickerIntent, REQUEST_DROP_DATE);

                    }else{
                        showToast("Please select Pick up date and time.");
                    }
                }else{


                    Intent datePickerIntent = new Intent(getActivity(), DatePickerActivity.class);
                    datePickerIntent.putExtra("BlockedDates" , tempAllDates.toString());
                    datePickerIntent.putExtra("BikeId" , bikeId);
                    datePickerIntent.putExtra("IsDrop" , false);

                    startActivityForResult(datePickerIntent, REQUEST_PIVKUP_DATE );
                }
                break;
        }
    }

    private void openAccessoryFragment() {
        if (!ValidatorUtils.isValidEmail(mBikerEmail.getText().toString().trim()) || !ValidatorUtils.isEmailValid(mBikerEmail.getText().toString().trim())) {
            Toast.makeText(getActivity(), R.string.enter_proper_email, Toast.LENGTH_SHORT).show();
            return;
        } else if (mBikerPhone.getText().toString().length() < 10 || mBikerPhone.getText().toString().length() > 13 && !ValidatorUtils.isNumeric(mBikerPhone.getText().toString())) {
            Toast.makeText(getActivity(), R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mPickUpDate.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), R.string.select_pick_drop, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mBikerName.getText().toString().isEmpty()){
            showToast("All fields are mandatory.");
            return;
        }

        if(!mBikerPhone.getText().toString().startsWith("+91")){
            showToast("Please enter phone number with +91.");
            return;
        }

        Bundle data = new Bundle();
        data.putString(Constants.BIKE_NAME, bikeNameSmall.getText().toString());
        data.putString(Constants.BIKE_IMAGE_URL, bikeImageUrl);
        data.putString(Constants.START_DATE, mPickUpDate.getText().toString());
        data.putString(Constants.DROP_DATE, mDropDate.getText().toString());
        data.putString(Constants.LOCATION, locationName.getText().toString());
        data.putInt(Constants.TOTAL_PRICE, totalAmount);
        WickedRideManager.getInstance(getActivity()).setBookingPrice("" + totalAmount);
        ((BaseNoActionBarActivity) getActivity()).loadAccessoriesFragment(data);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null)
        {
            if (requestCode == REQUEST_PIVKUP_DATE ||requestCode == REQUEST_DROP_DATE )
            {
                if(resultCode == getActivity().RESULT_OK)
                {
                    if( data.getStringExtra("SelectedDate")!= null)
                    {
                        Log.v("Selected date", data.getStringExtra("SelectedDate"));
                        String selectedDate = data.getStringExtra("SelectedDate");
                        selectedDateMonthYear.setText(selectedDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(selectedDate.split("-")[1])))+" "+selectedDate.split("-")[0] );

                        if(requestCode == REQUEST_PIVKUP_DATE)
                        {
                            mStartDate = data.getStringExtra("SelectedDate");
                            isPickUp_Check = true;
                            isDrop = false;

                        }

                        else if (requestCode == REQUEST_DROP_DATE)
                        {
                            mEndDate = data.getStringExtra("SelectedDate");
                            isPickUp_Check = false;
                            isDrop = true;
                        }

                        if(bikeInfoResultModel.getResult().getMessage() != null && bikeInfoResultModel.getResult().getData().size() ==0 ){
                            showToast(bikeInfoResultModel.getResult().getMessage());
                        }
                        else
                        {
                            showDatePickerDialog();
                            showTimeSlots();
                        }

                    }
                }

            }
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedView() != null) {
            ((TextView) parent.getSelectedView()).setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSpinnerOpened() {
//        mSpinnerCities.setBackgroundResource(R.drawable.spinner_exp_bg_black);
    }

    @Override
    public void onSpinnerClosed() {
//        mSpinnerCities.setBackgroundResource(R.drawable.spinner_bg_black);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        mProgress.setVisibility(View.GONE);

        if(apiMethod.contains(APIMethods.FROM_AVAILABILITY_CALENDAR) || apiMethod.contains(APIMethods.TO_AVAILABILITY_CALENDAR)) {
            bikeInfoResultModel = (BikeInfoResultModel) response;


            tempAllDates = new StringBuilder();
            HashMap<String, ArrayList<String>> timeSlotsForSelectedMonth = new HashMap<String, ArrayList<String>>();

            for (int i = 0; i < bikeInfoResultModel.getResult().getData().size(); i++) {
                if (bikeInfoResultModel.getResult().getData().get(i).getStatus().equals("N")) {
                    Log.d("", bikeInfoResultModel.getResult().getData().get(i).getDate());
                    Log.d("", bikeInfoResultModel.getResult().getData().get(i).getStatus());

                    tempAllDates.append(bikeInfoResultModel.getResult().getData().get(i).getDate()+" ");
                }
                    else
                    {
                        ArrayList<String>tempSlotsListForSingleDay = new ArrayList<String>();

                        for ( int j=0; j< bikeInfoResultModel.getResult().getData().get(i).getSlots().size();j++)
                        {
                            if(bikeInfoResultModel.getResult().getData().get(i).getSlots().get(j).getStatus()) {
                                String [] timeArr = bikeInfoResultModel.getResult().getData().get(i).getSlots().get(j).getStartTime().split(":");
                                tempSlotsListForSingleDay.add(timeArr[0]+":"+timeArr[1]);
                            }
                        }

                        timeSlotsForSelectedMonth.put(bikeInfoResultModel.getResult().getData().get(i).getDate(), tempSlotsListForSingleDay);

                    }
            }

                SessionManager.setTimeSlotsForSelectedMonth(timeSlotsForSelectedMonth);
        }

        if(apiMethod.contains(APIMethods.BOOKINGS_TOTAL_PRICE)){
            GetResultData data = (GetResultData) response;
            totalAmount = data.getResult().getData().getBikeRentalTotal();
            totalPrice.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(totalAmount) + "/-");
            WickedRideManager.getInstance(getActivity()).setBookingPrice(data.getResult().getData().getBikeRentalTotal() + "");
            WickedRideManager.getInstance(getActivity()).setNoOfDaysHrs(data.getResult().getData().getNoOfDays() + "," + data.getResult().getData().getNoOfHours());
            noOfDays.setText(data.getResult().getData().getNoOfHours()+" hours");

        }

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        mProgress.setVisibility(View.GONE);
        super.onErrorResponse(error, apiMethod);
    }

//    private String getMonthInWords(int month) {
//        String monthInWords = "";
//        switch (month){
//            case 1:
//                monthInWords = "Jan";
//                break;
//            case 2:
//                monthInWords = "Feb";
//                break;
//            case 3:
//                monthInWords = "Mar";
//                break;
//            case 4:
//                monthInWords = "Apr";
//                break;
//            case 5:
//                monthInWords = "May";
//                break;
//            case 6:
//                monthInWords = "Jun";
//                break;
//            case 7:
//                monthInWords = "Jul";
//                break;
//            case 8:
//                monthInWords = "Aug";
//                break;
//            case 9:
//                monthInWords = "Sep";
//                break;
//            case 10:
//                monthInWords = "Oct";
//                break;
//            case 11:
//                monthInWords = "Nov";
//                break;
//            case 12:
//                monthInWords = "Dec";
//                break;
//
//        }
//
//        return monthInWords;
//    }

    private void showTimeSlots() {

        slots = filteredTimeSlots();
        if(slots == null){
            showToast("No slots available on the selected date.");
            return;
        }

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

                    if(mDropTime == null && slots!= null && slots.size() > 0 ){
                        mDropTime = slots.get(0);
                    }

                    if(mStartDate!= null && mStartTime != null)
                       mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
                    if(mEndDate!= null && mDropTime != null)
                       mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+ mDropTime);

                    WickedRideManager.getInstance(getActivity()).setPickupTime(mStartTime);
                    WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate);
                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate);
                    // Calling this method to load available slots details of Current month
                    getAvailableDates();

                    if(bikeId!= null & mStartDate!= null && mEndDate!=null && mStartTime!= null && mDropTime!=null)
                        fetcBookingTotalPrice(""+WickedRideManager.getInstance(getActivity()).getAreaId(), bikeId, mStartDate, mEndDate, mStartTime, mDropTime);
                } else {
                    mDialog.dismiss();
                    if(mStartTime == null && slots!= null && slots.size() > 0){
                        mStartTime = slots.get(0);
                    }

                    Log.v("MSD", mStartDate);

//                    if(mStartDate!= null && mStartTime != null)
//                    mPickUpDate.setText(mStartDate.split("-")[2] +" "+(getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);

                    //isPickUpSelected = true;
                    Intent datePickerIntent = new Intent(getActivity(), DatePickerActivity.class);

                    datePickerIntent.putExtra("BikeId", bikeId);
                    datePickerIntent.putExtra("MinDate" , mStartDate);
                    datePickerIntent.putExtra("MinTime" , mStartTime);
                    datePickerIntent.putExtra("IsDrop" , true);

                    startActivityForResult(datePickerIntent, REQUEST_DROP_DATE);

                  //  showDatePickerDialog(mView.findViewById(R.id.tv_drop_date));
                }
            }
        });

        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radio_group_time_slots);
        if (slots!= null)
        {
            radioGroup.removeAllViews();
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

            /*
             * Changing the time to 12 hours format
             */
//                if(Integer.parseInt(slots.get(i).split(":")[0]) < 12)
//                    // rb.setText(String.format("%s%s AM", slots.get(i).split(":")[0],slots.get(i).split(":")[1]));
//                    rb.setText(slots.get(i).split(":")[0] + ":" + slots.get(i).split(":")[1] + " AM");
//                else if(Integer.parseInt(slots.get(i).split(":")[0])==12)
//                    rb.setText("12 PM");
//                else
//                    rb.setText((Integer.parseInt(slots.get(i).split(":")[0]) - 12)+ ":" + slots.get(i).split(":")[1] + " PM");

                ///////////// uncomment to enable 12 hours format ////////////////
                rb.setText(slots.get(i));
                rb.setId(i);
//                if (i == 0) rb.setChecked(true);
                radioGroup.addView(rb);
            }

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(isPickUp_Check){
                    mStartTime = slots.get(checkedId);
                    // mPickUpDate.setText(mStartDate.split("-")[2] +" "+(getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);

//                    mPickUpDate.setText(mStartDate+ '\n'+mStartTime);
//                    mDialog.dismiss();

                    // WickedRideManager.getInstance(getActivity()).setPickupTime(mStartTime);
                }else{
                    mDropTime = slots.get(checkedId);
                    // mDropDate.setText(mEndDate.split("-")[2] +" "+(getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
//                    mDropDate.setText(mEndDate + '\n' + mDropTime);
                    // WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);

//                    mDialog.dismiss();
                }
                Log.d("",""+slots.get(checkedId));

                if(isDrop) {
                    mDialog.dismiss();

                    if(slots!= null && slots.size() > 0 ){
                        mDropTime = slots.get(checkedId);
                    }


                    if(mStartDate!= null && mStartTime != null)
                        mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
                    if(mEndDate!= null && mDropTime != null)
                        mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+ mDropTime);

//                    WickedRideManager.getInstance(getActivity()).setPickupTime(mStartTime);
//                    WickedRideManager.getInstance(getActivity()).setDropTime(mDropTime);
//                    WickedRideManager.getInstance(getActivity()).setPickUpDate(mStartDate);
//                    WickedRideManager.getInstance(getActivity()).setDropDate(mEndDate);
                    if(bikeId!= null & mStartDate!= null && mEndDate!=null && mStartTime!= null && mDropTime!=null)
                        fetcBookingTotalPrice(""+WickedRideManager.getInstance(getActivity()).getAreaId(), bikeId, mStartDate, mEndDate, mStartTime, mDropTime);
                } else {
                    mDialog.dismiss();
                    if(mStartTime == null && slots!= null && slots.size() > 0){
                        mStartTime = slots.get(checkedId);
                    }

                    Log.v("MSD", mStartDate);

//                    if(mStartDate!= null && mStartTime != null)
//                    mPickUpDate.setText(mStartDate.split("-")[2] +" "+(getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);

                    //isPickUpSelected = true;
                    Intent datePickerIntent = new Intent(getActivity(), DatePickerActivity.class);

                    datePickerIntent.putExtra("BikeId", bikeId);
                    datePickerIntent.putExtra("MinDate" , mStartDate);
                    datePickerIntent.putExtra("MinTime" , mStartTime);
                    datePickerIntent.putExtra("IsDrop" , true);

                    startActivityForResult(datePickerIntent, REQUEST_DROP_DATE);

                    //  showDatePickerDialog(mView.findViewById(R.id.tv_drop_date));
                }

            }
        });


       /* LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        TimeSlotsAdapter adapter = new TimeSlotsAdapter(getActivity(), slots);
        recyclerView.setAdapter(adapter);*/
        mDialog.show();
    }

    private ArrayList<String> filteredTimeSlots() {

        ArrayList<String> tempList;
        if (!isDrop)
            tempList = getTimeSlotsForPickUpFromAPI();
         else
            tempList = getTimeSlotsForDropFromAPI();

        return tempList;
    }

    //=====================================================//

    private ArrayList<String> getTimeSlotsForPickUpFromAPI(){

        HashMap<String, ArrayList<String>> resultTimeSlotsFromCache;
        ArrayList<String> temp = null;
        if(SessionManager.getTimeSlotsForSelectedMonth()!= null)
        {
            resultTimeSlotsFromCache = SessionManager.getTimeSlotsForSelectedMonth();
            if(resultTimeSlotsFromCache.containsKey(mStartDate))
                temp = SessionManager.getTimeSlotsForSelectedMonth().get(mStartDate);
        }

        return temp;
    }


    private ArrayList<String> getTimeSlotsForDropFromAPI()
    {
        HashMap<String, ArrayList<String>> resultTimeSlotsFromCache;

        ArrayList<String> filteredTemp = new ArrayList<String>();
        if(SessionManager.getTimeSlotsForSelectedMonth()!= null)
        {
            ArrayList<String> temp = null;
           // resultTimeSlotsFromCache = SessionManager.getTimeSlotsForSelectedMonth();
            //if(resultTimeSlotsFromCache.containsKey(mEndDate))
              //  temp = SessionManager.getTimeSlotsForSelectedMonth().get(mEndDate);
                temp = Util.getWorkingDaysSlots();
//            if(temp!= null)
//              {
//                  if(mStartDate.equalsIgnoreCase(mEndDate))
//                  {
//                      for(int c = 0; c < temp.size(); c++ )
//                      {
//                          if((Integer.parseInt(temp.get(c).split(":")[0]) < Integer.parseInt(SessionManager.getEndTimeSlot().split(":")[0]))
//                                  && (Integer.parseInt(temp.get(c).split(":")[0]) > Integer.parseInt(mStartTime.split(":")[0])))
//                              filteredTemp.add(temp.get(c));
//                      }
//                  }
//                  else
//                  {
//                      for(int c = 0; c < temp.size(); c++ )
//                      {
//                          if(Integer.parseInt(temp.get(c).split(":")[0]) < Integer.parseInt(SessionManager.getEndTimeSlot().split(":")[0]))
//                              filteredTemp.add(temp.get(c));
//                      }
//
//                  }
//              }

            if(temp!= null)
            {

                if(!mStartDate.equalsIgnoreCase(mEndDate) && !mEndDate.equalsIgnoreCase(SessionManager.getEndDate()))
                {
                    // Add all the time slots available
                    for(int c = 0; c < temp.size(); c++ )
                    {
                            filteredTemp.add(temp.get(c));
                    }
                }
                else
                {
                    if (mStartDate.equalsIgnoreCase(mEndDate) && mEndDate.equalsIgnoreCase(SessionManager.getEndDate()))
                    {
                        // Check for both lower limit and uper limit
                        for(int c = 0; c < temp.size(); c++ )
                        {
                            if((Integer.parseInt(temp.get(c).split(":")[0]) < Integer.parseInt(SessionManager.getEndTimeSlot().split(":")[0]))
                                    && (Integer.parseInt(temp.get(c).split(":")[0]) > Integer.parseInt(mStartTime.split(":")[0])))
                                filteredTemp.add(temp.get(c));
                        }

                    }
                    else {

                        if(mStartDate.equalsIgnoreCase(mEndDate) && !mEndDate.equalsIgnoreCase(SessionManager.getEndDate()))
                        {
                             // Check for lower limit
                            for(int c = 0; c < temp.size(); c++ )
                            {
                                if((Integer.parseInt(temp.get(c).split(":")[0]) > Integer.parseInt(mStartTime.split(":")[0])))
                                    filteredTemp.add(temp.get(c));
                            }
                        }
                        else
                        {
                            // Check for upper limit
                            for(int c = 0; c < temp.size(); c++ )
                            {
                                if((Integer.parseInt(temp.get(c).split(":")[0]) < Integer.parseInt(SessionManager.getEndTimeSlot().split(":")[0])))
                                    filteredTemp.add(temp.get(c));
                            }

                        }
                    }

                }

            }

        }

        return filteredTemp;
    }
    //=====================================================//

    private void fetcBookingTotalPrice(String areaId, String modleId, String startDate, String dropDate, String startTime, String dropTime) {
        mProgress.setVisibility(View.VISIBLE);
        String apiMethod = APIMethods.BOOKINGS_TOTAL_PRICE;
        HashMap<String, String> params = new HashMap<>();
        if (modleId != null)
            params.put(Constants.MODEL_ID, modleId);
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
        if(SessionManager.getUserCityID(getActivity()) != -1)
            params.put(Constants.CITY_ID, "" + SessionManager.getUserCityID(getActivity()));

        placeRequest(apiMethod, GetResultData.class, params);
    }

}
