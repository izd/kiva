package com.zackhsi.kiva.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SentenceManager {
    public static String SHARED_STATE = "STATE";

    public static enum OptionType {
        GROUP, COUNTRY, SECTOR
    }

    public static void updatePreferences(Context context, SentenceManager.OptionType itemBeingEdited, int selectedPosition) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_STATE, Context.MODE_PRIVATE).edit();
        editor.putInt(itemBeingEdited.toString(), selectedPosition);
        editor.commit();
    }

    public static int readPreference(Context context, OptionType itemBeingRead) {
        return context.getSharedPreferences(SHARED_STATE, Context.MODE_PRIVATE).getInt(itemBeingRead.toString(), 0);
    }

    public static String readPreferenceRawString(Context context, OptionType itemBeingRead) {
        readPreference(context, itemBeingRead);
        return "";
    }

//    public static String readPreferenceCodeString(Context context, OptionType itemBeingRead) {
//
//    }
//
//    public static ArrayList readArrayCodeString(Context context, OptionType itemBeingRead) {
//
//    }

    public static String getPrettyStringFromKey(String key) {
        if (key.contains(",")) {
            return key.split(",")[1];
        } else {
            return key;
        }
    }

    public static String getCodeStringFromKey(String key) {
        if (key.contains(",")) {
            return key.split(",")[0];
        } else {
            return key;
        }
    }

    public static String readPreferencePrettyString(Context context, OptionType itemBeingRead) {
        ArrayList<String> itemArray = getArrayForOptionType(context, itemBeingRead);
        String rawItem = itemArray.get(readPreference(context, itemBeingRead));

        return getPrettyStringFromKey(rawItem);
    }

    private static ArrayList<String> getArrayForOptionType(Context context, OptionType optionType) {
        Resources resources = context.getResources();

        ArrayList<String> result = new ArrayList<>();

        if (optionType == OptionType.GROUP) {
            result.addAll(Arrays.asList(resources.getStringArray(R.array.sentence_gender)));
            result.addAll(Arrays.asList(resources.getStringArray(R.array.sentence_borrower_type)));
        } else if (optionType == OptionType.COUNTRY) {
            result.addAll(Arrays.asList(resources.getStringArray(R.array.sentence_country)));
        } else if (optionType == OptionType.SECTOR) {
            result.addAll(Arrays.asList(resources.getStringArray(R.array.sentence_sector)));
        }

        return result;
    }
}
