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

public class MyMap extends Activity implements MyAsyncResponse{


    ArrayList<Pair<String,String>> coordinates;
    ArrayList<Edge> edge_list;
    ArrayList<Vertex> stores;
    ArrayList<Vertex> points;
    int start_pos;
    int wait_for = 0;

    public MyMap(){

    }

    public ArrayList<Pair<String,String>> callApi(ArrayList<Pair<String,String>> input){

        edge_list = new ArrayList<Edge>();
        coordinates = input;
        stores = new ArrayList<Vertex>();
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
            //destinations
            String destinations = "";
            for( int j = i + 1 ; j < coordinates.size(); ++j) {
                destinations += coordinates.get(j).first + "," + coordinates.get(j).second + "|";
                size = coordinates.size();
                t= "" + size + " "+ coordinates.get(j).first + "," + coordinates.get(j).second;
                Log.d("Coordinates size = inside ", t);

            }


            List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("origins", orig));
            params.add(new BasicNameValuePair("destinations", destinations));
            params.add(new BasicNameValuePair("units","imperial" ));
            params.add(new BasicNameValuePair("mode","driving" ));
            params.add(new BasicNameValuePair("key", "AIzaSyC7bJzIpJkliaoW11SFz_nEcCRi1YxRZHc"));

            String paramString = URLEncodedUtils.format(params, "utf-8");

            url += paramString;
            Log.d("URL =  " , url);
            HttpGet httpGet = new HttpGet(url);

            size = coordinates.size();
            t= "" + size;
            Log.d("Coordinates size = outside ", t);

            wait_for++;
            new MyHttpGet(this).execute(httpGet);

        }//*/

        for (int j = 0; j < 10000; j++){

        }
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e )
        {

        }

        //add
        orderVertices();
        //points is sorted vertices
        ArrayList<Pair<String, String>> coordinates2 = new ArrayList<Pair<String, String>>();
        for(int i = 0; i < points.size(); ++i)
        {
            coordinates2.add(Pair.create( points.get(i).lat, points.get(i).longi) );
            Log.d("coordinates2 are " , i + ""+  points.get(i).lat + "," + points.get(i).longi);
        }
        return coordinates ;
    }


    public void reset()
    {
        stores = new ArrayList<Vertex>();
        coordinates = new ArrayList<Pair<String, String>>();
        edge_list = new ArrayList<Edge>();
        start_pos = 0;
    }

    public void orderVertices()
    {
        //set up list of store vertices
        for(int i = 0; i < coordinates.size(); ++i)
        {
            stores.add(new Vertex(coordinates.get(i).first,coordinates.get(i).second));
        }
        //assign p_level values for each edge by trying all possible paths
        antFunction(0,0);

        //set the correct order for the vertices
        sortVertices();
        //points is the sorted vertices

    }

    public void sortVertices()
    {
        points = new ArrayList<Vertex>();
        sort_helper(0);
    }

    public void sort_helper(int cur_vertex)
    {
        points.add(stores.get(cur_vertex));
        stores.get(cur_vertex).was_visited = true;
        int best_edge = -1;
        double highest_p = 0;
        int best_vertex = -1;
        //find the edge with the highest p_lvl
        for(int i = 0; i < edge_list.size(); ++i)
        {
            if( edge_list.get(i).A == cur_vertex && edge_list.get(i).p_lvl > highest_p
                    && !(stores.get(edge_list.get(i).B).was_visited) )
            {
                best_edge = i;
                highest_p = edge_list.get(i).p_lvl;
                best_vertex = edge_list.get(i).B;
            }

            else if( edge_list.get(i).B == cur_vertex && edge_list.get(i).p_lvl > highest_p
                    && !(stores.get(edge_list.get(i).A).was_visited))
            {
                best_edge = i;
                highest_p = edge_list.get(i).p_lvl;
                best_vertex = edge_list.get(i).A;
            }
        }

        if(best_edge == -1 || best_vertex == -1) return; //done
        else
        {
            sort_helper(best_vertex);
        }

    }

    public void antFunction(int center, int length)
    {
        stores.get(center).was_visited = true;
        //loop through each edge
        for( int i = 0; i < edge_list.size(); ++i)
        {
            //find adjacent edge
            if(edge_list.get(i).A == center && !(stores.get(edge_list.get(i).B).was_visited) )
            {
                length += edge_list.get(i).dist;
                antFunction(edge_list.get(i).B, length);
                edge_list.get(i).p_lvl += 1000.0 / length;
            }
            else if(edge_list.get(i).B == center && !(stores.get(edge_list.get(i).A).was_visited) )
            {
                length += edge_list.get(i).dist;
                antFunction(edge_list.get(i).A, length);
                edge_list.get(i).p_lvl += 1000.0 / length;
            }
        }
        stores.get(center).was_visited = false;
    }
    int j = 1;

    @Override
    public void processFinish(String result){
        Log.d("coordinates size", ""+coordinates.size());

        Log.d("process finish called time : ", ""+j);
        j++;

        JSONObject my_json;
        int distance = 0;
        try {
            my_json = new JSONObject(result);

            //actual
            int obj_pos = 0;
            int A = 0;
            int B = 0;

            Log.d("start_pos" , ""+start_pos);
            Log.d("coordinates size", ""+coordinates.size());
            for(int i = start_pos; i < coordinates.size() - 1; ++i) {
                A = i;

                Log.d("Process finish called ", "for loop CALLED");

                Log.d("edge from", String.valueOf(A));
                Log.d("edge to", ""+ B);
                Log.d("distace from" , ""+distance);

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
                }
            }
        }
        catch(Exception e){}

        for(int i = 0; i < edge_list.size(); ++i)
        {
            String s1 = "" + edge_list.get(i).dist;
        }
        start_pos++;
        wait_for--;
    }

    public class Vertex
    {
        public String lat;
        public String longi;
        public boolean was_visited;

        public Vertex()
        {}

        public Vertex(String latitude, String longitude)
        {
            lat = latitude;
            longi = longitude;
            was_visited = false;
        }
    }

    public class Edge
    {
        public int A;
        public int B;
        public int dist;
        double p_lvl;
        public Edge()
        {
            A = 0;
            B = 0;
            dist = 0;
            p_lvl = 0;
        }
        public Edge(int a, int b, int d)
        {
            A = a;
            B = b;
            dist = d;
            p_lvl = 0;
        }
    }
}


