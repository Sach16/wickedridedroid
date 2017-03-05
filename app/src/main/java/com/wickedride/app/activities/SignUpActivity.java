package com.wickedride.app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.SignIn;
import com.wickedride.app.models.SignInResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.utils.ValidatorUtils;
import com.wickedride.app.views.WRProgressView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class SignUpActivity extends BaseDefaultActionActivity {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final int RESULT_CODE_SIGN_UP = 1002;
    public final int CROP_IMAGE = 10005;
    @InjectView(R.id.password_title)
    TextView mPasswordTitle;

    @InjectView(R.id.first_name)
    EditText mUserFirstName;

    @InjectView(R.id.last_name)
    EditText mUserLastName;

    @InjectView(R.id.user_phone)
    EditText mUserPhone;

    @InjectView(R.id.user_email)
    EditText mUserEmail;

    @InjectView(R.id.birthday)
    TextView mUserBirthday;

    @InjectView(R.id.password)
    EditText mPassword;

    @InjectView(R.id.confirm_password)
    EditText mConfirmPassword;

    @InjectView(R.id.profile_pic)
    ImageView mProfilePic;

    @InjectView(R.id.progress)
    WRProgressView mProgress;
    String password;
    String  profileImagePath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.birthday, R.id.create, R.id.sign_in, R.id.close,R.id.profile_pic,R.id.img_profile_plus})
    public void onClickListener(View view){
        switch (view.getId()){
            case R.id.profile_pic:
            case R.id.img_profile_plus:
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra(Constants.FROM_SCREEN, "EditUserProfile");
                startActivityForResult(intent, Constants.FROM_CAMERAGALLERY);
                break;
            case R.id.birthday:
                showDatePickerDialog();
                break;
            case R.id.create:
                createNewUser();
                break;
            case R.id.sign_in:
                finish();
                break;
            case R.id.close:
                finish();
                break;
        }
    }

    private void createNewUser() {
        String userFirstName = mUserFirstName.getText().toString();
        String userLastName = mUserLastName.getText().toString();
        String userEmail = mUserEmail.getText().toString();
        String userPhone = mUserPhone.getText().toString();
        String userPassword = mPassword.getText().toString();
        String userConfirmPassword = mConfirmPassword.getText().toString();
        String userDob = mUserBirthday.getText().toString();
        if(userFirstName.isEmpty()) {
            Toast.makeText(getBaseContext(), R.string.enter_proper_name, Toast.LENGTH_SHORT).show();
            return;
        }else if (!ValidatorUtils.isValidEmail(userEmail.trim()) || !ValidatorUtils.isEmailValid(userEmail.trim())) {
            Toast.makeText(getBaseContext(), R.string.enter_proper_email, Toast.LENGTH_SHORT).show();
            return;
        } else if (userPhone.length() < 10 || userPhone.length() > 13 && !ValidatorUtils.isNumeric(userPhone)) {
            Toast.makeText(getBaseContext(), R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();

            return;
        }else if (userPassword.length() < 6) {
            Toast.makeText(getBaseContext(), R.string.password_too_small, Toast.LENGTH_SHORT).show();
            return;
        }else if (!userPassword.equals(userConfirmPassword)) {
            Toast.makeText(getBaseContext(), R.string.password_doesnt_match, Toast.LENGTH_SHORT).show();
            return;
        }else if(userDob.isEmpty()){
            showToast("Kindly select your date of birth.");
            return;
        }

        if(!userPhone.startsWith("+91")){
            showToast("Please enter phone number with +91.");
            return;
        }
        mProgress.setVisibility(View.VISIBLE);
        password=userConfirmPassword;
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.FIRST_NAME, userFirstName);
        params.put(Constants.LAST_NAME, userLastName);
        params.put(Constants.PASSWORD, userPassword);
        params.put(Constants.CONFIRM_PASSWORD, userConfirmPassword);
        params.put(Constants.EMAIL, userEmail);
        params.put(Constants.MOBILE,userPhone);
        params.put(Constants.DOB, Util.changeSendFormat(userDob));
        placeRequest(APIMethods.SIGN_UP, SignInResult.class, params, true);

    }

    private void setDate() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String today = dateFormat.format(c.getTime());
            c.setTime(dateFormat.parse(today));
            c.add(Calendar.DATE, 2);
            mUserBirthday.setText(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //'dd MMM yyyy'
                        mUserBirthday.setText(dayOfMonth + " " + Util.getMonthInWords(monthOfYear+1)+ " " + year);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
        //datePickerDialog.setMinDate(c);
        datePickerDialog.setYearRange(1980, mYear);
        datePickerDialog.setMaxDate(c);
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        if(APIMethods.SIGN_UP.equals(apiMethod)){
            SignInResult result = (SignInResult) response;
            if(result.getResult() != null && result.getResult().getData()!= null) {
                SignIn signIn = result.getResult().getData();
                SessionManager.saveUserSession(getApplicationContext(), Constants.EMAIL_SESSION,
                        signIn.getFirstName(), signIn.getLastName(),
                        signIn.getEmail(), password, signIn.getToken(), signIn.getId(), signIn.getDob(), signIn.getMobile(), null);
                if (profileImagePath != null && !profileImagePath.isEmpty()) {
                    uploadProfilePic(profileImagePath);
                } else {
                    mProgress.setVisibility(View.GONE);
                    setResult(RESULT_CODE_SIGN_UP);
                    finish();
                }
            }else{
                if(result.getResult().getMessage() != null){
                    mProgress.setVisibility(View.GONE);
                    showToast(result.getResult().getMessage());
                }
            }
        }
        if (APIMethods.PROFILE_DETAILS.equals(apiMethod)){
            mProgress.setVisibility(View.GONE);
            SignInResult profileDetails = (SignInResult) response;
            if(profileDetails.getResult() != null && profileDetails.getResult().getData() != null){
                SignIn profileData=profileDetails.getResult().getData();
                String password=SessionManager.getUserPassword(getApplicationContext());
                String token=SessionManager.getSessionToken(getApplicationContext());
                SessionManager.saveUserSession(getApplicationContext(), Constants.EMAIL_SESSION,
                        profileData.getFirstName(), profileData.getLastName(),
                        profileData.getEmail(), password, token, profileData.getId(), profileData.getDob(), profileData.getMobile(), profileData.getImage().getFull());
                setResult(RESULT_CODE_SIGN_UP);
                finish();
            }
        }
    }

    private void uploadProfilePic(String imagePath) {
        File imgFile = new File(imagePath);
        HashMap<String, String> params = new HashMap<>();
        placeMultiPartRequest(APIMethods.PROFILE_DETAILS, SignInResult.class, params, imgFile, Constants.PROFILE_IMAGE);
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.SIGN_UP)) {
            try {
                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                JSONObject result = errorObject.getJSONObject("result");
                String message = result.getString("message");
                showToast(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (APIMethods.PROFILE_DETAILS.equals(apiMethod)) {
            showToast("Unable to upload profile picture.");
            mProgress.setVisibility(View.GONE);
            setResult(RESULT_CODE_SIGN_UP);
            finish();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.FROM_CAMERAGALLERY) {
            if (resultCode == Constants.FROMCAMERA_SUCCESS) {
                Bundle extras = data.getExtras();


                String tempPath = extras.getString(Constants.DATA);//data.getExtras().getString(IntentConstants.DATA);//.substring(11);// getPath(selectedImageUri, SignUpActivity.this);

                File imgFile = new File(tempPath);
                if (imgFile.exists()) {
                    startCroppingImg(null, tempPath, true);
                    /*

                    */
                } else {
                    showToast("No image found");
                }
            }

        } else if (requestCode == CROP_IMAGE) {
            if (resultCode == Constants.FROM_EDITIMAGE_SUCCESS) {

                Bundle extras = data.getExtras();

                profileImagePath = extras.getString(Constants.DATA);
                File imgFile = new File(profileImagePath);
                //if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap circleBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(myBitmap.getWidth() / 2, myBitmap.getHeight() / 2, myBitmap.getWidth() / 2, paint);
                //ivProfileImage.setVisibility(View.VISIBLE);
                //findViewById(R.id.edit_user_profile_add).setVisibility(View.GONE);
                mProfilePic.setImageBitmap(circleBitmap);
                //ivProfileImageClose.setImageResource(R.drawable.closecheckedtextview);
                // profileImageBoolean = false;
            }
        }

    }
    /**
     * Start cropping of image
     *
     * @param circle - if true circle image will be cropped, else square
     */
    public void startCroppingImg(Uri uri, String imgPath, boolean circle) {

        Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
        intent.putExtra(Constants.DATA, imgPath);
        if (uri != null)
            intent.putExtra(Constants.FILE_PATH, uri.toString());
        if(circle) {
            startActivityForResult(intent, CROP_IMAGE);
        }else {
            startActivity(intent);
        }
    }
}
