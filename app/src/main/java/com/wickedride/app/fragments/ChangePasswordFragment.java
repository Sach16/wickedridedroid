package com.wickedride.app.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.wickedride.app.R;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.RequestPasswordResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class ChangePasswordFragment extends BaseFragment {

    @InjectView(R.id.old_password)
    EditText mOldPassword;

    @InjectView(R.id.new_password)
    EditText mNewPassword;

    @InjectView(R.id.confirm_new_password)
    EditText mConfirmPassword;

    @InjectView(R.id.reset_password)
    Button mResetPassword;

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.inject(this, mView);
        mOldPassword.requestFocus();
        mOldPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.showKeyboard(getActivity(), mOldPassword, true);
            }
        }, 200);
        return mView;
    }

    @OnClick({R.id.reset_password,R.id.close_password})
    public void onClickListener(View view) {
        switch (view.getId()) {

            case R.id.reset_password:
                requestNewPassword();
                break;
            case R.id.close_password:
                getActivity().finish();
                break;
        }
    }

    private void requestNewPassword() {
        if(mOldPassword.getText().toString().length() == 0){
            showToast("Enter your old password.");
            return;
        }
        if(mNewPassword.getText().toString().length() ==0){
            showToast("Enter your new password.");
             return;
        }
        if(mConfirmPassword.getText().toString().length() == 0){
            showToast("Enter your confirm password.");
            return;
        }

        if(mOldPassword.getText().toString().length() <6 || mNewPassword.getText().toString().length() <6 || mConfirmPassword.getText().toString().length() <6){
            showToast("Minimum password length is 6.");
            return;
        }

        if (mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString()) && mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {

            //  placeRequest(APIMethods.RESET_PASSWORD + "/" + SessionManager.getUserEmail(getActivity()) + "/" + mOldPassword.getText().toString() + "/" + mNewPassword.getText().toString(), RequestPasswordResult.class);
            // mProgress.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put(Constants.EMAIL, SessionManager.getUserEmail(getActivity()));
            params.put(Constants.OLD_PASSWORD, mOldPassword.getText().toString());
            params.put(Constants.NEW_PASSWORD, mNewPassword.getText().toString());
            placeRequest(APIMethods.RESET_PASSWORD, RequestPasswordResult.class, params, true);

        }else if(!mNewPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
            showToast("New passwords doesn't match.");
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

       // {"result":{"errors":{"new_password":["The new password field is required."]},"message":"Validation Failed!","status_code":200}}

        if(apiMethod.startsWith(APIMethods.RESET_PASSWORD)){
            RequestPasswordResult result = (RequestPasswordResult) response;
            if(result.getResult() != null && result.getResult().getData() != null) {
                showToast("Password changes successfully !!");
                getActivity().finish();
            }else{
                showToast((result.getResult().getData() != null) ? result.getResult().getData() : result.getResult().getMessage());
            }
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
}
