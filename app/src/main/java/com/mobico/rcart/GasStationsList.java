package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import java.util.List;
import java.util.Map;


public class GasStationsList extends Activity {

    ArrayList<HashMap<String,String>> ListOfRows;

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

    //FILTERING PART
    private String checkdistance = "" , oldd = "";
    private String checkbrand = "" , oldb= "";
    private String checkgastype = "" , oldg = "";
    private String checksortby = "", olds = "";
    private int dpos= 0, bpos = 0, gpos =0 , spos =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations_list);

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

        // call AsynTask to perform network operation on separate thread
        String url1 ="https://mobibuddy.herokuapp.com/nearby_gas.json?lat=" + String.valueOf(latitude) +
                "&long=" + String.valueOf(longitude) +
                "&dist="+ distance + "&sortBy=" + sortby;

        new HttpAsyncTask().execute(url1);

        /***********************************************************************************************
         * Main point is to go to the station details whenever a row is clicked, bringing element with it
         **********************************************************************************************/
        //Detects clicks on the ListView and returns the position of the click
        gasStationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("indexxxxxx: ", Integer.toString(i));
//                ArrayList<Integer> al = new ArrayList<Integer>();
////                al = fillList();
//                int ii = al.get(i);
                goToStationDetail(view, i);

            }
        });
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
//            ArrayList<Integer> al = new ArrayList<Integer>();
//            al = fillList();
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

    /***********************************************************************************************
     * function invalidEntryAlert
     * Displays an invalid message
     *
     * @param   String
     * @return  NONE
     **********************************************************************************************/
    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GasStationsList.this);

        builder.setTitle("Invalid entry");
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    /***********************************************************************************************
     * function searchBar
     *
     * @param   view
     * @return  NONE
     **********************************************************************************************/
    public void searchBar(View view) {
        EditText search = (EditText) findViewById(R.id.SearchBar);
        String searchBarText = search.getText().toString();


        if (searchBarText.length() == 0) {
            invalidEntryAlert("Please enter address, city, or zip");
        }
        else {
            search.setText("");
            String url = "https://mobibuddy.herokuapp.com/search.json?query=" + searchBarText;
            new HttpAsyncTask().execute(url);
        }
    }

    /***********************************************************************************************
     * function fillList
     * Fills the ArrayList of Hashmaps which is then pushed into the list adapter.
     * Each Hashmap contains each individual description (text) of the row.
     * This function takes parameters from gas array for the ArrayList to hold
     * the values of each row. This also then calls our listAdapter which outputs the values
     * of the rows, as well as set each row to a specific element. This allows multiple applications
     * with our rows, including pushing information to the Gas Details as well as easy access
     * to every description(text) in the row.
     *
     * @param   NONE
     * @return  NONE
     **********************************************************************************************/
    public void fillList(){

        ListOfRows = new ArrayList<HashMap<String,String>>();
        ArrayList<Integer> al = new ArrayList<Integer>();

        //Increment through every element in the gas array to push a hashmap back to be stored
        //into the ArrayList
        for(int i = 0 ; i < gasArray.length() ; i++){
            if(!checkbrand.equals("s") && !checkbrand.equals("")){
               // hash1 = pushListItemToLayoutView(i);
                if(pushListItemToLayoutView(i).get("station").equals(brand)){
                    ListOfRows.add(pushListItemToLayoutView(i)); //ListOfRows.add(hash1);
                    al.add(i);
                }
            }else if (checkbrand.equals("s")){ //empty choice case
                ListOfRows.add(pushListItemToLayoutView(i));
                al.add(i);
                Log.d("add rows: ", Integer.toString(i));
            }else if (checkbrand.equals("")){ //initial case
                ListOfRows.add(pushListItemToLayoutView(i));
                al.add(i);
                Log.d("add rows: ", Integer.toString(i));
            }

        }
        //Sets the List Adapter to be used, calls getView automatically, displaying the rows
        listAdapter = new GasStationListAdapter(this);
        listAdapter.insertList(ListOfRows);
        gasStationList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    /***********************************************************************************************
     * function pushListItemToLayoutView
     * Returns the HashMap of the row in order for the hash map to be stored into the array
     *
     * @param   i       Which is used to get the specific element in the gas array
     * @return  Hashmap<String,String></String,String>
     **********************************************************************************************/
    private HashMap<String,String> pushListItemToLayoutView(int i) {
       // LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //iew listItem = inflater.inflate(R.layout.gas_station_row, null);
        HashMap<String,String> hash = new HashMap<String, String>();

        try {
            //Gets the JSON strings corresponding to their objects, assorting each string with
            //their keys
            hash.put("station",gasArray.getJSONObject(i).getString("station"));
            hash.put("distance",gasArray.getJSONObject(i).getString("distance"));

            hash.put("address",gasArray.getJSONObject(i).getString("address"));
            hash.put("city",gasArray.getJSONObject(i).getString("city"));
            hash.put("region",gasArray.getJSONObject(i).getString("region"));
            hash.put("zip",gasArray.getJSONObject(i).getString("zip"));
            hash.put("country",gasArray.getJSONObject(i).getString("country"));

            hash.put("reg_price",gasArray.getJSONObject(i).getString("reg_price"));
            hash.put("mid_price",gasArray.getJSONObject(i).getString("mid_price"));
            hash.put("pre_price",gasArray.getJSONObject(i).getString("pre_price"));

            hash.put("station_id",gasArray.getJSONObject(i).getString("id"));
            hash.put("reg_date",gasArray.getJSONObject(i).getString("updated_at"));

            hash.put("phone",gasArray.getJSONObject(i).getString("phone"));
            hash.put("credit",gasArray.getJSONObject(i).getString("credit"));
            hash.put("carwash",gasArray.getJSONObject(i).getString("carwash"));
            hash.put("hours",gasArray.getJSONObject(i).getString("hours"));
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /***********************************************************************************************
     * function goToStationDetail
     * Goes to the Station Detail activity as well as sending in necessary parameters through
     *
     * @param   view            Needed for function to work (Not used)
     * @param   specific_row    Used to see which row is clicked in order to receive the correct
     *                          element in the gas array
     * @return  NONE
     **********************************************************************************************/
    public void goToStationDetail(View view, int index) {
        Intent i = new Intent(GasStationsList.this, StationDetail.class);

        //Stations and distance
        i.putExtra("istation", ListOfRows.get(index).get("station"));
        i.putExtra("idistance", ListOfRows.get(index).get("distance"));

        //Prices
        i.putExtra("ireg_price", ListOfRows.get(index).get("reg_price"));
        i.putExtra("imid_price", ListOfRows.get(index).get("mid_price"));
        i.putExtra("ipre_price", ListOfRows.get(index).get("pre_price"));

        //Locations
        i.putExtra("iaddress", ListOfRows.get(index).get("address"));
        i.putExtra("icity", ListOfRows.get(index).get("city"));
        i.putExtra("iregion", ListOfRows.get(index).get("region"));
        i.putExtra("izip", ListOfRows.get(index).get("zip"));
        i.putExtra("icountry", ListOfRows.get(index).get("country"));

        //StationID
        i.putExtra("istation_id", ListOfRows.get(index).get("station_id"));

        //Features
        i.putExtra("iphone", ListOfRows.get(index).get("phone"));
        i.putExtra("icredit", ListOfRows.get(index).get("credit"));
        i.putExtra("icarwash", ListOfRows.get(index).get("carwash"));
        i.putExtra("ihours", ListOfRows.get(index).get("hours"));

        //Will return to the onActivityResult function
        startActivityForResult(i, 3);
    }

    /***********************************************************************************************
     * function goToFilterFromGasStation
     * Goes to the Filter activity
     *
     * @param   view    Needed for function to work (Not used)
     * @return  NONE
     **********************************************************************************************/
    public void goToFilterFromGasStation(View view) {
        Intent i = new Intent(GasStationsList.this, Filter.class);
        //Will return to the onActivityResult function
        i.putExtra("mile",dpos);
        i.putExtra("name", bpos);
        i.putExtra("type", gpos);
        i.putExtra("sort", spos);
        startActivityForResult(i, 1);
    }

    /***********************************************************************************************
     * function onActivityResult
     * Gets called whenever a user returns from the different activities (Filter, StationDetail)
     *
     * @param   requestCode Used to differentiate between the different activities
     * @param   resultCode  The status of the changes from the previous activity
     * @param   data        The intent from the previous activity for the updated variables
     * @return  NONE
     **********************************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        //When the activity switches from Filter to Gas Stations list
        if(requestCode == 1 && resultCode == RESULT_OK) {
            checkdistance = data.getStringExtra("distance");
            checkbrand = data.getStringExtra("brand");
            checkgastype = data.getStringExtra("gastype");
            checksortby = data.getStringExtra("sortby");
            dpos = data.getIntExtra("mile", 0);
            bpos = data.getIntExtra("name", 0);
            gpos = data.getIntExtra("type", 0);
            spos = data.getIntExtra("sort", 0);

            if(checkdistance.equals("s")){
                distance = oldd;
            } else{
                distance = checkdistance;
            }
            if(checkbrand.equals("s")){
                brand= oldb;
            } else{
                brand = checkbrand;
            }
            if(checkgastype.equals("s")){
                gastype = oldg;
            } else{
                gastype = checkgastype;
            }
            if(checksortby.equals("s")){
                sortby = olds;
            } else{
                sortby = checksortby;
            }

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

        //When the activity switches from Station Details to Gas Stations list
        //Updates the list only if user updates gas
        else if (requestCode == 3 && resultCode == RESULT_OK) {
            //Receives the boolean value from Station Details if gas is updated or not
            boolean if_updated = data.getBooleanExtra("updated", false);

            //If it was updated, update the actual list with the new price
            if (if_updated) {

                //Our mobi url which includes latitude, longitude, distance, and sortby as params
                String url1 = "https://mobibuddy.herokuapp.com/nearby_gas.json?lat="
                        + String.valueOf(latitude) +
                        "&long=" + String.valueOf(longitude) +
                        "&dist=" + distance + "&sortBy=" + sortby;

                //Executes a GET request to our own server
                new HttpAsyncTask().execute(url1);
            }
        }
    }
    public void goToMap(View view){
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/33.9759789,-117.3259195/Starbucks/@33.9653373,-117.3180231,14z/data=!4m8!4m7!1m0!1m5!1m1!1s0x0:0x391a790c6071196e!2m2!1d-117.330413!2d33.957142"));
        startActivity(i);
    }
}
