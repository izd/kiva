package com.zackhsi.kiva;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class KivaApplication {
    private static Context context;


//	public static KivaClient getRestClient() {
//		return (KivaClient) KivaClient.getInstance(KivaClient.class, KivaApplication.context);
//	}
}