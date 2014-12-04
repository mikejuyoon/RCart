package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Login extends Activity implements MyAsyncResponse{

    private final static String SHARED_PREFERENCES_NAME = "com.mobico.rcart.savedData";
    public static SharedPreferences savedData;

    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Opens up SharedPreferences
        savedData = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        setupKeyboardHide(findViewById(R.id.LoginLayout));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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


    public void loginButtonPress(View view) {

        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        //Hides keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputEmail.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(inputPassword.getWindowToken(), 0);

        postLoginData(inputEmail.getText().toString(), inputPassword.getText().toString());
    }

    private void postLoginData(String inputEmail, String inputPassword) {

        HttpPost httppost = new HttpPost("https://mobibuddy.herokuapp.com/users/sign_in.json");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", inputEmail));
            nameValuePairs.add(new BasicNameValuePair("password", inputPassword));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            if(isConnected()){
                //Toast.makeText(getBaseContext(), "CONNECTED", Toast.LENGTH_LONG).show();
                new MyHttpPost(this).execute(httppost);
            }
            else{
                Toast.makeText(getBaseContext(), "NOT CONNECTED!", Toast.LENGTH_LONG).show();
            }
            //new MyHttpPost().execute(httppost);

        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void invalidEntryAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        builder.setTitle("Invalid Username or Password");
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    public void openSignupActivity(View view){
        Intent i = new Intent(Login.this, Signup.class);
        startActivity(i);
        //finish();
    }

    private void setupKeyboardHide(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupKeyboardHide(innerView);
            }
        }
    }
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void processFinish(String result) {
        try{
            JSONObject jsonLoginResult = new JSONObject(result);
            if( jsonLoginResult.getBoolean("success") ){
                // Saves received "auth_token" in SharedPreferences
                SharedPreferences.Editor preferencesEditor = savedData.edit();
                preferencesEditor.putString("email", jsonLoginResult.getJSONObject("user").getString("email"));
                preferencesEditor.putString("auth_token", jsonLoginResult.getJSONObject("user").getString("auth_token"));
                preferencesEditor.apply();

                // Clears the input EditTexts
                inputEmail.setText(null);
                inputPassword.setText(null);

                Toast.makeText(getBaseContext(), "Signed in!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            else{
                invalidEntryAlert("Invalid username or password. Please try again.");
            }
        }
        catch(JSONException e){}
    }
}
