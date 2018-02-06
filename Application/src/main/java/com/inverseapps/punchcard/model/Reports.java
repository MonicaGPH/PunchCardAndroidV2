package com.inverseapps.punchcard.model;



import android.os.Parcel;
import android.os.Parcelable;

public class Reports implements Parcelable {


    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.data);

    }

    public Reports() {
    }

    protected Reports(Parcel in) {
        this.data = in.readInt();


    }

    public static final Parcelable.Creator<Reports> CREATOR = new Parcelable.Creator<Reports>() {
        @Override
        public Reports createFromParcel(Parcel source) {
            return new Reports(source);
        }

        @Override
        public Reports[] newArray(int size) {
            return new Reports[size];
        }
    };
}
