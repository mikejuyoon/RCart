package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.net.HttpURLConnection;
import java.net.URL;


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
import android.widget.AdapterView;
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

    Double latitude;
    Double longitude;

    GasStationListAdapter listAdapter;
    private String distance = "2";
    private String sortby = "price";
    private String brand = "";
    private String gastype = "";
    private String checkdistance = "" , oldd = "";
    private String checkbrand = "" , oldb= "";
    private String checkgastype = "" , oldg = "";
    private String checksortby = "", olds = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations_list);

       /* ImageView i = (ImageView)findViewById(R.id.img1);
        i.setBackgroundResource(R.drawable.gif);

        AnimationDrawable pro = (AnimationDrawable)i.getBackground();
        pro.start();*/

        gasStationList = (ListView) findViewById(R.id.myListView);

        listAdapter = new GasStationListAdapter(this);
        gasStationList.setAdapter(listAdapter);

        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);

        Intent intent2 = getIntent();
        onActivityResult(0, RESULT_OK, intent2);
        if(distance == null) distance = "2";
        if(sortby == null) sortby = "distance";
        oldd = distance;
        olds = sortby;

       // tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        // check if you are connected or not
        /*txtLat.setText(latitude+ "");
        txtLong.setText(longitude+"");
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }*/
        // call AsynTask to perform network operation on separate thread
        String url1 ="https://mobibuddy.herokuapp.com/nearby_gas.json?lat=" + String.valueOf(latitude) +
                "&long=" + String.valueOf(longitude) +
                "&dist="+ distance + "&sortBy=" + sortby;
        //String url2 ="https://mobibuddy.herokuapp.com/nearby_gas.json?lat=" + String.valueOf(latitude) +
        //        "&long=" + String.valueOf(longitude) +
         //      "&dist="+ distance + "&sortBy=" + sortby + brand + gastype;
        // String url1 = "http://api.mygasfeed.com/stations/radius/34.081823/-118.09926/3/reg/price/xfakzg0s3n.json";


 //        invalidEntryAlert(url2);

        //vIsConnected.setText(url2);

        new HttpAsyncTask().execute(url1);

        /***********************************************************************************************
         * Main point is to go to the station details whenever a row is clicked, bringing element with it
         **********************************************************************************************/
        //Detects clicks on the ListView and returns the position of the click
        gasStationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("indexxxxxx: ", Integer.toString(i));
                goToStationDetail(view, i);
            }
        });
    }

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GasStationsList.this);

        builder.setTitle("Error");
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == RESULT_OK){
            checkdistance = data.getStringExtra("distance");
            checkbrand = data.getStringExtra("brand");
            checkgastype = data.getStringExtra("gastype");
            checksortby = data.getStringExtra("sortby");


            if(checkdistance.equals("s")){distance = oldd;} else distance = checkdistance;
            if(checkbrand.equals("s")){brand= oldb;} else brand = checkbrand;
            if(checkgastype.equals("s")){gastype = oldg;} else gastype = checkgastype;
            if(checksortby.equals("s")){sortby = olds;} else sortby = checksortby;

            oldd = distance;
            oldb = brand;
            oldg = gastype;
            olds = sortby;
            //String url2 = "&dist="+ checkdistance + "!!!" + distance + " &gastype=" + gastype +" brand=" + brand + " sortby=" + sortby;

            //tvIsConnected.setText(url2);

            String url1 ="https://mobibuddy.herokuapp.com/nearby_gas.json?lat=" + String.valueOf(latitude) +
                    "&long=" + String.valueOf(longitude) +
                    "&dist="+ distance + "&sortBy=" + gastype;
            //invalidEntryAlert(url2);
            new HttpAsyncTask().execute(url1);

        }
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
            fillList();
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

    public void fillList(){
        ArrayList<HashMap<String,String>> ListOfRows = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> hash1 = new HashMap<String, String>();
        for(int i = 0 ; i < gasArray.length() ; i++){
            if(!checkbrand.equals("s") && !checkbrand.equals("")){
                hash1 = pushListItemToLayoutView(i);
                if(pushListItemToLayoutView(i).get("station").equals(brand)){
                    ListOfRows.add(hash1);
                }
            }else if (checkbrand.equals("s")){
                ListOfRows.add(pushListItemToLayoutView(i));
                Log.d("add rows: ", Integer.toString(i));
            }else if (checkbrand.equals("")){
                ListOfRows.add(pushListItemToLayoutView(i));
                Log.d("add rows: ", Integer.toString(i));
            }

        }
        listAdapter.insertList(ListOfRows);
        gasStationList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private HashMap<String,String> pushListItemToLayoutView(int i) {
       // LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //iew listItem = inflater.inflate(R.layout.gas_station_row, null);
        HashMap<String,String> hash = new HashMap<String, String>();
            try {
                hash.put("station", gasArray.getJSONObject(i).getString("station"));
                hash.put("distance", gasArray.getJSONObject(i).getString("distance"));
                hash.put("address", gasArray.getJSONObject(i).getString("address"));
//                if(checkgastype.equals("mid"))
//                    hash.put("mid_price", gasArray.getJSONObject(i).getString("mid_price"));
//                else if(checkgastype.equals("pre"))
//                    hash.put("pre_price", gasArray.getJSONObject(i).getString("pre_price"));
//                else
                    hash.put("reg_price", gasArray.getJSONObject(i).getString("reg_price"));
                hash.put("station_id", gasArray.getJSONObject(i).getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return hash;
    }

    public void goToStationDetail(View view, int specific_row){
        Intent i = new Intent(GasStationsList.this, StationDetail.class);
        try {
            i.putExtra("istation", gasArray.getJSONObject(specific_row).getString("station"));
            i.putExtra("idistance", gasArray.getJSONObject(specific_row).getString("distance"));
            i.putExtra("iaddress", gasArray.getJSONObject(specific_row).getString("address"));
            i.putExtra("ireg_price", gasArray.getJSONObject(specific_row).getString("reg_price"));
            i.putExtra("imid_price", gasArray.getJSONObject(specific_row).getString("mid_price"));
            i.putExtra("ipre_price", gasArray.getJSONObject(specific_row).getString("pre_price"));
            i.putExtra("icity", gasArray.getJSONObject(specific_row).getString("city"));
            i.putExtra("iregion", gasArray.getJSONObject(specific_row).getString("region"));
            i.putExtra("izip", gasArray.getJSONObject(specific_row).getString("zip"));
            i.putExtra("icountry", gasArray.getJSONObject(specific_row).getString("country"));
            i.putExtra("istation_id", gasArray.getJSONObject(specific_row).getString("id"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        startActivity(i);
    }

    public void goToFilterFromGasStation(View view){
        Intent i = new Intent(GasStationsList.this, Filter.class);
        startActivityForResult(i, 1); //getting result back from next activity(child activity)
        //startActivity(i);
        //finish();
    }
}
