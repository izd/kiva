package com.zackhsi.kiva;

import android.net.Uri;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.extractors.RequestTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.scribe.services.SignatureService;


public class KivaApi extends DefaultApi10a {
    public static String clientId = "com.kiva.dotdot2";
    public static String callbackUrl = "oauth://kivaclient";

    @Override
    public String getAccessTokenEndpoint() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.kivaws.org")
                .appendPath("oauth")
                .appendPath("access_token");

        return builder.build().toString();
    }

    @Override
    public String getRequestTokenEndpoint() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.kivaws.org")
                .appendPath("oauth")
                .appendPath("request_token");

        return builder.build().toString();
    }

    @Override
    public SignatureService getSignatureService() {
        return super.getSignatureService();
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.kiva.org")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("oauth_token", requestToken.getToken())
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("oauth_callback", callbackUrl)
                .appendQueryParameter("scope", "access,user_balance,user_email,user_expected_repayments,user_anon_lender_data,user_anon_lender_loans,user_loan_balances,user_stats,user_anon_lender_teams");

        return builder.build().toString();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        return super.createService(config);
    }

}
