package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/18/16.
 */

public class UserJDoc implements Parcelable {

    private String trade;
    private Address address;
    private Vehicle vehicle;
    private String job_title;
    private String department;
    private String homeNumber;
    private Scheduling scheduling;
    private Demographics demographics;
    private PrimaryAddress primaryAddress;
    private SecondaryAddress secondaryAddress;
    private String middleName;
    private String workNumber;
    private EmerContact emerContact;


    protected UserJDoc(Parcel in) {
        trade = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        job_title = in.readString();
        department = in.readString();
        homeNumber = in.readString();
        scheduling = in.readParcelable(Scheduling.class.getClassLoader());
        demographics = in.readParcelable(Demographics.class.getClassLoader());
        primaryAddress = in.readParcelable(PrimaryAddress.class.getClassLoader());
        secondaryAddress = in.readParcelable(SecondaryAddress.class.getClassLoader());
        middleName = in.readString();
        workNumber = in.readString();
        emerContact = in.readParcelable(EmerContact.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trade);
        dest.writeParcelable(address, flags);
        dest.writeParcelable(vehicle, flags);
        dest.writeString(job_title);
        dest.writeString(department);
        dest.writeString(homeNumber);
        dest.writeParcelable(scheduling, flags);
        dest.writeParcelable(demographics, flags);
        dest.writeParcelable(primaryAddress, flags);
        dest.writeParcelable(secondaryAddress, flags);
        dest.writeString(middleName);
        dest.writeString(workNumber);
        dest.writeParcelable(emerContact, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserJDoc> CREATOR = new Creator<UserJDoc>() {
        @Override
        public UserJDoc createFromParcel(Parcel in) {
            return new UserJDoc(in);
        }

        @Override
        public UserJDoc[] newArray(int size) {
            return new UserJDoc[size];
        }
    };

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }

    public void setScheduling(Scheduling scheduling) {
        this.scheduling = scheduling;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public PrimaryAddress getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(PrimaryAddress primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public SecondaryAddress getSecondaryAddress() {
        return secondaryAddress;
    }

    public void setSecondaryAddress(SecondaryAddress secondaryAddress) {
        this.secondaryAddress = secondaryAddress;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public EmerContact getEmerContact() {
        return emerContact;
    }

    public void setEmerContact(EmerContact emerContact) {
        this.emerContact = emerContact;
    }

    public static Creator<UserJDoc> getCREATOR() {
        return CREATOR;
    }
}
