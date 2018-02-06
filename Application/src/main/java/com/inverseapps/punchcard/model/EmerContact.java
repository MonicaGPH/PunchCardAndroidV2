package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class EmerContact implements Parcelable {

    private String name;
    private String email;
    private String relation;
    private String homeNumber;
    private String workNumber;
    private String mobileNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.relation);
        dest.writeString(this.homeNumber);
        dest.writeString(this.workNumber);
        dest.writeString(this.mobileNumber);
    }

    public EmerContact() {
    }

    protected EmerContact(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.relation = in.readString();
        this.homeNumber = in.readString();
        this.workNumber = in.readString();
        this.mobileNumber = in.readString();
    }

    public static final Parcelable.Creator<EmerContact> CREATOR = new Parcelable.Creator<EmerContact>() {
        @Override
        public EmerContact createFromParcel(Parcel source) {
            return new EmerContact(source);
        }

        @Override
        public EmerContact[] newArray(int size) {
            return new EmerContact[size];
        }
    };
}
