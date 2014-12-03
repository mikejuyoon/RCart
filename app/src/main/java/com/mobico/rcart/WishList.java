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


public class WishList extends Activity {

    ArrayList<String> myWishList;
    ListView listView;
    double latitude,longitude;

    JSONObject wishJson;
    JSONArray wishArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        //Creates the intent to bring in the extras sent from the previous activities
        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);

        //Declaring variables of the public variables
        myWishList = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.myListView);

        //Creates the URL to call Get requests from the server with the added headers that
        //deal with authentication
        String url = "https://mobibuddy.herokuapp.com/wishlist.json";
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("X-API_EMAIL", "test@test.com");
        httpGet.addHeader("X-API-TOKEN", "kjoeCiMizXfngxfqutE3xz_QiHxeCgZESQ");
        new MyHttpGet().execute(httpGet);

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
        for (int i  = 0; i < wishArray.length(); ++i) {
            try {
                myWishList.add(wishArray.getJSONObject(i).getString("name"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myWishList);
        listView.setAdapter(adapter);
    }
    
    private class MyHttpGet extends AsyncTask<HttpGet, Void, String> {

        @Override
        protected String doInBackground(HttpGet... getUrl) {
            return GET(getUrl[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                wishJson = new JSONObject(result);
                wishArray = wishJson.getJSONArray("wishlist");
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            //Fills the list with the JSON values
            fillList();
        }

        public String GET(HttpGet getUrl){
            InputStream inputStream = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(getUrl);
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                }
                else{
                    result = "Did not work!";
                    invalidEntryAlert("GET request error");
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            return result;
        }

        private void invalidEntryAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WishList.this);

            builder.setTitle("Invalid entry");
            builder.setPositiveButton("OK", null);
            builder.setMessage(message);

            AlertDialog theAlertDialog = builder.create();
            theAlertDialog.show();
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }
    }
}
