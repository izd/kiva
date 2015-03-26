package com.zackhsi.kiva;

/**
 * Created by zackhsi on 3/25/15.
 */

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;


public class KivaApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://www.kiva.org/oauth/authorize?response_type=code&oauth_token=%s&oauth_callback=oob&scope=access,user_balance,user_email";

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
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return super.createService(config);
    }

}
