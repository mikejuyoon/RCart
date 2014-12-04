package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class StationDetail extends Activity implements MyAsyncResponse {

    private final static String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    public static SharedPreferences savedData;

    //Public variables of StationDetail
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
    String phone;
    String credit;
    String carwash;
    String hours;
    boolean updated;

    Double curlati, curlongi;
    String lati, longi;

    /***********************************************************************************************
     * function onCreate
     *
     * Provides the initial variables used for the station details. Calls the Async to Post the
     * updated gas prices.
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

        //Changes to true if user actually updates gas prices
        updated = false;

        //Receiving the inputs of the gas station information.
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
        phone = row_intent.getStringExtra("iphone");
        credit = row_intent.getStringExtra("icredit");
        carwash = row_intent.getStringExtra("icarwash");
        hours = row_intent.getStringExtra("ihours");

      //  curlati = row_intent.getDoubleExtra("latitude", 0);
       // curlongi = row_intent.getDoubleExtra("longitude", 0);

        //Layouts information for the Gas Detail Activity with the specific inputs
        //Address
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

        //Features
        Details = (TextView) findViewById(R.id.distanceView);
        Details.setText(distance);
        if (credit.equals("1")) {
            credit = "Credit Accepted";
        }
        else {
            credit = "Credit Unaccepted";
        }
        if (carwash.equals("1")) {
            carwash = "Car Wash: Yes";
        }
        else {
            carwash = "Car Wash: No";
        }
        if (hours.equals("1")) {
            hours = "Hours: 24/7";
        }
        else {
            hours = "Hours: Not 24/7";
        }
        Details = (TextView) findViewById(R.id.features);
        Details.setText("Phone: " + phone + "\n" + hours + "\n" + credit + "\n" + carwash);

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

        //Setting the correct logo for the each specific gas station
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
        else if(station.equals("Usa Petroleum")) {
            my_image.setImageResource(R.drawable.usa_petroleum);
        }
        else if(station.equals("Circle K")) {
            my_image.setImageResource(R.drawable.circle_k);
        }
        else if(station.equals("G & M Food Mart")) {
            my_image.setImageResource(R.drawable.g_and_m);
        }
        else {
            my_image.setImageResource(R.drawable.splashscreen);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.station_detail, menu);
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
     * function goBack
     * Returns to previous activity, finishing current one
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void goBack(View view) {
        //finish();
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        Intent return_intent = new Intent();
        return_intent.putExtra("updated", updated);
        setResult(RESULT_OK, return_intent);
        updated = false;
        super.onBackPressed();
    }

    /***********************************************************************************************
     * function goToUpdateGas
     * Goes to the UpdateGas activity, pushing every needed variable
     *
     * @param   View
     * @return  NONE
     **********************************************************************************************/
    public void goToUpdateGas(View view) {

        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String authToken = savedData.getString("auth_token", "0");

        if( ! authToken.equals("0") ){
            Intent i = new Intent(StationDetail.this, UpdateGas.class);
            i.putExtra("istation", station);
            i.putExtra("idistance", distance);
            i.putExtra("iaddress", address);
            i.putExtra("ireg_price", reg_price);
            i.putExtra("imid_price", mid_price);
            i.putExtra("ipre_price", pre_price);
            i.putExtra("icity", city);
            i.putExtra("iregion", region);
            i.putExtra("izip", zip);
            i.putExtra("icountry", country);
            i.putExtra("istation_id", station_id);
            startActivityForResult(i, 2);
        }else{
            errorAlert("Please login before updating gas.");
            //errorAlert(authToken);
        }
    }

    private void errorAlert(String message) {
        /// change to current class
        AlertDialog.Builder builder = new AlertDialog.Builder(StationDetail.this);

        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    /***********************************************************************************************
     * function onActivityResult
     * Updates the previous StationDetails after updating the specific gases
     *
     * @param   requestCode
     * @param   resultCode
     * @param   data
     * @return  NONE
     **********************************************************************************************/
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 2) {
            if(resultCode == RESULT_OK){
                updated = true;
                //Updates the text of regular for station details
                if(data.getStringExtra("gas_type").equals("reg")) {
                    reg_price = data.getStringExtra("new_gas_price");
                    Details = (TextView) findViewById(R.id.reg_price);
                    Details.setText(reg_price);
                }
                //Updates the text of plus for station details
                else if(data.getStringExtra("gas_type").equals("mid")) {
                    mid_price = data.getStringExtra("new_gas_price");
                    Details = (TextView) findViewById(R.id.mid_price);
                    Details.setText(mid_price);
                }
                //Updates the text of premium for station details
                else if(data.getStringExtra("gas_type").equals("pre")) {
                    pre_price = data.getStringExtra("new_gas_price");
                    Details = (TextView) findViewById(R.id.pre_price);
                    Details.setText(pre_price);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                updated = false;
            }
        }
        else {
            updated = false;
        }
    }
    public void goDirection(View view){
        String addstr = address + ","+ "+" + city + ",+" + region;
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?";
        params.add(new BasicNameValuePair("key", "AIzaSyDnnBpMCtrnDehllEZA-XPJJwYEFlpSjnw"));
        params.add(new BasicNameValuePair("address", addstr));

        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        //invalidEntryAlert(url);
        HttpGet httpGet = new HttpGet(url);
        new MyHttpGet(this).execute(httpGet);
    }
    @Override
    public void processFinish(String result) {
        // invalidEntryAlert(result);
        JSONObject json;
        try {
            json = new JSONObject(result);
            lati = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
            longi = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
            Log.d("latitude = ", lati);
            Log.d("longitude = " , longi);
            curlati = ((globalvariable) this.getApplication()).getGloballati();
            curlongi = ((globalvariable) this.getApplication()).getGloballongi();
            Log.d("globallatitude = ", String.valueOf(curlati));
            Log.d("globallongitude = " , String.valueOf(curlongi));
            String url = "https://www.google.com/maps/dir/" + String.valueOf(curlati)+ "," + String.valueOf(curlongi) + "/" + lati + "," + longi;
            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }catch(Exception e){}
    }
}
