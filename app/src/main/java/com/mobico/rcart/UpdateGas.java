package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
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

    //Public variables of UpdateGas
    TextView UpdateGas;
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
        UpdateGas = (TextView) findViewById(R.id.gasStationName);
        UpdateGas.setText(station);
        UpdateGas = (TextView) findViewById(R.id.address);
        UpdateGas.setText(address + "\n" + city + ", " + region + " " + zip + "\n" + country);
        UpdateGas = (TextView) findViewById(R.id.reg_price);
        UpdateGas.setText(reg_price);
        UpdateGas = (TextView) findViewById(R.id.mid_price);
        UpdateGas.setText(mid_price);
        UpdateGas = (TextView) findViewById(R.id.pre_price);
        UpdateGas.setText(pre_price);

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

    /***********************************************************************************************
    * function invalidEntryAlert
    * Displays an invalid message
    *
    * @param   String
    * @return  NONE
    **********************************************************************************************/
    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateGas.this);

        builder.setTitle("Invalid Gas Price");
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    /***********************************************************************************************
     * function clickRegular
     * On click function when clicking on Update Regular
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void clickRegular(View view) {
        String fuelType = "reg";
        EditText priceText = (EditText) findViewById(R.id.editRegular);
        String priceTemp = priceText.getText().toString();

        if (priceTemp.length() == 3) {
            //Appends the string price correctly to update the price in the server with that string
            String newPrice = priceTemp.substring(0,1) + "." + priceTemp.substring(1,3);
            postGasPrice(newPrice, fuelType, station_id);

            //The intent used to push the changed variables to the previous station detail activity
            Intent return_intent = new Intent();
            return_intent.putExtra("new_gas_price",newPrice);
            return_intent.putExtra("gas_type", fuelType);
            setResult(RESULT_OK,return_intent);
            finish();
        }
        else {
            invalidEntryAlert("Please enter 3 digit Regular price");
        }
    }

    /***********************************************************************************************
     * function clickPlus
     * On click function when clicking on Update Plus
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void clickPlus(View view) {
        String fuelType = "mid";
        EditText priceText = (EditText) findViewById(R.id.editPlus);
        String priceTemp = priceText.getText().toString();

        if (priceTemp.length() == 3) {
            //Appends the string price correctly to update the price in the server with that string
            String newPrice = priceTemp.substring(0,1) + "." + priceTemp.substring(1,3);
            postGasPrice(newPrice, fuelType, station_id);

            //The intent used to push the changed variables to the previous station detail activity priceTemp.substring(1,3);
            postGasPrice(newPrice, fuelType, station_id);
            Intent return_intent = new Intent();
            return_intent.putExtra("new_gas_price",newPrice);
            return_intent.putExtra("gas_type", fuelType);
            setResult(RESULT_OK,return_intent);
            finish();
        }
        else {
            invalidEntryAlert("Please enter 3 digit Plus price");
        }
    }

    /***********************************************************************************************
     * function clickPremium
     * On click function when clicking on Update Premium
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void clickPremium(View view) {
        String fuelType = "pre";
        EditText priceText = (EditText) findViewById(R.id.editPremium);
        String priceTemp = priceText.getText().toString();

        if (priceTemp.length() == 3) {
            //Appends the string price correctly to update the price in the server with that string
            String newPrice = priceTemp.substring(0,1) + "." + priceTemp.substring(1,3);
            postGasPrice(newPrice, fuelType, station_id);

            //The intent used to push the changed variables to the previous station detail activity
            Intent return_intent = new Intent();
            return_intent.putExtra("new_gas_price", newPrice);
            return_intent.putExtra("gas_type", fuelType);
            setResult(RESULT_OK,return_intent);
            finish();
        }
        else {
            invalidEntryAlert("Please enter 3 digit Premium price");
        }
    }

    /***********************************************************************************************
     * function goBack
     * Returns to previous activity, finishing current one
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void goBack(View view) {
        finish();
    }

    /***********************************************************************************************
     * function postGasPrice
     * Updates the given gas price with the given parameters to GET to our own server/database
     * with the server sending a POST request to the gas API and update our server
     *
     * @param   price
     * @param   fuelType
     * @param   stationID
     * @return  NONE
     **********************************************************************************************/
    public void postGasPrice(String price, String fuelType, String stationId) {
        //The URL used for the gas price updates for sending GET requests to our own server.
        String url = "https://mobibuddy.herokuapp.com/update_gas.json?stationid="
                + stationId + "&fueltype=" + fuelType + "&price=" + price;

        new HttpAsyncTask().execute(url);
    }

    /***********************************************************************************************
     * function GET
     * GET request to the url, receiving a JSON format input to the stream. (JSON NOT USED)
     *
     * @param   url
     * @return  STRING result
     **********************************************************************************************/
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

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

    /***********************************************************************************************
     * function convertInputStreamToString
     * Converts the input stream into an actual stream to be used
     *
     * @param   inputStream
     * @return  STRING result
     **********************************************************************************************/
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    /***********************************************************************************************
     * class HttpAsyncTask
     *
     * Extends the AsyncTask, used to send a GET request to update gas prices in our own server
     **********************************************************************************************/
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        }
    }

//   *****************THIS IS THE ORIGINAL POST TO THE ACTUAL GAS API*****************
//
//    public void postGasPrice(String price, String fuelType, String stationId) {
//        HttpPost httppost = new HttpPost("https://mobibuddy.herokuapp.com/update_gas.json?stationid="
//                                        + stationId + "&fueltype=" + fuelType + "&price=" + price);
//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("price", price));
//            nameValuePairs.add(new BasicNameValuePair("fueltype", fuelType));
//            nameValuePairs.add(new BasicNameValuePair("stationid", stationId));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            new MyHttpPost().execute(httppost);
//
//        } catch (IOException e) {
//            Log.d("POSTINGFAILURE", e.getLocalizedMessage());
//        }
//    }

    // **************************** NOT USED ANYMORE **************************************
    /***********************************************************************************************
     * class MyHttpPost
     *
     * Extends the AsyncTask, used to send POST requests to the Gas API
     **********************************************************************************************/
    private class MyHttpPost extends AsyncTask<HttpPost, Void, String> {

        @Override
        protected String doInBackground(HttpPost... postUrl) {
            return POST(postUrl[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            // TO DO
            // WHAT YOU WANT TO DO WITH THE RESULT
            // "result" is the json data received back!
            //JSONObject jsonObject =
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
}
