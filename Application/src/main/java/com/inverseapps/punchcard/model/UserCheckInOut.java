package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class UserCheckInOut implements Parcelable {

    private int id;
    private String uniq_id;
    private Date checkInDateTime;
    private Date checkOutDateTime;
    private int project_id;
    private int user_id;
    private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public Date getCheckInDateTime() {
        return checkInDateTime;
    }

    public void setCheckInDateTime(Date checkInDateTime) {
        this.checkInDateTime = checkInDateTime;
    }

    public Date getCheckOutDateTime() {
        return checkOutDateTime;
    }

    public void setCheckOutDateTime(Date checkOutDateTime) {
        this.checkOutDateTime = checkOutDateTime;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.uniq_id);
        dest.writeLong(this.checkInDateTime != null ? this.checkInDateTime.getTime() : -1);
        dest.writeLong(this.checkOutDateTime != null ? this.checkOutDateTime.getTime() : -1);
        dest.writeInt(this.project_id);
        dest.writeInt(this.user_id);
        dest.writeParcelable(this.project, flags);
    }

    public UserCheckInOut() {
    }

    protected UserCheckInOut(Parcel in) {
        this.id = in.readInt();
        this.uniq_id = in.readString();
        long tmpCheckInDateTime = in.readLong();
        this.checkInDateTime = tmpCheckInDateTime == -1 ? null : new Date(tmpCheckInDateTime);
        long tmpCheckOutDateTime = in.readLong();
        this.checkOutDateTime = tmpCheckOutDateTime == -1 ? null : new Date(tmpCheckOutDateTime);
        this.project_id = in.readInt();
        this.user_id = in.readInt();
        this.project = in.readParcelable(Project.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserCheckInOut> CREATOR = new Parcelable.Creator<UserCheckInOut>() {
        @Override
        public UserCheckInOut createFromParcel(Parcel source) {
            return new UserCheckInOut(source);
        }

        @Override
        public UserCheckInOut[] newArray(int size) {
            return new UserCheckInOut[size];
        }
    };
}
