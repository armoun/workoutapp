package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
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

    public void RegisterButton(View v) {


        EditText FirstName = (EditText) findViewById(R.id.RegisterFirstNameTextbox);
        EditText LastName = (EditText) findViewById(R.id.RegisterLastNameTextbox);
        EditText Username = (EditText) findViewById(R.id.RegisterUsernameTextbox);
        EditText Password = (EditText) findViewById(R.id.RegisterPasswordTextbox);

        String RegisterFirstName = FirstName.getText().toString();
        String RegisterLastName = LastName.getText().toString();
        String RegisterUsername = Username.getText().toString();
        String RegisterPassword = Password.getText().toString();

        Integer ResponseCode = 404;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                HttpGet httpRequest = new HttpGet("http://www.viktordebock.be/mad_backend/registerURL.php?firstname="+ RegisterFirstName +"&lastname=" + RegisterLastName + "&username="+ RegisterUsername +"&password=" + RegisterPassword);
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

        if (ResponseCode == 201) {
            // USERNANE IN SHARED PREFRENCES ZETTEN
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USERNAME", RegisterUsername);
            editor.putString("FIRSTNAME", RegisterFirstName);
            editor.putString("LASTNAME", RegisterLastName);
            editor.apply();


            // DOORSTUREN NAAR APP
            Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            RegisterActivity.this.startActivity(myIntent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "TRY AGAIN",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void goToLogin(View v)
    {
        // DOORSTUREN NAAR LOGIN
        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        RegisterActivity.this.startActivity(myIntent);
        finish();
    }

}
