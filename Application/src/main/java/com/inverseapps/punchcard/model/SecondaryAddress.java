package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 15-Jan-18.
 */

public class SecondaryAddress implements Parcelable{
    private String zip;
    private String city;
    private String state;
    private String address1;
    private String address2;

    protected SecondaryAddress(Parcel in) {
        zip = in.readString();
        city = in.readString();
        state = in.readString();
        address1 = in.readString();
        address2 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(zip);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(address1);
        dest.writeString(address2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SecondaryAddress> CREATOR = new Creator<SecondaryAddress>() {
        @Override
        public SecondaryAddress createFromParcel(Parcel in) {
            return new SecondaryAddress(in);
        }

        @Override
        public SecondaryAddress[] newArray(int size) {
            return new SecondaryAddress[size];
        }
    };

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
}
