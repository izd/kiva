package com.zackhsi.kiva.models;

public class OauthToken {
    final String oauth_token;
    final String oauth_token_secret;

    public OauthToken(String oauth_token, String oauth_token_secret) {
        this.oauth_token = oauth_token;
        this.oauth_token_secret = oauth_token_secret;
    }
}
