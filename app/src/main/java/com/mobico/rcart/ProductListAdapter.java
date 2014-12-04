package com.mobico.rcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ProductListAdapter extends BaseAdapter implements MyAsyncResponse{

    private final static String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    public static SharedPreferences savedData;

    ProductListAdapter thisAdapter;

    ArrayList<HashMap<String,String>> list;
    Context context;

    ProductListAdapter(Context c) {
        this.context = c;
        this.thisAdapter = this;
        list = new ArrayList<HashMap<String, String>>();
    }

    ProductListAdapter(Context c, ArrayList<HashMap<String,String>> list_hash) {
        context = c;
        list = list_hash;
    }

    public void insertList(ArrayList<HashMap<String,String>> list_hash) {
        list = list_hash;
        Log.d("Inserting ArrayList into Adapter: ", "SUCCESS");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.product_row, viewGroup, false);

        final HashMap<String, String> listHash = list.get(i);

        TextView productName = (TextView) row.findViewById(R.id.productName);
        productName.setText(listHash.get("name"));

        TextView productStoreName = (TextView) row.findViewById(R.id.productStoreName);
        productStoreName.setText(listHash.get("store_name"));

        TextView productPrice = (TextView) row.findViewById(R.id.productPrice);
        productPrice.setText("$" + listHash.get("price"));

        Button removeItem = (Button) row.findViewById(R.id.productButton);
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedData = context.getSharedPreferences(SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
                String email = savedData.getString("email", "");
                String auth_token = savedData.getString("auth_token", "");

                String url = "https://mobibuddy.herokuapp.com/items/" + listHash.get("item_id") +".json";

                WishList myWishlist =(WishList) context;
                //myWishlist.invalidEntryAlert(url);
                HttpDelete httpdelete = new HttpDelete(url);
                httpdelete.addHeader("X-API_EMAIL", email);
                httpdelete.addHeader("X-API-TOKEN", auth_token);
                new MyHttpDelete(thisAdapter).execute(httpdelete);

            }
        });
        new DownloadImageTask((ImageView) row.findViewById(R.id.productImage)).execute(listHash.get("image_url"));

        return row;
    }

    @Override
    public void processFinish(String output) {

        WishList myWishlist =(WishList) context;
        //myWishlist.invalidEntryAlert(output);
        myWishlist.getWishList();

    }
}
