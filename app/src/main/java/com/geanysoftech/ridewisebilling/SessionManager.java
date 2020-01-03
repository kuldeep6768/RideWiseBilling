package com.geanysoftech.ridewisebilling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.geanysoftech.ridewisebilling.activity.LoginActivity;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static SessionManager mInstance;

    public static final String PREF_NAME = "ride_wise_billing";
    public static final String TAG_ACCESS_TOKEN = "access_token";
    public static final String TAG_USER_ID = "user_id";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private static final String KEY_REMAINING_AMOUNT = "remaining_amount";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String email, String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_EMAIL, email);

        // Storing email in pref
        editor.putString(KEY_EMAIL, password);

        // commit changes
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }

    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public boolean saveAccessToken(String token){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(TAG_ACCESS_TOKEN, token);
        editor.apply();
        return true;

    }

    public String getAccessToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(TAG_ACCESS_TOKEN, null);

    }

    public boolean saveRemainingAmount(String remainingAmount){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REMAINING_AMOUNT, remainingAmount);
        editor.apply();
        return true;

    }

    public String getRemainingAmount(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(KEY_REMAINING_AMOUNT, "");
    }


}
