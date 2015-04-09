package com.zackhsi.kiva;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;
import com.zackhsi.kiva.helpers.SentenceManager;
import com.zackhsi.kiva.models.PaymentStub;
import com.zackhsi.kiva.models.User;

import android.support.v4.app.FragmentActivity;


public class KivaApplication extends Application {
    public static Context context;
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
        if (!KivaProxy.isAuthenticated()) {
            client.connect(callingContext);
        }
    }
}
