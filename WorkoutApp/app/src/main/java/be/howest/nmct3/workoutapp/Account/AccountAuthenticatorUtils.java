package be.howest.nmct3.workoutapp.Account;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by verborghs on 13/10/2014.
 */
public class AccountAuthenticatorUtils {
    private static final String AUTHORIZE_URL = "/oauth/authorize?client_id=%s&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code";
    private static final String ACCESS_TOKEN_BODY = "client_id=%s&client_secret=%s&code=%s&grant_type=authorization_code&redirect_uri=urn:ietf:wg:oauth:2.0:oob";
    private static final String REFRESH_TOKEN_BODY = "client_id=%s&client_secret=%s&refresh=%s&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob";

    public class Tokens {
        String accessToken;
        String refreshToken;
    }

    private final Context mContext;
    private final String mServer;
    private final String mClientId;
    private final String mClientSecret;

    public AccountAuthenticatorUtils(Context ctx) {
        mContext = ctx;
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            mServer = bundle.getString("be.howest.ribbons2.webservice.auth.location");
            mClientId = bundle.getString("be.howest.ribbons2.webservice.auth.client_id");
            mClientSecret = bundle.getString("be.howest.ribbons2.webservice.auth.client_secret");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getServer() {
        return mServer;
    }

    public String getAuthorizeUrl() {
        return mServer + String.format(AUTHORIZE_URL, mClientId);
    }

    public String getEmail(String accessToken) throws Exception{
        try {
            URL url = new URL(mServer + "/api/profile");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setUseCaches (false);

            BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = rdr.readLine()) != null)
                sb.append(line);

            rdr.close();
            connection.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            String email = json.getString("email");
            return email;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Tokens getAccessToken(String code) throws Exception{
        String urlParameters = String.format(ACCESS_TOKEN_BODY, mClientId, mClientSecret, code);

        try {
            URL url = new URL(mServer + "/oauth/token");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches (false);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream ()));
            wr.write(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = rdr.readLine()) != null)
                sb.append(line);

            rdr.close();
            connection.disconnect();

            JSONObject json = new JSONObject(sb.toString());

            Tokens tokens = new Tokens();
            tokens.accessToken = json.getString("access_token");
            tokens.refreshToken = json.getString("refresh_token");
            return tokens;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Tokens refreshAccessToken(String refreshToken) throws Exception{
        String urlParameters = String.format(REFRESH_TOKEN_BODY, mClientId, mClientSecret, refreshToken);
        try {
            URL url = new URL(mServer + "/oauth/token");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches (false);

            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream ()));
            wr.write(urlParameters);
            wr.flush();
            wr.close();

            BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = rdr.readLine()) != null)
                sb.append(line);

            rdr.close();
            connection.disconnect();

            JSONObject json = new JSONObject(sb.toString());

            Tokens tokens = new Tokens();
            tokens.accessToken = json.getString("access_token");
            tokens.refreshToken = json.getString("refresh_token");
            return tokens;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isTokenExpired(String accessToken) throws Exception {
        try {
            URL url = new URL(mServer + "/oauth/token/info");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setUseCaches (false);

            BufferedReader rdr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while((line = rdr.readLine()) != null)
                sb.append(line);

            rdr.close();
            connection.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            int secondsToExpiration = json.getInt("expires_in_seconds");
            return secondsToExpiration > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Map<String, String> getParams(String s) {
        String[] keyValues = s.split("&");
        Map<String, String> params = new HashMap<String, String>(keyValues.length);
        for(String keyValue : keyValues) {
            String[] pair = keyValue.split("=");
            if(pair.length == 2)
                params.put(pair[0], pair[1]);
            else
                params.put(pair[0], null);
        }
        return params;
    }
}
