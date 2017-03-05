package com.wickedride.app.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseNoActionBarActivity;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.manager.WickedRideManager;
import com.wickedride.app.models.GetResultData;
import com.wickedride.app.models.GetResultDataArr;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class SummaryFragment extends BaseFragment {

    @InjectView(R.id.bike_image)
    ImageView bikeImage;

    @InjectView(R.id.bike_price)
    TextView bikePrice;
    @InjectView(R.id.bike_name)
    TextView bikeName;
    @InjectView(R.id.location)
    TextView locationNme;
    @InjectView(R.id.dropDate)
    TextView dropDateTv;
    @InjectView(R.id.startDate)
    TextView startDateTv;
    @InjectView(R.id.bike_name_small)
    TextView bikeNameSmall;
    @InjectView(R.id.days_small)
    TextView daysSmall;
    @InjectView(R.id.total_price)
    TextView totalPriceTv;
    @InjectView(R.id.promo_applybtn)
    Button applyPromoBtn;
    @InjectView(R.id.promo_et)
    EditText promoEt;
    @InjectView(R.id.accessories_holder)
    HorizontalScrollView accessoriesHolder;
    @InjectView(R.id.accessories_holder_Lyt)
    LinearLayout mAccessoryLyt;
    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;


    List<String> mImageUrls;
    List<String> mPrices;
    List<String> mBikeNames;

    private String bikeNameStr,bikeImageUrl, startDate, dropDate, locationSelcted, accessoriesImagesUrls, accessoriesPrices,totalPrice;
    private String orderId;
    private String accessoriesNamesStr;
    private String accessoriesIds;
    private Integer finalPrice;
    private String merchantID,bankTxnId,bankName,currency, gateWay, paymentMode, txnAmt, txnDate, txnId, txnType, pg_status, service_provider, raw_data;
    private boolean isStatusCompleted;
    private boolean isCancelled;

    @Override
    public String getSelfTag() {
        return null;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.inject(this, view);
        initOrderId();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        bikeImageUrl = getArguments().getString(Constants.BIKE_IMAGE_URL);
        startDate = getArguments().getString(Constants.START_DATE);
        dropDate = getArguments().getString(Constants.DROP_DATE);
        locationSelcted = getArguments().getString(Constants.LOCATION);
        accessoriesImagesUrls = getArguments().getString(Constants.ACCESSORY_IMAGES);
        totalPrice = getArguments().getString(Constants.PRICE);
        bikeNameStr = getArguments().getString(Constants.BIKE_NAME);
        accessoriesNamesStr = getArguments().getString(Constants.ACCESSORIES_DESCRIPTION);
        accessoriesIds = getArguments().getString(Constants.ACCESSORIES_IDS);

        if(accessoriesImagesUrls != null && !accessoriesImagesUrls.isEmpty()) {
            mImageUrls = new ArrayList<String>(Arrays.asList(accessoriesImagesUrls.split(",")));
            Log.i("img urls", mImageUrls.size() + ":" + accessoriesImagesUrls);
            accessoriesPrices = getArguments().getString(Constants.ACCESSORY_PRICE);
            mPrices = new ArrayList<String>(Arrays.asList(accessoriesPrices.split(",")));
            mBikeNames = new ArrayList<String>(Arrays.asList(accessoriesNamesStr.split(",")));
            Log.i("img prices", mPrices.size() + ":" + accessoriesPrices);

            if (mPrices != null && mPrices.size()>0){
                loadAccessoryData();
            }
        }
        startDateTv.setText(startDate);
        dropDateTv.setText(dropDate);
        locationNme.setText(locationSelcted);
        totalPriceTv.setText("Rs."+ NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(WickedRideManager.getInstance(getActivity()).getPriceInclusiveOfVat()))+"/-");
        bikeName.setText(bikeNameStr);
        bikeNameSmall.setText(bikeNameStr);
        bikePrice.setText("Rs."+ NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(totalPrice))+"/-");


        if(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs() != null && WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().length() >0) {
            daysSmall.setText(WickedRideManager.getInstance(getActivity()).getNoOfDaysHrs().split(",")[1] + " hours");
        }
        if(bikeImageUrl != null && !bikeImageUrl.isEmpty()) {
            Picasso.with(getActivity()).load(bikeImageUrl)
                    .placeholder(R.drawable.place_holder).into(bikeImage);
        }


        return view;
    }

    private void loadAccessoryData() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for(int i=0;i<mPrices.size();i++) {
            Log.i(i + "price", mPrices.get(i) + "::imag" + mImageUrls.get(i));
            View accessory = inflater.inflate(R.layout.snippet_accessory_holder, null);
            ((TextView) accessory.findViewById(R.id.text_accessory_price)).setText(mPrices.get(i));
            ((TextView)accessory.findViewById(R.id.text_accessory_name)).setText(mBikeNames.get(i));
            ImageView imgBike=(ImageView)accessory.findViewById(R.id.image_accessory);
            Picasso.with(getActivity()).load(mImageUrls.get(i))
                    .placeholder(R.drawable.place_holder).fit().centerCrop().into(imgBike);

            mAccessoryLyt.addView(accessory);


        }

        }

    @OnClick({R.id.confirm,R.id.promo_applybtn})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.confirm:
//                ((BaseNoActionBarActivity) getActivity()).loadHappyRidingFragment();
                //clear booking ID

                    Util.hideKeyboard(getActivity());
                callCheckAvailability();

                SessionManager.setBookingId(getActivity(),"", "");
//                sendPrebooking call

                //Commented as we are not sending this status as per Sharath from 1985 as of now
//                sendBookingStatusCalls("PENDING", null);
                break;
            case R.id.promo_applybtn:
                Util.hideKeyboard(getActivity());
                if(promoEt.getText().toString().length() >0) {
//                    HashMap<String, String> paramsq = new HashMap<>();
//                    paramsq.put(Constants.PROMO_CODE, promoEt.getText().toString());
//                    placeRequest(APIMethods.PROMO_APPLY_API, GetResultData.class, paramsq, true);
                    fetcBookingTotalPrice(""+SessionManager.getUserCityID(getActivity()), WickedRideManager.getInstance(getActivity()).getBikeId(), startDate.split("\n")[0],dropDate.split("\n")[0],startDate.split("\n")[1],dropDate.split("\n")[1],promoEt.getText().toString(), SessionManager.getSessionId(getActivity()),accessoriesIds);
                }else{
                    showToast("Enter promo code");
                }
                break;
        }
    }

    private void callCheckAvailability() {
        mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.START_DATE, Util.getMonthInNumberFormatFromNameFormat(startDate.split("\n")[0]));
        params.put(Constants.START_TIME, startDate.split("\n")[1]);
        params.put(Constants.DROP_DATE, Util.getMonthInNumberFormatFromNameFormat(dropDate.split("\n")[0]));
        params.put(Constants.DROP_TIME, dropDate.split("\n")[1]);
        params.put(Constants.MODEL_ID, WickedRideManager.getInstance(getActivity()).getBikeId());
        params.put(Constants.AREA_ID, ""+WickedRideManager.getInstance(getActivity()).getAreaId());
        params.put(Constants.CITY_ID, ""+SessionManager.getUserCityID(getActivity()));
        placeRequest(APIMethods.CHECK_AVAILABILITY , GetResultData.class, params, false, false, false);

    }

    private void sendBookingStatusCalls(String status,String bookingId){
        mProgress.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.START_DATE, Util.getMonthInNumberFormatFromNameFormat(startDate.split("\n")[0]));
        params.put(Constants.START_TIME, startDate.split("\n")[1]);
        params.put(Constants.DROP_DATE, Util.getMonthInNumberFormatFromNameFormat(dropDate.split("\n")[0]));
        params.put(Constants.DROP_TIME, dropDate.split("\n")[1]);
        params.put(Constants.MODEL_ID, WickedRideManager.getInstance(getActivity()).getBikeId());
        params.put(Constants.STATUS, status);
        params.put(Constants.AREA_ID, ""+WickedRideManager.getInstance(getActivity()).getAreaId());
//        params.put(Constants.TOTAL_PRICE, "1152");
        if(bookingId != null) {
            if(status.equals("COMPLETED")){
                params.put(Constants.MERCHANT_ID, merchantID);
                params.put(Constants.BANK_TXN_ID, bankTxnId);
                params.put(Constants.BANK_NAME, bankName);
                params.put(Constants.CURRENCY, currency);
                params.put(Constants.GATEWAY, gateWay);
                params.put(Constants.PAYMENT_MODE, paymentMode);
                params.put(Constants.TXN_AMT, txnAmt);
                params.put(Constants.TXN_DATE, txnDate);
                params.put(Constants.TXN_ID, txnId);
                params.put(Constants.TXN_TYPE, txnType);
                params.put(Constants.PG_STATUS, pg_status);
                params.put(Constants.SERVICE_PROVIDER, service_provider);
                params.put(Constants.ORDER_ID, orderId);
                params.put(Constants.MODEL_ID,WickedRideManager.getInstance(getActivity()).getBikeId());
                params.put(Constants.RAW_DATA, raw_data);
                //This is PUT request as per API doc, change /test as per live server on launch
                placeRequest(APIMethods.PRE_BOOK + "/"+ bookingId+"/test", GetResultData.class, params, false, true, true);
            }else {
//                //This is PUT request as per API doc, change /test as per live server on launch
//                params.put(Constants.ORDER_ID, orderId);
//                placeRequest(APIMethods.PRE_BOOK +"/" + bookingId+"/test", GetResultData.class, params, false, true, true);
            }
        }else{
//            //This is POST request as per API doc, change /test as per live server on launch
//            placeRequest(APIMethods.PRE_BOOK+"/test", GetResultData.class, params, true, true, false);

        }

    }



    private void fetcBookingTotalPrice(String areaId, String modleId, String startDate, String dropDate, String startTime, String dropTime, String couponCode, String userId, String accessoriesIds) {
        mProgress.setVisibility(View.VISIBLE);

        String apiMethod = APIMethods.PROMO_APPLY_API;
        HashMap<String, String> params = new HashMap<>();
        if (modleId != null)
            params.put(Constants.MODEL_ID, modleId);
        if (areaId != null)
            params.put(Constants.AREA_ID, areaId);
        if (startDate != null)
            params.put(Constants.START_DATE,  Util.getMonthInNumberFormatFromNameFormat(startDate.split("\n")[0]));
        if (dropDate != null)
            params.put(Constants.DROP_DATE,  Util.getMonthInNumberFormatFromNameFormat(dropDate.split("\n")[0]));
        if (startTime != null)
            params.put(Constants.START_TIME, startTime);
        if (dropTime != null)
            params.put(Constants.DROP_TIME, dropTime);
        if(SessionManager.getUserCityID(getActivity()) != -1)
            params.put(Constants.CITY_ID, ""+SessionManager.getUserCityID(getActivity()));
        if(couponCode != null)
            params.put(Constants.COUPON, couponCode);
        if(userId != null)
            params.put(Constants.USER_ID, userId);
        if(accessoriesIds != null)
            params.put(Constants.ACCESSORIES_IDS, accessoriesIds);
        if(totalPrice != null)
            params.put(Constants.PRICE, ""+totalPrice);

        placeRequest(apiMethod, GetResultData.class, params);
    }


    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        mProgress.setVisibility(View.GONE);

        if(apiMethod.contains(APIMethods.PRE_BOOK)) {
            GetResultData bookingData = (GetResultData) response;
            if(bookingData.getResult().getData() != null && (bookingData.getResult().getData().getId() != null && bookingData.getResult().getData().getReferenceId()!=null) || bookingData.getResult().getData().getBookingId()!= null){
                SessionManager.setBookingId(getActivity(),bookingData.getResult().getData().getId(), bookingData.getResult().getData().getReferenceId());
                if(isStatusCompleted){
                    ((BaseNoActionBarActivity) getActivity()).loadHappyRidingFragment();
                }
                else {
                    if(isCancelled){
                        showToast("Your transaction has been cancelled.");
                        isCancelled = false;
                    }else {
                    }
                }
            }else{
                showToast(bookingData.getResult().getData().getError());
            }
        }
        if(apiMethod.equals(APIMethods.PROMO_APPLY_API)){
            GetResultData updatedTotalPriceData = (GetResultData) response;
            if(updatedTotalPriceData.getResult().getData() != null) {
                if(updatedTotalPriceData.getResult().getData().getFinalPrice() != null && updatedTotalPriceData.getResult().getData().getStatus()) {
                    finalPrice = updatedTotalPriceData.getResult().getData().getFinalPrice();
                    WickedRideManager.getInstance(getActivity()).setBookingPrice(finalPrice + "");
                    totalPriceTv.setText("Rs." + NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(WickedRideManager.getInstance(getActivity()).getPriceInclusiveOfVat())) + "/-");
                    showToast(updatedTotalPriceData.getResult().getData().getMessage());

                }else{
                    showToast(updatedTotalPriceData.getResult().getData().getMessage());
                }
            }else{
                showToast(updatedTotalPriceData.getResult().getMessage() + "");
            }

        }
        if(apiMethod.equals(APIMethods.CHECK_AVAILABILITY)){
            GetResultData bookingData = (GetResultData) response;
            if(bookingData.getResult().getData().getBikeId()!=null) {
                onStartTransaction(mView);
            }else{
                showToast(bookingData.getResult().getMessage());
            }

        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }


    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = ""+ (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", Constants.MID);
        paramMap.put("CUST_ID", SessionManager.getUserEmail(getActivity()));
        paramMap.put("CHANNEL_ID", Constants.CHANNEL_ID);
        paramMap.put("INDUSTRY_TYPE_ID", Constants.INDUSTRY_TYPE_ID);
        paramMap.put("WEBSITE", Constants.WEBSITE);
        paramMap.put("TXN_AMOUNT", WickedRideManager.getInstance(getActivity()).getPriceInclusiveOfVat());
        paramMap.put("THEME", Constants.PAY_THEME);
        paramMap.put("EMAIL", SessionManager.getUserEmail(getActivity()));
        paramMap.put("MOBILE_NO", SessionManager.getUserPhone(getActivity()));
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                Constants.PAYTM_GENERATE_CHECKSUM_URL,
                Constants.PAYTM_VERIFY_CHECKSUM_URL);

        PaytmClientCertificate Certificate = new PaytmClientCertificate(
                "admin", "client");
        Service.initialize(Order, Merchant, Certificate);

        Service.startPaymentTransaction(getActivity(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.


                        //Commented as we are not sending this status as per Sharath from 1985 as of now
//                        sendBookingStatusCalls("FAILED", SessionManager.getBookingID(getActivity()));

                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getActivity().getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();

                        //merchant_id,  bank_txn_id, bank_name, currency, gateway, payment_mode,
//                        txn_amt, txn_date, txn_id, txn_type, pg_status(payment gateway status string),
//                        ‘service_provider’(PAYTM), ‘raw_data’ (This will be a json encoded string of entire response data you received ie.,  just the key-value pairs)
//Send above things
                        //{"MID":"Wicked38054624802526","ORDERID":"20000388","TXNAMOUNT":"1512.00","CURRENCY":"INR","TXNID":"543899","BANKTXNID":"201601270337868","STATUS":"TXN_SUCCESS","RESPCODE":"01","RESPMSG":"Txn Successful.","TXNDATE":"2016-01-27 12:49:33.0","GATEWAYNAME":"ICICI","BANKNAME":"HDFC","PAYMENTMODE":"DC","IS_CHECKSUM_VALID":"Y","TXNTYPE":"","REFUNDAMT":""}

                        merchantID = inResponse.getString("MID");
                        bankTxnId = inResponse.getString("TXNID");
                        bankName = inResponse.getString("BANKNAME");
                        currency = inResponse.getString("CURRENCY");
                        gateWay = inResponse.getString("GATEWAYNAME");
                        paymentMode = inResponse.getString("PAYMENTMODE");
                        txnAmt = inResponse.getString("TXNAMOUNT");
                        txnDate = inResponse.getString("TXNDATE");
                        txnId = inResponse.getString("TXNID");
                        txnType = inResponse.getString("TXNTYPE");
                        pg_status = inResponse.getString("STATUS");
                        orderId = inResponse.getString("ORDERID");
                        service_provider = "PAYTM";
                        raw_data = inResponse.toString().replace("Bundle[", "");
                        raw_data = raw_data.replace("]","");
                        sendBookingStatusCalls("COMPLETED", SessionManager.getBookingID(getActivity()));
                        isStatusCompleted = true;
                    }

                    @Override
                    public void onTransactionFailure(String inErrorMessage,
                                                     Bundle inResponse) {
                        // This method gets called if transaction failed. //
                        // Here in this case transaction is completed, but with
                        // a failure. // Error Message describes the reason for
                        // failure. // Response bundle contains the merchant
                        // response parameters.
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getActivity().getBaseContext(), "Payment Transaction Failed "+inErrorMessage, Toast.LENGTH_LONG).show();
                        //Commented as we are not sending this status as per Sharath from 1985 as of now
//                        sendBookingStatusCalls("FAILED", SessionManager.getBookingID(getActivity()));
                        isCancelled = true;
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                        //Commented as we are not sending this status as per Sharath from 1985 as of now
//                        sendBookingStatusCalls("FAILED", SessionManager.getBookingID(getActivity()));
                        isCancelled = true;
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.


                        //Commented as we are not sending this status as per Sharath from 1985 as of now
//                      sendBookingStatusCalls("FAILED", SessionManager.getBookingID(getActivity()));
                        isCancelled = true;

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        //Commented as we are not sending this status as per Sharath from 1985 as of now

//                        sendBookingStatusCalls("FAILED", SessionManager.getBookingID(getActivity()));
                        isCancelled = true;

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                        //Commented as we are not sending this status as per Sharath from 1985 as of now

//                        sendBookingStatusCalls("CANCELLED", SessionManager.getBookingID(getActivity()));
                        isCancelled = true;

                    }

                });
    }
}
