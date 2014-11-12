package com.mobico.rcart;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 11/8/2014.
 */


public class MyHttpPost extends AsyncTask<HttpPost, Void, String> {

    @Override
    protected String doInBackground(HttpPost... postUrl) {
        return POST(postUrl[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText( getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        try{
            JSONObject jsonLoginResult = new JSONObject(result);
            if( jsonLoginResult.getBoolean("success") ){
                //will save our auth_token in our SharedPreferences
                //SharedPreferences.Editor preferencesEditor = savedData.edit();
                //preferencesEditor.putString("auth_token", jsonLoginResult.getJSONObject("user").getString("auth_token"));
            }
            else{
                //invalidEntryAlert("Invalid username or password. Please try again.");
            }
        }
        catch(JSONException e){
            //do nothing
        }
    }

    public static String POST(HttpPost postUrl){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(postUrl);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
