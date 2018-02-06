package com.inverseapps.punchcard.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 02-Nov-17.
 */

public class Choose_Option implements Parcelable {

    private String option_name;
    private String option_price;
    private String option_keyword;
    private int option_status;

    public Choose_Option(String option_name, String option_price, String option_keyword, int option_status) {
        this.option_name = option_name;
        this.option_price = option_price;
        this.option_keyword = option_keyword;
        this.option_status = option_status;
    }

    protected Choose_Option(Parcel in) {
        option_name = in.readString();
        option_price = in.readString();
        option_keyword = in.readString();
        option_status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(option_name);
        dest.writeString(option_price);
        dest.writeString(option_keyword);
        dest.writeInt(option_status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Choose_Option> CREATOR = new Creator<Choose_Option>() {
        @Override
        public Choose_Option createFromParcel(Parcel in) {
            return new Choose_Option(in);
        }

        @Override
        public Choose_Option[] newArray(int size) {
            return new Choose_Option[size];
        }
    };

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getOption_price() {
        return option_price;
    }

    public void setOption_price(String option_price) {
        this.option_price = option_price;
    }

    public String getOption_keyword() {
        return option_keyword;
    }

    public void setOption_keyword(String option_keyword) {
        this.option_keyword = option_keyword;
    }

    public int getOption_status() {
        return option_status;
    }

    public void setOption_status(int option_status) {
        this.option_status = option_status;
    }
}
