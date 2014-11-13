package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;


public class StationDetail extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);

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

    //Transitioning from Station Detail Activity to Gas Stations List
    public void goBack(View view) {
        finish();
    }

    //Transitioning from Station Detail Activity to Update Gas
    public void goToUpdateGas(View view) {
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
        startActivity(i);
    }
}
