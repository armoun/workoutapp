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

    private String KEY_ALLOW_DOWNLOAD_EXERCISES = "sync.allowdownloadexercises";

    private String KEY_ALLOW_UPLOAD_WORKOUTS = "sync.allowuploadworkouts";
    private String KEY_ALLOW_DOWNLOAD_WORKOUTS = "sync.allowdownloadworkouts";

    private String KEY_ALLOW_UPLOAD_PLANNERS = "sync.allowuploadplaners";
    private String KEY_ALLOW_DOWNLOAD_PLANNERS = "sync.allowdownloadplanner";

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
        prefs.edit().putBoolean(KEY_ALLOW_DOWNLOAD_WORKOUTS, value).apply();
    }

    public boolean getAllowWorkoutsDownload(){
        boolean bool = prefs.getBoolean(KEY_ALLOW_DOWNLOAD_WORKOUTS,false);
        return bool;
    }

    //  upload
    public void setAllowWorkoutsUpload(boolean value){
        prefs.edit().putBoolean(KEY_ALLOW_UPLOAD_WORKOUTS, value).apply();
    }

    public boolean getAllowWorkoutsUpload(){
        boolean bool = prefs.getBoolean(KEY_ALLOW_UPLOAD_WORKOUTS,false);
        return bool;
    }


    //planners
    //  download
    public void setAllowPlannersDownload(boolean value){
        prefs.edit().putBoolean(KEY_ALLOW_DOWNLOAD_PLANNERS, value).apply();
    }

    public boolean getAllowPlannersDownload(){
        boolean bool = prefs.getBoolean(KEY_ALLOW_DOWNLOAD_PLANNERS,false);
        return bool;
    }

    //  upload
    public void setAllowPlannersUpload(boolean value){
        prefs.edit().putBoolean(KEY_ALLOW_UPLOAD_PLANNERS, value).apply();
    }

    public boolean getAllowPlannersUpload(){
        boolean bool = prefs.getBoolean(KEY_ALLOW_UPLOAD_PLANNERS,false);
        return bool;
    }


    //exercises
    //  download
    public void setAllowExercisesDownload(boolean value){
        prefs.edit().putBoolean(KEY_ALLOW_DOWNLOAD_EXERCISES, value).apply();
    }

    public boolean getAllowExercisesDownload(){
        boolean bool = prefs.getBoolean(KEY_ALLOW_DOWNLOAD_EXERCISES,false);
        return bool;
    }
}
