package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 06-Dec-17.
 */

public class PresentBilling implements Parcelable {

    private int punches;
    private String next_billing_date;


    public int getPunches() {
        return punches;
    }

    public void setPunches(int punches) {
        this.punches = punches;
    }

    public String getNext_billing_date() {
        return next_billing_date;
    }

    public void setNext_billing_date(String next_billing_date) {
        this.next_billing_date = next_billing_date;
    }

    public static Creator<PresentBilling> getCREATOR() {
        return CREATOR;
    }

    protected PresentBilling(Parcel in) {
        punches = in.readInt();
        next_billing_date = in.readString();
    }

    public static final Creator<PresentBilling> CREATOR = new Creator<PresentBilling>() {
        @Override
        public PresentBilling createFromParcel(Parcel in) {
            return new PresentBilling(in);
        }

        @Override
        public PresentBilling[] newArray(int size) {
            return new PresentBilling[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(punches);
        parcel.writeString(next_billing_date);
    }
}
