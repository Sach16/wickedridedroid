package com.wickedride.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.wickedride.app.R;
import com.wickedride.app.manager.SessionManager;
import com.wickedride.app.models.GraphObject;
import com.wickedride.app.models.SignIn;
import com.wickedride.app.models.SignInResult;
import com.wickedride.app.utils.APIMethods;
import com.wickedride.app.utils.Constants;
import com.wickedride.app.utils.Util;
import com.wickedride.app.utils.ValidatorUtils;
import com.wickedride.app.views.WRProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * Created by Nitish Kulkarni on 2/8/15.
 */
public class SignInActivity extends BaseDefaultActionActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "SignInActivity";

    //Google Declarations
    private static final int RC_SIGN_IN = 9001;
    public static final int RESULT_CODE_SIGN_IN = 10010;
    public static final int RESULT_CODE_SIGN_UP = 1002;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    //Views
    @InjectView(R.id.fb_login_button)
    RelativeLayout mFBLoginButton;
    @InjectView(R.id.gp_login_button)
    RelativeLayout mGPLoginButton;
    @InjectView(R.id.fb_login_text)
    TextView mFBLoginText;
    @InjectView(R.id.gp_login_text)
    TextView mGPLoginText;
    @InjectView(R.id.forgot_pwd)
    TextView mForgotPassword;
    @InjectView(R.id.user_name)
    EditText mUserName;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.progress)
    WRProgressView mProgress;
    String savedPassword;

    //Facebook Callbacks
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

        //GoogleAPIClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        //FaceBookSDK Initialize
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "Facebook Login Success::" + loginResult);
                        Set<String> permissionsDenied = loginResult.getRecentlyDeniedPermissions();
                        Log.d(TAG, "Facebook Login Success Permission Denied::" + permissionsDenied);
                        final AccessToken accesstoken = loginResult.getAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("SigninActivity", response.toString());
                                        Log.v("SigninActivity", object.toString());
                                        GraphObject graphObject = new Gson().fromJson(object.toString(), GraphObject.class);
//                                        SessionManager.saveUserSession(getApplicationContext(), Constants.FB_SESSION, graphObject.getName(),
//                                                graphObject.getEmail(), null, accesstoken.toString(), graphObject.getId(),
//                                                graphObject.getDate_of_birth(), getFacebookProfilePicUrl(graphObject.getId()));
                                        Log.v("SigninActivity", "User Image::" + getFacebookProfilePicUrl(graphObject.getId()));
                                        finish();

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignInActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Facebook Login Cancel::");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SignInActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Facebook Login Error::" + exception);
                    }
                });

        setContentView(R.layout.activity_sign_in);
        ButterKnife.inject(this);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Bold.otf");
        mFBLoginText.setTypeface(typeface);
        mGPLoginText.setTypeface(typeface);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginUser();
                }
                return false;
            }
        });

        mUserName.postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.showKeyboard(SignInActivity.this,mUserName,true);
            }
        }, 200);
    }

    @OnClick({R.id.login, R.id.fb_login_button, R.id.gp_login_button, R.id.close, R.id.forgot_pwd, R.id.sign_up})
    public void onClickListener(View view) {
        switch (view.getId()) {
            case R.id.login:
                loginUser();
                break;
            case R.id.fb_login_button:
                fbLogin();
                break;
            case R.id.gp_login_button:
                gpLogin();
                break;
            case R.id.close:
                onBackPressed();
                break;
            case R.id.forgot_pwd:
                openForgotPassword();
                break;
            case R.id.sign_up:
                openSignup();
                break;
        }
    }

    private void loginUser() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        Log.d(TAG, "UserName::" + userName + " Password::" + password);
        if (ValidatorUtils.isValidEmail(userName) && ValidatorUtils.isEmailValid(userName)) {
            if (!password.isEmpty()) {
                mProgress.setVisibility(View.VISIBLE);
                savedPassword=password;
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.EMAIL, userName);
                params.put(Constants.PASSWORD, password);
                placeRequest(APIMethods.SIGN_IN, SignInResult.class, params, true);
                Util.showKeyboard(this, mPassword, false);

            } else {
                showToast(R.string.enter_proper_password);

            }
        } else {
            showToast(R.string.enter_proper_email);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void openSignup() {
        Intent openSignup = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(openSignup, RESULT_CODE_SIGN_UP);
    }

    private void openForgotPassword() {
        Intent openForgotPassword = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(openForgotPassword);
    }

    private void gpLogin() {

        Toast.makeText(getApplicationContext(), "Google Logging in", Toast.LENGTH_SHORT).show();
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    private void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email, public_profile, user_birthday"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        } else if (resultCode == RESULT_CODE_SIGN_UP) {
            setResult(RESULT_CODE_SIGN_IN);
            finish();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            updateUI(false);
        }
    }

    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected:" + bundle);
        // Show the signed-in UI
        updateUI(true);
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                            updateUI(false);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            String errorString = getString(R.string.play_services_error_fmt, errorCode);
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

            mShouldResolve = false;
            updateUI(false);
        }
    }

    private void updateUI(boolean isSignedIn) {
        Log.d(TAG, "updateUI isSignedIn::" + isSignedIn);
        if (isSignedIn) {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String userID = currentPerson.getId();
                String biryhday = currentPerson.getBirthday();
                Log.d(TAG, "updateUI Person ID::" + userID);
                Log.d(TAG, "updateUI Person Name::" + personName);
                Log.d(TAG, "updateUI Person DOB::" + currentPerson.getBirthday());
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Log.d(TAG, "updateUI Person Email::" + email);
                String personPhoto = currentPerson.getImage().toString();
                try {
                    JSONObject jsonObject = new JSONObject(personPhoto);
                    personPhoto = jsonObject.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "updateUI Person Photo::" + personPhoto);
//                String personGooglePlusProfile = currentPerson.getUrl();
//                Log.d(TAG, "updateUI Person Profile::" + personGooglePlusProfile);
//                SessionManager.saveUserSession(getApplicationContext(), Constants.GPLUS_SESSION, personName,
//                        email, null, null, userID, biryhday, personPhoto);
                finish();
            }
        } else {
            // Show signed-out message
        }
    }


    public static String getFacebookProfilePicUrl(String userID) {
        return "https://graph.facebook.com/" + userID + "/picture?type=large";
    }

    public static Bitmap getFacebookProfilePicture(String userID) {
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        Log.d(TAG, "ApiMethod::" + apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.SIGN_IN)) {
            SignInResult result = (SignInResult) response;
            if(result.getResult().getData() != null) {
                SignIn signIn = result.getResult().getData();
                SessionManager.saveUserSession(getApplicationContext(), Constants.EMAIL_SESSION,
                        signIn.getFirstName(), signIn.getLastName(),
                        signIn.getEmail(), savedPassword, signIn.getToken(), signIn.getId(), signIn.getDob(), signIn.getMobile(), signIn.getImage().getFull());
                setResult(RESULT_CODE_SIGN_IN);
                finish();
            }else{
                showToast(result.getResult().getMessage());
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        mProgress.setVisibility(View.GONE);
        if (apiMethod.startsWith(APIMethods.SIGN_IN)) {
            Log.d(TAG, "error::" + error);
            Log.d(TAG, "error::" + error.networkResponse.statusCode);
            Util.showKeyboard(this, mPassword, false);

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
    }
}
