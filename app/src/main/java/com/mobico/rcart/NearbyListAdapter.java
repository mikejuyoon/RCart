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

public class NearbyListAdapter extends BaseAdapter{

    //Public class variables
    ArrayList<HashMap<String,String>> list;
    Context context;

    NearbyListAdapter(Context c) {
        context = c;
        list = new ArrayList<HashMap<String, String>>();
        Log.d("Constructor ", "Haha");

    }

    NearbyListAdapter(Context c, ArrayList<HashMap<String,String>> list_hash) {
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
        row = inflater.inflate(R.layout.store_row, viewGroup, false);

        HashMap<String, String> listHash = list.get(i);

        TextView gasStationName = (TextView) row.findViewById(R.id.store_name);
        gasStationName.setText(listHash.get("store_name"));

        TextView gasDistance = (TextView) row.findViewById(R.id.store_price);
        gasDistance.setText(listHash.get("store_price"));

        TextView gasAddress = (TextView) row.findViewById(R.id.store_distance);
        gasAddress.setText( listHash.get("store_distance"));

        return row;
    }

}
