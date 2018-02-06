package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 15-Jan-18.
 */

public class Demographics implements Parcelable {
    private String sex;
    private String ethnicity;
    private String drugTestDate;
    private String drugTestResult;
    private String backgroundCheckDate;

    protected Demographics(Parcel in) {
        sex = in.readString();
        ethnicity = in.readString();
        drugTestDate = in.readString();
        drugTestResult = in.readString();
        backgroundCheckDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sex);
        dest.writeString(ethnicity);
        dest.writeString(drugTestDate);
        dest.writeString(drugTestResult);
        dest.writeString(backgroundCheckDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Demographics> CREATOR = new Creator<Demographics>() {
        @Override
        public Demographics createFromParcel(Parcel in) {
            return new Demographics(in);
        }

        @Override
        public Demographics[] newArray(int size) {
            return new Demographics[size];
        }
    };

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getDrugTestDate() {
        return drugTestDate;
    }

    public void setDrugTestDate(String drugTestDate) {
        this.drugTestDate = drugTestDate;
    }

    public String getDrugTestResult() {
        return drugTestResult;
    }

    public void setDrugTestResult(String drugTestResult) {
        this.drugTestResult = drugTestResult;
    }

    public String getBackgroundCheckDate() {
        return backgroundCheckDate;
    }

    public void setBackgroundCheckDate(String backgroundCheckDate) {
        this.backgroundCheckDate = backgroundCheckDate;
    }
}
