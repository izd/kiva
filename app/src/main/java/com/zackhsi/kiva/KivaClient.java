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
    public static final String REST_URL = "http://api.kivaws.org/v1";
    public static final String REST_CONSUMER_KEY = "com.izd.kiva3";
    public static final String REST_CONSUMER_SECRET = "yixoCvAqyrFLnnCurLtxDeABrtAEqmqN";


    public KivaClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, null);
    }

    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
    public void searchUnfundedLoans(String region, String themes, JsonHttpResponseHandler handler) {
        String searchEndpoint = REST_URL + "/loans/search.json";
        RequestParams params = new RequestParams();
        params.put("status", "fundraising");
        params.put("per_page", 12);

        params.put("regions", region);
        params.put("themes", themes);
        getClient().get(searchEndpoint, params, handler);
    }
}
