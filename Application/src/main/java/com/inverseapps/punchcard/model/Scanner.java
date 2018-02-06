package com.inverseapps.punchcard.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Scanner implements Parcelable {


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

    public Scanner() {
    }

    protected Scanner(Parcel in) {
        this.data = in.readInt();


    }

    public static final Parcelable.Creator<Scanner> CREATOR = new Parcelable.Creator<Scanner>() {
        @Override
        public Scanner createFromParcel(Parcel source) {
            return new Scanner(source);
        }

        @Override
        public Scanner[] newArray(int size) {
            return new Scanner[size];
        }
    };
}
