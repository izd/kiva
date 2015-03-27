package com.zackhsi.kiva;

import android.os.AsyncTask;
import android.util.Log;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

/**
 * Created by zackhsi on 3/26/15.
 */
public class OAuthTask extends AsyncTask<Void, Void, Token> {

    protected Token doInBackground(Void... urls) {
        OAuthService service = new ServiceBuilder()
                .provider(KivaApi.class)
                .apiKey("com.izd.kiva")
                .apiSecret("sIqmBCJlGtpMnmOveknfvvbyjMqIbvCF") // TODO: don't stick this in Github
                .build();

        Token requestToken = service.getRequestToken();
        Log.d("OAuth", requestToken.toString());
        return requestToken;
    }

    protected void onPostExecute(Token token) {
        // TODO: set token in preferences
    }
}