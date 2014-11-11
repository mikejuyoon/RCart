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

public class GasStationListAdapter extends BaseAdapter{

    //Public class variables
    ArrayList<HashMap<String,String>> list;
    Context context;

    /***********************************************************************************************
     * Constructor, initializes context and list
     *
     * @param   Context
     **********************************************************************************************/
    GasStationListAdapter(Context c) {
        context = c;
        list = new ArrayList<HashMap<String, String>>();
        Log.d("Constructor ", "Haha");

    }

    /***********************************************************************************************
     * Constructor, initializes context and list
     *
     * @param   Context
     * @param   ArrayList<HashMap<String,String>>
     **********************************************************************************************/
    GasStationListAdapter(Context c, ArrayList<HashMap<String,String>> list_hash) {
        context = c;
        list = list_hash;
    }

    /***********************************************************************************************
     * Sets the list (ArrayList of HashMaps) equal to the inserted ArrayList of HashMaps
     *
     * @param   ArrayList<HashMap<String,String>>
     **********************************************************************************************/
    public void insertList(ArrayList<HashMap<String,String>> list_hash) {
        list = list_hash;
        Log.d("insertion hehehee ", "Haha");
    }

    /***********************************************************************************************
     * Returns the size of the list
     *
     * @param   NONE
     * @return  size of the list
     **********************************************************************************************/
    @Override
    public int getCount() {
        return list.size();
    }

    /***********************************************************************************************
     * Returns the element of list given by the parameter
     *
     * @param   int
     * @return  specified element
     **********************************************************************************************/
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    /***********************************************************************************************
     * Returns 0
     *
     * @param   NONE
     * @return  0
     **********************************************************************************************/
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /***********************************************************************************************
     * Function getView
     * Displays the view of the row of each gas station
     *
     * @param   int
     * @param   View
     * @param   ViewGroup
     * @return  View
     **********************************************************************************************/
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.gas_station_row, viewGroup, false);

            HashMap<String, String> listHash = list.get(i);
            Log.d("rows: ", Integer.toString(i));

            TextView gasStationName = (TextView) row.findViewById(R.id.gasStationName);
            gasStationName.setText(listHash.get("station"));
            Log.d("Gas station name: ", listHash.get("station"));

            TextView gasDistance = (TextView) row.findViewById(R.id.gasDistance);
            gasDistance.setText(listHash.get("distance"));
            Log.d("Gas distance: ", listHash.get("distance"));

            TextView gasAddress = (TextView) row.findViewById(R.id.gasAddress);
            gasAddress.setText(listHash.get("address"));
            Log.d("Gas address: ", listHash.get("address"));

            TextView gasPrice = (TextView) row.findViewById(R.id.gasPrice);
            gasPrice.setText(listHash.get("reg_price"));
            Log.d("Gas price: ", listHash.get("reg_price"));

            return row;
        }
        else {
            return row;
        }
    }

}
