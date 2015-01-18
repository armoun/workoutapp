package be.howest.nmct3.workoutapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import be.howest.nmct3.workoutapp.data.SettingsAdmin;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        EditText FirstName = (EditText) findViewById(R.id.RegisterFirstNameTextbox);
        EditText LastName = (EditText) findViewById(R.id.RegisterLastNameTextbox);
        EditText Username = (EditText) findViewById(R.id.RegisterUsernameTextbox);
        EditText Password = (EditText) findViewById(R.id.RegisterPasswordTextbox);

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

        Username.setFilters(new InputFilter[] { filter });
        Password.setFilters(new InputFilter[] { filter });

        getActionBar().hide();
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

        if(SettingsAdmin.getInstance(getApplicationContext()).isNetworkAvailable(getApplicationContext()) == true)
        {
            EditText FirstName = (EditText) findViewById(R.id.RegisterFirstNameTextbox);
            EditText LastName = (EditText) findViewById(R.id.RegisterLastNameTextbox);
            EditText Username = (EditText) findViewById(R.id.RegisterUsernameTextbox);
            EditText Password = (EditText) findViewById(R.id.RegisterPasswordTextbox);

            String RegisterFirstName = FirstName.getText().toString();
            String RegisterLastName = LastName.getText().toString();
            String RegisterUsername = Username.getText().toString();
            String RegisterPassword = Password.getText().toString();

            String RegisterFirstNameURL = RegisterFirstName.replace(" ", "%20");
            String RegisterLastNameURL = RegisterLastName.replace(" ", "%20");

            Integer ResponseCode = 404;

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    HttpGet httpRequest = new HttpGet("http://www.viktordebock.be/mad_backend/registerURL.php?firstname=" + RegisterFirstNameURL + "&lastname=" + RegisterLastNameURL + "&username=" + RegisterUsername + "&password=" + RegisterPassword);
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
                SettingsAdmin.getInstance(getApplicationContext()).setUsername(RegisterUsername);
                SettingsAdmin.getInstance(getApplicationContext()).setFirstname(RegisterFirstName);
                SettingsAdmin.getInstance(getApplicationContext()).setLastname(RegisterLastName);

                // DOORSTUREN NAAR APP
                Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                RegisterActivity.this.startActivity(myIntent);
                finish();

            }
            else
            {
                AlertDialog.Builder builder  = new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Try again.")
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

    public void goToLogin(View v)
    {
        // DOORSTUREN NAAR LOGIN
        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        RegisterActivity.this.startActivity(myIntent);
        finish();
    }

}
