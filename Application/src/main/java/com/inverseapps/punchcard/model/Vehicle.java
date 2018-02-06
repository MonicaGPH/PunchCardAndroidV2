package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Inverse, LLC on 10/20/16.
 */

public class Vehicle implements Parcelable {

    private String carMake;
    private String carModel;
    private String licPlateState;
    private String licPlateNumber;
    private String driversLicenseState;
    private String driversLicenseNumber;

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getLicPlateState() {
        return licPlateState;
    }

    public void setLicPlateState(String licPlateState) {
        this.licPlateState = licPlateState;
    }

    public String getLicPlateNumber() {
        return licPlateNumber;
    }

    public void setLicPlateNumber(String licPlateNumber) {
        this.licPlateNumber = licPlateNumber;
    }

    public String getDriversLicenseState() {
        return driversLicenseState;
    }

    public void setDriversLicenseState(String driversLicenseState) {
        this.driversLicenseState = driversLicenseState;
    }

    public String getDriversLicenseNumber() {
        return driversLicenseNumber;
    }

    public void setDriversLicenseNumber(String driversLicenseNumber) {
        this.driversLicenseNumber = driversLicenseNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.carMake);
        dest.writeString(this.carModel);
        dest.writeString(this.licPlateState);
        dest.writeString(this.licPlateNumber);
        dest.writeString(this.driversLicenseState);
        dest.writeString(this.driversLicenseNumber);
    }

    public Vehicle() {
    }

    protected Vehicle(Parcel in) {
        this.carMake = in.readString();
        this.carModel = in.readString();
        this.licPlateState = in.readString();
        this.licPlateNumber = in.readString();
        this.driversLicenseState = in.readString();
        this.driversLicenseNumber = in.readString();
    }

    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
