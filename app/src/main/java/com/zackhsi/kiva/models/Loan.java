package com.zackhsi.kiva.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Loan {
    int id;
    int amountFunded;
    int loanAmount;
    String name;
    long imageId;
    String country;
    String plannedExpirationDate;
    String countryCode;
    String[] themes;
    String use;
    String town;
    String latLong;

    public String getLatLong() {
        return latLong;
    }

    public String getTown() {
        return town;
    }

    public String getUse() {
        return use;
    }

    public String[] getThemes() {
        return themes;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPlannedExpirationDate() {
        return plannedExpirationDate;
    }

    public String getCountry() {
        return country;
    }

    public long getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public int getAmountFunded() {
        return amountFunded;
    }

    public ArrayList<Loan> fromJson(JSONObject json) {
        ArrayList<Loan> result = new ArrayList<>();
        try {
            JSONArray loans = json.getJSONArray("loans");
            for (int i = 0; i < loans.length(); i++) {
                JSONObject jsonLoan = loans.getJSONObject(i);
                Loan loan = new Loan();
                loan.id = jsonLoan.getInt("id");
                loan.amountFunded = jsonLoan.getInt("funded_amount");
                loan.loanAmount = jsonLoan.getInt("loan_amount");
                loan.imageId = jsonLoan.getJSONObject("image").getLong("id");
                JSONArray themes = jsonLoan.getJSONArray("themes");

                String[] themesArray;
                themesArray = new String[themes.length()];
                for (int j = 0; j < themes.length(); j++) {
                    themesArray[j] = themes.getString(j);
                }

                loan.themes = themesArray;
                loan.name = jsonLoan.getString("name");
                loan.use = jsonLoan.getString("use");
                loan.plannedExpirationDate = jsonLoan.getString("planned_expiration_date");
                JSONObject location = jsonLoan.getJSONObject("location");
                loan.country = location.getString("country");
                loan.countryCode = location.getString("country_code");
                loan.town = location.getString("town");
                loan.latLong = location.getJSONObject("geo").getString("pairs");
                result.add(loan);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String imageUrl() {
        return "http://www.kiva.org/img/w800/" + String.valueOf(imageId) + ".jpg";
    }
}
