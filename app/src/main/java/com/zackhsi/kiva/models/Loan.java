package com.zackhsi.kiva.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by isaac on 3/21/15.
 */
public class Loan {
    int id;

    public ArrayList<Loan> fromJson(JSONObject json){
        ArrayList<Loan> result = new ArrayList<>();
        try {
            JSONArray loans = json.getJSONArray("loans");
            for (int i = 0; i < loans.length() ; i++) {
                JSONObject jsonLoan = loans.getJSONObject(i);
                Loan loan = new Loan();
                loan.id = jsonLoan.getInt("id");
                result.add(loan);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }
}
