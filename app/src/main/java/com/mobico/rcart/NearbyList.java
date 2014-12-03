package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class NearbyList extends Activity {

    ArrayList<HashMap<String,String>> ListOfProduct;
    ListView listView;
    double latitude, longitude;
    TextView productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        productName = (TextView) findViewById(R.id.product_name);

        listView = (ListView) findViewById(R.id.myListView);

        ProductListAdapter listAdapter = new ProductListAdapter(this);
        listView.setAdapter(listAdapter);

        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);
        String name = intent1.getStringExtra("name");
        String imageUrl = intent1.getStringExtra("imgUrl");
        String price = intent1.getStringExtra("price");
        String category = intent1.getStringExtra("category");

        productName.setText(name);

        invalidEntryAlert("name: "+name+"\nimageUrl: " + imageUrl+"\nprice: " + price+"\ncategory: " + category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nearby_list, menu);
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

    public void addWishlistItem(View view) {

    }

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NearbyList.this);
        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }
}
