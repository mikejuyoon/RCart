package com.mobico.rcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.os.StrictMode;

public class UpdateGas extends Activity {

    TextView Details;
    String station;
    String distance;
    String address;
    String reg_price;
    String mid_price;
    String pre_price;
    String city;
    String region;
    String zip;
    String country;
    String station_id;

    /***********************************************************************************************
     * function onCreate
     *
     * Provides the initial variables used to update the gas. Calls the Async to Post the
     * updated gas prices.
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gas);

        //Receiving the inputs of the gas station informations.
        Intent row_intent = getIntent();
        station = row_intent.getStringExtra("istation");
        distance = row_intent.getStringExtra("idistance");
        address = row_intent.getStringExtra("iaddress");
        reg_price = row_intent.getStringExtra("ireg_price");
        mid_price = row_intent.getStringExtra("imid_price");
        pre_price = row_intent.getStringExtra("ipre_price");
        city = row_intent.getStringExtra("icity");
        region = row_intent.getStringExtra("iregion");
        zip = row_intent.getStringExtra("izip");
        country = row_intent.getStringExtra("icountry");
        station_id = row_intent.getStringExtra("istation_id");

        //Layouts information for the Gas Detail Activity with the specific inputs
        Details = (TextView) findViewById(R.id.gasStationName);
        Details.setText(station);
        Details = (TextView) findViewById(R.id.address);
        Details.setText(address + "\n" + city + ", " + region + " " + zip + "\n" + country);
        Details = (TextView) findViewById(R.id.reg_price);
        Details.setText(reg_price);
        Details = (TextView) findViewById(R.id.mid_price);
        Details.setText(mid_price);
        Details = (TextView) findViewById(R.id.pre_price);
        Details.setText(pre_price);

        //Logs each information of the station
        Log.d("Station", station);
        Log.d("Distance", distance);
        Log.d("Address", address);
        Log.d("Reg_price", reg_price);
        Log.d("Mid_Price", mid_price);
        Log.d("Pre_price", pre_price);
        Log.d("City:", city);
        Log.d("Region:", region);
        Log.d("Zip:", zip);
        Log.d("Country:", country);
        Log.d("Station_ID", station_id);

        //setting the correct logo for the gas station
        ImageView my_image;
        my_image = (ImageView) findViewById(R.id.imageView);

        if(station.equals("Shell")) {
            my_image.setImageResource(R.drawable.shell_logo);
        }
        else if(station.equals("BP")) {
            my_image.setImageResource(R.drawable.bp_logo);
        }
        else if(station.equals("Mobil")) {
            my_image.setImageResource(R.drawable.mobil_logo);
        }
        else if(station.equals("Costco")) {
            my_image.setImageResource(R.drawable.costco_logo);
        }
        else if(station.equals("7-Eleven")) {
            my_image.setImageResource(R.drawable.seven_eleven_logo);
        }
        else if(station.equals("Arco")) {
            my_image.setImageResource(R.drawable.arco_logo);
        }
        else if(station.equals("Sam's Club")) {
            my_image.setImageResource(R.drawable.sams_club_logo);
        }
        else if(station.equals("Valero")) {
            my_image.setImageResource(R.drawable.valero_logo);
        }
        else if(station.equals("Chevron")) {
            my_image.setImageResource(R.drawable.chevron_logo);
        }
        else if(station.equals("76")) {
            my_image.setImageResource(R.drawable.union_76_logo);
        }
        else {
            my_image.setImageResource(R.drawable.splashscreen);
        }

        //Dunno why we use this, it makes the POSTING work
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String url = "http://api.mygasfeed.com/locations/price/xfakzg0s3n.json";
        new HttpPOSTAsyncTask().execute(url);
    }

    /***********************************************************************************************
     * class HttpPostAsyncTask
     *
     * protected String doInBackground(String)
     * publiv void POST(String)
     **********************************************************************************************/
    private class HttpPOSTAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            POST(urls[0]);
            return null;
        }

        public void POST(String url) {
            //Prints if Post request sent
           // Toast.makeText(getBaseContext(), "Sent!", Toast.LENGTH_LONG).show();

            // Create a new HttpClient and Post Header
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://api.mygasfeed.com/locations/price/xfakzg0s3n.json");

            try {
                // The key and Value pairs for the Post
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("price", "6.00"));
                nameValuePairs.add(new BasicNameValuePair("fueltype", "reg"));
                nameValuePairs.add(new BasicNameValuePair("stationid", "1"));

                // Sets the pairs and sends them to the Post request
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                // As well send in stream in response
                HttpResponse response = httpClient.execute(httpPost);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_gas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickRegular() {

    }

    public void clickPlus() {

    }

    public void clickPremium() {

    }

    //Transitioning from Station Detail Activity to Gas Stations List
    public void goBack(View view) {
        finish();
    }
}
