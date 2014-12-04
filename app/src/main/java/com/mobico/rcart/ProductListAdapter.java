package com.mobico.rcart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductListAdapter extends BaseAdapter{

    ArrayList<HashMap<String,String>> list;
    Context context;

    ProductListAdapter(Context c) {
        context = c;
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

        HashMap<String, String> listHash = list.get(i);

        TextView productName = (TextView) row.findViewById(R.id.productName);
        productName.setText(listHash.get("name"));

        TextView productStoreName = (TextView) row.findViewById(R.id.productStoreName);
        productStoreName.setText(listHash.get("store_name"));

        TextView productPrice = (TextView) row.findViewById(R.id.productPrice);
        productPrice.setText(listHash.get("price"));

        TextView productImage = (TextView) row.findViewById(R.id.productImage);
        productImage.setText(listHash.get("image_url"));

        return row;

//        hash.put("name", wishArray.getJSONObject(i).getString("name"));
//        hash.put("category", wishArray.getJSONObject(i).getString("category"));
//        hash.put("price",wishArray.getJSONObject(i).getString("price"));
//        hash.put("image_url",wishArray.getJSONObject(i).getString("image_url"));
//        hash.put("lat",wishArray.getJSONObject(i).getString("lat"));
//        hash.put("long",wishArray.getJSONObject(i).getString("long"));
//        hash.put("store_name",wishArray.getJSONObject(i).getString("store_name"));
    }



}
