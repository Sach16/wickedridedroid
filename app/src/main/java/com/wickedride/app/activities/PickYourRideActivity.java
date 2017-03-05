package com.wickedride.app.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.adapters.PickRideAdapter;
import com.wickedride.app.interfaces.OnSpinnerEventsListener;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.Area;
import com.wickedride.app.models.AreaResult;
import com.wickedride.app.models.BrandResult;
import com.wickedride.app.models.all_bike_models.Datum;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.views.CustomSpinner;
import com.wickedride.app.views.HeaderGridView;
import com.wickedride.app.views.WRProgressView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class PickYourRideActivity extends BaseDefaultActionActivity implements AdapterView.OnItemSelectedListener,
        OnSpinnerEventsListener, AdapterView.OnItemClickListener {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    ArrayList<String> spinnerValues = new ArrayList<>();

    @InjectView(R.id.gridView)
    HeaderGridView mGridView;

    @InjectView(R.id.toolbar)
    Toolbar mToolBar;

    @InjectView(R.id.back_btn)
    ImageView mBackButton;

    @InjectView(R.id.action_title)
    TextView mTextActionTitle;

    @InjectView(R.id.action_btn)
    ImageView mActionButton;

    @InjectView(R.id.search)
    Button mSearch;

    @InjectView(R.id.progress)
    WRProgressView mProgressBar;

    @InjectView(R.id.error_msg)
    TextView errorMsg;

    private ArrayList<String> slots;
    private View mPickRideHeader;
    private HeaderViewHolder headerViewHolder;
    private ArrayAdapter<String> spinnerCitiesArrayAdapter;
    private boolean isPickUp,isDrop;
    private boolean isPickUpSelected;
    private PickRideAdapter mPickRideAdapter;
    private String mAreaId = null, mMakeID = "", mEndDate = null,mStartDate = null, mDropDate = null, mStartTime = null, mDropTime = null;
    private ArrayList<Area> mAreas;
    private TextView selectedDateMonthYear;
    private Dialog mDialog;
    private boolean userSelectedSameDay;
    final int  startTime = 9;/*Morning 9 am*/
    final int endTime = 22;/*Night 10pm*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_your_ride);
        ButterKnife.inject(this);
        errorMsg.setVisibility(View.GONE);
        initHeader();
        //setDate();
        getAllAreas();
        if (WickedRideManager.getInstance(this).getBrandsSaved() != null && WickedRideManager.getInstance(this).getBrandsSaved().size()>0){
            List<Datum> mBrands  = WickedRideManager.getInstance(this).getBrandsSaved();
            for (int i=0;i<mBrands.size();i++){
                if(mBrands.get(i).isChecked()){
                    setSelectedBrand(mBrands.get(i).getId(), true);
                }
            }
            mPickRideAdapter = new PickRideAdapter(this,mBrands );
            mGridView.setAdapter(mPickRideAdapter);
        }
        else {
            getAllMakes();
        }
    }

    private void getAllAreas() {
        placeRequest(APIMethods.GET_ALL_CITIES + "/" + SessionManager.getUserCityID(getApplicationContext()) + "/" + APIMethods.GET_ALL_AREAS + "/", AreaResult.class);
    }

    private void getAllMakes() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.CITY_ID, "" + SessionManager.getUserCityID(getApplicationContext()));
        placeRequest(APIMethods.GET_ALL_MAKE_LOGO, BrandResult.class, params);
    }

    @OnClick({R.id.search, R.id.back_btn})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.search:
                WickedRideManager.getInstance(this).setBrandsSaved(mPickRideAdapter.getBrandList());
                searchBikes(mMakeID, mAreaId, mStartDate, (mEndDate != null && !mEndDate.isEmpty()) ? mEndDate : mDropDate, mStartTime, mDropTime);
                break;
            case R.id.back_btn:
//                searchBikes(mMakeID, mAreaId, mStartDate, (mEndDate != null && !mEndDate.isEmpty()) ? mEndDate : mDropDate, Util.removeSeconds(mStartTime), Util.removeSeconds(mDropTime));
                onBackPressed();
                break;
        }
    }

    private void initHeader() {
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        mTextActionTitle.setText(getResources().getString(R.string.pick_your_ride));
        spinnerValues.add("");
        mBackButton.setVisibility(View.VISIBLE);
        mActionButton.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        headerViewHolder = new HeaderViewHolder();
        mPickRideHeader = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.header_pick_ride, null, false);
        ButterKnife.inject(headerViewHolder, mPickRideHeader);
        mGridView.addHeaderView(mPickRideHeader);
        mGridView.setOnItemClickListener(this);
        spinnerCitiesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_spinner_item, spinnerValues);
        headerViewHolder.spinnerCities.setAdapter(spinnerCitiesArrayAdapter);
        headerViewHolder.spinnerCities.setOnItemSelectedListener(this);
        headerViewHolder.spinnerCities.setSpinnerEventsListener(this);

        mPickRideHeader.setVisibility(View.VISIBLE);

        if(WickedRideManager.getInstance(this).getPickupDate() != null && WickedRideManager.getInstance(this).getPickupDate().length()>0){
            mStartDate =  WickedRideManager.getInstance(this).getPickupDate();
            mStartTime = WickedRideManager.getInstance(this).getPickupTime();
            headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0] + '\n' +mStartTime);

        }
        if(WickedRideManager.getInstance(this).getDropDate() != null && WickedRideManager.getInstance(this).getDropDate().length()>0) {
            mDropDate = WickedRideManager.getInstance(this).getDropDate();
            mDropTime = WickedRideManager.getInstance(this).getDropTime();
            headerViewHolder.mDropDate.setText(mDropDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mDropDate.split("-")[1]))) + " " + mDropDate.split("-")[0] + '\n' + mDropTime);
        }



    }


    public void searchBikes(String makeId, String areaId, String startDate, String dropDate, String startTime, String dropTime) {
        Intent filterIntent = new Intent();
        filterIntent.putExtra(Constants.MAKE_ID, makeId);
        filterIntent.putExtra(Constants.AREA_ID, areaId);
        filterIntent.putExtra(Constants.START_DATE, startDate);
        filterIntent.putExtra(Constants.DROP_DATE, dropDate);
        filterIntent.putExtra(Constants.START_TIME, startTime);
        filterIntent.putExtra(Constants.DROP_TIME, dropTime);
        setResult(Constants.FILTER_REQUEST_CODE, filterIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getSelectedView() != null) {
            ((TextView) parent.getSelectedView()).setPadding(0, 0, 0, 0);
        }
        if (mAreas != null && mAreas.size() > 0) {
            setSelectedArea(mAreas.get(position).getId());

        }
    }

    private void setSelectedArea(String id) {
        mAreaId = id;
        WickedRideManager.getInstance(this).setSelectedArea(mAreaId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSpinnerOpened() {
        headerViewHolder.spinnerCities.setBackgroundResource(R.drawable.spinner_exp_bg_black);
    }

    @Override
    public void onSpinnerClosed() {
        //headerViewHolder.spinnerCities.setBackgroundResource(R.drawable.spinner_bg_black);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position = position - 3;
        Datum brand = mPickRideAdapter.getItem(position);
        if(!brand.isChecked()) {
            brand.setChecked(true);
            setSelectedBrand(brand.getId(), true);

        }else{
            brand.setChecked(false);
            setSelectedBrand(brand.getId(), false);
        }
        mPickRideAdapter.notifyDataSetChanged();
    }

    private void setSelectedBrand(String makeID, boolean isSelected) {
        if(isSelected) {
            mMakeID += makeID + ",";
            WickedRideManager.getInstance(this).setSelectedBrands(mMakeID);
        }else{
           mMakeID = mMakeID.replace(makeID+",","");
        }
    }

    public class HeaderViewHolder {

        @InjectView(R.id.tv_pickup_date)
        TextView mPickUpDate;

        @InjectView(R.id.tv_drop_date)
        TextView mDropDate;

        @InjectView(R.id.spinner_cities)
        CustomSpinner spinnerCities;

        @InjectView(R.id.pick_brand)
        TextView mPickBrand;

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

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.d("PickYourRideActivity", "Response::" + response);
        Log.d("PickYourRideActivity", "ApiMethod::" + apiMethod);
        mGridView.setVisibility(View.VISIBLE);
        errorMsg.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.GET_ALL_MAKE_LOGO)) {
            BrandResult brands = (BrandResult) response;
            mPickRideAdapter = new PickRideAdapter(this, brands.getResult().getData());
            mGridView.setAdapter(mPickRideAdapter);
        } else if (apiMethod.contains(APIMethods.GET_ALL_AREAS)) {
            AreaResult results = (AreaResult) response;
            mAreas = results.getResult().getData();
            spinnerValues.clear();
            for (int i = 0; i < mAreas.size(); i++) {
                spinnerValues.add(mAreas.get(i).getArea());
            }

            spinnerCitiesArrayAdapter.notifyDataSetChanged();
            if(WickedRideManager.getInstance(this).getSelectedArea() != null && !WickedRideManager.getInstance(this).getSelectedArea().isEmpty()){
                headerViewHolder.spinnerCities.setSelection(Integer.parseInt(WickedRideManager.getInstance(this).getSelectedArea())-1);
            }
            setSelectedArea(mAreas.get(0).getId());
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgressBar.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
        if(error instanceof com.android.volley.NoConnectionError){
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText("No internet connection.");
        }
    }

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
                           // headerViewHolder.mPickUpDate.setText(dayOfMonth + " " + (Util.getMonthInWords(monthOfYear + 1))+" "+year);
                            selectedDateMonthYear.setText(dayOfMonth + " " + (Util.getMonthInWords(monthOfYear + 1))+" "+year);
                            //WickedRideManager.getInstance(getApplicationContext()).setPickUpDate(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
                        } else if (id == R.id.tv_drop_date || id == R.id.drop_holder) {
                            mEndDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            if(mStartDate.equals(mEndDate))
                                userSelectedSameDay = true;
                            else
                                userSelectedSameDay = false;

                           // headerViewHolder.mDropDate.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
                            selectedDateMonthYear.setText(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear + 1))+" "+year);
//                            WickedRideManager.getInstance(getApplicationContext()).setDropDate(dayOfMonth+" "+(Util.getMonthInWords(monthOfYear+1))+" "+year);

                        }

                        showTimeSlots();
                    }
                },c.get(Calendar.YEAR) , c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
        datePickerDialog.setMinDate(c);
        datePickerDialog.autoDismiss(true);
        datePickerDialog.setYearRange(c.get(Calendar.YEAR), c.get(Calendar.YEAR) + 1);
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
        //datePickerDialog.setCancelable(false);

        mDialog = new Dialog(PickYourRideActivity.this);
       // mDialog.setCancelable(false);
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

                    headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0] + '\n' + mStartTime);
                    WickedRideManager.getInstance(getApplicationContext()).setPickupTime(Util.removeSeconds(mStartTime));

                    headerViewHolder.mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+ mDropTime);
                    WickedRideManager.getInstance(getApplicationContext()).setDropTime(Util.removeSeconds(mDropTime));
                    WickedRideManager.getInstance(getApplicationContext()).setPickUpDate(mStartDate);
                    WickedRideManager.getInstance(getApplicationContext()).setDropDate(mEndDate);

                    // fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
                }else{
                    mDialog.dismiss();
                    //   mStartTime = slots.get(0);
//                    headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
//                    WickedRideManager.getInstance(getApplicationContext()).setPickupTime(mStartTime);
                    isPickUpSelected = true;
                    showDatePickerDialog(findViewById(R.id.tv_drop_date));
                }
            }
        });

        RadioGroup radioGroup = (RadioGroup) mDialog.findViewById(R.id.radio_group_time_slots);
        for (int i = 0; i < slots.size(); i++) {
            RadioButton rb = new RadioButton(getApplicationContext());
            RadioGroup.LayoutParams radioParams = (new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            int expectedDpFont = (int) Util.convertDpToPixel(17, getApplicationContext());
            int expectedSpFont = (int) Util.pixelsToSp(getApplicationContext(), expectedDpFont);
            rb.setTextSize(expectedSpFont);

            int dpMargin = 9;
            int pixelMargin = (int) Util.convertDpToPixel(dpMargin, getApplicationContext());
            radioParams.setMargins(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
            rb.setPadding(pixelMargin, pixelMargin, pixelMargin, pixelMargin);
            rb.setLayoutParams(radioParams);

            rb.setTextColor(getResources().getColor(R.color.black));
            rb.setButtonDrawable(R.drawable.check_circle);
            rb.setText(slots.get(i));
            rb.setId(i);
//            if (i == 0) rb.setChecked(true);
//            if(!isDrop)
//                mStartTime = slots.get(0);
//            else
//                mDropTime = slots.get(0);
            //mDropTime = slots.get(slots.size() - 1);

            radioGroup.addView(rb);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(isPickUp){
                    mStartTime = slots.get(checkedId);
                    // headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
                }else{
                    mDropTime = slots.get(checkedId);

                    //headerViewHolder.mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
                    // WickedRideManager.getInstance(getApplicationContext()).setDropTime(mDropTime);
                }

                Log.d("",""+slots.get(checkedId));

                if(isDrop) {
                    mDialog.dismiss();

                    headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] + " " + (Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1]))) + " " + mStartDate.split("-")[0] + '\n' + mStartTime);
                    WickedRideManager.getInstance(getApplicationContext()).setPickupTime(mStartTime);

                    headerViewHolder.mDropDate.setText(mEndDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mEndDate.split("-")[1])))+" "+mEndDate.split("-")[0]+ '\n'+mDropTime);
                    WickedRideManager.getInstance(getApplicationContext()).setDropTime(mDropTime);
                    WickedRideManager.getInstance(getApplicationContext()).setPickUpDate(mStartDate);
                    WickedRideManager.getInstance(getApplicationContext()).setDropDate(mEndDate);

                    // fetchAllBikes(null, null, mStartDate, mEndDate, mStartTime, mDropTime);
                }else{
                    mDialog.dismiss();
                    //   mStartTime = slots.get(0);
//                    headerViewHolder.mPickUpDate.setText(mStartDate.split("-")[2] +" "+(Util.getMonthInWords(Integer.parseInt(mStartDate.split("-")[1])))+" "+mStartDate.split("-")[0]+ '\n'+mStartTime);
//                    WickedRideManager.getInstance(getApplicationContext()).setPickupTime(mStartTime);
                    isPickUpSelected = true;
                    showDatePickerDialog(findViewById(R.id.tv_drop_date));
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
