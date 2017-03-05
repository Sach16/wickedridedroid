package com.wickedride.app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseNoActionBarActivity;
import com.wickedride.app.activities.CameraActivity;
import com.wickedride.app.activities.EditImageActivity;
import com.wickedride.app.activities.EditProfileActivity;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.SignIn;
import com.wickedride.app.models.SignInResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.utils.ValidatorUtils;
import com.wickedride.app.views.WRProgressView;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class EditProfileFragment extends BaseFragment{

    @InjectView(R.id.profile_pic)
    ImageView mProfilePic;

    @InjectView(R.id.profile_pic_Lyt)
    RelativeLayout mProfileLayout;

    @InjectView(R.id.signin_holder)
    LinearLayout mSigninHolder;

    @InjectView(R.id.sign_up_title)
    TextView mSignupTitle;

    @InjectView(R.id.first_name_title)
    TextView mUserNameTitle;

    @InjectView(R.id.change_password)
    TextView mChangePassword;

    @InjectView(R.id.first_name)
    EditText mUserName;

    @InjectView(R.id.user_email)
    EditText mUserEmail;

    @InjectView(R.id.user_phone)
    EditText mUserPhone;

    @InjectView(R.id.user_phone_title)
    TextView mPhoneTitle;

    @InjectView(R.id.create)
    Button mCreate;
    @InjectView(R.id.last_name)
    TextView mLastname;
    @InjectView(R.id.progress)
    WRProgressView mProgress;
    @InjectView(R.id.password_title)
            TextView mPasswordTitle;
    @InjectView(R.id.confirm_password_title)
            TextView mConfirmPasswordTitle;
    @InjectView(R.id.password)
            EditText mPassword;
    @InjectView(R.id.confirm_password)
            EditText mConfirmPassword;
    @InjectView(R.id.birthday)
            TextView mBirthday;
    Dialog pickImageDialog;
    String profileImagePath;
    public final int CROP_IMAGE = 10005;

    //private ProfilePagerAdapter mAdapter;


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
        mView = inflater.inflate(R.layout.activity_signup, container, false);
        ButterKnife.inject(this, mView);
        mPasswordTitle.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mConfirmPasswordTitle.setVisibility(View.GONE);
        mConfirmPassword.setVisibility(View.GONE);
        mSignupTitle.setText("EDIT PROFILE");
        mCreate.setText("UPDATE");
        mSigninHolder.setVisibility(View.GONE);
        mUserPhone.setVisibility(View.VISIBLE);
        mPhoneTitle.setVisibility(View.VISIBLE);
        mChangePassword.setVisibility(View.VISIBLE);
        mUserEmail.setEnabled(false);
        if(!SessionManager.getUserImageUrl(getActivity()).isEmpty()) {
            Picasso.with(getActivity()).load(SessionManager.getUserImageUrl(getActivity())).placeholder(R.drawable.user).into(mProfilePic);
        }
        mUserEmail.setText(SessionManager.getUserEmail(getActivity()));
        mUserName.setText(SessionManager.getUserFirstName(getActivity()));
        mLastname.setText(SessionManager.getUserLastName(getActivity()));
        mUserPhone.setText(SessionManager.getUserPhone(getActivity()));
        mBirthday.setText(Util.changeFormatYMDtoDMY(SessionManager.getUserDOB(getActivity())));

        mUserName.requestFocus();
        mProfileLayout.setEnabled(true);


        return mView;
    }

    @OnClick({R.id.profile_pic_Lyt, R.id.create, R.id.close, R.id.change_password,R.id.birthday})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.profile_pic_Lyt:
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    intent.putExtra(Constants.FROM_SCREEN, "EditUserProfile");
                startActivityForResult(intent, Constants.FROM_CAMERAGALLERY);
                mProfileLayout.setEnabled(false);
                break;
            case R.id.birthday:
                showDatePickerDialog();
                break;
            case R.id.create:
                //update info
                    updateUserInfo();


                break;
            case R.id.change_password:
               /* Intent changePassword = new Intent(getActivity(), EditProfileActivity.class);
                changePassword.putExtra("ChangePassword",true);
                startActivity(changePassword);*/
                ((BaseNoActionBarActivity)getActivity()).loadChangePasswordFregment();
                break;
            case R.id.close:
                ((EditProfileActivity)getActivity()).finish();
                break;
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
                        mBirthday.setText(dayOfMonth + " " + Util.getMonthInWords(monthOfYear+1)+ " " + year);
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setAccentColor(Color.parseColor("#000000"));
        //datePickerDialog.setMinDate(c);
        datePickerDialog.setYearRange(1980,mYear);
        datePickerDialog.setMaxDate(c);
        datePickerDialog.show(getActivity().getFragmentManager(),DATEPICKER_TAG);
    }
    private void updateUserInfo() {

        Log.i("token", SessionManager.getSessionToken(getActivity()));
        String userFirstName = mUserName.getText().toString();
        String userLastName = mLastname.getText().toString();
        String userPhone = mUserPhone.getText().toString();
        String userBirthday = mBirthday.getText().toString();

        if(userFirstName.isEmpty()){
            showToast("Please fill your First Name.");
            return;
        }

        if (userLastName.isEmpty()){
            showToast("Please fill your Last Name.");
            return;
        }
        if (userPhone.isEmpty()){
            showToast("Please fill your phone number.");
            return;
        }

        if (!userPhone.startsWith("+91")) {
            showToast("Please fill your phone number with country code +91.");
        }

        HashMap<String, String> params = new HashMap<>();
        if (!userFirstName.equals(SessionManager.getUserFirstName(getActivity()))){
            params.put(Constants.FIRST_NAME, userFirstName);

        }
        if (!userLastName.equals(SessionManager.getUserLastName(getActivity()))) {

            params.put(Constants.LAST_NAME, userLastName);
        }

        if (!userPhone.equals(SessionManager.getUserPhone(getActivity()))) {
            params.put(Constants.MOBILE, userPhone);
            if (userPhone.length() < 10 || userPhone.length() > 13 && !ValidatorUtils.isNumeric(userPhone)) {
                showToast(R.string.invalid_phone_number);
                return;
            }
        }
        if (!Util.changeToServerDate(userBirthday).equals(SessionManager.getUserDOB(getActivity()))) {
            params.put(Constants.DOB, Util.changeSendFormat(userBirthday));
        }

        if (profileImagePath!=null && !profileImagePath.isEmpty()) {
            File imgFile = new File(profileImagePath);
            if (Util.isNetworkOnline(getActivity())) {

                mProgress.setVisibility(View.VISIBLE);
                placeMultiPartRequest(APIMethods.PROFILE_DETAILS, SignInResult.class, params, imgFile, Constants.PROFILE_IMAGE);
            } else{
                showToast(getActivity().getResources().getString(R.string.no_internet));
            }
        }
        else{
            if (params!=null) {
                if (Util.isNetworkOnline(getActivity())) {

                    mProgress.setVisibility(View.VISIBLE);
                    placeRequest(APIMethods.PROFILE_DETAILS, SignInResult.class, params, true);
                }else{
                        showToast(getActivity().getResources().getString(R.string.no_internet));
                    }
            }
        }
    }

    private void showDialog() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    mProfileLayout.setEnabled(true);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (items[item].equals("Choose from Library")) {
                    mProfileLayout.setEnabled(true);

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            0);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    mProfileLayout.setEnabled(true);

                }
            }
        });
        builder.show();



       /* pickImageDialog = new Dialog(getActivity());
        pickImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pickImageDialog.setContentView(R.layout.popup_logout);
        pickImageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pickImageDialog.getWindow().setGravity(Gravity.CENTER);
        pickImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Button logoutNo=(Button)logoutPopUp.findViewById(R.id.logoutNoBtn);
        //Button logoutYes=(Button)logoutPopUp.findViewById(R.id.logoutYesBtn);
        pickImageDialog.show();*/

    }

    @Override
    public void onResume() {
        super.onResume();
        mProfileLayout.setEnabled(true);

    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }
    // set profile image path here
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

        Intent intent = new Intent(getActivity(), EditImageActivity.class);
        intent.putExtra(Constants.DATA, imgPath);
        if (uri != null)
            intent.putExtra(Constants.FILE_PATH, uri.toString());
        if(circle) {
            startActivityForResult(intent, CROP_IMAGE);
        }else {
            startActivity(intent);
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        mProgress.setVisibility(View.GONE);
        SignInResult profileDetails = (SignInResult) response;
            if(profileDetails.getResult() != null && profileDetails.getResult().getData() != null){
                Log.i("Success", "profile");
                SignIn profileData=profileDetails.getResult().getData();
                String password=SessionManager.getUserPassword(getActivity());
                String token=SessionManager.getSessionToken(getActivity());
                SessionManager.saveUserSession(getActivity(), Constants.EMAIL_SESSION,
                        profileData.getFirstName(), profileData.getLastName(),
                        profileData.getEmail(), password, token, profileData.getId(), profileData.getDob(), profileData.getMobile(), profileData.getImage().getFull());
                Constants.IS_PROFILE_CHANGED=true;

            }

        ((EditProfileActivity)getActivity()).finish();
        showToast("Updated Successfully");
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
        if(error instanceof NoConnectionError) {
            showToast(getActivity().getResources().getString(R.string.no_internet));
        }
    }
}
