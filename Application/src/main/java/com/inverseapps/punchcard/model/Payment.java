package com.inverseapps.punchcard.model;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 17-Nov-17.
 */

public class Payment implements Parcelable {



    private String card_number;
    private String card_type;
    private String expire_month;
    private String expire_year;
    private String cvv2;
    private String first_name;
    private String last_name;
    private String line1;
    private String city;
    private String country_code;
    private String postal_code;
    private String state;
    private String phone;
    private String version;
    private String price;
    private String id;
    private boolean cardRenew;


    protected Payment(Parcel in) {
        card_number = in.readString();
        card_type = in.readString();
        expire_month = in.readString();
        expire_year = in.readString();
        cvv2 = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        line1 = in.readString();
        city = in.readString();
        country_code = in.readString();
        postal_code = in.readString();
        state = in.readString();
        phone = in.readString();
        version = in.readString();
        price = in.readString();
        id = in.readString();
        cardRenew = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(card_number);
        dest.writeString(card_type);
        dest.writeString(expire_month);
        dest.writeString(expire_year);
        dest.writeString(cvv2);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(line1);
        dest.writeString(city);
        dest.writeString(country_code);
        dest.writeString(postal_code);
        dest.writeString(state);
        dest.writeString(phone);
        dest.writeString(version);
        dest.writeString(price);
        dest.writeString(id);
        dest.writeByte((byte) (cardRenew ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getExpire_month() {
        return expire_month;
    }

    public void setExpire_month(String expire_month) {
        this.expire_month = expire_month;
    }

    public String getExpire_year() {
        return expire_year;
    }

    public void setExpire_year(String expire_year) {
        this.expire_year = expire_year;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCardRenew() {
        return cardRenew;
    }

    public void setCardRenew(boolean cardRenew) {
        this.cardRenew = cardRenew;
    }

    public static Creator<Payment> getCREATOR() {
        return CREATOR;
    }
}