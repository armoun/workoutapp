package be.howest.nmct3.workoutapp.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import be.howest.nmct3.workoutapp.R;

/**
 * Created by verborghs on 8/10/2014.
 */
public class AccountAuthenticatorActivity extends FragmentActivity implements AccountAuthenticatorFragment.AccountAuthenticationListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_authenticator);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AccountAuthenticatorFragment())
                    .commit();
        }
    }


    @Override
    public void onAccountAuthenticated(Intent result) {
        setResult(RESULT_OK, result);
        finish();
    }
}
