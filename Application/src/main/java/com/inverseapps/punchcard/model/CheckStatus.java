package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Inverse, LLC on 10/19/16.
 */

public class CheckStatus implements Parcelable {

    private int id;
    private String uniq_id;
    private int user_id;
    private int project_id;
    private Date checkInDateTime;
    private Date checkOutDateTime;
    private double longitudeIn;
    private double latitudeIn;
    private double longitudeOut;
    private double latitudeOut;
    private String checkMethod;
    private Date created_at;
    private Date updated_at;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
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

    public double getLongitudeIn() {
        return longitudeIn;
    }

    public void setLongitudeIn(double longitudeIn) {
        this.longitudeIn = longitudeIn;
    }

    public double getLatitudeIn() {
        return latitudeIn;
    }

    public void setLatitudeIn(double latitudeIn) {
        this.latitudeIn = latitudeIn;
    }

    public double getLongitudeOut() {
        return longitudeOut;
    }

    public void setLongitudeOut(double longitudeOut) {
        this.longitudeOut = longitudeOut;
    }

    public double getLatitudeOut() {
        return latitudeOut;
    }

    public void setLatitudeOut(double latitudeOut) {
        this.latitudeOut = latitudeOut;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
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
        dest.writeInt(this.user_id);
        dest.writeInt(this.project_id);
        dest.writeLong(this.checkInDateTime != null ? this.checkInDateTime.getTime() : -1);
        dest.writeLong(this.checkOutDateTime != null ? this.checkOutDateTime.getTime() : -1);
        dest.writeDouble(this.longitudeIn);
        dest.writeDouble(this.latitudeIn);
        dest.writeDouble(this.longitudeOut);
        dest.writeDouble(this.latitudeOut);
        dest.writeString(this.checkMethod);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeLong(this.updated_at != null ? this.updated_at.getTime() : -1);
        dest.writeParcelable(this.project, flags);
    }

    public CheckStatus() {
    }

    protected CheckStatus(Parcel in) {
        this.id = in.readInt();
        this.uniq_id = in.readString();
        this.user_id = in.readInt();
        this.project_id = in.readInt();
        long tmpCheckInDateTime = in.readLong();
        this.checkInDateTime = tmpCheckInDateTime == -1 ? null : new Date(tmpCheckInDateTime);
        long tmpCheckOutDateTime = in.readLong();
        this.checkOutDateTime = tmpCheckOutDateTime == -1 ? null : new Date(tmpCheckOutDateTime);
        this.longitudeIn = in.readDouble();
        this.latitudeIn = in.readDouble();
        this.longitudeOut = in.readDouble();
        this.latitudeOut = in.readDouble();
        this.checkMethod = in.readString();
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        long tmpUpdated_at = in.readLong();
        this.updated_at = tmpUpdated_at == -1 ? null : new Date(tmpUpdated_at);
        this.project = in.readParcelable(Project.class.getClassLoader());
    }

    public static final Parcelable.Creator<CheckStatus> CREATOR = new Parcelable.Creator<CheckStatus>() {
        @Override
        public CheckStatus createFromParcel(Parcel source) {
            return new CheckStatus(source);
        }

        @Override
        public CheckStatus[] newArray(int size) {
            return new CheckStatus[size];
        }
    };
}
