package com.mobico.rcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class GasStationsList extends Activity {

    ListView gasStationList;
    JSONObject gasJson;
    JSONArray gasArray;

    EditText etResponse;
    TextView tvIsConnected;
    TextView txtLong,txtLat;

    GasStationListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations_list);
        txtLong = (TextView) findViewById(R.id.txtLong);
        txtLat = (TextView) findViewById(R.id.txtLat);

        gasStationList = (ListView) findViewById(R.id.myListView);
        listAdapter = new GasStationListAdapter(this);

        gasStationList.setAdapter(listAdapter);

        Intent intent1 = getIntent();
        Double latitude = intent1.getDoubleExtra("lati", 1.0);
        Double longitude = intent1.getDoubleExtra("longi", 1.0);

        // get reference to the views
        //etResponse = (EditText) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        // check if you are connected or not

        txtLat.setText(latitude+ "");
        txtLong.setText(longitude+"");
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }
        // call AsynTask to perform network operation on separate thread
        String url1 ="https://mobibuddy.herokuapp.com/nearby_gas.json?lat=" + String.valueOf(latitude) + "&long=" + String.valueOf(longitude) + "&dist=2&sortBy=price";
        //String url1 = "http://api.mygasfeed.com/stations/radius/34.081823/-118.09926/3/reg/price/xfakzg0s3n.json";

        tvIsConnected.setText(url1);

        new HttpAsyncTask().execute(url1);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try{
                gasJson = new JSONObject(result);
                gasArray = gasJson.getJSONArray("stations");
                //etResponse.setText(json.toString(1));
            }
            catch(JSONException e){
                //do nothing
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gas_stations_list, menu);
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

    public void fillList(View view){
        ArrayList<HashMap<String,String>> ListOfRows = new ArrayList<HashMap<String, String>>();
        for(int i = 0 ; i < gasArray.length() ; i++){
            ListOfRows.add(pushListItemToLayoutView(i));
            Log.d("add rows: ", Integer.toString(i));
        }
        listAdapter.insertList(ListOfRows);
        gasStationList.setAdapter(listAdapter);
    }

    private HashMap<String,String> pushListItemToLayoutView(int i) {
        HashMap<String,String> hash = new HashMap<String, String>();

        try {
            hash.put("station",gasArray.getJSONObject(i).getString("station"));
            hash.put("distance",gasArray.getJSONObject(i).getString("distance"));
            hash.put("address",gasArray.getJSONObject(i).getString("address"));
            hash.put("reg_price",gasArray.getJSONObject(i).getString("reg_price"));
            hash.put("station_id",gasArray.getJSONObject(i).getString("id"));
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public void goToStationDetail(View view){
    //    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //    View listItem = inflater.inflate(R.layout.gas_station_row, null);
    //    RelativeLayout gasStationRow = (RelativeLayout) listItem.findViewById(R.id.gasBackground);
    //    gasStationRow.setBackgroundColor(Color.WHITE);

        //gasStationList.addView(listIt);
        Intent i = new Intent(GasStationsList.this, StationDetail.class);
        startActivity(i);

        //i.putExtra("istation_id", );
        //finish();
    }
}
