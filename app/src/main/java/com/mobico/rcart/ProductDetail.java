package com.mobico.rcart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class ProductDetail extends Activity implements MyAsyncResponse{

    //Public Variables
    Intent intent;

    //Used for the displays in the product details page
    String product_name, store_name, store_distance, store_price, lati, longi, category, image_url;
    TextView product_name_tv, store_name_tv, store_distance_tv, store_price_tv;
    Double curlati, curlongi;

    //Used to store the email and auth_token
    private final static String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    SharedPreferences savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Used for checking the email and token
        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        //Receiving the variables sent from the previous activity
        intent = getIntent();
        product_name = intent.getStringExtra("product_name");
        store_name = intent.getStringExtra("store_name");
        store_price = intent.getStringExtra("store_price");
        store_distance = intent.getStringExtra("store_distance");
        lati = intent.getStringExtra("lati");
        longi = intent.getStringExtra("longi");
        category = intent.getStringExtra("category");
        image_url = intent.getStringExtra("image_url");

        //The view of the properties of the product
        product_name_tv = (TextView) findViewById(R.id.product_name);
        store_name_tv = (TextView) findViewById(R.id.store_name);
        store_distance_tv = (TextView) findViewById(R.id.store_distance);
        store_price_tv = (TextView) findViewById(R.id.store_price);
        //To display the image of the product
        new DownloadImageTask((ImageView) findViewById(R.id.productImage)).execute(image_url);

        //Sets the text for each of the Text Views accordingly to the variables sent from the
        //previous activity
        product_name_tv.setText(product_name);
        store_name_tv.setText(store_name);
        store_distance_tv.setText(store_distance);
        store_price_tv.setText(store_price);
        curlati = ((globalvariable) this.getApplication()).getGloballati();
        curlongi = ((globalvariable) this.getApplication()).getGloballongi();
        store_name_tv.setText("Store: " + store_name);
        store_distance_tv.setText(store_distance + " miles");
        store_price_tv.setText("$" + store_price);
    }

    public void goToWishlist(View view){
        //Creates the wish list when user clicks the button to add to wishlist.
        String url = "https://mobibuddy.herokuapp.com/items.json?";

        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("name", product_name));
        params.add(new BasicNameValuePair("category", category));
        params.add(new BasicNameValuePair("store_name", store_name));
        params.add(new BasicNameValuePair("price", store_price));
        params.add(new BasicNameValuePair("image_url", image_url));
        params.add(new BasicNameValuePair("lat", lati));
        params.add(new BasicNameValuePair("long", longi));
        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("X-API-EMAIL", savedData.getString("email", "DEFAULT"));
        httpPost.addHeader("X-API-TOKEN", savedData.getString("auth_token", "DEFAULT"));
        new MyHttpPost(this).execute(httpPost);
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

    @Override
    public void processFinish(String output) {
        //Gives a toast depending if the item is in the wish list or not.
        JSONObject json;
        try {
            json = new JSONObject(output);
            if (json.getString("success").equals("true"))
                Toast.makeText(getBaseContext(), "Added to wishlist!", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getBaseContext(), "Already in wishlist!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    public void goToRouteMe(View view) {
        // invalidEntryAlert(result);
        Log.d("latitude = ", lati);
        Log.d("longitude = ", longi);
        Log.d("globallatitude = ", String.valueOf(curlati));
        Log.d("globallongitude = ", String.valueOf(curlongi));
        String url = "https://www.google.com/maps/dir/" + String.valueOf(curlati) + "," + String.valueOf(curlongi) + "/" + lati + "," + longi;
        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }


}

