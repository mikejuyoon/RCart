package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

    private double curlati = 0, curlongi= 0;
    ArrayList<HashMap<String,String>> mylist;
    String address = "", city = "", region = "";
    String address1 = "", city1 = "", region1 = "";
    String address2 = "", city2 = "", region2 = "";
    String lati, longi;
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

        curlati = ((globalvariable) this.getApplication()).getGloballati();
        curlongi = ((globalvariable) this.getApplication()).getGloballongi();
        //Declaring variables of the public variables
        myWishList = new ArrayList<HashMap<String,String>>();
        listView = (ListView) findViewById(R.id.myListView);

        //Setting up the adapter
        listAdapter = new ProductListAdapter(this);
        listView.setAdapter(listAdapter);

        getWishList();

        //On click listener
        //sends activity to the new activity (Nearby Product) in accordance to the product clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToProductDetail(view, i);
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

    public void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WishList.this);
        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    public void getWishList(){
        //Creates the URL to call Get requests from the server with the added headers that
        //deal with authentication
        String url = "https://mobibuddy.herokuapp.com/wishlist.json";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X-API_EMAIL", email);
        httpGet.addHeader("X-API-TOKEN", auth_token);
        new MyHttpGet(this).execute(httpGet);
    }
    public void fillList() {
        //Goes through the JSON of the wishArray to return each individual item and its characteristics
        myWishList.clear();
        for (int i  = 0; i < wishArray.length(); ++i) {
            HashMap<String,String> hash = new HashMap<String, String>();
            try {
                hash.put("name", wishArray.getJSONObject(i).getString("name"));
                hash.put("store_name",wishArray.getJSONObject(i).getString("store_name"));
                hash.put("item_id",wishArray.getJSONObject(i).getString("id"));
                hash.put("price",wishArray.getJSONObject(i).getString("price"));
                hash.put("lat",wishArray.getJSONObject(i).getString("lat"));
                hash.put("long",wishArray.getJSONObject(i).getString("long"));
                hash.put("category", wishArray.getJSONObject(i).getString("category"));
                hash.put("image_url",wishArray.getJSONObject(i).getString("image_url"));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Fills the list with the JSON values
        fillList();
    }

    public void goToProductDetail(View view, int index) {
        Intent intent = new Intent(WishList.this, ProductDetail.class);
        intent.putExtra("product_name", myWishList.get(index).get("name"));
        intent.putExtra("store_name", myWishList.get(index).get("store_name"));
        intent.putExtra("store_price", myWishList.get(index).get("price"));
        //DISTANCE DOES NOT HAVE A VALUE YET
        intent.putExtra("store_distance", "0");
        intent.putExtra("lati", myWishList.get(index).get("lat"));
        intent.putExtra("longi", myWishList.get(index).get("long"));
        intent.putExtra("category", myWishList.get(index).get("category"));
        intent.putExtra("image_url", myWishList.get(index).get("image_url"));

        startActivityForResult(intent, 1555);
    }

//==========GOING TO BE USED IN PRODUCT DETAILS============

//    public void goToRoute(View view){
//        String addstr = address + ","+ "+" + city + ",+" + region;
//        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//        String url = "https://maps.googleapis.com/maps/api/geocode/json?";
//        params.add(new BasicNameValuePair("key", "AIzaSyDnnBpMCtrnDehllEZA-XPJJwYEFlpSjnw"));
//        params.add(new BasicNameValuePair("address", addstr));
//
//        String paramString = URLEncodedUtils.format(params, "utf-8");
//        url += paramString;
//        //invalidEntryAlert(url);
//        HttpGet httpGet = new HttpGet(url);
//        new MyHttpGet(this).execute(httpGet);
//        MyMap m1 = new MyMap();
//        ArrayList<Pair<String,String>> coordinates = new ArrayList<Pair<String, String>>();
//        coordinates.add(Pair.create("", ""));
//        m1.callApi(coordinates);
//
//    }
//
//    @Override
//    public void processFinish(String result) {
//        // invalidEntryAlert(result);
//        JSONObject json;
//        try {
//            json = new JSONObject(result);
//            lati = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
//            longi = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
//            Log.d(lati, "latitude = ");
//            Log.d(longi, "longitude = ");
//
//            String url = "https://www.google.com/maps/dir/" + String.valueOf(curlati)+ "," + String.valueOf(curlongi) + "/" + lati + "," + longi;
//            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(i);
//        }catch(Exception e){}

    public void goToRoute(View view){
        ArrayList<Pair<String, String>> mylist = new ArrayList<Pair<String, String>>();
        ArrayList<Pair<String, String>> mylist2 = new ArrayList<Pair<String, String>>();

        mylist.add(Pair.create(String.valueOf(curlati), String.valueOf(curlongi)));
//        mylist.add(Pair.create(""+33.945358 , "-"+117.326893));
//        mylist.add(Pair.create(""+33.971984 , "-"+117.351784));
//        mylist.add(Pair.create(""+33.985649 , "-"+117.342342));
        for(int i  = 0; i < wishArray.length(); ++i) {
            try {
                mylist.add(Pair.create(wishArray.getJSONObject(i).getString("lat"), wishArray.getJSONObject(i).getString("long")));
                Log.d("lati : ", wishArray.getJSONObject(i).getString("lat"));
                Log.d("Long : ",wishArray.getJSONObject(i).getString("long"));
            }catch(JSONException e){}
        }
        MyMap mymap = new MyMap();
        mylist2 = mymap.callApi(mylist);
        Log.d("Mylist 2 size : ", String.valueOf(mylist2.size()));

        Log.d("globallatitude = ", String.valueOf(curlati));
        Log.d("globallongitude = ", String.valueOf(curlongi));
        String url = "https://www.google.com/maps/dir";// + String.valueOf(curlati) + "," + String.valueOf(curlongi);
        for(int i  = 0; i < mylist2.size(); ++i) {
                  url += "/";
                  url += mylist2.get(i).first;
                  url += ",+";
                  url += mylist2.get(i).second;
        }
        invalidEntryAlert(url);
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }

}
