package com.inverseapps.punchcard.model.param;

/**
 * Created by asus on 24-Nov-17.
 */

public class BillingAddressParam {

    private String line1;
    private String city;
    private String country_code;
    private String postal_code;
    private String state;
    private String phone;

    public BillingAddressParam(String line1, String city, String country_code, String postal_code, String state, String phone) {
        this.line1 = line1;
        this.city = city;
        this.country_code = country_code;
        this.postal_code = postal_code;
        this.state = state;
        this.phone = phone;
    }
}
