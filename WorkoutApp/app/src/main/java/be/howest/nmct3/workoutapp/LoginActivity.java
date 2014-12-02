package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;


public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // CHECK IF THERE IS A SHARED PREFERENCE USERNAME
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("USERNAME","");
        if(!(username.equals("")))
        {
            //AL REEDS INGELOGD

            //DOORSTUREN NAAR APP
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            LoginActivity.this.startActivity(myIntent);
        }
        else if(username == null)
        {
            //NOG NIET INGELOGD
        }
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

    public void LoginButton(View v) {

        EditText Username = (EditText) findViewById(R.id.LoginUsernameTextbox);
        EditText Password = (EditText) findViewById(R.id.LoginPasswordTextbox);

        String LoginUsername = Username.getText().toString();
        String LoginPassword = Password.getText().toString();

        Integer ResponseCode = 404;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                HttpGet httpRequest = new HttpGet("http://www.viktordebock.be/mad_backend/loginURL.php?username=" + LoginUsername + "&password=" + LoginPassword);
                HttpEntity httpEntity = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);
                ResponseCode = response.getStatusLine().getStatusCode();
            } catch (ClientProtocolException e) {
                // I keep catching this exception
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Check network");
            }
        }

        ///NIEUX


        if(ResponseCode == 201)
        {
            // USERNANE IN SHARED PREFRENCES ZETTEN
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USERNAME", LoginPassword);
            editor.apply();


            // DOORSTUREN NAAR APP
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            LoginActivity.this.startActivity(myIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "TRY AGAIN",
                    Toast.LENGTH_LONG).show();
        }





    }
}
