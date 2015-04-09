package com.zackhsi.kiva.models;

import java.io.Serializable;

/**
 * Created by zackhsi on 3/21/15.
 */
public class User implements Serializable {
    public String account_first_name;
    public String account_id;
    public String account_is_developer;
    public String account_is_public;
    public String account_last_name;
    public String account_lender_id;
    public int stats_amount_donated;
    public int stats_amount_in_arrears;
    public int stats_amount_of_loans;
    public int stats_amount_of_loans_by_invitees;
    public int stats_amount_outstanding;
    public int stats_amount_outstanding_promo;
    public int stats_amount_refunded;
    public int stats_amount_repaid;
    public int stats_arrears_rate;
    public int stats_currency_loss;
    public int stats_currency_loss_rate;
    public int stats_default_rate;
    public int stats_num_defaulted;
    public int stats_num_ended;
    public int stats_num_expired;
    public int stats_num_fund_raising;
    public int stats_num_inactive;
    public int stats_num_inactive_expired;
    public int stats_num_paying_back;
    public int stats_num_raised;
    public int stats_num_refunded;
    public int stats_number_delinquent;
    public int stats_number_of_gift_certificates;
    public int stats_number_of_invites;
    public int stats_number_of_loans;
    public int stats_number_of_loans_by_invitees;
    public int stats_total_defaulted;
    public int stats_total_ended;
    public int stats_total_expired;
    public int stats_total_fund_raising;
    public int stats_total_inactive;
    public int stats_total_inactive_expired;
    public int stats_total_paying_back;
    public int stats_total_refunded;

//    public String getImageUrl() {
//        int maxImageSize = 1000;
//        return "http://www.kiva.org/img/" + maxImageSize + "/" + this.getImageId() + ".jpg";
//    }
}
