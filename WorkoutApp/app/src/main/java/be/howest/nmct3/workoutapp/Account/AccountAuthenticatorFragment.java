package be.howest.nmct3.workoutapp.Account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import be.howest.nmct3.workoutapp.R;
import be.howest.nmct3.workoutapp.data.Contract;

/**
 * Created by verborghs on 8/10/2014.
 */
public class AccountAuthenticatorFragment extends Fragment {
    private static String TAG = AccountAuthenticatorFragment.class.getCanonicalName();

    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private AccountManager mAccountManager;
    private AccountAuthenticationListener mListener;
    private AccountAuthenticatorUtils mUtils;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_authenticator,container, false);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(mUtils.getServer()))
                    return false;
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url.contains("https://android.verborgh.be/oauth/authorize/")){
                        List<String> segments = Uri.parse(url).getPathSegments();
                        String code = segments.get(segments.size() -1);
                        new GetTokenTask().execute(code);
                } else {
                    super.onPageFinished(view, url);
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(errorCode == WebViewClient.ERROR_AUTHENTICATION) {
                    if(mAccountAuthenticatorResponse != null) {
                        mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_BAD_AUTHENTICATION, description);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAccountManager = AccountManager.get(getActivity());

        mAccountAuthenticatorResponse =
                getActivity().getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        mUtils = new AccountAuthenticatorUtils(getActivity());



        mWebView.loadUrl(mUtils.getAuthorizeUrl());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (AccountAuthenticationListener)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class GetTokenTask extends AsyncTask<String, Void, GetTokenTask.Data> {
        class Data {
            String email;
            String accessToken;
            String refreshToken;
        }

        @Override
        protected Data doInBackground(String... codes) {
            try {
                AccountAuthenticatorUtils util = new AccountAuthenticatorUtils(getActivity());
                String code = codes[0];
                AccountAuthenticatorUtils.Tokens tokens = util.getAccessToken(code);
                if(!TextUtils.isEmpty(tokens.accessToken)) {
                    String email = util.getEmail(tokens.accessToken);

                    if(!TextUtils.isEmpty(email)) {
                        Data data = new Data();
                        data.accessToken = tokens.accessToken;
                        data.refreshToken = tokens.refreshToken;
                        data.email = email;
                        return data;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                if(mAccountAuthenticatorResponse != null) {
                    mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_BAD_AUTHENTICATION, e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Data data) {
            if(data != null) {

                AccountManager manager = AccountManager.get(getActivity());
                Account[] accountsByType = manager.getAccountsByType(Contract.ACCOUNT_TYPE);
                Account account;
                if(accountsByType.length == 0) { // new account
                    account = new Account(data.email, Contract.ACCOUNT_TYPE);
                    manager.addAccountExplicitly(account, null, null);
                } else if(!accountsByType[0].name.equals(data.email)) { // different account
                    //should probably do something more usefull here.
                    //like removing old account and adding new
                    account = accountsByType[0];
                } else { // existing account
                    account = accountsByType[0];
                }

                manager.setAuthToken(account, "access_token", data.accessToken);
                manager.setAuthToken(account, "refresh_token", data.refreshToken);
                ContentResolver.setSyncAutomatically(account, Contract.AUTHORITY, true);
                ContentResolver.requestSync(account, Contract.AUTHORITY, new Bundle());
                Intent  intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, data.email);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
                if(mAccountAuthenticatorResponse != null) {
                    Bundle bundle = intent.getExtras();
                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, data.email);
                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
                    mAccountAuthenticatorResponse.onResult(bundle);
                }

                mListener.onAccountAuthenticated(intent);
            }
        }

    }

    public interface AccountAuthenticationListener {
        public void onAccountAuthenticated(Intent intent);
    }

}
