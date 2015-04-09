package com.zackhsi.kiva.oauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaProxy;
import com.zackhsi.kiva.fragments.LoginDialogFragment;
import com.zackhsi.kiva.models.KivaProxyId;
import com.zackhsi.kiva.models.OauthToken;

import org.apache.http.Header;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Token;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class OAuthBaseClient {
    protected static HashMap<Class<? extends OAuthBaseClient>, OAuthBaseClient> instances =
            new HashMap<Class<? extends OAuthBaseClient>, OAuthBaseClient>();
    protected String baseUrl;
    protected Context context;
    protected OAuthAsyncHttpClient client;
    protected SharedPreferences prefs;
    protected SharedPreferences.Editor editor;
    protected OAuthAccessHandler accessHandler;
    protected String callbackUrl;
    protected int requestIntentFlags = -1;
    private FragmentActivity callingContext;

    public OAuthBaseClient(Context c, Class<? extends Api> apiClass, String consumerUrl, String consumerKey, String consumerSecret, String callbackUrl) {
        this.baseUrl = consumerUrl;
        this.callbackUrl = callbackUrl;
        client = new OAuthAsyncHttpClient(apiClass, consumerKey,
                consumerSecret, callbackUrl, new OAuthAsyncHttpClient.OAuthTokenHandler() {

            // Store request token and launch the authorization URL in the browser
            @Override
            public void onReceivedRequestToken(Token requestToken, String authorizeUrl) {
                if (requestToken != null) { // store for OAuth1.0a
                    editor.putString("request_token", requestToken.getToken());
                    editor.putString("request_token_secret", requestToken.getSecret());
                    editor.commit();
                }
                FragmentManager fm = callingContext.getSupportFragmentManager();
                LoginDialogFragment loginDialogFragment = LoginDialogFragment.newInstance(authorizeUrl);
                loginDialogFragment.show(fm, "fragment_login");
            }

            // Store the access token in preferences, set the token in the client and fire the success callback
            @Override
            public void onReceivedAccessToken(Token accessToken) {
                logInToKivaServer(accessToken);

                client.setAccessToken(accessToken);
                editor.putString(OAuthConstants.TOKEN, accessToken.getToken());
                editor.putString(OAuthConstants.TOKEN_SECRET, accessToken.getSecret());
                editor.commit();
                accessHandler.onLoginSuccess();
                Log.d("OAuth", "onReceivedAccessToken");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("OAuth", "onFailure", e);
            }

        });

        this.context = c;
        // Store preferences namespaced by the class and consumer key used
        this.prefs = this.context.getSharedPreferences("OAuth_" + apiClass.getSimpleName() + "_" + consumerKey, 0);
        this.editor = this.prefs.edit();

        /** DEBUG: use when we get a token */
        // hardCodeAccessToken();

        // Set access token in the client if already stored in preferences
        if (this.checkAccessToken() != null) {
            client.setAccessToken(this.checkAccessToken());
        }
    }

    private void logInToKivaServer(Token accessToken) {
        OauthToken oauthToken = new OauthToken(accessToken.getToken(), accessToken.getSecret());
        KivaProxy.getKivaProxyClient().logIn(oauthToken, new Callback<KivaProxyId>() {
            @Override
            public void success(KivaProxyId kivaProxyId, Response response) {
                Log.d("RETRO", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("RETRO", "failure");
            }
        });
    }

    public static OAuthBaseClient getInstance(Class<? extends OAuthBaseClient> klass, Context context) {
        OAuthBaseClient instance = instances.get(klass);
        if (instance == null) {
            try {
                instance = (OAuthBaseClient) klass.getConstructor(Context.class).newInstance(context);
                instances.put(klass, instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    // Fetches a request token and retrieve and authorization url
    // Should open a browser in onReceivedRequestToken once the url has been received
    public void connect(FragmentActivity callingContext) {
        this.callingContext = callingContext;
        client.fetchRequestToken();
    }

    // Retrieves access token given authorization url
    public void authorize(Uri uri, OAuthAccessHandler handler) {
        this.accessHandler = handler;
        if (checkAccessToken() == null && uri != null) {
            String uriServiceCallback = uri.getScheme() + "://" + uri.getHost();
            // check if the authorize callback matches this service before trying to get an access token
            if (uriServiceCallback.equals(callbackUrl)) {
                client.fetchAccessToken(getRequestToken(), uri);
            } else {
                this.accessHandler.onLoginFailure(new Exception("OAuth callback URL does not match uri"));
            }
        } else if (checkAccessToken() != null) { // already have access token
            this.accessHandler.onLoginSuccess();
        }
    }

    // Return access token if the token exists in preferences
    public Token checkAccessToken() {
        if (prefs.contains(OAuthConstants.TOKEN) && prefs.contains(OAuthConstants.TOKEN_SECRET)) {
            return new Token(prefs.getString(OAuthConstants.TOKEN, ""),
                    prefs.getString(OAuthConstants.TOKEN_SECRET, ""));
        } else {
            return null;
        }
    }

    protected OAuthAsyncHttpClient getClient() {
        return client;
    }

    // Returns the request token stored during the request token phase
    protected Token getRequestToken() {
        return new Token(prefs.getString("request_token", ""),
                prefs.getString("request_token_secret", ""));
    }

    // Assigns the base url for the API
    protected void setBaseUrl(String url) {
        this.baseUrl = url;
    }

    // Returns the full ApiUrl
    protected String getApiUrl(String path) {
        return this.baseUrl + "/" + path;
    }

    // Removes the access tokens (for signing out)
    public void clearAccessToken() {
        client.setAccessToken(null);
        editor.remove(OAuthConstants.TOKEN);
        editor.remove(OAuthConstants.TOKEN_SECRET);
        editor.commit();
    }

    // Returns true if the client is authenticated; false otherwise.
    public boolean isAuthenticated() {
        return client.getAccessToken() != null;
    }

    // Sets the flags used when launching browser to authenticate through OAuth
    public void setRequestIntentFlags(int flags) {
        this.requestIntentFlags = flags;
    }

    // Defines the handler events for the OAuth flow
    public static interface OAuthAccessHandler {
        public void onLoginSuccess();

        public void onLoginFailure(Exception e);
    }

}
