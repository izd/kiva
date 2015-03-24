package com.zackhsi.kiva;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by isaac on 3/20/15.
 */
public class KivaClient {


    //  public static final Class<? extends Api> REST_API_CLASS = KivaApi.class; // Change this
    public static final String REST_URL = "http://api.kivaws.org/v1"; // Change this, base API URL
    private AsyncHttpClient client;
//  public static final String REST_CONSUMER_KEY = "NouOJfB8hsnOg0VHxEupbTcVz";       // Change this
//  public static final String REST_CONSUMER_SECRET = "Z9X3bfUAXuRPDLyKGjejLT1kt3CDF05ke4DZrQ004eHRO0ig55"; // Change this
//  public static final String REST_CALLBACK_URL = "oauth://tweetbutlerpro"; // Change this (here and in manifest)

//  public KivaClient(Context context) {
//    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
//  }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here

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
        client = new AsyncHttpClient();
        client.get(searchEndpoint, params, handler);
    }
}
