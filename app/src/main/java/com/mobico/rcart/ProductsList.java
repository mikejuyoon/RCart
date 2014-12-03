package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;


public class ProductsList extends Activity {

    ArrayList<String> productsList;
    ListView listView;
    double latitude, longitude;
    EditText productSearchBar;
    Spinner catagories_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);

        productSearchBar = (EditText) findViewById(R.id.productSearchBar);
        catagories_spinner = (Spinner) findViewById(R.id.catagories_spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.catagories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagories_spinner.setAdapter(spinner_adapter);


        listView = (ListView) findViewById(R.id.myListView);

        productsList = new ArrayList<String>();
        productsList.add("Apple");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductsList.this, NearbyList.class);
                intent.putExtra("lati", latitude);
                intent.putExtra("longi", longitude);
                startActivityForResult(intent, 1234);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_list, menu);
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

    public void goToWishlist(View view) {
        Intent i = new Intent(ProductsList.this, WishList.class);
        //Will return to the onActivityResult function
        startActivityForResult(i, 1);
    }

    public void productSearchButton(View view){
        String itemSelected = catagories_spinner.getSelectedItem().toString();
        String searchKeyword = productSearchBar.getText().toString();
        if(itemSelected.equals("Select Catagory") || searchKeyword.length()==0){ //I know. Horrible.
            invalidEntryAlert("Please enter a product keyword and select a catagory");
            return;
        }

    }



    // ============ HELPER FUNCTIONS =================

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsList.this);
        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }
}
