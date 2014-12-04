package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class WishList extends Activity implements MyAsyncResponse {

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
   public void goToRoute(View view){
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
        MyMap m1 = new MyMap();
        ArrayList<Pair<String,String>> coordinates = new ArrayList<Pair<String, String>>();
        coordinates.add(Pair.create("", ""));
        m1.callApi(coordinates);
       
    }

    @Override
    public void processFinish(String result) {
        // invalidEntryAlert(result);
        JSONObject json;
        try {
            json = new JSONObject(result);
            lati = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
            longi = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
            Log.d(lati, "latitude = ");
            Log.d(longi, "longitude = ");

            String url = "https://www.google.com/maps/dir/" + String.valueOf(curlati)+ "," + String.valueOf(curlongi) + "/" + lati + "," + longi;
            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }catch(Exception e){}
    }
}
