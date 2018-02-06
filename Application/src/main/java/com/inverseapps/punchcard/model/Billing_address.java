package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by asus on 24-Nov-17.
 */

public class Billing_address implements Parcelable{
    private String line1;
    private String city;
    private String country_code;
    private String postal_code;
    private String state;
    private String phone;

    public Billing_address(String line1, String city, String country_code, String postal_code, String state, String phone) {
        this.line1 = line1;
        this.city = city;
        this.country_code = country_code;
        this.postal_code = postal_code;
        this.state = state;
        this.phone = phone;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static Creator<Billing_address> getCREATOR() {
        return CREATOR;
    }

    protected Billing_address(Parcel in) {
        line1 = in.readString();
        city = in.readString();
        country_code = in.readString();
        postal_code = in.readString();
        state = in.readString();
        phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(line1);
        dest.writeString(city);
        dest.writeString(country_code);
        dest.writeString(postal_code);
        dest.writeString(state);
        dest.writeString(phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Billing_address> CREATOR = new Creator<Billing_address>() {
        @Override
        public Billing_address createFromParcel(Parcel in) {
            return new Billing_address(in);
        }

        @Override
        public Billing_address[] newArray(int size) {
            return new Billing_address[size];
        }
    };
}
