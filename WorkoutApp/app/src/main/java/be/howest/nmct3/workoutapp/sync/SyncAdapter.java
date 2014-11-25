package be.howest.nmct3.workoutapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.JsonReader;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by nielslammens on 19/11/14.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    public static final String TAG = "";
    private static final String FEED_URL = "https://www.viktordebock.be/mad_backend/api/workoutsbyuser/index.php?username=viktordebock";
    //private static final String FEED_URL = "http://ip.jsontest.com/";
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL);
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC WORKOUTS");

                    URL url = new URL(FEED_URL);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    Log.d("","_____________________________response code : "+ connection.getResponseCode());
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    Log.d("","______________STREAM: " + getStringFromInputStream(connection.getInputStream()));
                    JsonReader reader = new JsonReader(isr);
                    reader.beginArray();
                    while (reader.hasNext()){
                        Log.d("","" + reader.toString());
                    }
                    reader.endArray();
                    isr.close();
                    connection.disconnect();
                } catch (Exception e){
                    e.printStackTrace();
                }


            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.d(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.d(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        }
        Log.d(TAG, "Network synchronization complete");
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
