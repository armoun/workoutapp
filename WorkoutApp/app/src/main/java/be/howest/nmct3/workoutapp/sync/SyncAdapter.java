package be.howest.nmct3.workoutapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;

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
    private static final String FEED_URL_EXERCISES = "https://viktordebock.be/mad_backend/api/exercises/index.php";
    private static final String FEED_URL_WORKOUTS = "http://www.viktordebock.be/mad_backend/api/workoutsbyuser/index.php?username=viktordebock";
    private static final String UPLOAD_URL_WORKOUTS = "http://www.viktordebock.be/mad_backend/add/addworkoutbyusername/index.php?";
    public static final String UPLOAD_URL_WORKOUTEXERCISES = "http://www.viktordebock.be/mad_backend/add/addexercisetoworkout/?";
    public static final String FEED_URL_PLANNER = "http://viktordebock.be/mad_backend/api/plannerworkoutsbyusernameanddate/index.php?username=viktordebock";

    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        //downloadExercises(contentProviderClient, syncResult);
        downloadWorkouts(contentProviderClient, syncResult);
        //uploadWorkouts(contentProviderClient, syncResult);
        downloadPlanner(contentProviderClient, syncResult);
    }

    private void uploadWorkouts(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");

        try {
            final URL location = new URL(UPLOAD_URL_WORKOUTS + "username=X&name=Y");
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {


                    String[] proj = new String[]{Contract.Workouts._ID, Contract.Workouts.NAME, Contract.Workouts.ISPAID};
                    String username = SettingsAdmin.getInstance(getContext()).getUsername();
                    Cursor c = contentProviderClient.query(Contract.Workouts.CONTENT_URI, proj, null, null, null);

                    //c.moveToFirst();

                    Log.d("","Aantal workouts to sync: " + c.getCount());

                    // voor elke workout moet dit gebeuren
                    while(c.moveToNext()){
                        Log.d("","____________________________________________________________________________________________________________________________________________________________");
                        Log.d(TAG,"Trying to SYNC for " + username + ": " + c.getString(c.getColumnIndex(Contract.Workouts._ID)) + "; " + c.getString(c.getColumnIndex(Contract.Workouts.NAME)));

                        URL url = new URL(UPLOAD_URL_WORKOUTS + "username=" + "nielslammens" + "&name=" + c.getString(c.getColumnIndex(Contract.Workouts.NAME)));

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                        connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestProperty("charset", "utf-8");
                        connection.setDoInput(true);

                        connection.connect();

                        Log.d("","_____________________________response code : "+ connection.getResponseCode());
                        //Log.d("","______________CONNECTION: ");
                        InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                        Log.d("","Stream from workout: " + getStringFromInputStream(connection.getInputStream()));

                        connection.disconnect();

                        String wo_id = c.getString(c.getColumnIndex(Contract.Workouts._ID));

                        String[] proj2 = new String[]{Contract.WorkoutExercises._ID, Contract.WorkoutExercises.EXERCISE_ID, Contract.WorkoutExercises.WORKOUT_ID, Contract.WorkoutExercises.REPS};
                        Cursor c2 = contentProviderClient.query(Contract.WorkoutExercises.CONTENT_URI, proj2, Contract.WorkoutExercises.WORKOUT_ID +"=?", new String[]{wo_id}, null);

                        Log.d("","Aantal workoutExercises to sync: " + c2.getCount());

                        while (c2.moveToNext()){
                            Log.d("","------------------------------------------------------------------------------");
                            Log.d(TAG,"Trying to SYNC for wo_id: " + wo_id + ": wo_id " + c2.getString(c2.getColumnIndex(Contract.WorkoutExercises.WORKOUT_ID)) + "; ex_id" + c2.getString(c2.getColumnIndex(Contract.WorkoutExercises.EXERCISE_ID)));

                            URL url2 = new URL(UPLOAD_URL_WORKOUTEXERCISES + "workoutid=" + c2.getString(c2.getColumnIndex(Contract.WorkoutExercises.WORKOUT_ID)) + "&exerciseid=" + c2.getString(c2.getColumnIndex(Contract.WorkoutExercises.EXERCISE_ID)));

                            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                            connection2.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                            connection2.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                            connection2.setRequestMethod("GET");
                            connection2.setRequestProperty("Accept", "application/json");
                            connection2.setRequestProperty("charset", "utf-8");
                            connection2.setDoInput(true);

                            connection2.connect();

                            Log.d("","_____________________________response code : "+ connection2.getResponseCode());
                            //Log.d("","______________CONNECTION: ");
                            InputStreamReader reader2 = new InputStreamReader(connection2.getInputStream());

                            Log.d("","Stream from exercise: " + getStringFromInputStream(connection2.getInputStream()));

                            connection2.disconnect();
                        }
                    }



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

    private void downloadExercises(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL_EXERCISES);
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC Exercises");

                    URL url = new URL(FEED_URL_EXERCISES);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    Log.d("","_____________________________response code : "+ connection.getResponseCode());
                    Log.d("","______________CONNECTION: ");// + getStringFromInputStream(connection.getInputStream()));
                    JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
                    //Log.d("","______________INPUTSTREAM: " + reader.nextName());

                    parseExercises(reader, contentProviderClient);

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

    private void downloadWorkouts(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL_WORKOUTS);
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC WORKOUTS");

                    URL url = new URL(FEED_URL_WORKOUTS);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    Log.d("","_____________________________response code : "+ connection.getResponseCode());
                    //Log.d("","_____________________________CONNECTION: " + getStringFromInputStream(connection.getInputStream()));
                    JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));

                    parseWorkouts(reader, contentProviderClient);

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

    private void downloadPlanner(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL_PLANNER);
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC PLANNER");

                    URL url = new URL(FEED_URL_PLANNER);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    Log.d("","_____________________________response code : "+ connection.getResponseCode());
                    //Log.d("","_____________________________CONNECTION: " + getStringFromInputStream(connection.getInputStream()));
                    JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));

                    parsePlanner(reader, contentProviderClient);

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

    protected void parseExercises(JsonReader reader, ContentProviderClient contentProviderClient) throws Exception {
        int id = -1;
        String name = "";
        String musclegroup = "";
        String target = "";
        String description = "";
        String image = "";


        Log.d("", "______________DECLARED");


        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        Log.d("","______________BEGIN ARRAY");

        while (reader.hasNext()){
            ContentValues values = new ContentValues();

            reader.beginObject();

            reader.nextName();
            id = reader.nextInt();
            values.put(Contract.Exercises._ID, id);

            reader.nextName();
            name = reader.nextString();
            values.put(Contract.Exercises.EXERCISE_NAME, name);

            reader.nextName();
            musclegroup = reader.nextString();
            values.put(Contract.Exercises.MUSCLE_GROUP, musclegroup);

            reader.nextName();
            target = reader.nextString();
            values.put(Contract.Exercises.TARGET, target);

            reader.nextName();
            description = reader.nextString();
            values.put(Contract.Exercises.DESCRIPTION, description);

            reader.nextName();
            image = reader.nextString();
            values.put(Contract.Exercises.IMAGE_NAME, image);

            reader.endObject();

            Log.d("", "CONTENTVALUES FORMED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");

            Uri uri = Contract.Exercises.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Exercises._ID)).build();
            Cursor cursor = contentProviderClient.query(
                    uri,
                    new String[]{Contract.Exercises._ID}, null, null, null);

            if (cursor.getCount() > 0) {
                values.remove(Contract.Exercises._ID);
                contentProviderClient.update(uri, values, null, null);
                Log.d("", "UPDATED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");
            } else {
                contentProviderClient.insert(Contract.Exercises.CONTENT_URI, values);
                Log.d("", "INSERTED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");
            }
        }

        reader.endArray();
    }

    protected void parseWorkouts(JsonReader reader, ContentProviderClient contentProviderClient) throws Exception {
        int id = -1;
        String name = "";
        String owner = "";
        int isPaid = 0;


        Log.d("","______________DECLARED");


        reader.beginObject();   // open {
        reader.nextName();      // workouts
        reader.beginArray();    // open [


        Log.d("","______________BEGIN ARRAY");

        while (reader.hasNext()){
            ContentValues values = new ContentValues();

            reader.beginObject();

            reader.nextName();
            id = reader.nextInt();
            values.put(Contract.Workouts._ID, id);

            reader.nextName();
            name = reader.nextString();
            values.put(Contract.Workouts.NAME, name);

            reader.nextName();
            owner = reader.nextString();

            reader.nextName();
            isPaid = reader.nextInt();
            values.put(Contract.Workouts.ISPAID, isPaid);

            reader.nextName();      // exercises

            reader.beginArray();    // open

            while (reader.hasNext()){
                int e_id = -1;
                int workout_id = -1;
                int exercsise_id = -1;
                String ex_name = "";
                String reps = "";

                ContentValues valuesExercise = new ContentValues();
                reader.beginObject();

                reader.nextName();
                e_id = reader.nextInt();
                valuesExercise.put(Contract.WorkoutExercises._ID, e_id);

                reader.nextName();
                workout_id = reader.nextInt();
                valuesExercise.put(Contract.WorkoutExercises.WORKOUT_ID, workout_id);

                reader.nextName();
                exercsise_id = reader.nextInt();
                valuesExercise.put(Contract.WorkoutExercises.EXERCISE_ID, exercsise_id);

                reader.nextName();
                ex_name = reader.nextString();

                reader.nextName();
                reps = reader.nextString();
                valuesExercise.put(Contract.WorkoutExercises.REPS, reps);

                reader.endObject();

                Log.d("", "CONTENTVALUES FORMED " + e_id + ";" + workout_id + ";" + exercsise_id + ";" + reps);

                Uri uriEx = Contract.WorkoutExercises.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(valuesExercise.getAsString(Contract.WorkoutExercises._ID)).build();
                Cursor cursor = contentProviderClient.query(
                        uriEx,
                        new String[]{Contract.WorkoutExercises._ID}, null, null, null);

                if (cursor.getCount() > 0) {
                    valuesExercise.remove(Contract.WorkoutExercises._ID);
                    contentProviderClient.update(uriEx, valuesExercise, null, null);
                    Log.d("", "UPDATED " + e_id + ";" + workout_id + ";" + exercsise_id + ";" + reps);
                } else {
                    Log.d("","TO INSERT: " + valuesExercise.get(Contract.WorkoutExercises.WORKOUT_ID));
                    contentProviderClient.insert(Contract.WorkoutExercises.CONTENT_URI, valuesExercise);
                    Log.d("", "INSERTED " + e_id + ";" + workout_id + ";" + exercsise_id + ";" + reps);
                }
            }

            reader.endArray();

            reader.endObject();

            Log.d("", "CONTENTVALUES FORMED " + id + ";" + name + ";" + isPaid);

            Uri uriWorkout = Contract.Workouts.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Workouts._ID)).build();
            Cursor cursor = contentProviderClient.query(
                    uriWorkout,
                    new String[]{Contract.Workouts._ID}, null, null, null);

            if (cursor.getCount() > 0) {
                values.remove(Contract.Workouts._ID);
                contentProviderClient.update(uriWorkout, values, null, null);
                Log.d("", "UPDATED " + id + ";" + name + ";" + isPaid);
            } else {
                contentProviderClient.insert(Contract.Workouts.CONTENT_URI, values);
                Log.d("", "INSERTED " + id + ";" + name + ";" + isPaid);
            }
        }

        reader.endArray();
    }

    protected void parsePlanner(JsonReader reader, ContentProviderClient contentProviderClient) throws Exception {
        int id = -1;
        String wo_date = "";
        String username = "";
        int workout_id = 0;


        Log.d("","______________DECLARED");


        reader.beginObject();   // open {
        reader.nextName();      // workouts
        reader.beginArray();    // open [


        Log.d("","______________BEGIN ARRAY");

        while (reader.hasNext()){
            ContentValues values = new ContentValues();

            reader.beginObject();

            reader.nextName();
            id = reader.nextInt();
            values.put(Contract.Planners._ID, id);

            reader.nextName();
            wo_date = reader.nextString();
            values.put(Contract.Planners.WO_DATE, wo_date);

            reader.nextName();
            username = reader.nextString();

            reader.nextName();
            workout_id = reader.nextInt();
            values.put(Contract.Planners.WORKOUT_ID, workout_id);

            reader.endObject();

            Log.d("", "CONTENTVALUES FORMED " + id + ";" + workout_id + ";" + wo_date);

            Uri uriPlanner = Contract.Planners.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Planners._ID)).build();
            Cursor cursor = contentProviderClient.query(
                    uriPlanner,
                    new String[]{Contract.Planners._ID}, null, null, null);

            if (cursor.getCount() > 0) {
                values.remove(Contract.Planners._ID);
                contentProviderClient.update(uriPlanner, values, null, null);
                Log.d("", "UPDATED " + id + ";" + workout_id + ";" + wo_date);
            } else {
                contentProviderClient.insert(Contract.Planners.CONTENT_URI, values);
                Log.d("", "INSERTED " + id + ";" + workout_id + ";" + wo_date);
            }
        }

        reader.endArray();
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
