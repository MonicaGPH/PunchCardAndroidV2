package com.inverseapps.punchcard.model.param;

import com.inverseapps.punchcard.model.Billing_address;

/**
 * Created by asus on 05-Dec-17.
 */

public class UpdateParam {
    private String number;
    private String type;
    private int expire_month;
    private int expire_year;
    private String cvv2;
    private String first_name;
    private String last_name;
    private Billing_address billing_address;
  //  private String plan_test;
    //private String createdat;
    private String user_id;
    private int client_id;
    private boolean cardRenew;

    public UpdateParam(String number, String type, int expire_month, int expire_year, String cvv2, String first_name, String last_name, Billing_address billing_address,
                      /* String plan_test, String createdat,*/ String user_id, int client_id, boolean cardRenew) {
        this.number = number;
        this.type = type;
        this.expire_month = expire_month;
        this.expire_year = expire_year;
        this.cvv2 = cvv2;
        this.first_name = first_name;
        this.last_name = last_name;
        this.billing_address = billing_address;
//        this.plan_test = plan_test;
//        this.createdat = createdat;
        this.user_id = user_id;
        this.client_id = client_id;
        this.cardRenew = cardRenew;
    }

}
