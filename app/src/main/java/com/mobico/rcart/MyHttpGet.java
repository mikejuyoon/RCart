package com.mobico.rcart;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyHttpGet extends AsyncTask<HttpGet, Void, String> {

    public MyAsyncResponse delegate;

    public MyHttpGet(MyAsyncResponse listener){
        this.delegate = listener;
    }

    @Override
    protected String doInBackground(HttpGet... getUrl) {
        return GET(getUrl[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public String GET(HttpGet getUrl){
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(getUrl);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }    //invalidEntryAlert("GET request error");
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
