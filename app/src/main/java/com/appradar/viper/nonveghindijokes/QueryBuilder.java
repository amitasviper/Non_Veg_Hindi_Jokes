package com.appradar.viper.nonveghindijokes;

import android.content.Context;
import android.content.res.Resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by viper on 17/5/15.
 */
public class QueryBuilder {

    private final String BASE_URL = "https://api.mongolab.com/api/1/databases/";
    private final String DATABASE_NAME = "gcmkeys";
    private String COLLECTION_NAME;
    private final String MONGO_API_KEY = "htbpswFChbGzwJ_bRU4HEN1AVyFWXVul";

    private void setCollectionName(){

        Context context = MainOptions.getContext();
        Resources appR = context.getResources();

        CharSequence txt = appR.getText(appR.getIdentifier("app_name",
                "string", context.getPackageName()));

        COLLECTION_NAME = txt.toString();
        COLLECTION_NAME = COLLECTION_NAME.replaceAll("\\s+","");
    }

    public String getBaseUrl(){
        return BASE_URL+DATABASE_NAME+"/collections/";
    }

    public String getCollectionUrl(){
        setCollectionName();
        return getBaseUrl() + COLLECTION_NAME + "?apiKey=" + MONGO_API_KEY;
    }

    public String createJSONData(){

        GcmHelper gcmHelper = new GcmHelper(MainOptions.getContext());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        String json;
        json = String.format("{\"document\"  : {"
                        + "\"email\": \"%s\", \"gcmkey\": \"%s\", "
                        + "\"date\": \"%s\"}, \"safe\" : true}",
                gcmHelper.getAccountEmail(MainOptions.getContext()),
                gcmHelper.getRegistrationId(MainOptions.getContext()),
                dateFormat.format(date));

        return json;
    }

}
