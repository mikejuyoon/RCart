package com.mobico.rcart;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Michael on 12/3/2014.
 */
public class MyHttpDelete extends AsyncTask<HttpDelete, Void, String> {

    public MyAsyncResponse delegate;

    public MyHttpDelete(MyAsyncResponse listener){
        this.delegate = listener;
    }

    @Override
    protected String doInBackground(HttpDelete... getUrl) {
        return DELETE(getUrl[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public String DELETE(HttpDelete getUrl){
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
            }    //invalidEntryAlert("DELETE request error");
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
