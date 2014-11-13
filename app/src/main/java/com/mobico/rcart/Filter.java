package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.util.Log;
public class Filter extends Activity{

    private Spinner spinner1,spinner2,spinner3, spinner4;
    private static final String[]distanceMile = {"1", "2", "3"};
    private static final String[]brandname = {"Chevron", "Shell", "76", "Flyers", "Arco", "Texaco", "Mobil"};
    private static final String[]gastype = {"Gasoline", "Diesel"};
    private static final String[]sortby = {"price", "nearest"};
    private int mile= 0, name = 0, type = 0, sort = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(Filter.this,
                android.R.layout.simple_list_item_activated_1,distanceMile);

        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                mile = spinner1.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected

                        break;
                    case 1:
                        // mile = position;
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        //mile = position;
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 3:
                        // mile = position;
                        break;
                    case 4:
                        //mile = position;
                        break;
                    case 5:
                        //mile = position;
                        break;
                    case 6:
                        // mile = position;
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        ///////////////////////////////////spinner2 = brandname
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(Filter.this,
                android.R.layout.simple_list_item_activated_1,brandname);

        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                name = spinner2.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        //name = position;
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        // name = position;
                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        ///////////////////////////////////spinner3 = gastype
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<String>adapter3 = new ArrayAdapter<String>(Filter.this,
                android.R.layout.simple_list_item_activated_1,gastype);

        adapter3.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                type =spinner3.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        //type = position;
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        //type = position;
                        break;

                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        ///////////////////////////////////spinner4 = sortby
        spinner4 = (Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<String>adapter4 = new ArrayAdapter<String>(Filter.this,
                android.R.layout.simple_list_item_activated_1,sortby);

        adapter4.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                sort = spinner4.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        //sort = position;
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        //sort = position;
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        //sort = position;
                        break;

                }

            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToGasListFromFilter(View view){

        Intent j = new Intent();
        j.putExtra("distance", distanceMile[mile]);
        j.putExtra("brand", brandname[name]);
        j.putExtra("gastype", gastype[type]);
        j.putExtra("sortby", sortby[sort]);
        setResult(RESULT_OK, j);
        Log.d(distanceMile[mile] , "Distance = ");
        Log.d(brandname[name] , "Brand =" );
        Log.d(gastype[type], "gastype =");
        Log.d(sortby[sort], "sortby =" );
        finish();
    }

    public void cancelFilter(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
