package com.appradar.viper.nonveghindijokes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by viper on 27/4/15.
 */
public class MainOptions extends Activity {
    final static boolean DEBUG = true;
    ImageView santa_banta, husband_wife, boy_girl, exclusive, college, miscellaneous, favourite, politics, double_meaning, hindi, english;
    Button exit;
    AdView homeAd;
    InterstitialAd interstitialAd;
    TextView selectCategory;
    String interstitialAdId;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        interstitialAdId = getResources().getString(R.string.institialAdId);
        CacheLargeAd();

        setContentView(R.layout.activity_main_options);

        selectCategory = (TextView) findViewById(R.id.selectCategory);

        santa_banta = (ImageView) findViewById(R.id.santa_banta);
        husband_wife = (ImageView) findViewById(R.id.husband_wife);
        boy_girl = (ImageView) findViewById(R.id.boy_girl);
        exclusive  = (ImageView) findViewById(R.id.exclusive);
        college = (ImageView) findViewById(R.id.college);

        miscellaneous = (ImageView) findViewById(R.id.miscellaneous);
        favourite = (ImageView) findViewById(R.id.favourite);
        politics = (ImageView) findViewById(R.id.politics);
        double_meaning = (ImageView) findViewById(R.id.double_meaning);

        english = (ImageView) findViewById(R.id.english);
        hindi = (ImageView) findViewById(R.id.hindi);

        exit = (Button) findViewById(R.id.exit);

        homeAd = (AdView) findViewById(R.id.homeAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        homeAd.loadAd(adRequest);

        ButtonHandler buttonHandler = new ButtonHandler();
        santa_banta.setOnClickListener(buttonHandler);
        husband_wife.setOnClickListener(buttonHandler);
        boy_girl.setOnClickListener(buttonHandler);
        exclusive.setOnClickListener(buttonHandler);
        college.setOnClickListener(buttonHandler);

        miscellaneous.setOnClickListener(buttonHandler);
        favourite.setOnClickListener(buttonHandler);
        politics.setOnClickListener(buttonHandler);
        double_meaning.setOnClickListener(buttonHandler);

        english.setOnClickListener(buttonHandler);
        hindi.setOnClickListener(buttonHandler);

        exit.setOnClickListener(buttonHandler);

        AppRater.showRateDialogIfNeeded(this);

        registerForGCM();

    }

    private void registerForGCM(){

        SharedPreferences preferences = getSharedPreferences("GCM_REG_PREFS", 0);
        boolean isNotRegistered = preferences.getBoolean("gcm_reg", true);
        if(isNotRegistered){
            Toast.makeText(this, "Registering", Toast.LENGTH_LONG).show();
            Log.e("MAIN ACTIVITY","GCM REGISTERATION CALL");
            GcmHelper gcmHelper = new GcmHelper(this);
            String registrationId = gcmHelper.initiateRegistration();

            Log.e("MAIN ACTIVITY","Registration Id is "+registrationId);
            if (registrationId != "") {

                Log.e("MAIN ACTIVITY","Call to save at MOngoLab "+registrationId);
                SaveAsyncTask saveAsyncTask = new SaveAsyncTask();
                saveAsyncTask.execute();
            }
        }
        else{
            Toast.makeText(this, "Device allready registered", Toast.LENGTH_LONG).show();
        }
    }

    public static Context getContext(){
        return context;
    }

    @Override
    public void onBackPressed() {
        doExit();
    }

    private void CacheLargeAd() {

        interstitialAd = ManageAds.getInterstitialAd();
        if(interstitialAd == null) {
            ManageAds.prepareAds(this, "MainOptions", interstitialAdId);
            interstitialAd = ManageAds.getInterstitialAd();
        }

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }

            @Override
            public void onAdLoaded() {
                if (DEBUG)
                    Toast.makeText(MainOptions.this, "Ad loaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainOptions.this);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CacheLargeAd();
                if (interstitialAd.isLoaded())
                    interstitialAd.show();
                else
                    finish();
            }
        });

        alertDialog.setNegativeButton("No", null);

        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle(getApplicationContext().getResources().getString(R.string.app_name));
        alertDialog.show();
    }

    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.santa_banta) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.husband_wife) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.boy_girl) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.exclusive) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.college) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            }else if (v.getId() == R.id.politics) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.double_meaning) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.miscellaneous) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            } else if (v.getId() == R.id.favourite) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            }


            else if (v.getId() == R.id.english) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            }
            else if (v.getId() == R.id.hindi) {
                Intent intent = new Intent(MainOptions.this, JokesList.class);
                intent.putExtra("buttonId", v.getId());
                startActivity(intent);
            }

            else {      //listener for exit button
                doExit();
                //System.exit(0);
            }
        }
    }



}
