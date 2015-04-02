package com.zackhsi.kiva.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("PaymentStub")
public class PaymentStub extends ParseObject{
    public String getUserId() {
        return getString("userId");
    }

    public int getAmount() {
        return getInt("amount");
    }

    public String getPaymentId(){
        return getString("paymentId");
    }

    public String getPaymentCreated(){
        return getString("paymentCreated");
    }

    public int getLoanId(){
        return getInt("loanId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public void setAmount(int amount) {
        put("amount", amount);
    }

    public void setPaymentId(String paymentId){
        put("paymentId", paymentId);
    }

    public void setPaymentCreated(String paymentCreated){
        put("paymentCreated", paymentCreated);
    }

    public void setLoanId(int loanId){
        put("loanId", loanId);
    }

    public PaymentStub fromPaymentResponse(JSONObject confirm, JSONObject payment){
        PaymentStub responseStub = new PaymentStub();
        try {
            JSONObject response = confirm.getJSONObject("response");
            responseStub.setPaymentId(response.getString("id"));
            responseStub.setPaymentCreated(response.getString("create_time"));
            responseStub.setAmount(Integer.parseInt(payment.getString("amount")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseStub;
    }
}
