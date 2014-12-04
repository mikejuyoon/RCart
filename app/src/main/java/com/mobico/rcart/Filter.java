package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
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
    private static final String[]distanceMile = {"select one", "1", "2", "3", "4", "5"};
    private static final String[]brandname = {"select one", "Chevron", "Shell", "76", "Flyers", "Arco", "Texaco", "Mobil", "7-Eleven"};
    private static final String[]gastype = {"select one", "Regular 87", "Plus 89", "Premium 91", "diesel", "distance"};
    private static final String[]gasTypeReplace = {"select one", "reg", "mid", "pre", "diesel", "distance"};
    private static final String[]sortby = {"select one", "price", "distance"};
    private int mile1= 0, name1 = 0, type1 = 0, sort1 = 0;
    private int mile= 0, name = 0, type = 0, sort = 0;

    private void invalidEntryAlert(String message) {
        /// change to current class
        AlertDialog.Builder builder = new AlertDialog.Builder(Filter.this);

        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent intent1 = getIntent();
        mile1 = intent1.getIntExtra("mile", 0);
        name1 = intent1.getIntExtra("name", 0 );
        type1 = intent1.getIntExtra("type", 0 );
        sort1 = intent1.getIntExtra("sort", 0);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String>adapter1 = new ArrayAdapter<String>(Filter.this,
                android.R.layout.simple_list_item_activated_1,distanceMile);

        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner1.setAdapter(adapter1);
        spinner1.setSelection(mile1);
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
                    case 7:
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
        spinner2.setSelection(name1);
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
                    case 2:
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
        spinner3.setSelection(type1);
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
                    case 2:
                         break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
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
        spinner4.setSelection(sort1);
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
        if(mile == 0) j.putExtra("distance", "s"); else{ j.putExtra("distance", distanceMile[mile]);j.putExtra("mile",mile);}
        if(name == 0) j.putExtra("brand", "s"); else{ j.putExtra("brand", brandname[name]); j.putExtra("name",name);}
        if(type == 0) j.putExtra("gastype", "s"); else{ j.putExtra("gastype", gasTypeReplace[type]); j.putExtra("type",type);}
        if(sort == 0) j.putExtra("sortby", "s"); else{  j.putExtra("sortby", sortby[sort]); j.putExtra("sort",sort);}
        setResult(RESULT_OK, j);
        Log.d(distanceMile[mile] , "Distance = ");
        Log.d(brandname[name] , "Brand =" );
        Log.d(gasTypeReplace[type], "gastype =");
        Log.d(sortby[sort], "sortby =" );
        finish();
    }

    public void cancelFilter(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
