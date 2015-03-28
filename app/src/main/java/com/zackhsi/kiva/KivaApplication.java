package com.zackhsi.kiva;

import android.app.Application;
import android.content.Context;


public class KivaApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        KivaApplication.context = this;
    }

    public static KivaClient getRestClient() {
		return (KivaClient) KivaClient.getInstance(KivaClient.class, KivaApplication.context);
	}
}