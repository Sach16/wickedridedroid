package com.wickedride.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.interfaces.ConfigurationFragmentCallbacks;
import com.wickedride.app.interfaces.ServerCallback;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.utils.Constants;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Ramesh on 1/5/16.
 */
public class BikationConfirmActivity extends ActionBarActivity implements ConfigurationFragmentCallbacks, ServerCallback {

    @InjectView(R.id.summary)
    TextView titleSummary;
    @InjectView(R.id.back_button)
    ImageView backBtn;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.biker_name)
    EditText bikerNameEt;
    @InjectView(R.id.biker_email)
    EditText bikerEmailEt;
    @InjectView(R.id.biker_phone)
    EditText bikerPhoneEt;
    @InjectView(R.id.biker_bike)
    EditText bikerBikeEt;
    @InjectView(R.id.bike_name_small)
    TextView bikationName;
    @InjectView(R.id.days_small)
    TextView noOfDays;
    @InjectView(R.id.total_price)
    TextView totalPrice;
    @InjectView(R.id.confirm)
    Button confirmBtn;

    Bundle data;
    private String orderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bikation_confirm);
        ButterKnife.inject(this);
        data =  getIntent().getExtras();
        if(data != null) {
            titleSummary.setText(data.getString(Constants.BIKATION_TITLE));
            bikationName.setText(data.getString(Constants.BIKATION_TITLE));
            date.setText(data.getString(Constants.START_DATE));
            noOfDays.setText(data.getString(Constants.DAYS));
            totalPrice.setText("Rs. "+ NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(data.getString(Constants.PRICE)))+"/-");

        }
        if(SessionManager.isUserSessionValid(this)){
            bikerNameEt.setText(SessionManager.getUserFirstName(this) + " "+SessionManager.getUserLastName(this));
            bikerEmailEt.setText(SessionManager.getUserEmail(this));
            bikerPhoneEt.setText(SessionManager.getUserPhone(this));
        }

        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick({R.id.back_button, R.id.confirm})
    public void onClickListener(View view) {
        switch (view.getId()){
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.confirm:
                if(bikerNameEt.getText().toString().length() >0 && bikerEmailEt.getText().toString().length() >0 && bikerPhoneEt.getText().toString().length() >0 && bikerBikeEt.getText().toString().length()>0) {
                    onStartTransaction(view);
                }else{
                    Toast.makeText(this, "All Fields are mandatory.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart(){
        super.onStart();
        initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        orderId = ""+ (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);
    }

    @Override
    public void onFrictionChanged(float friction) {

    }

    @Override
    public void openDialog(float friction) {

    }

    @Override
    public void openActivity(Intent intent) {

    }

    @Override
    public void complete(int code) {

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {

    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

    }

    public void onStartTransaction(View view) {
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("MID", Constants.MID);
        paramMap.put("CUST_ID", SessionManager.getUserEmail(this));
        paramMap.put("CHANNEL_ID", Constants.CHANNEL_ID);
        paramMap.put("INDUSTRY_TYPE_ID", Constants.INDUSTRY_TYPE_ID);
        paramMap.put("WEBSITE", Constants.WEBSITE);
        paramMap.put("TXN_AMOUNT", data.getString(Constants.PRICE));
        paramMap.put("THEME", Constants.PAY_THEME);
        paramMap.put("EMAIL", SessionManager.getUserEmail(this));
        paramMap.put("MOBILE_NO", SessionManager.getUserPhone(this));
        PaytmOrder Order = new PaytmOrder(paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
               Constants.PAYTM_GENERATE_CHECKSUM_URL,
                Constants.PAYTM_VERIFY_CHECKSUM_URL);

        PaytmClientCertificate Certificate = new PaytmClientCertificate(
                "admin", "client");
        Service.initialize(Order, Merchant, Certificate);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionSuccess(Bundle inResponse) {
                        // After successful transaction this method gets called.
                        // // Response bundle contains the merchant response
                        // parameters.
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
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
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        // TODO Auto-generated method stub
                    }

                });
    }
}
