package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class Address implements Parcelable {

    private String zip;
    private String city;
    private String state;
    private String address1;
    private String address2;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.zip);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.address1);
        dest.writeString(this.address2);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.zip = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.address1 = in.readString();
        this.address2 = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
