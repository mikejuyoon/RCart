package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class WishList extends Activity implements MyAsyncResponse{

    //Public variables
    ArrayList<HashMap<String,String>> myWishList;
    ListView listView;
    double latitude,longitude;
    String email, auth_token;

    //JSON variables used to read in the JSON from the requests
    JSONObject wishJson;
    JSONArray wishArray;

    //Adapter variable
    ProductListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        //Creates the intent to bring in the extras sent from the previous activities
        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);
        email = intent1.getStringExtra("email");
        auth_token = intent1.getStringExtra("auth_token");

        //Declaring variables of the public variables
        myWishList = new ArrayList<HashMap<String,String>>();
        listView = (ListView) findViewById(R.id.myListView);

        //Setting up the adapter
        listAdapter = new ProductListAdapter(this);
        listView.setAdapter(listAdapter);

        //Creates the URL to call Get requests from the server with the added headers that
        //deal with authentication
        String url = "https://mobibuddy.herokuapp.com/wishlist.json";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X-API_EMAIL", email);
        httpGet.addHeader("X-API-TOKEN", auth_token);
        new MyHttpGet(this).execute(httpGet);

        //On click listener
        //sends activity to the new activity (Nearby Product) in accordance to the product clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WishList.this, NearbyList.class);
                intent.putExtra("lati", latitude);
                intent.putExtra("longi", longitude);
                startActivityForResult(intent, 1234);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wish_list, menu);
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

    public void fillList() {
        //Goes through the JSON of the wishArray to return each individual item and its characteristics
        for (int i  = 0; i < wishArray.length(); ++i) {
            HashMap<String,String> hash = new HashMap<String, String>();
            try {
                hash.put("name", wishArray.getJSONObject(i).getString("name"));
                hash.put("category", wishArray.getJSONObject(i).getString("category"));
                hash.put("price",wishArray.getJSONObject(i).getString("price"));
                hash.put("image_url",wishArray.getJSONObject(i).getString("image_url"));
                hash.put("lat",wishArray.getJSONObject(i).getString("lat"));
                hash.put("long",wishArray.getJSONObject(i).getString("long"));
                hash.put("store_name",wishArray.getJSONObject(i).getString("store_name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            myWishList.add(hash);
        }
        listAdapter = new ProductListAdapter(this);
        listAdapter.insertList(myWishList);
        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void processFinish(String output) {
        try {
            wishJson = new JSONObject(output);
            wishArray = wishJson.getJSONArray("wishlist");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        //Fills the list with the JSON values
        fillList();
    }
}
