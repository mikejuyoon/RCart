package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductsList extends Activity implements MyAsyncResponse {

    ArrayList<String> productsList;
    ListView listView;
    double latitude, longitude;
    EditText productSearchBar;
    Spinner categories_spinner;
    ArrayAdapter<String> adapter;
    String currCategory;

    HashMap<String,String> categoryMap;


    JSONArray listJson;

    //Used for checking if user is logged in for wishlist
    private final static String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    public static SharedPreferences savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Intent intent1 = getIntent();
        latitude = intent1.getDoubleExtra("lati", 1.0);
        longitude = intent1.getDoubleExtra("longi", 1.0);
        ((globalvariable) this.getApplication()).setGloballati(latitude);
        ((globalvariable) this.getApplication()).setGloballongi(longitude);
       // invalidEntryAlert(String.valueOf(latitude + String.valueOf(longitude)));

        productSearchBar = (EditText) findViewById(R.id.productSearchBar);
        categories_spinner = (Spinner) findViewById(R.id.categories_spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(spinner_adapter);


        listView = (ListView) findViewById(R.id.myListView);

        productsList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, productsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject currObject;
                try {
                    currObject = listJson.getJSONObject(i);
                    Intent intent = new Intent(ProductsList.this, NearbyList.class);
                    intent.putExtra("lati", latitude);
                    intent.putExtra("longi", longitude);
                    intent.putExtra("name", currObject.getString("name"));
                    intent.putExtra("imgUrl", currObject.getString("thumbnailImage"));
                    intent.putExtra("price", currObject.getString("salePrice"));
                    intent.putExtra("category", currCategory);
                    startActivityForResult(intent, 1234);

                }catch(Exception e){}

            }
        });

        makeCategoryHashMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.products_list, menu);
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

    //Used when user is not logged in as the user clicks on the Wishlist button
    private void notLoggedInError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsList.this);
        builder.setTitle("Not logged in"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    public void goToWishlist(View view) {
        //Opens up SharedPreferences
        //Reads from the SharedPreferences the email and token to access that specific user's wishlist
        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String defaultValue = "";
        String email = savedData.getString("email", "");
        String auth_token = savedData.getString("auth_token", "");

        //If there is a valid email and auth_token, then go to wishlist
        if (email.length() > 0 && auth_token.length() > 0) {
            Intent i = new Intent(ProductsList.this, WishList.class);
            i.putExtra("lati", latitude);
            i.putExtra("longi", longitude);
            i.putExtra("email", email);
            i.putExtra("auth_token", auth_token);
            startActivityForResult(i, 1111);
        }
        //If there is no valid email or auth_token, displays an invalid message
        else {
            //notLoggedInError("Please login to access your Wishlist.");
            Intent i = new Intent(ProductsList.this, Login.class);
            startActivityForResult(i, 999);
        }
    }

    public void productSearchButton(View view){
        String itemSelected = categories_spinner.getSelectedItem().toString();
        String searchKeyword = productSearchBar.getText().toString();
        if(itemSelected.equals("Select Category") || searchKeyword.length()==0){ //I know. Horrible.
            invalidEntryAlert("Please enter a product keyword and select a category");
            return;
        }

        currCategory = itemSelected;

        if(isConnected()){
            callWalmartApi(searchKeyword);
        }
        else
            invalidEntryAlert("No connection");

    }



    // ============ HELPER FUNCTIONS =================

    private void makeCategoryHashMap(){
        ArrayList<String> categoryKey = new ArrayList<String>(Arrays.asList("Electronics & Computers", "Groceries & Food", "Pharmacy & Health", "Sports & Outdoors", "Home, Garden & Tools", "Beauty & Health", "Automotive & Industrial", "Health & Household", "Toys, Kids & Baby", "Clothing, Shoes & Jewelry", "Entertainment & Books", "Movies, Music & Games", "Office & School Supplies"));
        ArrayList<String> categoryValue = new ArrayList<String>(Arrays.asList("3944","976759_1071964_976793","976760","4096_616859_617020","4044","1085666","91083_1104294_1218943","1115193","5427","5438","4044_103150_635499","4096","1085632_1104053_1199468"));
        categoryMap = new HashMap<String, String>();
        for(int i = 0 ; i < categoryKey.size() ; i++){
            categoryMap.put(categoryKey.get(i),categoryValue.get(i));
        }
    }
    private boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsList.this);
        builder.setTitle("Error"); /// change this
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);
        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    private void callWalmartApi(String searchKeyword){
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        String url = "http://walmartlabs.api.mashery.com/v1/search?&format=json&";
        params.add(new BasicNameValuePair("apiKey", "ucggnqrfww45m98bbv93ny4h"));
        params.add(new BasicNameValuePair("format", "json"));
        params.add(new BasicNameValuePair("query", searchKeyword));
        if(categoryMap.containsKey(currCategory)){
            params.add(new BasicNameValuePair("categoryId", categoryMap.get(currCategory)));
            //invalidEntryAlert(categoryMap.get(currCategory));
        }
        String paramString = URLEncodedUtils.format(params, "utf-8");
        url += paramString;
        HttpGet httpGet = new HttpGet(url);
        new MyHttpGet(this).execute(httpGet);
    }

    private void updateProductList(){
        productsList.clear();
        for(int i = 0 ; i < listJson.length() ; i++){
            try {
                productsList.add(listJson.getJSONObject(i).getString("name"));
            }catch(Exception e){}
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void processFinish(String result) {
        JSONObject json;
        try {
            json = new JSONObject(result);
            if(!json.getString("numItems").equals("0")){
                listJson = json.getJSONArray("items");
                updateProductList();
            }
            else{
                invalidEntryAlert("No results found");
            }
        }catch(Exception e){}
    }
}
