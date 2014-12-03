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

        TextView productDistance = (TextView) row.findViewById(R.id.productDistance);
        productDistance.setText(listHash.get("distance"));

//        TextView productAddress = (TextView) row.findViewById(R.id.productAddress);
//        productAddress.setText( listHash.get("address") + "\n"
//                + listHash.get("city")    + ", "
//                + listHash.get("region")  + " "
//                + listHash.get("zip")     + "\n");

        TextView productPrice = (TextView) row.findViewById(R.id.productPrice);
        productPrice.setText(listHash.get("price"));

        TextView productDate = (TextView) row.findViewById(R.id.productDate);
        productDate.setText(listHash.get("reg_date"));

        return row;
    }

}
