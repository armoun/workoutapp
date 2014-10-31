package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nielslammens on 31/10/14.
 */
public class SettingsAdmin {

    public static SettingsAdmin INSTANCE;
    public static Object lock = new Object();

    private Context mContext;

    private String KEY_NAME = "settings.name";
    private String KEY_GENDER = "settings.gender";
    private String KEY_DATE_OF_BIRTH = "settings.dob";
    private String KEY_EMAIL = "settings.email";
    private String KEY_PICTURE = "settings.picture";
    private String KEY_UNITS = "settings.units";

    private String KEY_SHAREDPREFS = "be.howest.nmct3.workoutapp.prefs";

    private SharedPreferences prefs;

    public SettingsAdmin(Context context){
        mContext = context;
        prefs = mContext.getSharedPreferences(KEY_SHAREDPREFS, Context.MODE_PRIVATE);
    }

    public static SettingsAdmin getInstance(Context context) {
        if(INSTANCE == null){
            synchronized (lock){
                if(INSTANCE == null){
                    INSTANCE = new SettingsAdmin(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public void setName(String value){
        prefs.edit().putString(KEY_NAME, value).apply();
    }

    public String getName(){
        String name = prefs.getString(KEY_NAME,"");
        return name;
    }

    public void setGender(int value){
        // 0 = male
        // 1 = female
        prefs.edit().putInt(KEY_GENDER, value).apply();
    }

    public int getGender(){
        int gender = prefs.getInt(KEY_GENDER, 0);
        return gender;
    }

    public void setEmail(String value){
        prefs.edit().putString(KEY_EMAIL, value).apply();
    }

    public String getEmail(){
        String email = prefs.getString(KEY_EMAIL,"");
        return email;
    }

    public void setPicture(String path){
        prefs.edit().putString(KEY_PICTURE, path).apply();
    }

    public String getPicture(){
        String path = prefs.getString(KEY_PICTURE,"");
        return path;
    }

    public void setUnits(int value){
        // 0 = kg, cm
        // 1 = lbs, ft & in
        prefs.edit().putInt(KEY_UNITS, value).apply();
    }

    public int getUnits(){
        int units = prefs.getInt(KEY_UNITS, 0);
        return units;
    }


}
