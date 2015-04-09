package com.zackhsi.kiva.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class CountryIconResource {
    private final String drawable = "drawable";
    private String packageName;
    private Context context;
    private String iso2;
    private final String unitedNations = "_un";

    public CountryIconResource(String iso2, Context context) {
        this.packageName = context.getPackageName();
        this.context = context;
        this.iso2 = iso2;
    }

    public int getIconId(){
        try {
            return context.getResources().getIdentifier(iso2, drawable, packageName);
        } catch (Exception e) {
            return context.getResources().getIdentifier(unitedNations, drawable, packageName);
        }
    }
}
