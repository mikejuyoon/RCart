package com.mobico.rcart;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andy on 12/2/2014.
 */
public class MyMap implements MyAsyncResponse{


    ArrayList<Pair<String,String>> coordinates;
    ArrayList<Edge> edge_list;
    int start_pos = 0;

    public MyMap(){

    }

    public void callApi(ArrayList<Pair<String,String>> input){

        edge_list = new ArrayList<Edge>();
        coordinates = new ArrayList<Pair<String, String>>(input);
        start_pos = 0;

        int size = coordinates.size();
        String t= "" + size;
        Log.d("Coordinates size = ", t);

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?";

        //loop through each vertex
        //first latitude
        //second longitude

        //actual code
        for( int i = 0; i < coordinates.size() - 1; ++i) {
            url = "https://maps.googleapis.com/maps/api/distancematrix/json?";
            //source
            String orig = coordinates.get(i).first;
            orig += "," + coordinates.get(i).second;
            Log.d("i vertex: ", "" + i);


            //destinations
            String destinations = "";
            for( int j = i + 1 ; j < coordinates.size(); ++j) {
                destinations += coordinates.get(j).first + "," + coordinates.get(j).second + "|";
                Log.d("j vertex: ", "" + j);
            }

            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("origins", orig));
            params.add(new BasicNameValuePair("destinations", destinations));
            params.add(new BasicNameValuePair("units","imperial" ));
            params.add(new BasicNameValuePair("mode","driving" ));
            params.add(new BasicNameValuePair("key", "AIzaSyC7bJzIpJkliaoW11SFz_nEcCRi1YxRZHc"));

            String paramString = URLEncodedUtils.format(params, "utf-8");

            url += paramString;
            HttpGet httpGet = new HttpGet(url);
            new MyHttpGet(this).execute(httpGet);

        }//*/

        //test hardcoded latitude longitude values
        /*
        List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("origins", "33.9759789,-117.3259195"));
        params.add(new BasicNameValuePair("destinations", "33.9653373,-117.3180231"));
        params.add(new BasicNameValuePair("units","imperial" ));
        params.add(new BasicNameValuePair("mode","driving" ));
        params.add(new BasicNameValuePair("key", "AIzaSyC7bJzIpJkliaoW11SFz_nEcCRi1YxRZHc"));

        String paramString = URLEncodedUtils.format(params, "utf-8");

        url += paramString;
        HttpGet httpGet = new HttpGet(url);
        new MyHttpGet(this).execute(httpGet);
        */
    }




    @Override
    public void processFinish(String result){
        Log.d("testing map: ", "inside processFinish");
        JSONObject my_json;
        int distance = 0;
        try {
            my_json = new JSONObject(result);

            //actual
            int obj_pos = 0;
            int A = 0;
            int B = 0;

            for(int i = start_pos; i < coordinates.size() - 1; ++i) {
                A = i;
                String test = "A = " + A;
                Log.d("inside for loop", test);
                for(int j = i + 1; j < coordinates.size(); ++j) {
                    B = j;
                    try {
                        distance = my_json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").
                                getJSONObject(obj_pos).getJSONObject("distance").getInt("value");

                        obj_pos++;
                        Edge my_edge = new Edge(A,B,distance);
                        edge_list.add(my_edge);


                    }

                    catch(Exception e)
                    {

                    }
                    Log.d("test", "after JSON");


                    Log.d("adding edge", "A " + A + " B " + B + " dist " + distance);


                    Log.d("edgelist size ", "" + edge_list.size());

                    Log.d("test", "after edge list");

                }
            }

            //testing
            /*distance = my_json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").
                    getJSONObject(0).getJSONObject("distance").getInt("value");
               */
        }
        catch(Exception e){}

        //String s = "distance: " + "" + distance;
        //Log.d("distance", s);

        for(int i = 0; i < edge_list.size(); ++i)
        {
            String s1 = "" + edge_list.get(i).dist;
            Log.d("distance: ", s1);
        }
        start_pos++;

    }

    public class Edge
    {
        public int A;
        public int B;
        public int dist;
        public Edge()
        {
            A = 0;
            B = 0;
            dist = 0;
        }
        public Edge(int a, int b, int d)
        {
            A = a;
            B = b;
            dist = d;
        }
    }


}
