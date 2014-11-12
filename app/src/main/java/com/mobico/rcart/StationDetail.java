package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class StationDetail extends Activity {

    TextView Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

        //Receiving the inputs of the gas station informations.
        Intent row_intent = getIntent();
        String station = row_intent.getStringExtra("istation");
        String distance = row_intent.getStringExtra("idistance");
        String address = row_intent.getStringExtra("iaddress");
        String reg_price = row_intent.getStringExtra("ireg_price");
        String mid_price = row_intent.getStringExtra("imid_price");
        String pre_price = row_intent.getStringExtra("ipre_price");
        String city = row_intent.getStringExtra("icity");
        String region = row_intent.getStringExtra("iregion");
        String zip = row_intent.getStringExtra("izip");
        String country = row_intent.getStringExtra("icountry");
        String station_id = row_intent.getStringExtra("istation_id");

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

        Details = (TextView) findViewById(R.id.gasStationName);
        Details.setText(station);

        Details = (TextView) findViewById(R.id.address);
        Details.setText(address + "\n" + city + ", " + region + " " + zip + "\n" + country);

        //Details = (TextView) findViewById(R.id.gasStationName);

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

    //Transitioning from Station Detail Activity to Gas Stations List
    public void goBack(View view) {
        finish();
    }

    //Transitioning from Station Detail Activity to Update Gas
    public void goToUpdateGas(View view) {
        Intent i = new Intent(StationDetail.this, UpdateGas.class);
        startActivity(i);
        finish();
    }
}
