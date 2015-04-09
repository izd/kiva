package com.zackhsi.kiva;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zackhsi.kiva.models.OauthToken;
import com.zackhsi.kiva.models.KivaProxyId;
import com.zackhsi.kiva.models.User;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public class KivaProxy {
    private static KivaProxyInterface kivaProxyInterface;

    public static KivaProxyInterface getKivaProxyClient() {
        if (kivaProxyInterface == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://kiva-server.herokuapp.com")
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Authorization", getKivaProxyId());
                        }
                    })
                    .build();

            kivaProxyInterface = restAdapter.create(KivaProxyInterface.class);
        }

        return kivaProxyInterface;
    }

    /**
     * @return uuid from preferences, or empty string
     */
    public static String getKivaProxyId() {
        return KivaApplication.context.getSharedPreferences("KivaServer", 0).getString("id", "");
    }

    public static boolean isAuthenticated() {
        return KivaApplication.context.getSharedPreferences("KivaServer", 0).getString("id", "") != "";
    }

    public static void logOut() {
        SharedPreferences kivaServerPrefs = KivaApplication.context.getSharedPreferences("KivaServer", 0);
        SharedPreferences.Editor editor = kivaServerPrefs.edit();
        editor.putString("id", "");
        editor.commit();
    }

    public interface KivaProxyInterface {
        @POST("/users")
        void logIn(@Body OauthToken token, Callback<KivaProxyId> callback);

        @GET("/profile")
        void getProfile(Callback<User> callback);
    }
}