package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
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

public class SplashScreen extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    // locations objects
    LocationClient mLocationClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;

    TextView txtLong,txtLat;
    Button gasListButton;

    /***********************************************************************************************
     *Initializes certain variables to use throughout the Android programs
     *
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        txtLong = (TextView) findViewById(R.id.txtLong);
        txtLat = (TextView) findViewById(R.id.txtLat);

        // 3. create LocationClient
        mLocationClient = new LocationClient(this, this, this);

        // 4. create & set LocationRequest for Location update
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000 * 5);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000 * 1);

        gasListButton = (Button) findViewById(R.id.gas_list_btn);
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

    /***********************************************************************************************
     *
     **********************************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();
        // 1. disconnecting the client invalidates it.
        mLocationClient.disconnect();
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

        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        if(mLocationClient != null){
            // get location
            mCurrentLocation = mLocationClient.getLastLocation();
            try{

                // set TextView(s)
                txtLat.setText(mCurrentLocation.getLatitude()+"");
                txtLong.setText(mCurrentLocation.getLongitude()+"");

            }catch(NullPointerException npe){

                Toast.makeText(this, "Failed to Connect", Toast.LENGTH_SHORT).show();

                // switch on location service intent
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
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
        Toast.makeText(this, "Location changed.", Toast.LENGTH_SHORT).show();
        mCurrentLocation = mLocationClient.getLastLocation();
        txtLat.setText(mCurrentLocation.getLatitude()+"");

        txtLong.setText(mCurrentLocation.getLongitude()+"");
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

<<<<<<< HEAD
    /***********************************************************************************************
     *
     **********************************************************************************************/
=======
    public void openLoginActivity(View view){
        Intent i = new Intent(SplashScreen.this, Login.class);
        startActivity(i);
        finish();
    }

>>>>>>> working
    public void goToGasList(View view){
        Intent i = new Intent(SplashScreen.this, GasStationsList.class);
        i.putExtra("lati", mCurrentLocation.getLatitude());
        i.putExtra("longi", mCurrentLocation.getLongitude());
        startActivity(i);
        finish();
    }
}
