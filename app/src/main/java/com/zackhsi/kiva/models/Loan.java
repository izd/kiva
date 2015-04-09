package com.zackhsi.kiva.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Loan implements Serializable {
    public long id;
    public long imageId;

    public String name;
    public String use;
    public String description;
    public String activity;
    public String sector;
    public Date postedDate;
    public Date plannedExpirationDate;

    public String country;
    public String countryCode;

    public String status;
    public int loanAmount;
    public int fundedAmount;
    public int percentFunded;
    public int lenderCount;

    private static SimpleDateFormat kivaDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    public static Loan fromJson(JSONObject json) {
        Loan loan = new Loan();

        try {
            loan.id = json.getLong("id");
            loan.imageId = json.getJSONObject("image").getLong("id");

            loan.name = json.getString("name");
            loan.use = json.getString("use");

            try {
                loan.description = json.getJSONObject("description").getJSONObject("texts").getString("en");
            } catch (JSONException e) {
                // pass
            }
            loan.activity = json.getString("activity");
            loan.sector = json.getString("sector");

            loan.postedDate = parseKivaDate(json.getString("posted_date"));
            loan.plannedExpirationDate = parseKivaDate(json.getString("planned_expiration_date"));

            loan.country = json.getJSONObject("location").getString("country");
            loan.countryCode = json.getJSONObject("location").getString("country_code");

            loan.status = json.getString("status");
            loan.loanAmount = json.getInt("loan_amount");
            loan.fundedAmount = json.getInt("funded_amount");
            if (loan.loanAmount != 0) {
                loan.percentFunded = (int) (((double) loan.fundedAmount / (double) loan.loanAmount) * 100);
            } else {
                loan.percentFunded = 0;
            }
            loan.lenderCount = json.getInt("lender_count");

            return loan;
        } catch (JSONException e) {
            Log.e("", "" , e);
            return null;
        }
    }

    private static Date parseKivaDate(String date) {
        try {
            return kivaDateFormat.parse(date.replace("Z", "+00:00"));
        } catch (Exception e) {
            Log.e("", "" , e);
            return null;
        }
    }

    public  String longUse(){
        int positionOfTo = use.indexOf("To");
        if ( (positionOfTo == -1) || (positionOfTo > 3 )){
           return "A loan " + use;
        } else {
            return "A loan " + use.replaceFirst("To ", "to");
        }
    }

    public static ArrayList<Loan> fromJson(JSONArray json) {
        ArrayList<Loan> result = new ArrayList<>(json.length());
        for (int i = 0; i < json.length(); i++) {
            try {
                Loan loan = Loan.fromJson(json.getJSONObject(i));
                if (loan != null) {
                    result.add(loan);
                }
            } catch (JSONException e) {
                Log.e("", "" , e);
                //pass
            }
        }
        return result;
    }

    public String getRelativePlannedExpiration() {
        Date now = new Date();

        return DateUtils.getRelativeTimeSpanString(
                this.plannedExpirationDate.getTime(),
                now.getTime(),
                DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
                .toString();
    }


    public String imageUrl() {
        return "http://www.kiva.org/img/w800/" + imageId + ".jpg";
    }

    public String imageThumbUrl() {
        return "http://www.kiva.org/img/s232/" + imageId + ".jpg";
    }

    public String getOverview() {
        return "A loan of $" + loanAmount + " helps " + name + " " + use;
    }
}
