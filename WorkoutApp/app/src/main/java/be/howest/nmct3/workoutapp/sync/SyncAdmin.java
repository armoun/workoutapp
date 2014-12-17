package be.howest.nmct3.workoutapp.sync;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nielslammens on 10/12/14.
 */
public class SyncAdmin {
    public static SyncAdmin INSTANCE;
    public static Object lock = new Object();

    private Context mContext;

    private String KEY_ARE_WORKOUTS_SYNCED = "sync.areworkoutssynced";
    private String KEY_ARE_EXERCISES_SYNCED = "sync.areexercisesynced";

    private String KEY_SHAREDPREFS = "be.howest.nmct3.workoutapp.syncprefs";

    private SharedPreferences prefs;

    public SyncAdmin(Context context){
        mContext = context;
        prefs = mContext.getSharedPreferences(KEY_SHAREDPREFS, Context.MODE_PRIVATE);
    }

    public static SyncAdmin getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new SyncAdmin(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    //workouts
    // download
    public void setAllowWorkoutsDownload(boolean value){
        prefs.edit().putBoolean(KEY_ARE_WORKOUTS_SYNCED, value).apply();
    }

    public boolean getAllowWorkoutsDownload(){
        boolean bool = prefs.getBoolean(KEY_ARE_WORKOUTS_SYNCED,false);
        return bool;
    }

    //  upload
    public void setAllowWorkoutsUpload(boolean value){
        prefs.edit().putBoolean(KEY_ARE_WORKOUTS_SYNCED, value).apply();
    }

    public boolean getAllowWorkoutsUpload(){
        boolean bool = prefs.getBoolean(KEY_ARE_WORKOUTS_SYNCED,false);
        return bool;
    }


    //planners
    //  download
    public void setAllowPlannersDownload(boolean value){
        prefs.edit().putBoolean(KEY_ARE_WORKOUTS_SYNCED, value).apply();
    }

    public boolean getAllowPlannersDownload(){
        boolean bool = prefs.getBoolean(KEY_ARE_WORKOUTS_SYNCED,false);
        return bool;
    }

    //  upload
    public void setAllowPlannersUpload(boolean value){
        prefs.edit().putBoolean(KEY_ARE_WORKOUTS_SYNCED, value).apply();
    }

    public boolean getAllowPlannersUpload(){
        boolean bool = prefs.getBoolean(KEY_ARE_WORKOUTS_SYNCED,false);
        return bool;
    }


    //exercises
    //  download
    public void setAllowExercisesDownload(boolean value){
        prefs.edit().putBoolean(KEY_ARE_EXERCISES_SYNCED, value).apply();
    }

    public boolean getAllowExercisesDownload(){
        boolean bool = prefs.getBoolean(KEY_ARE_EXERCISES_SYNCED,false);
        return bool;
    }
}
