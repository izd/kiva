package com.zackhsi.kiva;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zackhsi.kiva.models.User;


public class KivaApplication extends Application {
    private static Context context;
    public static User loggedInUser;

    @Override
    public void onCreate() {
        super.onCreate();
        KivaApplication.context = this;
        KivaApplication.loggedInUser = new User();
    }

    public static KivaClient getRestClient() {
        return (KivaClient) KivaClient.getInstance(KivaClient.class, KivaApplication.context);
    }

    public static void getAuthenticatedRestClient(FragmentActivity callingContext) {
        KivaClient client = (KivaClient) KivaClient.getInstance(KivaClient.class, context);
        if (!client.isAuthenticated()) {
            client.connect(callingContext);
        }
    }
}
