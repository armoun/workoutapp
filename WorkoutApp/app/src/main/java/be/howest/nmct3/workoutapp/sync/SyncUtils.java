package be.howest.nmct3.workoutapp.sync;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nielslammens on 19/11/14.
 */
public class SyncUtils {
    private final String mServer;
    private final Context mContext;

    public SyncUtils(Context ctx){
        mContext = ctx;
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            mServer = bundle.getString("be.howest.nmct3.workoutapp.sync.SyncUtils.server");
        } catch (PackageManager.NameNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void SyncWorkouts(String accessToken, ContentProviderClient contentProviderClient) throws Exception{

        Log.d("","Trying to SYNC WORKOUTS");

        try {
            URL url = new URL(mServer);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", "Bearer" + accessToken);
            connection.setUseCaches(false);

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
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
            throw e;
        }
    }
}
