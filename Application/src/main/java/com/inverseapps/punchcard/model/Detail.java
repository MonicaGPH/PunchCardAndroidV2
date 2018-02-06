package com.inverseapps.punchcard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 18-Nov-17.
 */

public class Detail {


    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("issue")
    @Expose
    private String issue;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

}
