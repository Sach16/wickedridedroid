package com.wickedride.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.models.GetResultDataString;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.utils.ValidatorUtils;
import com.wickedride.app.views.WRProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class ForgotPasswordActivity extends BaseDefaultActionActivity {

    @InjectView(R.id.user_email)
    EditText mUserEmail;

    @InjectView(R.id.progressLayout)
    RelativeLayout mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.inject(this);
        mUserEmail.postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.showKeyboard(ForgotPasswordActivity.this, mUserEmail, true);
            }
        }, 200);
    }

    @OnClick({R.id.close, R.id.send, R.id.sign_in})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.send:
                requestNewPassword();
                break;
            case R.id.sign_in:
                onBackPressed();
                break;
        }
    }

    private void requestNewPassword() {
        String emailID = mUserEmail.getText().toString();
        if (ValidatorUtils.isEmailValid(emailID) && ValidatorUtils.isValidEmail(emailID)) {
            mProgress.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put(Constants.EMAIL, emailID);
            placeRequest(APIMethods.FORGOT_PASSWORD, GetResultDataString.class, params);
        }else {
            showToast(R.string.enter_proper_email);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.FORGOT_PASSWORD)) {
            try {
                GetResultDataString resultData= (GetResultDataString)response;
                showToast(resultData.getResult().getMessage());
                if(resultData.getResult().getData().equals("Message successfully sent.")) {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.FORGOT_PASSWORD)) {
            Log.d("FPA", "error::" + error);
            Log.d("FPA", "error::" + error.networkResponse.statusCode);
            try {
                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                JSONObject result = errorObject.getJSONObject("result");
                String message = result.getString("message");
                showToast(message);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Toast.makeText(getApplicationContext(), "Invalid Email ID", Toast.LENGTH_SHORT).show();
    }
}
