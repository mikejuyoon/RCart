package com.mobico.rcart;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Signup extends Activity implements MyAsyncResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupKeyboardHide(findViewById(R.id.SignupLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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

    public void signupNewAccount(View view){
        EditText inputNewEmail = (EditText) findViewById(R.id.inputNewEmail);
        EditText inputNewPassword1 = (EditText) findViewById(R.id.inputNewPassword1);
        EditText inputNewPassword2 = (EditText) findViewById(R.id.inputNewPassword2);

        if( inputNewPassword1.getText().toString().equals(inputNewPassword2.getText().toString()) ){
            postSignupData(inputNewEmail.getText().toString(), inputNewPassword1.getText().toString());
        }else{
            invalidEntryAlert("Passwords do not match.");
        }
    }

            private void postSignupData(String inputEmail, String inputPassword) {

            HttpPost httppost = new HttpPost("https://mobibuddy.herokuapp.com/users.json");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("email", inputEmail));
                nameValuePairs.add(new BasicNameValuePair("password", inputPassword));
                nameValuePairs.add(new BasicNameValuePair("password_confirmation", inputPassword));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            if(isConnected()){
                Toast.makeText(getBaseContext(), "CONNECTED", Toast.LENGTH_LONG).show();
                new MyHttpPost(this).execute(httppost);
            }
            else{
                Toast.makeText(getBaseContext(), "NOT CONNECTED!", Toast.LENGTH_LONG).show();
            }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);

        builder.setTitle("Error");
        builder.setPositiveButton("OK", null);
        builder.setMessage(message);

        AlertDialog theAlertDialog = builder.create();
        theAlertDialog.show();
    }

    

    private void setupKeyboardHide(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Signup.this);
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
        try {
            JSONObject jsonLoginResult = new JSONObject(result);
            if (jsonLoginResult.getBoolean("success")) {
                //invalidEntryAlert("Successfully Signed Up! Please Login");
                Toast.makeText(getBaseContext(), "Signed Up! Please Login.", Toast.LENGTH_LONG).show();
                onBackPressed();
            } else {
                invalidEntryAlert("Email has already been used or password must be longer than 8 characters.");
            }
        }catch (JSONException e) {}
    }
}
