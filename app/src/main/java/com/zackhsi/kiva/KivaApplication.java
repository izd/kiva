package com.zackhsi.kiva;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;
import com.zackhsi.kiva.helpers.SentenceManager;
import com.zackhsi.kiva.models.PaymentStub;

import android.support.v4.app.FragmentActivity;


public class KivaApplication extends Application {
    private static Context context;
    private static String YOUR_APPLICATION_ID = "J74QgYRq67Ebc19VzDgfrF3rEus6MdbgBmnebLzp";
    private static String YOUR_CLIENT_KEY = "QaP2wdpTcipwrJNR1wsfZDXIEnlHdQAzskRxvcMs";

    @Override
    public void onCreate() {
        super.onCreate();
        KivaApplication.context = this;

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseObject.registerSubclass(PaymentStub.class);
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
