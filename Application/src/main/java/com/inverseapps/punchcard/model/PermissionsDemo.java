package com.inverseapps.punchcard.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PermissionsDemo implements Parcelable {


    private List<String> reports;
    private List<String> scanner;

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public List<String> getScanner() {
        return scanner;
    }

    public void setScanner(List<String> scanner) {
        this.scanner = scanner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList( this.reports);
        dest.writeStringList(this.scanner);

    }

    public PermissionsDemo() {
    }

    protected PermissionsDemo(Parcel in) {
        this.reports = in.readParcelable(Reports.class.getClassLoader());
        this.scanner = in.readParcelable(Scanner.class.getClassLoader());
    }

    public static final Parcelable.Creator<PermissionsDemo> CREATOR = new Parcelable.Creator<PermissionsDemo>() {
        @Override
        public PermissionsDemo createFromParcel(Parcel source) {
            return new PermissionsDemo(source);
        }

        @Override
        public PermissionsDemo[] newArray(int size) {
            return new PermissionsDemo[size];
        }
    };
}