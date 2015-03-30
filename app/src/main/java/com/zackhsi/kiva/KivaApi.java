package com.zackhsi.kiva;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;


public class KivaApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.kiva.org/oauth/authorize?client_id=%s&response_type=code&oauth_callback=%s&oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.kivaws.org/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.kivaws.org/oauth/request_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        String clientId = "com.izd.kiva5";

        /**
         * Callback must match the Kiva app callback
         *
         * Getting a request token doesn't work if the callback is set
         *
         * A redirect is the easiest way to get the access token
         */
        String oobCallback = "oob";
        return String.format(AUTHORIZE_URL, clientId, oobCallback, requestToken.getToken());
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return super.createService(config);
    }

}
