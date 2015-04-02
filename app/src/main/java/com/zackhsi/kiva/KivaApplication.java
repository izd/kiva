package com.zackhsi.kiva;

import android.app.Application;
import android.content.Context;
import android.os.Message;

import com.parse.Parse;
import com.parse.ParseObject;
import com.zackhsi.kiva.models.PaymentStub;


public class KivaApplication extends Application {
    private static Context context;
    private static String YOUR_APPLICATION_ID = "";
    private static String YOUR_CLIENT_KEY = "";

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
}
