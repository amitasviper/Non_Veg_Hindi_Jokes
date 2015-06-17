package com.appradar.viper.nonveghindijokes;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created by viper on 17/5/15.
 */
public class GcmHelper {

    String SENDER_ID = "381013050640";
    public static final String EXTRA_MESSAGE = "message";
    public  static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;

    String regid;

    Context context;

    public GcmHelper(Context context){
        this.context = context;
    }

    public String initiateRegistration(){
        gcm = GoogleCloudMessaging.getInstance(context);
        regid = getRegistrationId(context);

        Log.e("GcmHelper:InitiateRegistration: outer", "The new key is: " + regid);

        if (regid.isEmpty()){
            String new_reg_id = registerInBackground();
            Log.e("GcmHelper:InitiateRegistration", "The new key is: " +new_reg_id);
            return new_reg_id;
        }

        return regid;
    }


    public String getAccountEmail(Context context){

        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccounts();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name;
            }
        }

        return "No email Id found";
    }

    private SharedPreferences getGCMPreference(Context context){
        return context.getSharedPreferences(MainOptions.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public String getRegistrationId(Context context){
        final  SharedPreferences preferences = getGCMPreference(context);
        String registrationId = preferences.getString(PROPERTY_REG_ID,"");
        if (registrationId.isEmpty()){
            Log.i("getRegistrationId", "Registration not found");
            return "";
        }

        int registeredVersion  = preferences.getInt(PROPERTY_APP_VERSION,Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion){
            Log.i("getRegistrationId", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreference(context);
        int appVersion = getAppVersion(context);
        Log.i("storeRegistrationId", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public String registerInBackground(){
        Log.e("registerInBackground", "Method called.");
        String text = "";

            new AsyncTask<Void, Void, String>(){
                @Override
                protected void onPostExecute(String s) {

                }

                @Override
                protected String doInBackground(Void... params) {
                    String msg = "";
                    try {
                        if (gcm == null)
                            gcm = GoogleCloudMessaging.getInstance(context);

                            regid = gcm.register(SENDER_ID);

                            msg = "Device registered, Registration id = " + regid;
                            storeRegistrationId(context, regid);

                            Log.e("registerInBackground", "Inside CCCCCCCCCCC : ");

                    } catch (IOException ex){
                        msg = "Error :" + ex.getMessage();
                        Log.e("registerInBackground", "Exception : "+msg);
                    }
                    Log.e("registerInBackground", "Registration Result : " + msg);
                    return msg;
                }
            }.execute(null, null, null);

        return  text;
    }
}
