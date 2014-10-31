package com.mobico.rcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GasStationsList extends Activity {

    LinearLayout gasStationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations_list);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gas_stations_list, menu);
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

    public void fillList(View view){
        for(int i = 0 ; i < 5 ; i++){
            pushListItemToLayoutView();
        }
    }

    private void pushListItemToLayoutView(){
        gasStationList = (LinearLayout) findViewById(R.id.gasStationList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.gas_station_row, null);

        //TextView listNameTV = (TextView) listItem.findViewById(R.id.listNameTV);
        //listNameTV.setText(s);

        gasStationList.addView(listItem);
    }

    public void goToStationDetail(View view){
        Intent i = new Intent(GasStationsList.this, StationDetail.class);
        startActivity(i);
        finish();
    }
}
