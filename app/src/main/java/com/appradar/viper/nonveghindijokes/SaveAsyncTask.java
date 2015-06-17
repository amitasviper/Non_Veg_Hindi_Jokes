package com.appradar.viper.nonveghindijokes;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by viper on 17/5/15.
 */
public class SaveAsyncTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            QueryBuilder queryBuilder = new QueryBuilder();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(queryBuilder.getCollectionUrl());

            StringEntity data = new StringEntity(queryBuilder.createJSONData());
            request.addHeader("content-type", "application/json");
            request.setEntity(data);
            HttpResponse response = httpClient.execute(request);


            if (response.getStatusLine().getStatusCode() < 205 ) {
                Log.e("SaveAsyncTask", "GCM keys successfully saved to Mongolabs");
                SharedPreferences settings = MainOptions.getContext().getSharedPreferences("GCM_REG_PREFS", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("gcm_reg", false);
                editor.commit();
                return true;
            }
            else
                return false;
        }catch (IOException ex){
            return false;
        }
    }


}
