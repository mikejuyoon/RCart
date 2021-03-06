package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.location.Location;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

public class SplashScreen extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener, MyAsyncResponse {

    // locations objects
    LocationClient mLocationClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    //TextView txtLong,txtLat;

    String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    SharedPreferences savedData;

    /***********************************************************************************************
     *Initializes certain variables to use throughout the Android programs
     *
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // 3. create LocationClient
        mLocationClient = new LocationClient(this, this, this);
        // 4. create & set LocationRequest for Location update
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000 * 5);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000 * 1);

        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spash_screen, menu);
        return true;
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        // 1. connect the client.
        mLocationClient.connect();
    }

    private void invalidEntryAlert(String message) {
        /// change to current class
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);

        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }


    @Override
    protected void onResume(){
        super.onResume();
        //invalidEntryAlert("onresume");
        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        if( savedData.contains("auth_token") ){
            Button login_btn = (Button) findViewById(R.id.login_btn);
            login_btn.setText("Logout");

        }
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        // 1. disconnecting the client invalidates it.
        mLocationClient.disconnect();
    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
//        SharedPreferences.Editor preferencesEditor = savedData.edit();
//        preferencesEditor.apply();

        //super.onDestroy();
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    // GooglePlayServicesClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    // GooglePlayServicesClient.ConnectionCallbacks
    @Override
    public void onConnected(Bundle arg0) {

        if(mLocationClient != null)
            mLocationClient.requestLocationUpdates(mLocationRequest,  this);

        if(mLocationClient != null){
            // get location
            mCurrentLocation = mLocationClient.getLastLocation();
        }

    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected.", Toast.LENGTH_SHORT).show();

    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
    // LocationListener
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Location changed.", Toast.LENGTH_SHORT).show();
        mCurrentLocation = mLocationClient.getLastLocation();
       // txtLat.setText(mCurrentLocation.getLatitude()+"");
        //txtLong.setText(mCurrentLocation.getLongitude()+"");
    }

    /***********************************************************************************************
     *
     **********************************************************************************************/
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

    private void userLogout(){

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        String url = "https://mobibuddy.herokuapp.com/users/sign_out.json";
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.addHeader("X-API-EMAIL", savedData.getString("email", "DEFAULT"));
        httpDelete.addHeader("X-API-TOKEN", savedData.getString("auth_token", "DEFAULT"));
        new MyHttpDelete(this).execute(httpDelete);

        savedData.edit().remove("auth_token").commit();
        //invalidEntryAlert("loggedout");
        Button login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setText("Login");
        Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
    }
    /***********************************************************************************************
     *
     **********************************************************************************************/
    public void openLoginActivity(View view){
        if( savedData.contains("auth_token") ){
            userLogout();
        }
        else {
            Intent i = new Intent(SplashScreen.this, Login.class);
            startActivity(i);
        }
    }

    public void goToGasList(View view){

        Intent i = new Intent(SplashScreen.this, GasStationsList.class);
        i.putExtra("lati", mCurrentLocation.getLatitude());
        i.putExtra("longi", mCurrentLocation.getLongitude());

        startActivity(i);
    }

    public void goToProductsList(View view){
        Intent i = new Intent(SplashScreen.this, ProductsList.class);
        i.putExtra("lati", mCurrentLocation.getLatitude());
        i.putExtra("longi", mCurrentLocation.getLongitude());
        startActivity(i);
    }

    @Override
    public void processFinish(String output) {
        //invalidEntryAlert(output);
    }
}
