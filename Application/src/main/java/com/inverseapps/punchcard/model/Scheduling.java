package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 15-Jan-18.
 */

public class Scheduling implements Parcelable {

    private String payPeriod;
    private String paySchedule;
    private String dailyEndTime;
    private String workWeekStart;
    private String dailyStartTime;
    private String lunchTimeAmount;

    protected Scheduling(Parcel in) {
        payPeriod = in.readString();
        paySchedule = in.readString();
        dailyEndTime = in.readString();
        workWeekStart = in.readString();
        dailyStartTime = in.readString();
        lunchTimeAmount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payPeriod);
        dest.writeString(paySchedule);
        dest.writeString(dailyEndTime);
        dest.writeString(workWeekStart);
        dest.writeString(dailyStartTime);
        dest.writeString(lunchTimeAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Scheduling> CREATOR = new Creator<Scheduling>() {
        @Override
        public Scheduling createFromParcel(Parcel in) {
            return new Scheduling(in);
        }

        @Override
        public Scheduling[] newArray(int size) {
            return new Scheduling[size];
        }
    };

    public String getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(String payPeriod) {
        this.payPeriod = payPeriod;
    }

    public String getPaySchedule() {
        return paySchedule;
    }

    public void setPaySchedule(String paySchedule) {
        this.paySchedule = paySchedule;
    }

    public String getDailyEndTime() {
        return dailyEndTime;
    }

    public void setDailyEndTime(String dailyEndTime) {
        this.dailyEndTime = dailyEndTime;
    }

    public String getWorkWeekStart() {
        return workWeekStart;
    }

    public void setWorkWeekStart(String workWeekStart) {
        this.workWeekStart = workWeekStart;
    }

    public String getDailyStartTime() {
        return dailyStartTime;
    }

    public void setDailyStartTime(String dailyStartTime) {
        this.dailyStartTime = dailyStartTime;
    }

    public String getLunchTimeAmount() {
        return lunchTimeAmount;
    }

    public void setLunchTimeAmount(String lunchTimeAmount) {
        this.lunchTimeAmount = lunchTimeAmount;
    }
}
