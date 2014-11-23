package be.howest.nmct3.workoutapp.Account;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import be.howest.nmct3.workoutapp.data.Contract;

/**
 * Created by verborghs on 8/10/2014.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        if(!accountType.equals(Contract.ACCOUNT_TYPE))
            throw new  IllegalArgumentException();

        //We don't support any features
        if(!(requiredFeatures == null ||  requiredFeatures.length == 0))
            throw new IllegalArgumentException();

        return createAuthenticatorActivityBundle(response);
    }


    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String token, Bundle bundle) throws NetworkErrorException {

        AccountAuthenticatorUtils utils = new AccountAuthenticatorUtils(mContext);

        if(token != "access_token")
            throw new IllegalArgumentException("Only access_token is available");

        AccountManager mgt = AccountManager.get(mContext);
        String accessToken = mgt.peekAuthToken(account, token);

        try {
            if(accessToken == null || utils.isTokenExpired(accessToken)) {
                String refreshToken = mgt.peekAuthToken(account, "refresh_token");
                if (refreshToken == null) {
                    return createAuthenticatorActivityBundle(response);
                } else {
                    AccountAuthenticatorUtils.Tokens tokens = utils.refreshAccessToken(refreshToken);
                    mgt.setAuthToken(account, "access_token", tokens.accessToken);
                    mgt.setAuthToken(account, "refresh_token", tokens.refreshToken);
                    return createAccessTokenBundle(account, tokens.accessToken);
                }
            } else {
                return createAccessTokenBundle(account,accessToken);
            }
        } catch (Exception e) {
                throw new NetworkErrorException(e.getMessage());
         }
    }

    private Bundle createAuthenticatorActivityBundle(AccountAuthenticatorResponse response) {
        Intent intent = new Intent(mContext, AccountAuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    private Bundle createAccessTokenBundle(Account account, String accessToken) {
        Bundle reply = new Bundle();
        reply.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
        reply.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        reply.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        return reply;
    }

    @Override
    public String getAuthTokenLabel(String tokenType) {
        if(tokenType.equals("access_token"))
            return "Access Token";

        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] strings) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
