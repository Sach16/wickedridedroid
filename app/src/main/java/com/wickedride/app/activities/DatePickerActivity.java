package com.wickedride.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.GetResultData;
import com.wickedride.app.models.all_bike_models.BikeInfoResultModel;
import com.wickedride.app.networking.RequestManager;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.views.WRProgressView;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.DateCallBack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

@SuppressLint("SimpleDateFormat")
public class DatePickerActivity extends AppCompatActivity implements ServerCallback,DateCallBack{
    //private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private Date tempPickedDate;
    private boolean IsDrop , isAPICallFromFragment;

    private int displayedMonth = -1, displayedYear = -1, tempMaxDateMonth = -1, tempMaxDateYear = -1;

    private BikeInfoResultModel bikeInfoResultModel;
    StringBuilder tempAllDatestoBlock;

    private String bikeId,startTime,startDate;
    final Calendar c = Calendar.getInstance();
    private GetResultData toAvailabilityData;

    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;

    /*
     * @ Nihar : Initial customisation of Calendar
     */

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();
        // Min date is last 7 days
        //@ Nihar : Changing min date to current date as requirement is for future dates only at the time of pickup date selection


        cal.add(Calendar.DATE, 0);
        Date blueDate = cal.getTime();
        caldroidFragment.setMinDate(blueDate);


        /*
         * @ Nihar : Setting the disabled dates for current month
         */
        if (caldroidFragment != null) {

            //================= Only needed for Pick up ==================//

            if(!IsDrop){

                ((TextView)findViewById(R.id.title_Message)).setText("PICK UP DATE");

                ArrayList<String> disabledDates_stringFormat = new ArrayList<String>();

                if(getIntent().getStringExtra("BlockedDates")!= null && !(getIntent().getStringExtra("BlockedDates").equalsIgnoreCase("")))
                {
                    String tempArr[] = getIntent().getStringExtra("BlockedDates").split(" ");

                    for (String s : tempArr)
                    {
                        disabledDates_stringFormat.add(s);

                    }
                }

                // Calling Fragment method and setting disable dates

                if(disabledDates_stringFormat!= null && disabledDates_stringFormat.size()> 0)
                    caldroidFragment.setDisableDatesFromString(disabledDates_stringFormat,"yyyy-MM-dd");
            }


            /*
             * @ Nihar : Get params needed for future calls
             */

            if(getIntent().getStringExtra("BikeId")!= null)
                bikeId = getIntent().getStringExtra("BikeId");
            if(getIntent().getStringExtra("MinTime")!= null) {
                String temp = getIntent().getStringExtra("MinTime");
                startTime = temp.substring(0,5);
            }


            IsDrop = getIntent().getBooleanExtra("IsDrop", false);


            //public void setDisableDates(ArrayList<Date> disableDateList);
            // public void setDisableDatesFromString(ArrayList<String> disableDateStrings);
            // public void setDisableDatesFromString(ArrayList<String> disableDateStrings, String dateFormat);

            // Customize
            // caldroidFragment.setMinDate(minDate);
            // caldroidFragment.setMaxDate(maxDate);

                /*
                 *  @ Nihar Setting picked date as min date for DROP date selection
                 */

            if(IsDrop){

                ((TextView)findViewById(R.id.title_Message)).setText("DROP DATE");
                if(getIntent().getStringExtra("MinDate")!= null)
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                    try {

                        Date date = formatter.parse(getIntent().getStringExtra("MinDate"));
                        // Check whether user has selected the last time slot
                        if(startTime.equalsIgnoreCase("22:00"))
                        {
                            long time = date.getTime();
                            time += 1000L * 60 * 60 * 24;
                            Date tempDt = new Date(time);
                            caldroidFragment.setMinDate(tempDt);
                        }
                        else
                         caldroidFragment.setMinDate(date);

                        startDate = formatter.format(date);

                        // Setting the Month to be displayed as per PickUp Date

                        Bundle tempArgs = caldroidFragment.getArguments();

                        tempArgs.putInt(CaldroidFragment.YEAR, Integer.parseInt(startDate.split("-")[0]));
                        tempArgs.putInt(CaldroidFragment.MONTH, Integer.parseInt(startDate.split("-")[1]));

                        caldroidFragment.setArguments(tempArgs);

                        System.out.println(date);
                        System.out.println(formatter.format(date));

                        /*
                         * Call this to set uper limit of date
                         */

                        setCustomEndDateForDrop();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }

            // caldroidFragment.setDisableDates(disabledDates);
            // caldroidFragment.setDisableDates(null);

            // caldroidFragment.setSelectedDates(fromDate, toDate);
            // caldroidFragment.setShowNavigationArrows(false);
            //caldroidFragment.setEnableSwipe(false);

            caldroidFragment.refreshView();
        }


    }


    public void setMaximumDateLimit()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (tempAllDatestoBlock != null)
            {
                Date maxDate = formatter.parse(tempAllDatestoBlock.toString());
                System.out.println(maxDate);
                System.out.println(formatter.format(maxDate));
                caldroidFragment.setMaxDate(maxDate);

                //Setting temp variables for validation

                tempMaxDateYear = Integer.parseInt(tempAllDatestoBlock.toString().split("-")[0]);
                tempMaxDateMonth = Integer.parseInt(tempAllDatestoBlock.toString().split("-")[1]);

                //Checking for the Next Button Navigation Validation

                checkAndDisableNextMonthNav();


//                Calendar cal = Calendar.getInstance();
//                cal.setTime(maxDate);
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//
//                Calendar current = Calendar.getInstance();
//
//                int current_year = cal.get(Calendar.YEAR);
//                int current_month = cal.get(Calendar.MONTH);
//
//                if(year == current_year && month == current_month)

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        caldroidFragment.refreshView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        ButterKnife.inject(this);


        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH)+1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
           // args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);


            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Checking for Prev and Next Button

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
               // Toast.makeText(getApplicationContext(), formatter.format(date),
               //         Toast.LENGTH_SHORT).show();

                caldroidFragment.setIsDateSelected(true);

                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_black, date);
                caldroidFragment.setTextColorForDate(R.color.white, date);

                if(date != null)
                {
                    if(tempPickedDate != null)
                    {
                        caldroidFragment.clearBackgroundResourceForDate(tempPickedDate);
                        caldroidFragment.clearTextColorForDate(tempPickedDate);
                    }

                    tempPickedDate = date;

                    caldroidFragment.clearBackgroundResourceForDate(tempPickedDate);
                    caldroidFragment.clearTextColorForDate(tempPickedDate);

                    Calendar myCal = new GregorianCalendar();
                    myCal.setTime(tempPickedDate);
                    Intent resultIntent = new Intent();

                    //resultIntent.putExtra("SelectedDate", myCal.get(Calendar.YEAR) + "-" + (myCal.get(Calendar.MONTH) + 1) + "-" + myCal.get(Calendar.DAY_OF_MONTH));
                    String selectedDate = myCal.get(Calendar.YEAR) + "-";

                    if((myCal.get(Calendar.MONTH) + 1) < 10)
                        selectedDate = selectedDate + "0" + (myCal.get(Calendar.MONTH)+1) + "-";
                    else
                        selectedDate = selectedDate + (myCal.get(Calendar.MONTH)+1) + "-";

                    if(myCal.get(Calendar.DAY_OF_MONTH) < 10)
                        selectedDate = selectedDate + "0" + myCal.get(Calendar.DAY_OF_MONTH);
                    else
                        selectedDate = selectedDate +  myCal.get(Calendar.DAY_OF_MONTH);

                    resultIntent.putExtra("SelectedDate", selectedDate);

                    resultIntent.putExtra("selectedDateAsString", tempPickedDate.toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                caldroidFragment.refreshView();

            }

            @Override
            public void onChangeMonth(int month, int year) {

                displayedMonth = month;
                displayedYear = year;
                checkAndDisablePrevMonthNav();
                checkAndDisableNextMonthNav();
//                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getApplicationContext(), text,
//                       Toast.LENGTH_SHORT).show();
            }



            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getApplicationContext(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {

                checkAndDisablePrevMonthNav();
                if (caldroidFragment.getLeftArrowButton() != null) {
                  //  Toast.makeText(getApplicationContext(),
                       //     "Caldroid view is created", Toast.LENGTH_SHORT)
                       //     .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        final Button nextButton = (Button) findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempPickedDate != null){

                    Calendar myCal = new GregorianCalendar();
                    myCal.setTime(tempPickedDate);
                    Intent resultIntent = new Intent();

                    //resultIntent.putExtra("SelectedDate", myCal.get(Calendar.YEAR) + "-" + (myCal.get(Calendar.MONTH) + 1) + "-" + myCal.get(Calendar.DAY_OF_MONTH));
                    String selectedDate = myCal.get(Calendar.YEAR) + "-";

                    if((myCal.get(Calendar.MONTH) + 1) < 10)
                        selectedDate = selectedDate + "0" + (myCal.get(Calendar.MONTH)+1) + "-";
                    else
                    selectedDate = selectedDate + (myCal.get(Calendar.MONTH)+1) + "-";

                    if(myCal.get(Calendar.DAY_OF_MONTH) < 10)
                        selectedDate = selectedDate + "0" + myCal.get(Calendar.DAY_OF_MONTH);
                    else
                        selectedDate = selectedDate +  myCal.get(Calendar.DAY_OF_MONTH);

                    resultIntent.putExtra("SelectedDate", selectedDate);

                    resultIntent.putExtra("selectedDateAsString", tempPickedDate.toString());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
                   // showTimeSlots();;
                else
                    Toast.makeText(getApplicationContext(),"Please select any date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndDisableNextMonthNav() {

        if(caldroidFragment!= null) {
            if (tempMaxDateMonth == displayedMonth && tempMaxDateYear == displayedYear) {
                caldroidFragment.getRightArrowButton().setClickable(false);
                caldroidFragment.getRightArrowButton().setBackgroundColor(getResources().getColor(R.color.caldroid_darker_gray));
            } else {
                caldroidFragment.getRightArrowButton().setClickable(true);
                caldroidFragment.getRightArrowButton().setBackgroundColor(getResources().getColor(R.color.black));

            }
        }
    }

    private void checkAndDisablePrevMonthNav() {

        if(caldroidFragment!= null){
            Calendar calTemp =  Calendar.getInstance();

            if(!IsDrop)
            {
                if((displayedMonth == (calTemp.get(Calendar.MONTH)+1) && displayedYear == calTemp.get(Calendar.YEAR)))
                {
                    caldroidFragment.getLeftArrowButton().setClickable(false);
                    caldroidFragment.getLeftArrowButton().setBackgroundColor(getResources().getColor(R.color.caldroid_darker_gray));
                }
                else
                {
                    caldroidFragment.getLeftArrowButton().setClickable(true);
                    caldroidFragment.getLeftArrowButton().setBackgroundColor(getResources().getColor(R.color.black));

                }
            }
            else
            {
                if((Integer.parseInt(startDate.split("-")[0]) == displayedYear )&& (Integer.parseInt(startDate.split("-")[1]) == displayedMonth))
                {
                    caldroidFragment.getLeftArrowButton().setClickable(false);
                    caldroidFragment.getLeftArrowButton().setBackgroundColor(getResources().getColor(R.color.caldroid_darker_gray));
                }
                else
                {
                    caldroidFragment.getLeftArrowButton().setClickable(true);
                    caldroidFragment.getLeftArrowButton().setBackgroundColor(getResources().getColor(R.color.black));

                }
            }

        }

    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }


    /* ======================================
    * @ Nihar : Network call Implemantations
    * ====================================== */

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
       // super.onAPIResponse(response, apiMethod);
        mProgress.setVisibility(View.GONE);

        if(apiMethod.contains(APIMethods.FROM_AVAILABILITY_CALENDAR)) {
            bikeInfoResultModel = (BikeInfoResultModel) response;
            // calendarArray = new ArrayList<Calendar>();

            tempAllDatestoBlock = new StringBuilder();
            HashMap<String, ArrayList<String>> timeSlotsForSelectedMonth = new HashMap<String, ArrayList<String>>();

            for (int i = 0; i < bikeInfoResultModel.getResult().getData().size(); i++) {
                if (bikeInfoResultModel.getResult().getData().get(i).getStatus().equals("N")) {
                    Log.d("", bikeInfoResultModel.getResult().getData().get(i).getDate());
                    Log.d("", bikeInfoResultModel.getResult().getData().get(i).getStatus());
                    tempAllDatestoBlock.append(bikeInfoResultModel.getResult().getData().get(i).getDate()+" ");
                }
                else
                {
                    ArrayList<String>tempSlotsListForSingleDay = new ArrayList<String>();

                    for ( int j=0; j<bikeInfoResultModel.getResult().getData().get(i).getSlots().size();j++)
                    {
                        if(bikeInfoResultModel.getResult().getData().get(i).getSlots().get(j).getStatus())
                        tempSlotsListForSingleDay.add(bikeInfoResultModel.getResult().getData().get(i).getSlots().get(j).getStartTime());
                    }

                    timeSlotsForSelectedMonth.put(bikeInfoResultModel.getResult().getData().get(i).getDate(), tempSlotsListForSingleDay);

                }

                    //SessionManager.getTimeSlotsForSelectedMonth().clear();
                    SessionManager.setTimeSlotsForSelectedMonth(timeSlotsForSelectedMonth);
            }

           // caldroidFragment.customiseFragmentAccordingToPickUporDrop();
            customiseFragmentAccordingToPickUporDrop();

        } else if(apiMethod.contains(APIMethods.TO_AVAILABILITY_CALENDAR)) {
            toAvailabilityData =  (GetResultData) response;
            // calendarArray = new ArrayList<Calendar>();
            if(toAvailabilityData!= null)
            {
                toAvailabilityData.getResult().getData().getEndDate();
                tempAllDatestoBlock = new StringBuilder();
                tempAllDatestoBlock.append(toAvailabilityData.getResult().getData().getEndDate());

                /*
                 *  Add endDate and endTime to Session Manager to refer at the time of showing time slots inside Reserve Fragment
                 */
                SessionManager.setEndDate(toAvailabilityData.getResult().getData().getEndDate());
                SessionManager.setEndTimeSlot(toAvailabilityData.getResult().getData().getLastSlot().getEndTime());

                setMaximumDateLimit();
            }

        }

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        mProgress.setVisibility(View.GONE);
       // super.onErrorResponse(error, apiMethod);
        Toast.makeText(this, " API CALL ERROR", Toast.LENGTH_SHORT).show();
    }


    public void getBlockedDates() {
        //http://54.255.177.26/api/from-availability-calendar/?model_id=1&city_id=1&date=19&month=12&year=2015
        this.mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MODEL_ID, bikeId);
        params.put(Constants.CITY_ID, ""+ SessionManager.getUserCityID(this));
        //int mYear = c.get(Calendar.YEAR);
        //int mMonth = c.get(Calendar.MONTH);
        params.put(Constants.MONTH, displayedMonth+"");
        params.put(Constants.YEAR, displayedYear+"");
        params.put(Constants.AREA_ID, "" + WickedRideManager.getInstance(this).getAreaId());
       // RequestManager.getInstance(this).placeRequest(APIMethods.FROM_AVAILABILITY_CALENDAR, BikeInfoResultModel.class, params, false);
        RequestManager.getInstance(this).placeRequest(APIMethods.FROM_AVAILABILITY_CALENDAR, BikeInfoResultModel.class,this ,params, false);
    }

    public void getEndDate() {
        //http://54.255.177.26/api/from-availability-calendar/?model_id=1&city_id=1&date=19&month=12&year=2015
        mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MODEL_ID, bikeId);
        params.put(Constants.START_DATE,startDate);//);+1
        params.put(Constants.START_TIME,startTime);
        params.put(Constants.AREA_ID, "" + WickedRideManager.getInstance(this).getAreaId());
        // RequestManager.getInstance(this).placeRequest(APIMethods.FROM_AVAILABILITY_CALENDAR, BikeInfoResultModel.class, params, false);
        RequestManager.getInstance(this).placeRequest(APIMethods.TO_AVAILABILITY_CALENDAR, GetResultData.class,this ,params, false);
    }


    //
    @Override
    public void getDates(boolean isFromFrag) {
        getBlockedDates();
    }

    /*
     * setting the end date limit for Drop date selection
     */

    public void setCustomEndDateForDrop() {
            getEndDate();
    }

    public void customiseFragmentAccordingToPickUporDrop(){

        if(!IsDrop){

            ArrayList<String> disabledDates_stringFormat = new ArrayList<String>();

            if(tempAllDatestoBlock!= null && !tempAllDatestoBlock.equals(""))
            {
                String tempArr[] = tempAllDatestoBlock.toString().trim().split(" ");

                for (String s : tempArr)
                {
                    if(!s.equals(""))
                    disabledDates_stringFormat.add(s);

                }

                if(disabledDates_stringFormat!= null && disabledDates_stringFormat.size()> 0)
                    caldroidFragment.setDisableDatesFromString(disabledDates_stringFormat,"yyyy-MM-dd");
            }

        }
        caldroidFragment.refreshView();
    }
}
