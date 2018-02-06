package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 17-Jan-18.
 */

public class Stats implements Parcelable{
    private Double  daysWorked;
    private Double  hoursWorked;
    private int employeesOnSite;
    private int companiesOnSite;


    protected Stats(Parcel in) {
        if (in.readByte() == 0) {
            daysWorked = null;
        } else {
            daysWorked = in.readDouble();
        }
        if (in.readByte() == 0) {
            hoursWorked = null;
        } else {
            hoursWorked = in.readDouble();
        }
        employeesOnSite = in.readInt();
        companiesOnSite = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (daysWorked == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(daysWorked);
        }
        if (hoursWorked == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(hoursWorked);
        }
        dest.writeInt(employeesOnSite);
        dest.writeInt(companiesOnSite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Stats> CREATOR = new Creator<Stats>() {
        @Override
        public Stats createFromParcel(Parcel in) {
            return new Stats(in);
        }

        @Override
        public Stats[] newArray(int size) {
            return new Stats[size];
        }
    };

    public Double getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Double daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public int getEmployeesOnSite() {
        return employeesOnSite;
    }

    public void setEmployeesOnSite(int employeesOnSite) {
        this.employeesOnSite = employeesOnSite;
    }

    public int getCompaniesOnSite() {
        return companiesOnSite;
    }

    public void setCompaniesOnSite(int companiesOnSite) {
        this.companiesOnSite = companiesOnSite;
    }
}
