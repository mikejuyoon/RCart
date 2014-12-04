package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class ProductDetail extends Activity{

    Intent intent;
    String product_name, store_name, store_distance, store_price, lati, longi, category;

    TextView product_name_tv, store_name_tv, store_distance_tv, store_price_tv;
    Double curlati, curlongi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        intent = getIntent();
        product_name = intent.getStringExtra("product_name");
        store_name = intent.getStringExtra("store_name");
        store_price = intent.getStringExtra("store_price");
        store_distance = intent.getStringExtra("store_distance");
        lati = intent.getStringExtra("lati");
        longi = intent.getStringExtra("longi");
        category = intent.getStringExtra("category");

        product_name_tv = (TextView) findViewById(R.id.product_name);
        store_name_tv = (TextView) findViewById(R.id.store_name);
        store_distance_tv = (TextView) findViewById(R.id.store_distance);
        store_price_tv = (TextView) findViewById(R.id.store_price);

        product_name_tv.setText(product_name);
        store_name_tv.setText(store_name);
        store_distance_tv.setText(store_distance);
        store_price_tv.setText(store_price);
        curlati = ((globalvariable) this.getApplication()).getGloballati();
        curlongi = ((globalvariable) this.getApplication()).getGloballongi();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_detail, menu);
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


    public void goToRouteMe(View view) {
        // invalidEntryAlert(result);
            Log.d("latitude = ", lati);
            Log.d("longitude = " , longi);
            Log.d("globallatitude = ", String.valueOf(curlati));
            Log.d("globallongitude = " , String.valueOf(curlongi));
            String url = "https://www.google.com/maps/dir/" + String.valueOf(curlati)+ "," + String.valueOf(curlongi) + "/" + lati + "," + longi;
            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
    }
}
