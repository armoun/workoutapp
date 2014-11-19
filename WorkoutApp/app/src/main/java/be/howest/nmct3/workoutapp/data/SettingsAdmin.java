package be.howest.nmct3.workoutapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nielslammens on 31/10/14.
 */

/*
* http://stackoverflow.com/questions/15549421/how-to-download-and-save-an-image-in-android
*
* public static Bitmap getBitmapFromURL(String link) {
  //this method downloads an Image from the given URL, then decodes and returns a Bitmap object

    try {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url
        .openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);

        return myBitmap;

        } catch (IOException e) {
        e.printStackTrace();
        Log.e("getBmpFromUrl error: ", e.getMessage().toString());
        return null;
    }
  }
*
* */
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

    Map<Integer, String> map = new HashMap<Integer, String>();

    private SharedPreferences prefs;

    public SettingsAdmin(Context context){
        mContext = context;
        prefs = mContext.getSharedPreferences(KEY_SHAREDPREFS, Context.MODE_PRIVATE);
        map.put(0, KEY_NAME);
        map.put(1, KEY_GENDER);
        map.put(2, KEY_DATE_OF_BIRTH);
        map.put(3, KEY_EMAIL);
        map.put(4, KEY_PICTURE);
        map.put(5, KEY_UNITS);
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

    public String getValueForSetting(int index){
        String settingValue = "";


        switch (index){
            case 0:
                settingValue = getName();
                break;
            case 1:
                settingValue = getGender();
                break;
            case 2:
                settingValue = getDateOfBirth();
                break;
            case 3:
                settingValue = getEmail();
                break;
            case 4:
                settingValue = getPicture();
                break;
            case 5:
                settingValue = getUnits();
                break;
            default:
                settingValue = "No value";
        }
        return settingValue;
    }

    public void setName(String value){
        prefs.edit().putString(KEY_NAME, value).apply();
    }

    public String getName(){
        String name = prefs.getString(KEY_NAME,"Firstname Name");
        return name;
    }

    public void setGender(String value){
        // 0 = male
        // 1 = female
        prefs.edit().putString(KEY_GENDER, value).apply();
    }

    public String getGender(){
        String gender = prefs.getString(KEY_GENDER, "Male");
        return gender;
    }

    public void setDateOfBirth(Date value){
        prefs.edit().putString(KEY_DATE_OF_BIRTH, value.toString()).apply();
    }

    public String getDateOfBirth(){
        String dob = prefs.getString(KEY_DATE_OF_BIRTH, "1-1-1990");
        return dob;
    }

    public void setEmail(String value){
        prefs.edit().putString(KEY_EMAIL, value).apply();
    }

    public String getEmail(){
        String email = prefs.getString(KEY_EMAIL,"email@example.com");
        return email;
    }

    public void setPicture(String path){
        prefs.edit().putString(KEY_PICTURE, path).apply();
    }

    public String getPicture(){
        String path = prefs.getString(KEY_PICTURE,"Picture.jpg");
        return path;
    }

    public void setUnits(String value){
        // 0 = kg, cm
        // 1 = lbs, ft & in
        prefs.edit().putString(KEY_UNITS, value).apply();
    }

    public String getUnits(){
        String units = prefs.getString(KEY_UNITS, "Metric");
        return units;
    }


}
