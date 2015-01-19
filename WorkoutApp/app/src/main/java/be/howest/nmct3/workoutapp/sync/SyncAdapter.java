package be.howest.nmct3.workoutapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import be.howest.nmct3.workoutapp.data.Contract;
import be.howest.nmct3.workoutapp.data.SettingsAdmin;

/**
 * Created by nielslammens on 19/11/14.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private final ContentResolver mContentResolver;
    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs){
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public static final String TAG = "";
    private static final String FEED_URL_EXERCISES = "https://viktordebock.be/mad_backend/api/exercises/index.php";
    private static final String FEED_URL_WORKOUTS = "http://www.viktordebock.be/mad_backend/api/workoutsbyuser/index.php?username=";
    private static final String UPLOAD_URL_WORKOUTS = "http://www.viktordebock.be/mad_backend/add/addworkoutbyusername/index.php?";
    public static final String UPLOAD_URL_WORKOUTEXERCISES = "http://www.viktordebock.be/mad_backend/add/addexercisetoworkout/?";
    public static final String FEED_URL_PLANNER = "http://viktordebock.be/mad_backend/api/plannerworkoutsbyusernameanddate/index.php?username=";
    public static final String UPLOAD_URL_PLANNER = "http://www.viktordebock.be/mad_backend/add/addplannerworkoutbyusernameandworkoutidanddate/?";
    public static final String DELETE_URL_WORKOUTS = "http://www.viktordebock.be/mad_backend/delete/deleteworkout/index.php?";
    public static final String DELETE_URL_PLANNER = "http://www.viktordebock.be/mad_backend/delete/deleteplannerworkout/index.php?";

    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        //  exercises
        if(SyncAdmin.getInstance(mContext).getAllowExercisesDownload()){
            if(SettingsAdmin.isNetworkAvailable(getContext())){
                downloadExercises(contentProviderClient, syncResult);
                SyncAdmin.getInstance(mContext).setAllowExercisesDownload(false);
            }
        }

        //  workouts
        if (SyncAdmin.getInstance(mContext).getAllowWorkoutsDownload()){
            if(SettingsAdmin.isNetworkAvailable(getContext())){
                downloadWorkouts(contentProviderClient, syncResult);
                SyncAdmin.getInstance(mContext).setAllowWorkoutsDownload(false);
            }

        }

        if(SyncAdmin.getInstance(mContext).getAllowWorkoutsUpload()){
            if(SettingsAdmin.isNetworkAvailable(getContext())){
                uploadWorkouts(contentProviderClient, syncResult);
                SyncAdmin.getInstance(mContext).setAllowWorkoutsUpload(false);
            }

        }

        //  planner
        if(SyncAdmin.getInstance(mContext).getAllowPlannersDownload()){
            if(SettingsAdmin.isNetworkAvailable(getContext())){
                downloadPlanner(contentProviderClient, syncResult);
                SyncAdmin.getInstance(mContext).setAllowPlannersUpload(false);
            }

        }

        if(SyncAdmin.getInstance(mContext).getAllowPlannersUpload()){
            if(SettingsAdmin.isNetworkAvailable(getContext())){
                uploadPlanner(contentProviderClient, syncResult);
                SyncAdmin.getInstance(mContext).setAllowPlannersUpload(false);
            }

        }

        //downloadPlanner(contentProviderClient, syncResult);
        //uploadPlanner(contentProviderClient, syncResult);
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


                    String[] proj = new String[]{Contract.Workouts._ID, Contract.Workouts.NAME, Contract.Workouts.ISPAID, Contract.Workouts.USERNAME, Contract.Workouts.DELETE};
                    String username = SettingsAdmin.getInstance(getContext()).getUsername();
                    Cursor c = contentProviderClient.query(Contract.Workouts.CONTENT_URI, proj, Contract.Workouts.USERNAME + "=?", new String[]{username}, null);

                    //c.moveToFirst();

                    Log.d("","Aantal workouts to sync: " + c.getCount());

                    // voor elke workouts moet dit gebeuren
                    while(c.moveToNext()){
                        if(c.getInt(c.getColumnIndex(Contract.Workouts.DELETE)) == 0){
                            Log.d("","____________________________________________________________________________________________________________________________________________________________");
                            Log.d(TAG,"Trying to SYNC for " + username + ": " + c.getString(c.getColumnIndex(Contract.Workouts._ID)) + "; " + c.getString(c.getColumnIndex(Contract.Workouts.NAME)));

                            URL url = new URL(UPLOAD_URL_WORKOUTS + "username=" + username + "&name=" + URLEncoder.encode(c.getString(c.getColumnIndex(Contract.Workouts.NAME)),"UTF-8"));

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                            connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("charset", "utf-8");
                            connection.setDoInput(true);

                            connection.connect();

                            Log.d("","_____________________________response code : "+ connection.getResponseCode());

                            InputStreamReader reader = new InputStreamReader(connection.getInputStream());

                            Log.d("","Stream from workouts: " + getStringFromInputStream(connection.getInputStream()));

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

                                InputStreamReader reader2 = new InputStreamReader(connection2.getInputStream());

                                Log.d("","Stream from exercise: " + getStringFromInputStream(connection2.getInputStream()));

                                connection2.disconnect();
                            }
                        }else{
                            URL url = new URL(DELETE_URL_WORKOUTS + "username=" + username + "&workoutname=" + URLEncoder.encode(c.getString(c.getColumnIndex(Contract.Workouts.NAME)),"UTF-8"));

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                            connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("charset", "utf-8");
                            connection.setDoInput(true);

                            connection.connect();

                            Log.d("","_____________________________response code : "+ connection.getResponseCode());
                            Log.d("","Stream from workouts: " + getStringFromInputStream(connection.getInputStream()));

                            connection.disconnect();


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

    private void uploadPlanner(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "On perform sync is called");
        Log.d(TAG, "Beginning network synchronization");

        String username = SettingsAdmin.getInstance(getContext()).getUsername();

        try {
            final URL location = new URL(UPLOAD_URL_PLANNER + "username=" + username + "&workoutid=Y&date=Z");
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {

                    String[] proj = new String[]{Contract.Planners._ID, Contract.Planners.WO_DATE, Contract.Planners.WORKOUT_ID, Contract.Planners.USERNAME, Contract.Planners.DELETE};
                    Cursor c = contentProviderClient.query(Contract.Planners.CONTENT_URI, proj, Contract.Planners.USERNAME + "=?", new String[]{username}, null);

                    Log.d("","Aantal planners to sync: " + c.getCount());

                    while(c.moveToNext()){
                        Cursor wn = contentProviderClient.query(Contract.Workouts.CONTENT_URI, new String[]{Contract.Workouts.NAME}, Contract.Workouts._ID + "=?", new String[]{c.getString(c.getColumnIndex(Contract.Planners.WORKOUT_ID))}, null);

                        Log.d("upload planner", DatabaseUtils.dumpCursorToString(wn));
                        wn.moveToFirst();
                        String workoutname = wn.getString(0);

                        if(c.getInt(c.getColumnIndex(Contract.Planners.DELETE)) == 0){

                            Log.d("","____________________________________________________________________________________________________________________________________________________________");
                            Log.d(TAG,"Trying to SYNC for " + username + ": " + c.getString(c.getColumnIndex(Contract.Planners._ID)) + "; " + c.getString(c.getColumnIndex(Contract.Planners.WO_DATE)));

                            URL url = new URL(UPLOAD_URL_PLANNER
                                    + "username=" + username
                                    + "&workoutname=" + URLEncoder.encode(workoutname, "UTF-8")
                                    + "&date=" + c.getString(c.getColumnIndex(Contract.Planners.WO_DATE)));

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

                            Log.d("","Stream from planner: " + getStringFromInputStream(connection.getInputStream()));

                            connection.disconnect();
                        }else {

                            Log.d("","____________________________________________________________________________________________________________________________________________________________");
                            Log.d(TAG,"Trying to SYNC for " + username + ": " + c.getString(c.getColumnIndex(Contract.Planners._ID)) + "; " + c.getString(c.getColumnIndex(Contract.Planners.WO_DATE)));

                            URL url = new URL(DELETE_URL_PLANNER
                                    + "username=" + username
                                    + "&workoutname=" + URLEncoder.encode(workoutname, "UTF-8")
                                    + "&date=" + c.getString(c.getColumnIndex(Contract.Planners.WO_DATE)));

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                            connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                            connection.setRequestMethod("GET");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("charset", "utf-8");
                            connection.setDoInput(true);

                            connection.connect();

                            Log.d("","_____________________________response code : "+ connection.getResponseCode());
                            Log.d("","Stream from planner: " + getStringFromInputStream(connection.getInputStream()));

                            connection.disconnect();

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
        //Log.d(TAG, "On perform sync is called");
        //Log.d(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL_EXERCISES);
            InputStream stream = null;

            try {
                //Log.d(TAG, "Streaming data from network: " + location);


                try {
                    //Log.d(TAG,"Trying to SYNC Exercises");

                    URL url = new URL(FEED_URL_EXERCISES);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    //Log.d("","_____________________________response code : "+ connection.getResponseCode());
                    //Log.d("","______________CONNECTION: ");// + getStringFromInputStream(connection.getInputStream()));
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
            //Log.d(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            //Log.d(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        }
        //Log.d(TAG, "Network synchronization complete");
    }

    private void downloadWorkouts(ContentProviderClient contentProviderClient, SyncResult syncResult){
        Log.d(TAG, "Beginning network synchronization for WORKOUTS");
        try {
            String username = SettingsAdmin.getInstance(getContext()).getUsername();
            final URL location = new URL(FEED_URL_WORKOUTS + username);
            InputStream stream = null;

            try {
                //Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC WORKOUTS");

                    URL url = new URL(FEED_URL_WORKOUTS + username);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
                    connection.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("charset", "utf-8");
                    connection.setDoInput(true);

                    connection.connect();

                    Log.d("","_____________________________response code : "+ connection.getResponseCode());

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
            String username = SettingsAdmin.getInstance(getContext()).getUsername();
            final URL location = new URL(FEED_URL_PLANNER + username);
            InputStream stream = null;

            try {
                Log.d(TAG, "Streaming data from network: " + location);


                try {
                    Log.d(TAG,"Trying to SYNC PLANNER");

                    URL url = new URL(FEED_URL_PLANNER + username);

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


        //Log.d("", "______________DECLARED");


        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        //Log.d("","______________BEGIN ARRAY");

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

            String path = saveImageToSD(image, name);

            values.put(Contract.Exercises.IMAGE_NAME, path);

            reader.endObject();

            //Log.d("", "CONTENTVALUES FORMED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");

            Uri uri = Contract.Exercises.ITEM_CONTENT_URI.buildUpon().appendEncodedPath(values.getAsString(Contract.Exercises._ID)).build();
            Cursor cursor = contentProviderClient.query(
                    uri,
                    new String[]{Contract.Exercises._ID}, null, null, null);

            if (cursor.getCount() > 0) {
                values.remove(Contract.Exercises._ID);
                contentProviderClient.update(uri, values, null, null);
                //Log.d("", "UPDATED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");
            } else {
                contentProviderClient.insert(Contract.Exercises.CONTENT_URI, values);
                //Log.d("", "INSERTED " + id + ";" + name + ";" + musclegroup + ";" + target + ";" + description + ";" + image + "__________________________________________________");
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
            //values.put(Contract.Workouts._ID, id);

            reader.nextName();
            name = reader.nextString();
            values.put(Contract.Workouts.NAME, name);

            reader.nextName();
            owner = reader.nextString();
            values.put(Contract.Workouts.USERNAME, owner);

            reader.nextName();
            isPaid = reader.nextInt();
            values.put(Contract.Workouts.ISPAID, isPaid);

            Cursor cursor = contentProviderClient.query(
                    Contract.Workouts.CONTENT_URI,
                    new String[]{Contract.Workouts._ID},
                    Contract.Workouts.NAME + "=? AND " + Contract.Workouts.USERNAME + "=?",
                    new String[]{name, owner},
                    null);
            //Log.d("SyncAdapter",DatabaseUtils.dumpCursorToString(cursor));
            cursor.moveToFirst();
            String workout_id_l = "";

            if (cursor.getCount() > 0) {
                //values.remove(Contract.Workouts._ID);
                workout_id_l = cursor.getString(cursor.getColumnIndex(Contract.Workouts._ID));
                contentProviderClient.update(Contract.Workouts.CONTENT_URI, values, Contract.Workouts._ID + "=?", new String[]{workout_id_l});
                //Log.d("", "UPDATED " + id + ";" + name + ";" + isPaid);
            } else {
                Uri idUri = contentProviderClient.insert(Contract.Workouts.CONTENT_URI, values);
                //Log.d("SyncAdapter Inserted",idUri.toString());
                Cursor w = contentProviderClient.query(idUri,new String[]{Contract.Workouts._ID},null,null,null);
                w.moveToFirst();
                //Log.d("SyncAdapter Inserted",DatabaseUtils.dumpCursorToString(w));
                workout_id_l = w.getString(w.getColumnIndex(Contract.Workouts._ID));
                //Log.d("", "INSERTED " + id + ";" + name + ";" + isPaid);
            }

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
                //valuesExercise.put(Contract.WorkoutExercises._ID, e_id);

                reader.nextName();
                workout_id = reader.nextInt();
                //valuesExercise.put(Contract.WorkoutExercises.WORKOUT_ID, workout_id);

                reader.nextName();
                exercsise_id = reader.nextInt();
                //valuesExercise.put(Contract.WorkoutExercises.EXERCISE_ID, exercsise_id);

                reader.nextName();
                ex_name = reader.nextString();

                reader.nextName();
                reps = reader.nextString();
                valuesExercise.put(Contract.WorkoutExercises.REPS, reps);

                reader.endObject();

                Cursor c_e = contentProviderClient.query(
                        Contract.Exercises.CONTENT_URI,
                        new String[]{Contract.Exercises._ID},
                        Contract.Exercises.EXERCISE_NAME + "=?",
                        new String[]{ex_name},
                        null);
                c_e.moveToFirst();
                String ex_id_l = c_e.getString(c_e.getColumnIndex(Contract.Exercises._ID));

                Log.d("SyncAdapter Exercises",DatabaseUtils.dumpCursorToString(c_e));

                Cursor cursor_we = contentProviderClient.query(
                        Contract.WorkoutExercises.CONTENT_URI,
                        new String[]{Contract.WorkoutExercises._ID},
                        Contract.WorkoutExercises.WORKOUT_ID + "=? AND " + Contract.WorkoutExercises.EXERCISE_ID + "=?",
                        new String[]{workout_id_l, ex_id_l},
                        null
                );
                cursor_we.moveToFirst();

                Log.d("SyncAdapter WorkoutExercises",workout_id_l + " " + ex_id_l + " " + DatabaseUtils.dumpCursorToString(cursor_we));

                valuesExercise.put(Contract.WorkoutExercises.WORKOUT_ID, workout_id_l);
                valuesExercise.put(Contract.WorkoutExercises.EXERCISE_ID, ex_id_l);

                if (cursor_we.getCount() > 0) {
                    String workoutExerciseId = cursor_we.getString(cursor_we.getColumnIndex(Contract.WorkoutExercises._ID));
                    contentProviderClient.update(Contract.WorkoutExercises.CONTENT_URI, valuesExercise, Contract.WorkoutExercises._ID + "=?", new String[]{workoutExerciseId});
                    Log.d("SyncAdapter WorkoutExercises", "UPDATED ");
                } else {
                    //Log.d("","TO INSERT: " + valuesExercise.get(Contract.WorkoutExercises.WORKOUT_ID));
                    Uri u = contentProviderClient.insert(Contract.WorkoutExercises.CONTENT_URI, valuesExercise);
                    Log.d("SyncAdapter WorkoutExercises", "INSERTED ");
                }
            }

            reader.endArray();

            reader.endObject();
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
            values.put(Contract.Workouts.USERNAME, username);

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

    private String saveImageToSD(String filepath, String exName){
        try
        {
            URL url = new URL(filepath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            String filename=exName;
            Log.i("Local filename:",""+filename);
            File file = new File(SDCardRoot,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if(downloadedSize==totalSize) filepath=file.getPath();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            filepath=null;
            e.printStackTrace();
        }
        Log.i("filepath:"," "+filepath) ;
        return filepath;
    }
}
