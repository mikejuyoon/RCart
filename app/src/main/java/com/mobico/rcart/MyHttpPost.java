package com.mobico.rcart;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Michael on 12/3/2014.
 */
public class MyHttpPost extends AsyncTask<HttpPost, Void, String> {

	public MyAsyncResponse delegate;
	
	public MyHttpPost(MyAsyncResponse listener){
        this.delegate = listener;
    }

    @Override
    protected String doInBackground(HttpPost... postUrl) {

        return POST(postUrl[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public String POST(HttpPost postUrl){
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

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}