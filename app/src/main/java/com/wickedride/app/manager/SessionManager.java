package com.wickedride.app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.wickedride.app.models.FeedParams;
import com.wickedride.app.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Util class for storing and retrieving shared preferences. Must call {@link #initiateSession} on app launch.
 */
public class SessionManager {
    public static final int SESSION_TYPE_EMAIL = 1;
    public static final int SESSION_TYPE_FACEBOOK = 2;
    public static final int SESSION_TYPE_GPLUS = 3;
    public static final String SHARED_PREFS = "WickedRide";
    public static final String PREF_USER_CITY = "userCity";
    public static final String PREF_USER_CITY_ID = "userCityId";
    public static final String PREF_FILE_USERSESSION = "userSession";
    private static final String TAG = "StateManager";
    private static final String PREF_KEY_USERSESSION_TYPE = "sessionType";
    private static final String PREF_KEY_USERSESSION_FB_AUTHTOKEN = "sessionFbAuthToken";
    private static final String PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES = "sessionFbAuthTokenExpires";


    // --- Permanent Information
    private static final String PREF_KEY_USERSESSION_ID = "sessionId";
    private static final String PREF_KEY_USER_PROFILE_PIC = "profile_pic";
    private static final String PREF_KEY_FB_USER_ID = "sessionId";
    private static final String PREF_KEY_GPLUS_USER_ID = "sessionId";
    private static final String PREF_KEY_USERSESSION_USERFIRSTNAME = "PREF_KEY_USERSESSION_USERFIRSTNAME";
    private static final String PREF_KEY_USERSESSION_USERLASTNAME = "PREF_KEY_USERSESSION_USERLASTNAME";
    private static final String PREF_KEY_USERSESSION_EMAIL = "PREF_KEY_USERSESSION_EMAIL";
    private static final String PREF_KEY_USERSESSION_PASSWORD = "PREF_KEY_USERSESSION_PASSWORD";
    private static final String PREF_KEY_USER_DATE_OF_BIRTH = "PREF_KEY_USER_DATE_OF_BIRTH";
    private static final String PREF_USER_CITIES = "allCities";
    private static final String PREF_KEY_USERSESSION_AUTHTOKEN = "PREF_KEY_USERSESSION_AUTHTOKEN";
    private static final String PREF_KEY_USERSESSION_MOBILE = "PREF_KEY_USERSESSION_MOBILE";
    private static final String PREF_ABOUT_URL = "PREF_ABOUT_URL";
    private static final String PREF_REVIEW_URL = "PREF_REVIEW_URL";
    private static final String PREF_CONTACT_URL = "PREF_CONTACT_URL";
    private static final String PREF_TARIFF_URL = "PREF_TARIFF_URL";
    private static final String PREF_FAQ_URL = "PREF_FAQ_URL";
    private static final String PREF_REF_BOOKING_ID = "PREF_REF_BOOKING_ID";
    private static final String PREF_BOOKING_ID = "PREF_BOOKING_ID";

    private static Editor mEditor;
    private static SharedPreferences mPref;

   // public static ArrayList<ArrayList<String>> timeSlotsForSelectedMonth;

    public static HashMap<String,HashMap<String,ArrayList<String>>> allMonthsTimeSlots;

    public static HashMap<String,ArrayList<String>> timeSlotsForSelectedMonth;

    public static String endTimeSlot;

    public static String endDate;



    // TODO: bit OTT re-factor; Add generic login interface on top of different types of login

    /**
     * Save the user session to the shared preferences. WARNING: PREVIOUS SESSION WILL BE CLEARED.
     *
     * @param context
     * @param sessionType  cannot be NULL.
     * @param userFirstName cannot be NULL.
     * @param userLastName cannot be NULL.
     * @param email        cannot be NULL.
     * @param password     cannot be NULL.
     * @param authToken    cannot be NULL.
     */
    public static void saveUserSession(Context context, int sessionType, String userFirstName,String userLastName,
                                       String email, String password, String authToken, String UserID,
                                       String dob, String phone,String imgUrl){

        Log.d(TAG,"FirstName::" + userFirstName);
        Log.d(TAG,"LastName::" + userLastName);
        clearUserSession(context);
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();

        mEditor.putInt(PREF_KEY_USERSESSION_TYPE, sessionType);
        mEditor.putString(PREF_KEY_USERSESSION_USERFIRSTNAME, userFirstName);
        mEditor.putString(PREF_KEY_USERSESSION_USERLASTNAME, userLastName);
        mEditor.putString(PREF_KEY_USERSESSION_EMAIL, email);
        mEditor.putString(PREF_KEY_USER_PROFILE_PIC, imgUrl);
        mEditor.putString(PREF_KEY_USERSESSION_MOBILE, phone);
        if (sessionType == Constants.EMAIL_SESSION) {
            mEditor.putString(PREF_KEY_USERSESSION_PASSWORD, password);
            mEditor.putString(PREF_KEY_USERSESSION_ID, UserID);
            mEditor.putString(PREF_KEY_USERSESSION_AUTHTOKEN,authToken);
        } else if (sessionType == Constants.FB_SESSION) {
            mEditor.putString(PREF_KEY_FB_USER_ID, UserID);
            mEditor.putString(PREF_KEY_USERSESSION_FB_AUTHTOKEN, authToken);
        }else if(sessionType == Constants.GPLUS_SESSION){
            mEditor.putString(PREF_KEY_GPLUS_USER_ID, UserID);
        }
        if (dob != null) {
            mEditor.putString(PREF_KEY_USER_DATE_OF_BIRTH, dob);
        }

        mEditor.commit();

        Log.d(TAG,"FirstName::" + SessionManager.getUserFirstName(context));
        Log.d(TAG, "LastName::" + SessionManager.getUserLastName(context));
    }


    public static void saveFacebookSession(Context context, String token) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();

        mEditor.putString(PREF_KEY_USERSESSION_FB_AUTHTOKEN, token);
        //mEditor.putLong(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES, expiration);

        mEditor.commit();
    }

    public static String getFacebookToken(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        return mPref.getString(PREF_KEY_USERSESSION_FB_AUTHTOKEN, "");
    }

    public static long getFacebookExpiration(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        return mPref.getLong(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES, 0);
    }


    /**
     * Check if the User Session in the shared preferences is valid
     *
     * @param context
     * @return True or False
     */
    public static boolean isUserSessionValid(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);

        // Keep simple for now
        int authType = mPref.getInt(PREF_KEY_USERSESSION_TYPE, -1);
        String clipixId = "";
        if (authType == Constants.EMAIL_SESSION) {
            clipixId = mPref.getString(PREF_KEY_USERSESSION_ID, "");
        }else if (authType == Constants.FB_SESSION) {
            clipixId = mPref.getString(PREF_KEY_FB_USER_ID, "");
        }else if (authType == Constants.GPLUS_SESSION) {
            clipixId = mPref.getString(PREF_KEY_GPLUS_USER_ID, "");
        }
        if (TextUtils.isEmpty(clipixId)) {
            return false;
        }
        return true;
    }

    /**
     * Retrieve User FirstName stored in the User Session in the shared preferences
     *
     * @param context
     * @return userFirstName
     * @throws NullPointerException
     */
    public static String getUserFirstName(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_USERFIRSTNAME, null);
    }

    /**
     * Retrieve User FirstName stored in the User Session in the shared preferences
     *
     * @param context
     * @return userLastName
     * @throws NullPointerException
     */
    public static String getUserLastName(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_USERLASTNAME, null);
    }

    /**
     * Retrieve User Email stored in the User Session in the shared preferences
     *
     * @param context
     * @return username
     * @throws NullPointerException
     */
    public static String getUserEmail(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_EMAIL, null);
    }


    /**
     * Retrieve Password stored in the User Session in the shared preferences
     *
     * @param context
     * @return password
     * @throws NullPointerException
     */
    public static String getUserPassword(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_PASSWORD, null);
    }

    /**
     * Retrieve Session Type stored in the User Session in the shared preferences
     *
     * @param context
     * @return sessionType
     */
    public static int getUserSessionType(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getInt(PREF_KEY_USERSESSION_TYPE, 0);
    }

    public static String getUserImageUrl(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USER_PROFILE_PIC, "");
    }


    /**
     * Clear the user session available in the shared preferences. Removes the Username, Password and StartDate of current session
     *
     * @param context
     */
    public static void clearUserSession(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mEditor.remove(PREF_KEY_USERSESSION_ID);
        mEditor.remove(PREF_KEY_USERSESSION_USERFIRSTNAME);
        mEditor.remove(PREF_KEY_USERSESSION_USERLASTNAME);
        mEditor.remove(PREF_KEY_USERSESSION_EMAIL);
        mEditor.remove(PREF_KEY_USERSESSION_PASSWORD);
        mEditor.remove(PREF_KEY_USERSESSION_TYPE);
        mEditor.remove(PREF_KEY_USERSESSION_FB_AUTHTOKEN);
        mEditor.remove(PREF_KEY_USERSESSION_FB_AUTHTOKENEXPIRES);
        mEditor.remove(PREF_KEY_USERSESSION_AUTHTOKEN);
        mEditor.commit();
        Log.d(TAG, "StateManager: clearUserSession: SESSION CLEARED");
    }


    /**
     * To prevent null pointer exceptions while calling the preference file when it doesn't exist.
     * If a solution is found to avoid this behavior, this method should be removed and the code refactored.
     * #initiateSession
     *
     * @param context
     */
    public static void initiateSession(Context context) {
        mEditor =
                context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE).edit();
        mEditor.commit();
        Log.d(TAG, "StateManager: initiateSession: SESSION INITIATED");
    }

    public static String getSessionId(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_ID, "");
    }

    public static void saveAuthToken(Context context, String authToken) {

        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mEditor.putString(PREF_KEY_USERSESSION_ID, authToken);
        mEditor.putString(FeedParams.AUTHKEY, authToken);
        mEditor.commit();

    }

    public static String getUserCity(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_USER_CITY, null);
    }

    public static int getUserCityID(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getInt(PREF_USER_CITY_ID, -1);
    }

    public static void setUserCity(Context context, String city, int cityID) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_USER_CITY, city).commit();
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putInt(PREF_USER_CITY_ID, cityID).commit();
    }

    public static void setUserCityID(Context context, int cityId) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putInt(PREF_USER_CITY_ID, cityId).commit();
    }

    public static void setAllCities(Context context, String allCities) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_USER_CITIES, allCities).commit();
    }

    public static String getAllCities(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_USER_CITIES, null);
    }

    public static String getSessionToken(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_AUTHTOKEN, "");
    }


    public static String getUserPhone(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USERSESSION_MOBILE, null);
    }
    public static String getUserDOB(Context context) throws NullPointerException {
        mPref = context.getSharedPreferences(PREF_FILE_USERSESSION, Context.MODE_WORLD_READABLE);
        return mPref.getString(PREF_KEY_USER_DATE_OF_BIRTH, null);
    }

    public static void setAboutUrl(Context context,String aboutUrl) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_ABOUT_URL, aboutUrl).commit();
    }

    public static void setReviewUrl(Context context, String review) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_REVIEW_URL, review).commit();
    }

    public static void setContactUrl(Context context, String contact) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_CONTACT_URL, contact).commit();
    }

    public static void setTariffUrl(Context context, String tariff) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_TARIFF_URL, tariff).commit();
    }

    public static void setFaq(Context context, String faq) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_FAQ_URL, faq).commit();
    }

    public static String getAboutUrl(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_ABOUT_URL, null);
    }

    public static String getReviewUrl(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_REVIEW_URL, null);
    }

    public static String getContactUrl(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_CONTACT_URL, null);
    }

    public static String getTariffUrl(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_TARIFF_URL, null);
    }

    public static String getFaqUrl(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_FAQ_URL, null);
    }

    public static void setBookingId(Context context,String id, String referenceId) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_BOOKING_ID, id).commit();
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(PREF_REF_BOOKING_ID,referenceId).commit();
    }

    public static String getBookingID(Context context){
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_BOOKING_ID, null);
    }

    public static String getReferenceBookingID(Context context){
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(PREF_REF_BOOKING_ID, null);
    }
    public static HashMap<String, ArrayList<String>> getTimeSlotsForSelectedMonth() {
        return timeSlotsForSelectedMonth;
    }

    public static void setTimeSlotsForSelectedMonth(HashMap<String, ArrayList<String>> timeSlotsForSelectedMonth) {
        SessionManager.timeSlotsForSelectedMonth = timeSlotsForSelectedMonth;
    }

    public static String getEndTimeSlot() {
        return endTimeSlot;
    }

    public static void setEndTimeSlot(String endTimeSlot) {
        SessionManager.endTimeSlot = endTimeSlot;
    }

    public static String getEndDate() {
        return endDate;
    }

    public static void setEndDate(String endDate) {
        SessionManager.endDate = endDate;
    }
}
