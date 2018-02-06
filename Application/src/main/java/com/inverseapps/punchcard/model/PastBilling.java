package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


/**
 * Created by asus on 05-Dec-17.
 */

public class PastBilling implements Parcelable {

    private int id;
    private String txn_id;
    private String state;
    private String intent;
    private String payment_method;
    private String txn_amount;
    private String currency;
    private String per_punch_cost;
    private int client_id;
    private int user_id;
    private String create_time;
    private String update_time;
    private String total_punch;
    private String version;
    private Date billing_start_date;
    private Date billing_end_date;


    protected PastBilling(Parcel in) {
        id = in.readInt();
        txn_id = in.readString();
        state = in.readString();
        intent = in.readString();
        payment_method = in.readString();
        txn_amount = in.readString();
        currency = in.readString();
        per_punch_cost = in.readString();
        client_id = in.readInt();
        user_id = in.readInt();
        create_time = in.readString();
        update_time = in.readString();
        total_punch = in.readString();
        version = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(txn_id);
        dest.writeString(state);
        dest.writeString(intent);
        dest.writeString(payment_method);
        dest.writeString(txn_amount);
        dest.writeString(currency);
        dest.writeString(per_punch_cost);
        dest.writeInt(client_id);
        dest.writeInt(user_id);
        dest.writeString(create_time);
        dest.writeString(update_time);
        dest.writeString(total_punch);
        dest.writeString(version);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PastBilling> CREATOR = new Creator<PastBilling>() {
        @Override
        public PastBilling createFromParcel(Parcel in) {
            return new PastBilling(in);
        }

        @Override
        public PastBilling[] newArray(int size) {
            return new PastBilling[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTxn_amount() {
        return txn_amount;
    }

    public void setTxn_amount(String txn_amount) {
        this.txn_amount = txn_amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPer_punch_cost() {
        return per_punch_cost;
    }

    public void setPer_punch_cost(String per_punch_cost) {
        this.per_punch_cost = per_punch_cost;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getTotal_punch() {
        return total_punch;
    }

    public void setTotal_punch(String total_punch) {
        this.total_punch = total_punch;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getBilling_start_date() {
        return billing_start_date;
    }

    public void setBilling_start_date(Date billing_start_date) {
        this.billing_start_date = billing_start_date;
    }

    public Date getBilling_end_date() {
        return billing_end_date;
    }

    public void setBilling_end_date(Date billing_end_date) {
        this.billing_end_date = billing_end_date;
    }

    public static Creator<PastBilling> getCREATOR() {
        return CREATOR;
    }
}
