package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import be.howest.nmct3.workoutapp.data.SettingsAdmin;


public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        getActionBar().hide();

        EditText usernamebox = (EditText) findViewById(R.id.LoginUsernameTextbox);
        EditText passwordbox = (EditText) findViewById(R.id.LoginPasswordTextbox);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        usernamebox.setFilters(new InputFilter[] { filter });
        passwordbox.setFilters(new InputFilter[] { filter });

        // CHECK IF THERE IS A SHARED PREFERENCE USERNAME
        String username = SettingsAdmin.getInstance(getApplicationContext()).getUsername();
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

        if(SettingsAdmin.getInstance(getApplicationContext()).isNetworkAvailable(getApplicationContext()) == true)
        {
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

            if(ResponseCode == 201)
            {
                // USERNANE IN SHARED PREFRENCES ZETTEN
                Log.d("USERNAMEEEEEE: ", LoginUsername);
                SettingsAdmin.getInstance(getApplicationContext()).setUsername(LoginUsername);


                // DOORSTUREN NAAR APP
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                LoginActivity.this.startActivity(myIntent);
                finish();
            }
            else
            {
                AlertDialog.Builder builder  = new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Username and/or password not correct.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();
                            }
                        });

                //.show();

                //The tricky part
                Dialog d = builder.show();
                int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(getResources().getColor(R.color.headcolor));

                int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(getResources().getColor(R.color.headcolor));
            }

        }
        else
        {
            AlertDialog.Builder builder  = new AlertDialog.Builder(this)
                    .setTitle("Connection error")
                    .setMessage("Please connect to the internet and try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.cancel();
                        }
                    });

                    //.show();

            //The tricky part
            Dialog d = builder.show();
            int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = d.findViewById(dividerId);
            divider.setBackgroundColor(getResources().getColor(R.color.headcolor));

            int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
            TextView tv = (TextView) d.findViewById(textViewId);
            tv.setTextColor(getResources().getColor(R.color.headcolor));
        }

    }



    public void goToRegister(View v)
    {
        // DOORSTUREN NAAR REGISTER
        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        LoginActivity.this.startActivity(myIntent);
        finish();
    }

    public void LoginGuestButton(View v)
    {
        // LEGE USERNANE IN SHARED PREFRENCES ZETTEN
        SettingsAdmin.getInstance(getApplicationContext()).setUsername("");
        SettingsAdmin.getInstance(getApplicationContext()).setFirstname("GUEST");
        SettingsAdmin.getInstance(getApplicationContext()).setLastname("");

        // DOORSTUREN NAAR APP
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        LoginActivity.this.startActivity(myIntent);
        finish();
    }

}
