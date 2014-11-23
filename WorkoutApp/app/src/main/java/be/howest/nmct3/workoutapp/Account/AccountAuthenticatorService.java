package be.howest.nmct3.workoutapp.Account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by verborghs on 11/10/2014.
 */
public class AccountAuthenticatorService extends Service {

    private AccountAuthenticator mAuthenticator;

    public AccountAuthenticatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
