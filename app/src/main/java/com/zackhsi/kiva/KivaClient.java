package com.zackhsi.kiva;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zackhsi.kiva.oauth.OAuthBaseClient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;


public class KivaClient extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = KivaApi.class;
    public static final String REST_CONSUMER_KEY = KivaApi.clientId;
    public static final String REST_CALLBACK_URL = KivaApi.callbackUrl;

    public static final String REST_URL = "https://api.kivaws.org/v1";
    private static final String REST_CONSUMER_SECRET = "lgvkDDBFtRximElyzqytqAvclDxFzpqu";

    public KivaClient(Context context) {
        super(
                context,
                REST_API_CLASS,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    public void searchUnfundedLoans(String sector, String gender, String borrowerType, String countryCode, JsonHttpResponseHandler handler) {
        String searchEndpoint = REST_URL + "/loans/search.json";
        RequestParams params = new RequestParams();
        params.put("status", "fundraising");
        params.put("sort_by", "expiration");
        params.put("per_page", 20);

        if (sector != null) {
            params.put("sector", sector);
        }
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }

        if (borrowerType != null) {
            params.put("borrower_type", borrowerType);
        }

        if (gender != null) {
            params.put("gender", gender);
        }

        getClient().get(searchEndpoint, params, handler);
    }

    public void getLoan(long id, JsonHttpResponseHandler handler) {
        String searchEndpoint = REST_URL + "/loans/" + id + ".json";
        RequestParams params = new RequestParams();
        getClient().get(searchEndpoint, params, handler);
    }
}
