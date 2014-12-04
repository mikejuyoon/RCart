package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class NearbyList extends Activity implements MyAsyncResponse{

    ArrayList<HashMap<String, String>> storeList;
    NearbyListAdapter nearbyListAdapter;

    ListView listView;
    double latitude, longitude;
    TextView productName;
    ArrayAdapter<String> adapter;

    String name;
    String imageUrl;
    String price;
    String category;

    JSONArray resultsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);
        productName = (TextView) findViewById(R.id.product_name);

        storeList = new ArrayList<HashMap<String, String>>();

        listView = (ListView) findViewById(R.id.myListView);

        // ProductListAdapter listAdapter = new ProductListAdapter(this);
        // listView.setAdapter(listAdapter);

        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);
        name = intent1.getStringExtra("name");
        imageUrl = intent1.getStringExtra("imgUrl");
        price = intent1.getStringExtra("price");
        category = intent1.getStringExtra("category");

        productName.setText(name);

        googlePlacesPost(latitude, longitude, category);

        listView = (ListView) findViewById(R.id.myListView);

        nearbyListAdapter = new NearbyListAdapter(this, storeList);
        listView.setAdapter(nearbyListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NearbyList.this, ProductDetail.class);
                intent.putExtra("product_name", name);
                intent.putExtra("store_name", storeList.get(i).get("store_name"));
                intent.putExtra("store_price", storeList.get(i).get("store_price"));
                intent.putExtra("store_distance", storeList.get(i).get("store_distance"));
                intent.putExtra("lati", storeList.get(i).get("lati"));
                intent.putExtra("longi", storeList.get(i).get("longi"));
                intent.putExtra("category", storeList.get(i).get("category"));
                startActivity(intent);

            }
        });

        invalidEntryAlert("name: " + name + "\nimageUrl: " + imageUrl + "\nprice: " + price + "\ncategory: " + category);
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
        String url = "mobibuddy.herokuapp.com/items.json?";


        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("price", price));
        params.add(new BasicNameValuePair("image_url", imageUrl));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("X-API_EMAIL", "test@test.com");
        httpPost.addHeader("X-API-TOKEN", "kjoeCiMizXfngxfqutE3xz_QiHxeCgZESQ");
        new MyHttpPost(this).execute(httpPost);
    }

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NearbyList.this);
        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    private void googlePlacesPost(double lati, double longi, String category){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("location", lati + "," + longi));
        params.add(new BasicNameValuePair("radius", "6000"));
        params.add(new BasicNameValuePair("keyword", category));
        params.add(new BasicNameValuePair("key", "AIzaSyBkgVMXlCUpP4VJ52vnSzW_v5bD7ZSh6c0"));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpPost httpPost = new HttpPost(url);
        new MyHttpPost(this).execute(httpPost);
    }

    private void updateLocationsList(){

        for(int i = 0 ; i < resultsJson.length() ; i++){
            try {
                Double newPrice = (Double.parseDouble(price) - 0.3) + (Double.parseDouble(price) * 0.4 * Math.random());

                HashMap<String, String> storeInfo = new HashMap<String, String>();
                storeInfo.put("store_name", resultsJson.getJSONObject(i).getString("name"));
                storeInfo.put("store_price", String.valueOf(newPrice));
                storeInfo.put("store_distance", "1.5");
                storeInfo.put("lati", String.valueOf(latitude));
                storeInfo.put("longi", String.valueOf(longitude));
                storeInfo.put("category", category);
                storeList.add(storeInfo);
            }catch (Exception e){}

        }
        nearbyListAdapter = new NearbyListAdapter(this, storeList);
        listView.setAdapter(nearbyListAdapter);
    }

    @Override
    public void processFinish(String output) {
        try {
            JSONObject json = new JSONObject(output);
            resultsJson = json.getJSONArray("results");
        }catch(Exception e){}
        updateLocationsList();
    }
}
